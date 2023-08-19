# _Hadoop: The Definitive Guide_

## Ch. 2 MapReduce

### `max_temperature.sh`

https://github.com/tomwhite/hadoop-book/issues/35

```
vim max_temperature.sh
```
```bash
#!/usr/bin/env bash
for year in all/*
do
  echo -ne `basename $year .gz`"\t"
	gunzip -c $year | \
	  awk '{ temp = substr($0, 88, 5) + 0;
		       q    = substr($0, 93, 1);
				   if (temp != 9999 && q ~ /[01459]/ && temp > max) max = temp }
			   END { print max }'
done
```
```
cp -r input/ncdc/all .       &&
chmod 744 max_temperature.sh &&
./max_temperature.sh
```
```
1901	317
1902	244
```

### Java MapReduce

```
vim MaxTemperatureMapper.java
```
```java
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MaxTemperatureMapper
  // input key    - long integer offset              - LongWritable (Java Long)
	// input value  - a line of text                   - Text         (Java String)
	// output key   - a year                           - Text         (Java String)
	// output value - an air temperature as an integer - IntWritable  (Java Integer)
  extends Mapper<LongWritable, Text, Text, IntWritable> {
	private static final int MISSING = 9999;

	@Override
	public void map (LongWritable key,
	                         Text value,
									      Context context)
	  throws IOException, InterruptedException
	{
		String line = value.toString();
		String year = line.substring(15, 19);
				int airTemperature;

		if (line.charAt(87) == '+') {
			airTemperature = Integer.parseInt(line.substring(88, 92));
		}
		else {
			airTemperature = Integer.parseInt(line.substring(87, 92));
		}

		String quality = line.substring(92, 93);
		if (airTemperature != MISSING && quality.matches("[01459]")) {
			context.write(new Text(year), new IntWritable(airTemperature));
		}
	}
}
```

```
vim MaxTemperatureReducer.java
```
```java
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MaxTemperatureReducer
  // input key    - matches mapper output - Text         (Java String)
	// input value  - matches mapper output - IntWritable  (Java Integer)
	// output key   - year                  - Text         (Java String)
	// output value - maximum temperature   - IntWritable  (Java Integer)
  extends Reducer<Text, IntWritable, Text, IntWritable> {
	
	@Override
	public void reduce (Text key, Iterable<IntWritable> values, Context context)
	  throws IOException, InterruptedException
	{
		int maxValue = Integer.MIN_VALUE;
		for (IntWritable value : values) {
			maxValue = Math.max(maxValue, value.get());
		}
		context.write(key, new IntWritable(maxValue));
	}
}
```
```
vim MaxTemperature.java
```
```java
import java.lang.Object;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MaxTemperature extends Object {
	public static void main (String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: MaaxTemperature <input path> <output path>");
			System.exit(-1);
		}
		Job job = new Job();
		job.setJarByClass(MaxTemperature.class);
		job.setJobName("Max temperature");
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(MaxTemperatureMapper.class);
		job.setReducerClass(MaxTemperatureReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
```

```
hadoop jar hadoop-examples.jar MaxTemperature input/ncdc/sample.txt output &&
cat output/part-r-00000
```
```
1949	111
1950	22
```

```
vim MaxTemperatureWithCombiner.java
```
```java
import java.lang.Object;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MaxTemperatureWithCombiner extends Object {
	public static void main (String[] args) throws Exception {
    if (args.length != 2) {
			System.err.println("Usage: MaxTemperatureWithCombiner <input path> <output path>");
			System.exit(-1);
		}
		Job job = new Job();
		job.setJarByClass(MaxTemperatureWithCombiner.class);
		job.setJobName("Max temperature");
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(MaxTemperatureMapper.class);
		job.setCombinerClass(MaxTemperatureReducer.class);
		job.setReducerClass(MaxTemperatureReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
```

```
hadoop jar hadoop-examples.jar MaxTemperatureWithCombiner input/ncdc/sample.txt output &&
cat output/part-r-00000
```
```
1949	111
1950	22
```

### Hadoop Streaming

#### Ruby

