# mrjob

## Exercises

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

class WordTotal (MRJob):

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
	WordTotal.run()
```

This code is inefficient because the second mapreduce job (or step) is an extra transfer of data.

#### second attempt

```python
from mrjob.job  import MRJob

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

The statistics go to the first partition and the word counts are distributed to the remaining partitions.

### number of words starting with letter