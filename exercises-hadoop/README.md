# [Setting up a single node cluster on macOS](https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/SingleCluster.html)

1

```
brew install hadoop
```

2

```
vim ~/.zshrc
```
```
HADOOP_VERSION="3.3.6"
export HADOOP_HOME="/opt/homebrew/Cellar/hadoop/$HADOOP_VERSION/libexec"
export HADOOP_CONF_DIR="$HADOOP_HOME/etc/hadoop"
export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/share/hadoop/hdf"
```

3 - Install Java if you haven't already done so.

```
java -version
```
or
```
java --version
```
```
java version "18.0.2.1" 2022-08-18
Java(TM) SE Runtime Environment (build 18.0.2.1+1-1)
Java HotSpot(TM) 64-Bit Server VM (build 18.0.2.1+1-1, mixed mode, sharing)
```

4

```
hadoop version
```
or
```
hdfs version
```
```
Hadoop 3.3.6
Source code repository https://github.com/apache/hadoop.git -r 1be78238728da9266a4f88195058f08fd012bf9c
Compiled by ubuntu on 2023-06-18T08:22Z
Compiled on platform linux-x86_64
Compiled with protoc 3.7.1
From source with checksum 5652179ad55f76cb287d9c633bb53bbd
This command was run using /opt/homebrew/Cellar/hadoop/3.3.6/libexec/share/hadoop/common/hadoop-common-3.3.6.jar
```

5

```
brew install maven
```

6

```
mvn --version
```
```
Apache Maven 3.9.4 (dfbb324ad4a7c8fb0bf182e6d91b0ae20e3d2dd9)
Maven home: /opt/homebrew/Cellar/maven/3.9.4/libexec
Java version: 18.0.2.1, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/jdk-18.0.2.1.jdk/Contents/Home
Default locale: en_US, platform encoding: UTF-8
OS name: "mac os x", version: "13.5", arch: "aarch64", family: "mac"
```

## [Standalone Op](https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/SingleCluster.html#Standalone_Operation)

1

```
mkdir input &&
cp $HADOOP_HOME/etc/hadoop/*.xml input
```

2

```
tree input
```
```
input
├── capacity-scheduler.xml
├── core-site.xml
├── hadoop-policy.xml
├── hdfs-rbf-site.xml
├── hdfs-site.xml
├── httpfs-site.xml
├── kms-acls.xml
├── kms-site.xml
├── mapred-site.xml
└── yarn-site.xml

1 directory, 10 files
```

3

```
hadoop jar $HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-$HADOOP_VERSION.jar grep input output 'dfs[a-z.]+' &&
cat output/*
```
```
1	dfsadmin
```

4

```
tree output
```
```
output
├── _SUCCESS
└── part-r-00000

1 directory, 2 files
```

## [Pseudo-Distributed Op](https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/SingleCluster.html#Pseudo-Distributed_Operation)

1

```
vim $HADOOP_HOME/etc/hadoop/core-site.xml
```
```
<configuration>
  <property>
    <name>fs.defaultFS</name>
    <value>hdfs://localhost:9000</value>
  </property>
</configuration>
```

2

```
vim $HADOOP_HOME/etc/hadoop/hdfs-site.xml
```
```
<configuration>
  <property>
    <name>dfs.replication</name>
    <value>1</value>
  </property>
</configuration>
```

3 - Configure ssh if you haven't already done so.

```
vim ~/.ssh/config
```
```
Host m1
  Hostname       localhost
  IdentityFile   ~/.ssh/id_ed25519_m1
  User           df

Host *
  AddKeysToAgent yes
  IdentityAgent  "~/Library/Group Containers/.../agent.sock"
  IdentitiesOnly yes
  LogLevel       INFO
  Port           22
  UseKeychain    yes
```

4

```
ssh localhost
```
```
(df@localhost) Password:
Last login: Wed Aug 16 02:15:25 2023
```
```
exit
```
```
Connection to localhost closed.
```

5 - HDFS example

