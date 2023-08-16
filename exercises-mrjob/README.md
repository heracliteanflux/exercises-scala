# mrjob

## Exercises

### `word_count.py`

```python
# https://mrjob.readthedocs.io/en/latest/guides/quickstart.html

from mrjob.job import MRJob

class MRWordFrequencyCount (MRJob):

  def mapper (self, _, line):
    yield 'chars', len(line)
    yield 'words', len(line.split())
    yield 'lines', 1

  def reducer (self, key, values):
    yield key, sum(values)

if __name__ == '__main__':
  MRWordFrequencyCount.run()
```

```
python word_count.py war_and_peace.txt
```
```
"words"	564647
"lines"	54223
"chars"	3163475
```

### `most_common_word.py`

```python
from mrjob.job  import MRJob
from mrjob.step import MRStep

import re

WORD_RE = re.compile(r"[\w']+")

class MRMostUsedWord (MRJob):
    
  def steps (self):
    return [
      MRStep(
        mapper   = self.mapper_get_words,
        combiner = self.combiner_count_words,
        reducer  = self.reducer_count_words,
      ),
      MRStep(
        reducer  = self.reducer_find_max_word,
			),
		]

  def mapper_get_words (self, _, line):
    # yield each word in the line
    for word in WORD_RE.findall(line):
      yield (word.lower(), 1)
    
  def combiner_count_words (self, word, counts):
	  # optimization: sum the words we've seen so far
    yield (word, sum(counts))

  def reducer_count_words (self, word, counts):
	  # send all (num_occurrences, word) pairs to the same reducer
		# num_occurrences is so we can easily use Python's max function
    yield None, (sum(counts), word)

  def reducer_find_max_word (self, _, word_count_pairs):
	  # each item of word_count_pairs is (count, words)
		# so yielding one results in key=counts, value=word
    yield max(word_count_pairs)
    
if __name__ == '__main__':
	MRMostUsedWord.run()
```

```
python most_common_word.py war_and_peace.txt
```
```
34532	"the"
```

### `word_size.py`

```python
from mrjob.job import MRJob

class WordCount (MRJob):

  def mapper (self, key, line): # `key` will be `None`
    for word in line.split():
      yield len(word), 1

  def reducer (self, key, valuelist):
    yield key, sum(valuelist)

if __name__ == '__main__':
  WordCount.run()
```

```
python word_size.py war_and_peace.txt
```
```
6	49969
20	12
21	6
23	1
25	1
27	1
3	133936
31	1
4	93110
5	61605
9	21489
11	6496
12	3782
13	1999
14	900
15	348
16	150
17	74
18	23
19	10
2	87481
7	43916
8	32612
1	13797
10	12928
```

### `word_alpha.py`

```python
from mrjob.job import MRJob

class WordCount (MRJob):

  def mapper (self, key, line): # `key` will be `None`
    for word in line.split():
      yield len(word), word

  def reducer (self, key, valuelist):
    yield key, max(valuelist)

if __name__ == '__main__':
  WordCount.run()
```

```
python word_alpha.py war_and_peace.txt
```
```
6	"zigzag"
20	"good-for-nothing?..."
21	"gentlemen-in-waiting,"
23	"Hofs-kriegs-wurst-Raths"
25	"\"Mmm...ar...ate...ate...\""
27	"\"le-trip-ta-la-de-bu-de-ba,"
3	"zis"
31	"Hofs-kriegs-wurst-schnapps-Rath"
4	"zone"
5	"zero,"
9	"zealously"
11	"yourselves."
12	"youthfulness"
13	"youthfulness-"
14	"young-looking,"
15	"yellowish-green"
16	"well-provisioned"
17	"unintentionally,\""
18	"un-Petersburg-like"
19	"self-justifications"
2	"zu"
7	"zoology"
8	"zoology,"
1	"z"
10	"youthfully"
```

## Notes

mrjob
* a convenient Python interface to Hadoop's Java code
* slower that Java: uses Hadoop streaming interface to communicate with Java code via stdin and stdout
* less control of useful components such as the partitioner