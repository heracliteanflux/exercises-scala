# Big data programming in Scala

Exercises in the Scala programming language with an emphasis on big data programming and applications in Apache Hadoop and Apache Spark.

## Table of Contents

1. [Resources](#resources)
2. [Texts](#texts)
3. [Terms](#terms)
4. [Acknowledgements](#acknowledgements)
5. [Notes](#notes)

## Resources

[[H](https://hadoop.apache.org/)][[W](https://en.wikipedia.org/wiki/Apache_Hadoop)] Apache Hadoop
* [[D](https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/FileSystemShell.html)] HDFS shell
* https://www.databricks.com/glossary/hadoop-distributed-file-system-hdfs

Apache Maven
* [[P](https://repo.maven.apache.org/maven2/junit/junit/)] junit

[[H](https://spark.apache.org/)][[W](https://en.wikipedia.org/wiki/Apache_Spark)] Apache Spark

Scala
* [[H](https://get-coursier.io/)] Coursier
* [[H](https://www.scala-lang.org/)][[D](https://docs.scala-lang.org/)] Scala
  * [[Book](https://docs.scala-lang.org/scala3/book/introduction.html)]
* [[H](https://www.scala-sbt.org/)] Simple Build Tool (sbt)
* [[H](http://www.scalanlp.org/)][[G](https://github.com/scalanlp)] ScalaNLP
  * [[G](https://github.com/scalanlp/breeze)] Breeze
	  * [[G](https://github.com/scalanlp/breeze/wiki/Linear-Algebra-Cheat-Sheet)] Linear Algebra Cheat Sheet

Python
* [[D](https://www.dask.org)] dask
* [[D](https://mrjob.readthedocs.io/en/latest/index.html)] mrjob
* [[D](https://docs.pytest.org/en/latest/)] pytest

[[H](https://attic.apache.org/)] Apache Attic
* [[H](https://mrunit.apache.org/)] Apache MRUnit

More
* [Hadoop Basics](https://blog.ditullio.fr/category/hadoop-basics/)
  * [Hadoop Basics I: Working with Sequence Files](https://blog.ditullio.fr/2015/12/18/hadoop-basics-working-with-sequence-files/)
  * [Hadoop Basics II: Filter, Aggregate, and Sort with MapReduce](https://blog.ditullio.fr/2015/12/24/hadoop-basics-filter-aggregate-sort-mapreduce/)

## Texts

Apache Hadoop
* Sammer, Eric. (2012). _Hadoop Operations: A Guide for Developers and Administrators_. O'Reilly. [Home](http://www.hadoopbook.com). [GitHub](https://github.com/tomwhite/hadoop-book/).
* [[H](http://www.hadoopbook.com/)][[G1](https://github.com/tomwhite/hadoop-book/)][[G2](https://github.com/oreillymedia/hadoop_the_definitive_guide_4e)] White, Tom. (2015). _Hadoop: The Definitive Guide: Storage and Analysis at Internet Scale_. 4th Ed. O'Reilly.

Apache HBase
* [[H](http://www.hbasebook.com/)] George, Lars. _HBase: The Definitive Guide_.

Apache Spark
* Chambers, Bill & Matei Zaharia. (2018). _Spark The Definitive Guide: Big Data Processing Made Simple_. O'Reilly. [GitHub](https://github.com/databricks/Spark-The-Definitive-Guide).
* Damji et al. (2020). _Learning Spark: Lightning-Fast Data Analytics_. 2nd Ed. O'Reilly. [GitHub](https://github.com/databricks/LearningSparkV2).
* Karau, Holden & Rachel Warren. (2017). _High Performance Spark: Best Practices for Scaling & Optimizing Apache Spark_. O'Reilly. [GitHub](https://github.com/high-performance-spark/high-performance-spark-examples).
* Maas, Gerard & Francois Garillot. (2019). _Stream Processing with Apache Spark: Best Practices for Scaling and Optimizing Apache Spark_. O'Reilly. [GitHub](https://github.com/stream-processing-with-spark).
* Parsian, Mahmoud. (2022). _Data Algorithms with Spark: Recipes and Design Patterns for Scaling Up Using PySpark_. O'Reilly. [GitHub](https://github.com/mahmoudparsian/data-algorithms-with-spark).
* Perrin, Jean-Georges. (2020). _Spark in Action: With examples in Java, Python, and Scala_. 2nd Ed. Manning.
* Polak, Adi. (2023). _Machine Learning with Spark: Designing Distributed ML Platforms with PyTorch, TensorFlow, and MLLib_. O'Reilly.
* Ryza et al. (2017). _Advanced Analytics with Spark: Patterns for Learning from Data at Scale_. 2nd Ed. O'Reilly. [GitHub](https://github.com/sryza/aas).
* Tandon et al. (2022). _Advanced Analytics with PySpark: Patterns for Learning from Data at Scale Using Python and Spark_. O'Reilly.

Big Data
* Leskovec, Jure; Anand Rajaraman; & Jeff Ullman. _Mining of Massive Datasets_. [Home]( http://www.mmds.org).
* Linn, Jimmy & Chris Dyer. (2010). _Data-Intensive Text Processing with MapReduce_. [Home](https://lintool.github.io/MapReduceAlgorithms/).

MapReduce
* [[P](https://static.googleusercontent.com/media/research.google.com/en//archive/mapreduce-osdi04.pdf)] Jeffrey Dean and Sanjay Ghemawat. "MapReduce: Simplified Data Processing on Large Clusters". Communications of the ACM January 2008, Vol 52. No.1.

Scala
* Alexander, Alvin. (2021). _Scala Cookbook: Recipes for Object-Oriented and Functional Programming_. 2nd Ed. O'Reilly. [GitHub](https://github.com/alvinj/ScalaCookbook2Examples).
* Chiusano, Paul & Runar Bjarnason. (2014). _Functional Programming in Scala_. [Manning](https://www.manning.com/books/functional-programming-in-scala).
* Odersky, Martin; Lex Spoon; & Bill Venners. (2023). _Advanced Programming in Scala_. 5th Ed. [Artima](https://www.artima.com/shop/advanced_programming_in_scala_5ed).
* Odersky, Martin; Lex Spoon; Bill Venners; & Frank Sommers. (2021). _Programming in Scala_. 5th Ed. [Artima](https://booksites.artima.com/shop/programming_in_scala_5ed).
* Odersky, Martin; Lex Spoon; & Bill Venners. _Programming in Scala_. 3rd. Ed. Artima. [Home](https://booksites.artima.com/programming_in_scala_3ed).
* Phillips, Andrew & Nermin Šerifovic. (2014). _Scala Puzzlers_. [Artima](https://booksites.artima.com/shop/scala_puzzlers).
* Wampler, Dean. (2021). _Programming Scala: Scalability = Functional Programming + Objects_. 3rd Ed. O'Reilly. [Home](https://deanwampler.github.io/books/programmingscala.html). [GitHub](https://github.com/deanwampler/programming-scala-book-code-examples).

## Terms

* [[W](https://en.wikipedia.org/wiki/Anamorphism)] Anamorphism
* [[W](https://en.wikipedia.org/wiki/Apache_Hadoop)] Apache Hadoop
* [[W](https://en.wikipedia.org/wiki/Apache_Spark)] Apache Spark
* [[W](https://en.wikipedia.org/wiki/Big_data)] Big Data
* [[W](https://en.wikipedia.org/wiki/Catamorphism)] Catamorphism
* [[W](https://en.wikipedia.org/wiki/Clustered_file_system)] Clustered File System
* [[W](https://en.wikipedia.org/wiki/Communication_protocol)] Communication Protocol
* [[W](https://en.wikipedia.org/wiki/Computer_cluster)] Computer Cluster
* [[W](https://en.wikipedia.org/wiki/Consensus_(computer_science))] Consensus
* [[W](https://en.wikipedia.org/wiki/Device_file)] Device File
* [[W](https://en.wikipedia.org/wiki/Hard_disk_drive)] Disk
* [[W](https://en.wikipedia.org/wiki/Distributed_computing)] Distributed Computing
* [[W](https://en.wikipedia.org/wiki/Clustered_file_system)] Distributed File System
* [[W](https://en.wikipedia.org/wiki/Fault_tolerance)] Fault Tolerance
* [[W](https://en.wikipedia.org/wiki/Fold_(higher-order_function))] Fold
* [[W](https://en.wikipedia.org/wiki/Functional_programming)] Functional Programming
* [W] Hadoop Distributed File System (HDFS)
* [[W](https://en.wikipedia.org/wiki/Heartbeat_(computing))] Hearbeat
* [[W](https://en.wikipedia.org/wiki/High-availability_cluster)] High-Availability Cluster
* [[W](https://en.wikipedia.org/wiki/Higher-order_function)] Higher-Order Function
* [[W](https://en.wikipedia.org/wiki/Optimistic_replication)] Lazy Replication
* [W] Line-Oriented ASCII Format
* [[W](https://en.wikipedia.org/wiki/Map_(higher-order_function))] Map
* [[W](https://en.wikipedia.org/wiki/Map_(parallel_pattern))] Map
* [[W](https://en.wikipedia.org/wiki/MapReduce)] MapReduce
* [[W](https://en.wikipedia.org/wiki/Multi-master_replication)] Multi-Master Replication
* [[W](https://en.wikipedia.org/wiki/Network_partition)] Network Partition
* [[W](https://en.wikipedia.org/wiki/Node_(networking))] Node
* [[W](https://en.wikipedia.org/wiki/PageRank)] PageRank
* [[W](https://en.wikipedia.org/wiki/Parallel_computing)] Parallel Computing
* [[W](https://en.wikipedia.org/wiki/Programming_model)] Programming Model
* [[W](https://en.wikipedia.org/wiki/Quiesce)] Quiescence
* [[W](https://en.wikipedia.org/wiki/Quorum_(distributed_computing))] Quorum
* [[W](https://en.wikipedia.org/wiki/Reduction_operator)] Reduce
* [[W](https://en.wikipedia.org/wiki/Replication_(computing))] Replication
* [[W](https://en.wikipedia.org/wiki/System_resource)] Resource
* [W] Sequence File
* [[W](https://en.wikipedia.org/wiki/Shard_(database_architecture))] Shard
* [[W](https://en.wikipedia.org/wiki/Standard_streams)] Standard Stream
* [W] Yet Another Resource Manager (YARN)

## Acknowledgements

[[H](https://www.cse.psu.edu/~duk17/)] Dan Kifer. CMPSC/DS 410 Programming Models for Big Data. The Pennsylvania State University.

## Notes

[FILE SHARD]

A large file is split up into pieces called blocks, chunks, or shards (e.g., 64 MB chunk). Shards are replicated and then distributed to different physical machines (3 by default).

[DATA NODE]

A machine that hosts a file shard is called a data node.

[NAME NODE]

The name node is responsible for
* tracking file shards
  * the name node stores metadata (file name, file shard name, number of replicas, storage location of shards) in memory
	* which data nodes store which shards?
	* if a shard is under-replicated, the name node searches for other machines to replicate on
* detecting and managing machine failure
  * if a data node does not send a heartbeat message, then the name node assumes that the data node has failed
* reading files at the level of the application

[CLIENT]

The application that needs the data is called the client.

The client contacts the name node in order to discover the location of file shards, and then contacts the appropriate data nodes to retrieve them.

Limitations of HDFS
* slower than a native file system due to network communication
* better for a few large files than for many small files
  * one 6.4-GB file is 100 64 MB shards: 100 metadata entries are stored in the main memory of the name node
	* 6,400 1-MB files: 6,400 metadata entries...
* better for jobs that read the entire file than for jobs that require random access

[MAPREDUCE]

* Mapper
  * the mapper generates key-value messages from each line
	* can be written in Java instead of Python mrjob via Hadoop streaming
* Partitioner
  * the partitioner decides which reducer receives each key
	* write the partitioner in Java
* Reducer
  * the reducer performs an aggregation with the messages received
	* can be written in Java instead of Python mrjob via Hadoop streaming
* Sorter
  * the sorter determines the order in which messages are received
	* write the sorter in Java
* Combiner
  * the combiner suggests an optimization

parallel read: multiple mappers, single reducer
* problem: the data do not fit on one machine
* multiple mapper nodes read separate file shards in parallel
* each mapper node sends many messages to the reducer node: key "word", value "1"
* the reducer node interprets messages to mean "increment the count of the word by one"
* the reducer node stores the dictionary

```
mapper node j:
  read file shards on mapper node j
	for each line:
	  words = line.split()
		for word in words:
		  send message (word, 1) to the reducer node

reducer node:
  receive messages
	aggregate words
	increment words counts
```

parallel read, parallel aggregation: multiple mappers, multiple reducers
* problem: the words output does not fit on one machine
* multiple reducers: each reducer is associated with a set of words
* for example, 6 reducers:
  * reducer 0 gets words where hash(words) % 6 == 0
	* reducer 1 gets words where hash(words) % 6 == 1
* reducer i produces output shard i

```
mapper node j:
  read file shards on mapper node j
	for each line:
	  words = line.split()
		for word in words:
		  determine the appropriate reducer node
		  send message (word, 1) to the appropriate reducer node

reducer node i:
  receive messages
	aggregate words
	increment words counts
	save the output as file shard i to HDFS as a file shard
```

[MAPPER]

* input: key-value pair
  * key = line number, value = line
  * key = null, value = line
* output: key-value pairs

Hadoop automatically
* finds mapper nodes that have file shards or can get them quickly
* each mapper node reads its file shards
* for every line, it calls the mapper function on it and saves the results to local disk (not HDFS)
* messages for reducer 1 are saved in one file, sorted by keys assigned to that reducer; messages for reducer 2 are saved in another file, sorted by keys assigned to that reducer; etc.
* key-value pairs generated by the mapper are first save to a file on disk and then sent to reducer nodes

[PARTITIONER]

* the partitioner is a function that takes a key as input and produces a number as output, which represents the reducer the message is sent to
* if the partitioner is not specified, Hadoop uses the default

Hadoop automatically
* reads the output from each mapper
* collects key-value pairs for each reducer into a file sorted by key
* ensures reducers receive their keys in sorted order
* shuffle and sort phase
* the sort order may be specified

[REDUCER]

* input: key, list of values (i.e., a list of values from all the messages that share a key)
* output: key-value pair

Hadoop automatically
* collects the incoming key-value pair files and merges them to maintain the sorted order
* groups by key in order to generate the key-valuelist pair
* calls `reducer(key, valuelist)` on each key-valuelist pair
* saves the output to HDFS
* reducer input is locally sorted, not globally sorted

[EXAMPLE]

mapper node 1 input shards
```
That is that
```
```
to be or
not to be
```
mapper node 2 input shard
```
That is big
```
mapper node 1 function outputs
```
(That, 1)
(is, 1)
(that, 1)
(to, 1)
(be, 1)
(or, 1)
(not, 1)
(to, 1)
(be, 1)
```
mapper node 2 function output
```
(That, 1)
(is, 1)
(big, 1)
```
mapper node 1 function outputs sorted for reducer 1
```
(be, 1)
(be, 1)
(not, 1)
(that, 1)
(That, 1)
```
mapper node 1 function outputs sorted for reducer 2
```
(is, 1)
(or, 1)
(to, 1)
(to, 1)
```
mapper node 2 function outputs sorted for reducer 1
```
(big, 1)
(That, 1)
```
mapper node 2 function outputs sorted for reducer 2
```
(is, 1)
```
reducer node 1 input
```
be, [1, 1]
big, [1]
not, [1]
that, [1]
That, [1, 1]
```
reducer node 2 input
```
is, [1, 1]
or, [1]
to, [1, 1]
```
reducer node 1 output
```
be 2
big 1
not 1
that 1
That 2
```
reducer node 2 output
```
is 2
or 1
to 1
```

[COUNTER]

* Hadoop supports counters for maintaining job statistics
* counter work in a distributed setting because increment/decrement are associative: increment/decrement messages can arrive out of order; the order does not matter for the final result
* counters are useful only when the job has finished

[HADOOP STREAMING]

Hadoop supports other languages via Hadoop streaming. The mapper and reducer functions are written in the desired language. The key-value inputs are sent to stdin and the key-value outputs are sent to stdout. Tabs, spaces, non-ascii characters, and language encodings can cause crashes. Hadoop streaming is slower than using Java because it requires running code outside of, and communicating with, the JVM.

[YARN](https://hadoop.apache.org/docs/stable/hadoop-yarn/hadoop-yarn-site/YARN.html)

YARN allows Hadoop and Spark to coexist on a cluster.

components
* app master - starts up in the job's first container and negotiates additional containers from the resource manager; tracks the job's progress
* container - a share of CPU, memory, etc. (analogous to a VM)
* node manager - monitors containers and reports usage to the resource manager
* resource manager - the scheduler maintains a queue of waiting jobs and the applications manager accepts new jobs and determines its first container

[FAULT TOLERANCE]

worker nodes (mappers and reducers) send heartbeat messages
* indirectly via key-value messages
* directly via hearbeat messages (mrjob's `.set_status()`)

worker node failure is determined by a lack of heartbeat messages and the worker node's job is reallocated to another worker node

[mrjob]

* Hadoop's native language is Java
* writing jobs in Java provides type safety, and more control over such things as the partitioner
* mrjob is a convenient Python interface to Hadoop's Java code
* using mrjob is slower than using Java: mrjob uses Hadoop streaming interface to communicate with the Java code via stdin and stdout
* mrjob provides less control over some useful components like the partitioner

[IN MEMORY COMBINING]

* combining messages inside the mapper is called in memory combining
* Hadoop combiners do not guarantee when/if/how-many-times a combiner will run, so in memory combining is preferred
* don't let data stored by the mapper get too large, otherwise it will not fit in memory
* avoid small data solutions
* flush the cache: output key-value pairs when the cache gets too big, and then flush the cache
* reduces network traffic
* increases the speed of the shuffle-and-sort phase because there's less data to shuffle and sort
* helps to avoid overloading a reducer with too many messages