```
vim max_temperature_map.rb
```
```rb
#!/usr/bin/env ruby

STDIN.each_line do |line|
	val = line
	year, temp, q = val[15, 4], val[87, 5], val[92, 1]
	puts "#{year}\t#{temp}" if (temp != "+9999" && q =~ /[01459]/)
end
```
```
chmod 744 max_temperature_map.rb
cat input/ncdc/sample.txt | ./max_temperature_map.rb
```
```
1950	+0000
1950	+0022
1950	-0011
1949	+0111
1949	+0078
```
```
vim max_temperature_reduce.rb
```
```rb
#!/usr/bin/env ruby

last_key, max_val = nil, -1000000
STDIN.each_line do |line|
	key, val = line.split("\t")
	if last_key && last_key != key
		puts "#{last_key}\t#{max_val}"
		last_key, max_val = key, val.to_i
	else
		last_key, max_val = key, [max_val, val.to_i].max
	end
end
puts "#{last_key}\t#{max_val}" if last_key
```
```
cat input/ncdc/sample.txt \
| ./max_temperature_map.rb \
sort \
| ./max_temperature_reduce.rb
```
```
1950	22
1949	111
```
standalone mode
```
hadoop jar $HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-3.3.6.jar \
-input input/ncdc/sample.txt \
-output output \
-mapper max_temperature_map.rb \
-reducer max_temperature_reduce.rb
```
cluster mode
```
hadoop jar $HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-3.3.6.jar \
-files max_temperature_map.rb,max_temperature_reduce.rb \
-input input/ncdc/all \
-output output \
-mapper max_temperature_map.rb \
-combiner max_temperature_reduce.rb \
-reducer max_temperature_reduce.rb
```
```
cat output/part-00000
```
```
1901	317
1902	244
```

#### Python

```
vim max_temperature_map.py
```
```python
#!/usr/bin/env python

import re
import sys

for line in sys.stdin:
	val = line.strip()
	year, temp, q = val[15:19], val[87:92], val[92:93]
	if temp != '+9999' and re.match('[01459]', q):
		print(f'{year}\t{temp}')
```
```
vim max_temperature_reduce.py
```
```python
#!/usr/bin/env python

import sys

last_key, max_val = None, -sys.maxsize
for line in sys.stdin:
	key, val = line.strip().split('\t')
	if last_key and last_key != key:
		print(f'{last_key}\t{max_val}')
		last_key, max_val = key, int(val)
	else:
		last_key, max_val = key, max(max_val, int(val))

if last_key:
	print(f'{last_key}\t{max_val}')
```
```
chmod 744 max_temperature_map.py max_temperature_reduce.py &&
cat input/ncdc/sample.txt | \
./max_temperature_map.py | \
sort | \
./max_temperature_reduce.py
```
```
1949	111
1950	22
```

## Ch. 3 HDFS

