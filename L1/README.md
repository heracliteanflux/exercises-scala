### HDFS

```
hdfs dfs -mkdir hdfs_lab1

hdfs dfs -put f1.txt f2.txt f3.txt hdfs_lab1

hdfs dfs -mkdir hdfs_lab1a
hdfs dfs -mv hdfs_lab1/* hdfs_lab1a

hdfs dfs -rm -r hdfs_lab1a

hdfs dfs -mkdir hdfs_lab1b
hdfs dfs -put war_and_peace.txt hdfs_lab1b

hdfs dfs -tail hdfs_lab1b/war_and_peace.txt

hdfs dfs -stat "blocksize:%o repication:%r owner:%u mdate:%y" hdfs_lab1b/war_and_peace.txt
```

#### local macOS usage

Configuration
* `echo "HADOOP_HOME=\"/opt/homebrew/Cellar/hadoop/3.3.3/libexec\"" >> ~/.zshrc`

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