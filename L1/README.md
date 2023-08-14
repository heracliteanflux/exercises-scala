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