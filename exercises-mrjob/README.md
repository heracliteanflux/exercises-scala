# mrjob

## Exercises

### small data solution

```python
def word_count (filename):
  counter = {}
  with open(filename) as infile:
    for line in infile:
      words = line.split()
      for w in words:
        counter[w] = counter.get(w, 0) + 1
  return counter
```

A large file might not fit on one's machine or the counter (which is an associative array of key-value pairs) might not fit.

### word counts `word_count.py`

```python
from mrjob.job import MRJob

class WordCount (MRJob):

	def mapper (self, key, line):
		words = line.split()
		for word in words:
			yield word, 1

	def reducer (self, key, values):
		yield key, sum(values)

if __name__ == '__main__':
	WordCount.run()
```

### word totals `word_count_stats.py`

#### first attempt - a sequence of jobs

Run a mapreduce job on the output of `word_count.py` via mrjob's steps API.
* https://mrjob.readthedocs.io/en/latest/guides/writing-mrjobs.html
* https://mrjob.readthedocs.io/en/latest/step.html

```python
from mrjob.job  import MRJob
from mrjob.step import MRStep

class WordCountStats (MRJob):

	def mapper_word_count (self, key, line):
		words = line.split()
		for word in words:
			yield word, 1

	def reducer_word_count (self, key, values):
		yield key, sum(values)

	def mapper_word_total (self, key, value):
		yield 'Total', value
		first_letter = key[0].upper()
		yield first_letter, value

	def reducer_word_total (self, key, values):
		yield key, sum(values)

	def steps (self):
		return [
			MRStep(
				mapper  = self.mapper_word_count,
				reducer = self.reducer_word_count,
			),
			MRStep(
				mapper  = self.mapper_word_total,
				reducer = self.reducer_word_total,
			),
		]

if __name__ == '__main__':
	WordCountStats.run()
```

This code is inefficient because the second mapreduce job (or step) is an extra transfer of data.

#### second attempt

```python
from mrjob.job import MRJob

class WordCountStats (MRJob):

	def mapper (self, key, line):
		words = line.split()
		for word in words:
			yield ('_' + word, 1)
			yield 'Total_', 1
			first_letter = word[0].upper()
			yield first_letter + '_', 1

	def reducer (self, key, values):
		yield key, sum(values)

if __name__ == '__main__':
	WordCountStats.run()
```

The output isn't clean: the statistics are scattered across multiple shards and mixed with the normal word count information within shards.

#### third attempt with a partitioner

```java
int getPartition (Key key, Value value, int numPart) {
	if key.startsWith("_") { // normal word
    partition = 1 + (key.hash() % (numPart - 1))
	}
	else { // summary
		partition = 0
	}
	return partition
}
```

The statistics go to the first partition and the word counts are distributed to the remaining partitions. But this code is still inefficient:
1. The first reducer gets message ('Total_', 1) for every word in the input. This assumes that a single reducer can store something as big as the entire input. In big data programming, this will cause the job to crash.
2. There is no in-memory combining.

#### TO DO fourth attempt with in-memory combining

Use one dictionary for words (which needs to be flushed) and another dictionary for summary statistics (which never grows beyond 27 keys, so there is no need to flush it except in `mapper_final`).

```python
from mrjob.job import MRJob

class WordCountStats (MRJob):

	def mapper_init (self):
		self.words = {}
		self.stats = {}

	def mapper (self, key, line):
		words = line.split()
		for word in words:
			self.words[word] = self.words.get(word, 0) + 1
			if len(self.words) > 100:
				for w in self.words:
					yield w, self.words[w]
				self.words = {}
			self.stats['Total_'] = self.stats.get('Total_', 0) + 1
			first_letter = word[0].upper()
			self.stats[first_letter + '_'] = self.stats.get(first_letter + '_', 0) + 1

	def mapper_final (self):
		if self.words: # if len(self.words) > 0
			for w in self.words:
				yield w, self.words[w]
			self.words = {}
		self.stats = {}

	def reducer (self, key, values):
		yield key, sum(values)

if __name__ == '__main__':
	WordCountStats.run()
```

### letter count `letter_count.py`

```python
from mrjob.job import MRJob

class LetterCount (MRJob):

  def mapper (self, key, line):
		for symbol in line:
			if isletter (symbol):
				yield symbol, 1

	def reducer (self, key, values):
		yield key, sum(values)

if __name__ == '__main__':
	LetterCount.run()
```

What is the size of the data being shipped across the network from mappers to reducers?

L input characters

B size of a character

N size of an integer

L(B+N) bytes are being shipped across the network from mappers to reducers, which could be larger than the original input. It isn't necessary to wait until the reducer to add values: messages with the same key can be combined by adding their values.

### setup and teardown

The setup function `mapper_init` can initialize class variables (e.g., `self.cache = {}`) which are available when the same mapper processes subsequent lines and can output key-value pairs via `yield`.

The teardown function `mapper_final` can output key-value pairs and cleans up (e.g., closes open file connections).

#### word count

```python
from mrjob.job import MRJob

class WordCount (MRJob):

	def mapper_init (self):
		self.cache = {}

	def mapper (self, key, line):
		words = line.split()
		for word in words:
			self.cache[word] = self.cache.get(word, 0) + 1
			# if word not in self.cache:
			# 	self.cache[word] = 0
			# self.cache[word] += 1
			if len(self.cache) > 100:
				for w in self.cache:
					yield w, self.cache[w]
				self.cache = {}

	def mapper_final (self):
		if self.cache: # if len(self.cache) > 0
			for w in self.cache:
				yield w, self.cache[w]
			self.cache = {}

	def reducer (self, key, values):
		yield key, sum(values)

if __name__ == '__main__':
	WordCount.run()
```

#### letter count

```python
from   mrjob.job import MRJob
import string

class LetterCount (MRJob):

  def mapper_init (self):
		# initialize a dictionary (key = letter, value = how many times the letter has been seen so far)
    self.cache = dict.fromkeys(string.ascii_letters, 0)

  def mapper (self, key, line):
    self.set_status('heartbeat') # What would happen if the hearbeat message is not sent?
    for symbol in line:
      if symbol.isalpha():
        self.cache[symbol] += 1

  def mapper_final (self):
    for key, value in self.cache.items():
      yield key, value
		self.cache = {}

  def reducer (self, key, values):
    yield key, sum(values)

if __name__ == '__main__':
  LetterCount.run()
```

Why should a single reducer be used here? Can a single reducer handle all messages sent to it?