```
hdfs namenode -format        &&
hdfs --daemon start namenode &&
hdfs --daemon start datanode &&
hdfs dfs -mkdir -p /user/df  &&
hdfs dfs -mkdir input        &&
hdfs dfs -put $HADOOP_HOME/etc/hadoop/*.xml input &&
hdfs dfs -ls /user/df/input
```
```
Found 10 items
-rw-r--r--   1 df supergroup       9213 2023-08-15 19:39 /user/df/input/capacity-scheduler.xml
-rw-r--r--   1 df supergroup        872 2023-08-15 19:39 /user/df/input/core-site.xml
-rw-r--r--   1 df supergroup      11765 2023-08-15 19:39 /user/df/input/hadoop-policy.xml
-rw-r--r--   1 df supergroup        683 2023-08-15 19:39 /user/df/input/hdfs-rbf-site.xml
-rw-r--r--   1 df supergroup        855 2023-08-15 19:39 /user/df/input/hdfs-site.xml
-rw-r--r--   1 df supergroup        620 2023-08-15 19:39 /user/df/input/httpfs-site.xml
-rw-r--r--   1 df supergroup       3518 2023-08-15 19:39 /user/df/input/kms-acls.xml
-rw-r--r--   1 df supergroup        682 2023-08-15 19:39 /user/df/input/kms-site.xml
-rw-r--r--   1 df supergroup        758 2023-08-15 19:39 /user/df/input/mapred-site.xml
-rw-r--r--   1 df supergroup        690 2023-08-15 19:39 /user/df/input/yarn-site.xml
```

5

```
hadoop jar $HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-$HADOOP_VERSION.jar grep input output 'dfs[a-z.]+' &&
hdfs dfs -ls /user/df/output
```
```
Found 2 items
-rw-r--r--   1 df supergroup          0 2023-08-15 19:46 /user/df/output/_SUCCESS
-rw-r--r--   1 df supergroup         29 2023-08-15 19:46 /user/df/output/part-r-00000
```

6

```
hdfs dfs -get output output  &&
cat output/*
```
or
```
hdfs dfs -cat output/*
```
```
1	dfsadmin
```

7

```
hdfs --daemon stop namenode &&
hdfs --daemon stop datanode
```

## [MapReduce tutorial](https://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html)

1

```java
import java.io.IOException;
import java.lang.Object;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount extends Object {

	// mapper
	public static class TokenizerMapper
	  extends Mapper<Object, Text, Text, IntWritable>
	{
		private final static IntWritable  one = new IntWritable(1);
		private                     Text word = new Text();

		public void map (Object key, Text value, Context context)
		  throws IOException, InterruptedException
		{
			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				word.set(itr.nextToken());
				context.write(word, one);
			}
		}
	}

	// reducer
	public static class IntSumReducer
	  extends Reducer<Text, IntWritable, Text, IntWritable>
	{
    private IntWritable result = new IntWritable();

		public void reduce (Text key, Iterable<IntWritable> values, Context context)
		  throws IOException, InterruptedException
		{
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}

	public static void main (String[] args) {
		try {
			Configuration conf = new Configuration();
			Job job = Job.getInstance(conf, "word count");
			job.setJarByClass(WordCount.class);
			job.setMapperClass(TokenizerMapper.class);
			job.setCombinerClass(IntSumReducer.class);
			job.setReducerClass(IntSumReducer.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			FileInputFormat.addInputPath(job, new Path(args[0]));
			FileOutputFormat.setOutputPath(job, new Path(args[1]));
			System.exit(job.waitForCompletion(true) ? 0 : 1);
		}
		catch (IOException e) {
      e.printStackTrace();
		}
		catch (InterruptedException e) {
      e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
      e.printStackTrace();
		}
	}
}
```

2

```
tree
```
```
.
└── WordCount.java

1 directory, 1 file
```

3

```
hadoop com.sun.tools.javac.Main WordCount.java
```
```
tree
```
```
.
├── WordCount$IntSumReducer.class
├── WordCount$TokenizerMapper.class
├── WordCount.class
└── WordCount.java

1 directory, 4 files
```

4

