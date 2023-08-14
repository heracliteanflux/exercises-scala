# Exercises

## `mr_word_count.py`

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
python mr_word_count.py war_and_peace.txt
```
```
"words"	564647
"lines"	54223
"chars"	3163475
```

## `word_size.py`

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

## `word_alpha.py`

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