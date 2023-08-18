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