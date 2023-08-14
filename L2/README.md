### MapReduce

Each line of a file `.edges` represents and edge in the graph. For example, `123 95` means that there is an edge from node 123 to node 95. For each node, determine the number of incoming edges from even-numbered nodes (namely, for nodes that have at least 3 incoming edges from even-numbered nodes).

Example

```
input
```
```
22 7
32 7
33 7
34 7
7  6
2  6
8  6
```
```
output
```
```
7 3
```

In the output files, the key should be the node number and the value should be the number of incoming edges it has from even-numbered nodes.

#### Linux shell execution w/o HDFS

```
python fb.py edges.txt
```
```
No configs found; falling back on auto-configuration
No configs specified for inline runner
Creating temp directory /var/folders/89/5r24znsj4jbfr7zrccy5yn0c0000gn/T/fb.davefriedman.20230814.190625.537652
Running step 1 of 1...
job output is in /var/folders/89/5r24znsj4jbfr7zrccy5yn0c0000gn/T/fb.davefriedman.20230814.190625.537652/output
Streaming final output from /var/folders/89/5r24znsj4jbfr7zrccy5yn0c0000gn/T/fb.davefriedman.20230814.190625.537652/output...
"7"	3
Removing temp directory /var/folders/89/5r24znsj4jbfr7zrccy5yn0c0000gn/T/fb.davefriedman.20230814.190625.537652...
```

#### Linux shell execution w/ HDFS

```
HS_JAR=/usr/hdp/3.1.0.0-78/hadoop-mapreduce/hadoop-streaming.jar
MR_OPT="-r hadoop --hadoop-streaming-jar $HS_JAR"
```
```
python3 fb.py $MR_OPT \
--jobconf mapred.reduce.tasks=4 \
hdfs:///ds410/facebook \
--output-dir lab2 \
--no-output
```