```
hdfs dfs -copyFromLocal input/docs/quangle.txt quangle.txt &&
hdfs dfs -ls
```
```
Found 1 items
-rw-r--r--   1 df supergroup        119 2023-08-18 15:31 quangle.txt
```
```
hdfs dfs -copyToLocal quangle.txt quangle.copy.txt &&
md5 input/docs/quangle.txt quangle.copy.txt
```
```
MD5 (input/docs/quangle.txt) = e7891a2627cf263a079fb0f18256ffb2
MD5 (quangle.copy.txt) = e7891a2627cf263a079fb0f18256ffb2
```
```
hdfs dfs -mkdir books &&
hdfs dfs -ls
```
```
Found 2 items
drwxr-xr-x   - df supergroup          0 2023-08-18 15:37 books
-rw-r--r--   1 df supergroup        119 2023-08-18 15:31 quangle.txt
```
```
hdfs dfs -ls file:///
```
```
Found 18 items
----------   1 root admin          0 2023-07-11 04:56 file:///.file
drwxr-xr-x   - root wheel         64 2023-07-11 04:56 file:///.vol
drwxrwxr-x   - root admin       4640 2023-08-18 03:43 file:///Applications
drwxr-xr-x   - root wheel       2432 2023-07-28 12:21 file:///Library
drwxr-xr-x   - root wheel        320 2023-07-11 04:56 file:///System
drwxr-xr-x   - root admin        224 2023-08-15 15:46 file:///Users
drwxr-xr-x   - root wheel         96 2023-08-15 18:40 file:///Volumes
drwxr-xr-x   - root wheel       1248 2023-07-11 04:56 file:///bin
drwxr-xr-x   - root wheel         64 2021-09-18 02:26 file:///cores
dr-xr-xr-x   - root wheel       8190 2023-08-15 15:39 file:///dev
drwxr-xr-x   - root wheel       2528 2023-08-17 17:36 file:///etc
dr-xr-xr-x   - root wheel          1 2023-08-15 15:39 file:///home
drwxr-xr-x   - root wheel        128 2023-05-09 10:03 file:///opt
drwxr-xr-x   - root wheel        192 2023-08-15 15:39 file:///private
drwxr-xr-x   - root wheel       2048 2023-07-11 04:56 file:///sbin
drwxrwxrwt   - root wheel        512 2023-08-18 14:38 file:///tmp
drwxr-xr-x   - root wheel        352 2023-07-11 04:56 file:///usr
drwxr-xr-x   - root wheel       1152 2023-08-15 15:39 file:///var
```
```
vim URLCat.java
```
```java
import java.lang.Object;

public class URLCat extends Object {
	static {
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
	}
	public static void main (String[] args) throws Exception {
		InputStream in = null;
		try {
			in = new URL(args[0]).openStream();
			IOUtils.copyBytes(in, System.out, 4096, false);
		}
		finally {
			IOUtils.closeStream(in);
		}
	}
}
```
```
 hadoop jar hadoop-examples.jar URLCat hdfs://localhost:9000/user/df/quangle.txt
```
or
```
hadoop jar hadoop-examples.jar URLCat hdfs:///user/df/quangle.txt
```
```
On the top of the Crumpetty Tree
The Quangle Wangle sat,
But his face you could not see,
On account of his Beaver Hat.
```
```
vim FileSystemCat.java
```
```java
import java.lang.Object;

public class FileSystemCat extends Object {
	public static void main (String[] args) throws Exception {
		String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		InputStream in = null;
		try {
			in = fs.open(new Path(uri));
			IOUtils.copyBytes(in, System.out, 4096, false);
		}
		finally {
			IOUtils.closeStream(in);
		}
	}
}
```
```
hadoop jar hadoop-examples.jar FileSystemCat hdfs://localhost:9000/user/df/quangle.txt
```
```
On the top of the Crumpetty Tree
The Quangle Wangle sat,
But his face you could not see,
On account of his Beaver Hat.
```
```
vim FileSystemDoubleCat.java
```
```java
import java.lang.Object;

public class FileSystemDoubleCat extends Object {
	public static void main (String[] args) throws Exception {
    String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		FSDataInputStream in = null;
		try {
			in = fs.open(new Path(uri));
			IOUtils.copyBytes(in, System.out, 4096, false);
			in.seek(0);
			IOUtils.copyBytes(in, System.out, 4096, false);
		}
		finally {
			IOUtils.closeStream(in);
		}
	} 
}
```
```
 hadoop jar hadoop-examples.jar FileSystemDoubleCat hdfs://localhost:9000/user/df/quangle.txt
```
```
On the top of the Crumpetty Tree
The Quangle Wangle sat,
But his face you could not see,
On account of his Beaver Hat.
On the top of the Crumpetty Tree
The Quangle Wangle sat,
But his face you could not see,
On account of his Beaver Hat
```
```
vim FileCopyWithProgress.java
```
```java
import java.lang.Object;

public class FileCopyWithProgress extends Object {
	public static void main (String[] args) throws Exception {
		String localSrc = args[0];
		String dst      = args[1];
		InputStream in  = new BufferedInputStream(new FileInputStream(localSrc));
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dst), conf);
		OutputStream out = fs.create(new Path(dst), new Progressable() {
			public void progress () {
				System.out.print(".");
			}
		});
		IOUtils.copyBytes(in, out, 4096, true);
	}
}
```
```
hadoop jar hadoop-examples.jar FileCopyWithProgress input/docs/1400-8.txt hdfs://localhost:9000/user/df/1400-8.txt
```
```
..................
```
```
hdfs dfs -ls
```
```
Found 3 items
-rw-r--r--   1 df supergroup    1033751 2023-08-18 22:28 1400-8.txt
drwxr-xr-x   - df supergroup          0 2023-08-18 15:37 books
-rw-r--r--   1 df supergroup        119 2023-08-18 15:31 quangle.txt
```
```
vim ShowFileStatusTest.java
```
```java
import java.lang.Object;

public class ShowFileStatusTest extends Object {
	private MiniDFSCluster cluster;
	private FileSystem fs;

	@Before
	public void setUp () throws Exception {
		Configuration conf = new Configuration();
		if (System.getProperty("test.build.data") == null) {
			System.getProperty("test.build.data", "/tmp");
		}
		cluster = new MiniDFSCluster.Builder(conf).build();
		fs = cluster.getFileSystem();
		OutputStream out = fs.create(new Path("/dir/file"));
		out.write("content".getBytes("UTF-8"));
		out.close();
	}

	@After
	public void tearDown () throws IOException {
		if (fs != null) { fs.close(); }
		if (cluster != null) { cluster.shutdown(); }
	}

	@Test (expected = FileNotFoundException.class)
	public void throwsFileNotFoundForNonExistentFile () throws IOException {
		fs.getFileStatus(new Path("no-such-file"));
	}

	@Test
	public void fileStatusForFile () throws IOException {
		Path file = new Path("/dir/file");
		FileStatus stat = fs.getFileStatus(file);
		assertThat(stat.getPath().toUri().getPath(), is("/dir/file"));
		assertThat(stat.isDirectory(), is(false));
		assertThat(stat.getLen(), is(7L));
		assertThat(stat.getModificationTime(), is(lessThanOrEqualTo(System.currentTimeMillis())));
		assertThat(stat.getReplication(), is((short) 1));
		assertThat(stat.getBlockSize(), is(128 * 1024 * 1024L));
		assertThat(stat.getOwner(), is(System.getProperty("user.name")));
		assertThat(stat.getGroup(), is("supergroup"));
		assertThat(stat.getPermission().toString(), is("rw-r--r--"));
	}

	@Test
	public void fileStatusForDirectory () throws IOException {
		Path dir = new Path("/dir");
		FileStatus stat = fs.getFileStatus(dir);
		assertThat(stat.getPath().toUri().getPath(), is("/dir"));
		assertThat(stat.isDirectory(), is(true));
		assertThat(stat.getLen(), is(0L));
		assertThat(stat.getModificationTime(), is(lessThanOrEqualTo(System.currentTimeMillis())));
		assertThat(stat.getReplication(), is((short) 0));
		assertThat(stat.getBlockSize(), is(0L));
		assertThat(stat.getOwner(), is(System.getProperty("user.name")));
		assertThat(stat.getGroup(), is("supergroup"));
		assertThat(stat.getPermission().toString(), is("rwxr-xr-x"));
	}
}
```
```
vim ListStatus.java
```
```java
import java.lang.Object;

public class ListStatus extends Object {
	public static void main (String[] args) throws Exception {
    String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		Path[] paths = new Path[args.length];
		for (int i = 0; i < paths.length; i++) {
			paths[i] = new Path(args[i]);
		}
		FileStatus[] status = fs.listStatus(paths);
		Path[] listedPaths = FileUtil.stat2Paths(status);
		for (Path p : listPaths) {
			System.out.println(p);
		}
	}
}
```
```
hadoop jar hadoop-examples.jar ListStatus hdfs://localhost:9000/ hdfs://localhost:9000/user/df
```
```
hdfs://localhost:9000/user
hdfs://localhost:9000/user/df/1400-8.txt
hdfs://localhost:9000/user/df/books
hdfs://localhost:9000/user/df/quangle.txt
```