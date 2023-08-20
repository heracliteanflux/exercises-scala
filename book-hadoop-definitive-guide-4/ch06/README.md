# Ch. 6 MapReduce

Running jobs from the command line via helper classes
* `GenericOptionsParser` class - interprets common Hadoop command-line options and sets them on a `Configuration` object for the application to use them as desired.
* `Tool` interface - running the application via the `ToolRunner` uses `GenericOptionsParser` internally.

```java
public interface Tool extends Configurable {
	int run (String[] args) throws Exception;
}
```

1

```
mkdir -p conf src/main/java &&
vim conf/hadoop-localhost.xml
```
```xml
<?xml version="1.0"?>

<!-- points to a namenode and a YARN resource manager both running on localhost -->
<configuration>

  <property>
	  <name>fs.defaultFS</name>
		<value>hdfs://localhost:9000/</value>
	</property>

	<property>
	  <name>mapreduce.framework.name</name>
		<value>yarn</value>
	</property>

	<property>
	  <name>yarn.resourcemanager.address</name>
		<value>localhost:8032</value>
	</property>

</configuration>
```

2

```
vim pom.xml
```
```xml
<project>
  <modelVersion>4.0.0</modelVersion>
	<groupId>com.hadoopbook</groupId>
	<artifactId>hadoop-book-mr-dev</artifactId>
	<packaging>jar</packaging>
	<version>4.0</version>

	<properties>
	  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<hadoop.version>3.3.6</hadoop.version>
	</properties>

	<dependencies>
	  <!-- Hadoop main client artifact -->
		<dependency>
		  <groupId>org.apache.hadoop</groupId>
			<artifactId>hadoop-client</artifactId>
			<version>${hadoop.version}</version>
		</dependency>
		<!-- Unit test artifacts -->
		<dependency>
		  <groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.13.2</version>
			<scope>test</scope>
		</dependency>
		<!-- <dependency>
		  <groupId>org.apache.mrunit</groupId>
			<artifactId>mrunit</artifactId>
			<version>1.1.0</version>
			<scope>test</scope>
		</dependency> -->
		<!-- Hadoop test artifact for running mini clusters -->
		<dependency>
		  <groupId>org.apache.hadoop</groupId>
			<artifactId>hadoop-minicluster</artifactId>
			<version>${hadoop.version}</version>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
	  <finalName>hadoop-examples</finalName>
		<plugins>
		  <plugin>
			  <groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.11.0</version>
        <configuration>
				  <source>1.8</source>
					<target>1.8</target>
				</configuration>
			</plugin>
			<plugin>
			  <groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-jar-plugin</artifactId>
				<version>3.3.0</version>
				<configuration>
				  <outputDirectory>${basedir}</outputDirectory>
				</configuration>
			</plugin>
		</plugins>
	</build>
</project>
```

3

```
mvn package
```

4

```
vim src/main/java/ConfigurationPrinter.java
```
```java
// print the keys and values of all the properties in the `Tool`'s `Configuration` object

import java.util.Map.Entry;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.util.*;

// class `Configurationprinter` is a subclass of class `Configured`
// class `Configured` is an implementation of interface `Configurable`
// implementations of interface `Tool` must implement interface `Configurable` because `Tool` extends `Configurable`
// subclassing class `Configured` is often the easiest way to achieve this
public class ConfigurationPrinter extends Configured implements Tool {
	
	// the static block ensures that the HDFS, YARN, and MapReduce configurations are applied
	// in addition to core configurations which `Configuration` already knows about
	static {
		Configuration.addDefaultResource("hdfs-default.xml");
		Configuration.addDefaultResource("hdfs-site.xml");
    Configuration.addDefaultResource("yarn-default.xml");
    Configuration.addDefaultResource("yarn-site.xml");
    Configuration.addDefaultResource("mapred-default.xml");
    Configuration.addDefaultResource("mapred-site.xml");
	}

	// method `run()` obtains the `Configuration` using `Configurable`'s `getConf()` method
	// and then iterates over it and prints each property to standard output
	@Override
	public int run (String[] args) throws Exception {
		Configuration conf = getConf();
		for (Entry<String, String> entry : conf) {
			System.out.printf("%s=%s\n", entry.getKey(), entry.getValue());
		}
		return 0;
	}

	// method `main()` does not invoke its own `run()` method directly
	// `ToolRunner`'s static method `run()` is called, which takes care of creating a `Configuration` object for the `Tool` before calling its `run()` method
	// `ToolRunner` uses a `GenericOptionsParser` to pick up any standard options specified on the command line
	// and to set them on the `Configuration` instance
	public static void main (String[] args) throws Exception {
		int exitCode = ToolRunner.run(new ConfigurationPrinter(), args);
		System.exit(exitCode);
	}
}
```

5