```
jar cf wc.jar WordCount*.class
```
```
.
├── WordCount$IntSumReducer.class
├── WordCount$TokenizerMapper.class
├── WordCount.class
├── WordCount.java
└── wc.jar

1 directory, 5 files
```

5

```
vim file01
```
```
Hello World Bye World
```
```
vim file01
```
```
Hello Hadoop Goodbye Hadoop
```

6

```
 hdfs dfs -mkdir -p wordcount/input &&
 hdfs dfs -ls /user/df/wordcount
```
```
Found 1 items
drwxr-xr-x   - df supergroup          0 2023-08-16 19:37 /user/df/wordcount/input
```

7

```
hdfs dfs -put file01 file02 wordcount/input &&
hdfs dfs -ls -R /user/df/wordcount
```
```
drwxr-xr-x   - df supergroup          0 2023-08-16 19:39 /user/df/wordcount/input
-rw-r--r--   1 df supergroup         22 2023-08-16 19:39 /user/df/wordcount/input/file01
-rw-r--r--   1 df supergroup         28 2023-08-16 19:39 /user/df/wordcount/input/file02
```

8

```
hdfs dfs -cat wordcount/input/{file01,file02}
```
```
Hello World Bye World
Hello Hadoop Goodbye Hadoop
```

9

```
hadoop jar wc.jar WordCount wordcount/input wordcount/output
```

10

```
hdfs dfs -cat wordcount/output/part-r-00000
```
```
Bye	1
Goodbye	1
Hadoop	2
Hello	2
World	2
```

## `WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable`

* https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/NativeLibraries.html
* https://cwiki.apache.org/confluence/display/HADOOP/Develop+on+Apple+Silicon+%28M1%29+macOS

```
hadoop checknative -a
```
```
2023-08-16 00:35:27,962 WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
Native library checking:
hadoop:  false
zlib:    false
zstd  :  false
bzip2:   false
openssl: false
ISA-L:   false
PMDK:    false
2023-08-16 00:35:28,000 INFO util.ExitUtil: Exiting with status 1: ExitException
```

```
 mvn package -Pdist,native -DskipTests -Dtar -Dmaven.javadoc.skip=true
```

## HDFS commands

[`hdfs dfs`](https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/HDFSCommands.html#dfs)
* `hdfs dfs -ls /home/foo` list the files in a directory
* `hdfs dfs -cat /home/foo/part-0001` view the contents of a file
* `hdfs dfs -get source destination` download from hdfs to local
* `hdfs dfs -put source destination` upload from local to hdfs
* https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/FileSystemShell.html

[`hdfs dfsadmin`](https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/HDFSCommands.html#dfsadmin)
* `hdfs dfsadmin -report`

[`hdfs fsck`](https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/HDFSCommands.html#fsck) file system check
* `hdfs fsck /`

## to review

Configuration
* `$HADOOP_HOME/etc/hadoop/hadoop-env.sh`
* `$HADOOP_HOME/etc/hadoop/core-site.xml`
* `$HADOOP_HOME/etc/hadoop/hdfs-site.xml`
* `$HADOOP_HOME/etc/hadoop/mapred-site.xml`
* `$HADOOP_HOME/etc/hadoop/yarn-site.xml`

Defaults
* `$HADOOP_HOME/share/doc/hadoop/hadoop-project-dist/hadoop-common/core-default.xml`
* `$HADOOP_HOME/share/doc/hadoop/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml`
* `$HADOOP_HOME/share/doc/hadoop/hadoop-mapreduce-client/hadoop-mapreduce-client-core/mapred-default.xml`
* `$HADOOP_HOME/share/doc/hadoop/hadoop-yarn/hadoop-yarn-common/yarn-default.xml`

Startup
* `hdfs --daemon start namenode`
* `hdfs --daemon start secondarynamenode`
* `hdfs --daemon start datanode`
* `yarn --daemon start resourcemanager`
* `yarn --daemon start nodemanager`
* `yarn --daemon start proxyserver`
* `mapred --daemon start historyserver`
* `jps` Lists the instrumented Java Virtual Machines (JVMs) on the target system. This command is experimental and unsupported.