Install [pytest](https://docs.pytest.org/en/latest/) on hadoop-gateway.

```
pip install --user pytest
```

Download `hw2test.py` and `helpers.py`. `hw2test.py` is the skeleton of the unit testing code to be implemented. Each test function has a parameter called `output_data`. This is called a ficture. Here, `output_data` represents the contents of the part files in the `hw2output` hdfs directory (or is an empty list if that directory does not exist). Each part file is a list of key-value pairs. Since the directory is essentially a list of part files, then `output_data` is a list of lists of key-value pairs.

Example:
```
part-00000
```
```
the  1
word 3
```
```
part-00001
```
```
a    8
be   9
```
```
output_data
```
```
[
	[(the, 1), (word, 3)],
	[(a, 8), (be, 9)],
]
```

Create an input file for testing the code in HDFS with the following contents:

```
This enormously large file
is the testing input file for HW2.
```

Write mapreduce code that calculates, for each letter that appears, how many words contain that letter (case sensitive). A word is defined as any sequence of non blank characters (essentially, what one gets from Python's `split()` function). For example, `t` appears in four words in the input file. Use in-mapper combining (also known as in-memory combining), which means functions `mapper_init`, `mapper_final`, `reducer_init`, and `reducer_final` may need to be overwritten.

Running `hw2test.py` may take some time because it is read from HDFS.

#### Linux shell execution

```
python hw2.py test_input.txt
```
```
No configs found; falling back on auto-configuration
No configs specified for inline runner
Creating temp directory /var/folders/89/5r24znsj4jbfr7zrccy5yn0c0000gn/T/hw2.davefriedman.20230814.183256.931354
Running step 1 of 1...
job output is in /var/folders/89/5r24znsj4jbfr7zrccy5yn0c0000gn/T/hw2.davefriedman.20230814.183256.931354/output
Streaming final output from /var/folders/89/5r24znsj4jbfr7zrccy5yn0c0000gn/T/hw2.davefriedman.20230814.183256.931354/output...
"u"	2
"v"	0
"w"	1
"x"	0
"k"	0
"l"	4
"e"	6
"f"	3
"g"	2
"h"	3
"i"	6
"j"	0
"y"	1
"z"	0
"q"	0
"r"	3
"c"	0
"d"	0
"m"	1
"n"	3
"o"	2
"p"	1
"a"	1
"b"	0
"s"	4
"t"	4
Removing temp directory /var/folders/89/5r24znsj4jbfr7zrccy5yn0c0000gn/T/hw2.davefriedman.20230814.183256.931354...
```

```
python hw2.py war_and_peace.txt
```
```
No configs found; falling back on auto-configuration
No configs specified for inline runner
Creating temp directory /var/folders/89/5r24znsj4jbfr7zrccy5yn0c0000gn/T/hw2.davefriedman.20230814.183533.430561
Running step 1 of 1...
job output is in /var/folders/89/5r24znsj4jbfr7zrccy5yn0c0000gn/T/hw2.davefriedman.20230814.183533.430561/output
Streaming final output from /var/folders/89/5r24znsj4jbfr7zrccy5yn0c0000gn/T/hw2.davefriedman.20230814.183533.430561/output...
"u"	62736
"v"	26299
"w"	58582
"x"	3937
"k"	20256
"l"	80207
"e"	250475
"f"	51360
"g"	48294
"h"	161558
"i"	156468
"j"	2485
"y"	45519
"z"	2313
"q"	2317
"r"	132559
"c"	56520
"d"	108667
"m"	57927
"n"	166253
"o"	173704
"p"	40339
"a"	187852
"b"	34154
"s"	140681
"t"	202317
Removing temp directory /var/folders/89/5r24znsj4jbfr7zrccy5yn0c0000gn/T/hw2.davefriedman.20230814.183533.430561...
```