```
export HADOOP_CLASSPATH=target/classes/ &&
hadoop jar hadoop-examples.jar ConfigurationPrinter -conf conf/hadoop-localhost.xml | grep yarn.resourcemanager.address=
```
```
yarn.resourcemanager.address=localhost:8032
```

Option `-D` sets the configuration property with key `color` to the value `yellow`.

```
hadoop jar hadoop-examples.jar ConfigurationPrinter -D color=yellow | grep color
```
```
color=yellow
```

Options set with `-D` take priority over properties set in configuration files.

The number of reducers for a MapReduce job can be set via `-D mapreduce.job.reduces=<n>`

6

Default settings for public properties
```
find $HADOOP_HOME -type f -regex '.*default.xml'
```
```
$HADOOP_HOME/share/doc/hadoop/hadoop-yarn/hadoop-yarn-common/yarn-default.xml
$HADOOP_HOME/share/doc/hadoop/hadoop-project-dist/hadoop-common/core-default.xml
$HADOOP_HOME/share/doc/hadoop/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml
$HADOOP_HOME/share/doc/hadoop/hadoop-mapreduce-client/hadoop-mapreduce-client-core/mapred-default.xml
```

Site-specific settings for public properties
```
find $HADOOP_HOME -type f -regex '.*site.xml'
```
```
$HADOOP_HOME/libexec/etc/hadoop/yarn-site.xml
$HADOOP_HOME/libexec/etc/hadoop/hdfs-site.xml
$HADOOP_HOME/libexec/etc/hadoop/core-site.xml
$HADOOP_HOME/libexec/etc/hadoop/mapred-site.xml
```

## Writing a Unit Test with MRUnit

1

```
cd ch06 &&
mkdir -pv src/{main,test}/java/v{1..4}
```
```
src
src/main
src/main/java
src/main/java/v1
src/main/java/v2
src/main/java/v3
src/main/java/v4
src/test
src/test/java
src/test/java/v1
src/test/java/v2
src/test/java/v3
src/test/java/v4
```

2

```
vim src/main/java/v1/MaxTemperatureMapper.java
```
```java
package v1;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class MaxTemperatureMapper
  extends Mapper<LongWritable, Text, Text, IntWritable> {
		
	@Override
	public void map (LongWritable key,
	                         Text value,
											  Context context)
		throws IOException, InterruptedException {

		String line           = value.toString();
		String year           = line.substring(15, 19);
		   int airTemperature = Integer.parseInt(line.substring(87, 92));
		context.write(new Text(year), new IntWritable(airTemperature));
	}
}
```

```
vim src/test/java/v1/MaxTemperatureMapperTest.java
```
```java
// pass a weather record as input to the mapper
// and check that the output is the year and temperature reading

package v1;

import java.io.Exception;
import java.lang.Object;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.*;

public class MaxTemperatureMapperTest extends Object {

	@Test
	public void processesValidRecord () throws IOException, InterruptedException {
		Text value = new Text("0043011990999991950051518004+68750+023550FM-12+0382" +
		                              // year ^^^^
													"99999V0203201N00261220001CN9999999N9-00111+99999999999");
													                      // temperature ^^^^^
		new MapDriver<LongWritable, Text, Text, IntWritable>() // configuration
		  .withMapper(new MaxTemperatureMapper())              //   mapper
			.withInput(new LongWritable(0), value)               //   input key and value
			.withOutput(new Text("1950"), new IntWritable(-11))  //   expected output key and value
			.runTest();
	}

	@Test
	public void ignoresMissingTemperatureRecord () throws IOException, InterruptedException {
		Text value = new Text("0043011990999991950051518004+68750+023550FM-12+0382" +
		                              // year ^^^^
													"99999V0203201N00261220001CN9999999N9+99991+99999999999");
													                      // temperature ^^^^^
																								// missing values are represented by value `+9999`
		new MapDriver<LongWritable, Text, Text, IntWritable>() // configuration
		  .withMapper(new MaxTemperatureMapper())              //   mapper
			.withInput(new LongWritable(0), value)               //   input key and value
			.runTest();
	}

}
```

3

```
 mvn compile
 ```
 ```log
[INFO] Scanning for projects...
[INFO]
[INFO] -----------------< com.hadoopbook:hadoop-book-mr-dev >------------------
[INFO] Building hadoop-book-mr-dev 4.0
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- resources:3.3.1:resources (default-resources) @ hadoop-book-mr-dev ---
[INFO] skip non existing resourceDirectory .../ch06/src/main/resources
[INFO]
[INFO] --- compiler:3.11.0:compile (default-compile) @ hadoop-book-mr-dev ---
[INFO] Changes detected - recompiling the module! :source
[INFO] Compiling 2 source files with javac [debug target 1.8] to target/classes
[WARNING] bootstrap class path not set in conjunction with -source 8
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.930 s
[INFO] Finished at: 2023-08-19T20:30:28-04:00
[INFO] ------------------------------------------------------------------------
```