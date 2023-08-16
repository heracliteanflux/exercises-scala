# Setting up a single node cluster on macOS

https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/SingleCluster.html

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

3

```
hadoop version
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

[Standalone Op](https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/SingleCluster.html)

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

[Standalone Op](https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/SingleCluster.html)

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

3 - Configure ssh

```
ssh localhost
```

4

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

### `WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable`

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

TO REVIEW

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

Monitoring
* `hdfs dfsadmin -report`
* `hdfs fsck /`