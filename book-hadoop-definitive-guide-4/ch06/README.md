# Ch. 6 MapReduce

1

```zsh
vim ~/.zshrc
```
```zsh
# JAVA 8
export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk1.8.0_311.jdk/Contents/Home"
```

2

```zsh
sysinfo
```
```
------------------ Java Information ------------------
Java Version:    1.8.0_311
Java Vendor:     Oracle Corporation
Java home:       /Library/Java/JavaVirtualMachines/jdk1.8.0_311.jdk/Contents/Home/jre
```

3

```zsh
cd ch06 &&
mkdir -pv src/{main,test}/java/v{1..4}
```
```zsh
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

## Configuration API

Running jobs from the command line via helper classes
* `GenericOptionsParser` class - interprets common Hadoop command-line options and sets them on a `Configuration` object for the application to use them as desired.
* `Tool` interface - running the application via the `ToolRunner` uses `GenericOptionsParser` internally.

```java
public interface Tool extends Configurable {
	int run (String[] args) throws Exception;
}
```

1

```zsh
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

```zsh
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
			<version>4.11</version>
			<scope>test</scope>
		</dependency>
		<dependency>
		  <groupId>org.apache.mrunit</groupId>
			<artifactId>mrunit</artifactId>
			<version>1.1.0</version>
			<scope>test</scope>
			<classifier>hadoop2</classifier>
		</dependency>
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
				<version>3.1</version>
        <configuration>
				  <source>1.6</source>
					<target>1.6</target>
				</configuration>
			</plugin>
			<plugin>
			  <groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-jar-plugin</artifactId>
				<version>2.5</version>
				<configuration>
				  <outputDirectory>${basedir}</outputDirectory>
				</configuration>
			</plugin>
		</plugins>
	</build>
</project>
```

3

```zsh
mvn package
```

4

```zsh
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

```zsh
export HADOOP_CLASSPATH=target/classes/ &&
hadoop jar hadoop-examples.jar ConfigurationPrinter -conf conf/hadoop-localhost.xml | grep yarn.resourcemanager.address=
```
```
yarn.resourcemanager.address=localhost:8032
```

Option `-D` sets the configuration property with key `color` to the value `yellow`.

```zsh
hadoop jar hadoop-examples.jar ConfigurationPrinter -D color=yellow | grep color
```
```
color=yellow
```

Options set with `-D` take priority over properties set in configuration files.

The number of reducers for a MapReduce job can be set via `-D mapreduce.job.reduces=<n>`

6

Default settings for public properties
```zsh
find $HADOOP_HOME -type f -regex '.*default.xml'
```
```zsh
$HADOOP_HOME/share/doc/hadoop/hadoop-yarn/hadoop-yarn-common/yarn-default.xml
$HADOOP_HOME/share/doc/hadoop/hadoop-project-dist/hadoop-common/core-default.xml
$HADOOP_HOME/share/doc/hadoop/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml
$HADOOP_HOME/share/doc/hadoop/hadoop-mapreduce-client/hadoop-mapreduce-client-core/mapred-default.xml
```

Site-specific settings for public properties
```zsh
find $HADOOP_HOME -type f -regex '.*site.xml'
```
```zsh
$HADOOP_HOME/libexec/etc/hadoop/yarn-site.xml
$HADOOP_HOME/libexec/etc/hadoop/hdfs-site.xml
$HADOOP_HOME/libexec/etc/hadoop/core-site.xml
$HADOOP_HOME/libexec/etc/hadoop/mapred-site.xml
```

## Writing a Unit Test with MRUnit

### Version 1

```zsh
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
```zsh
vim src/main/java/v1/MaxTemperatureReducer.java
```
```java
package v1;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MaxTemperatureReducer 
  extends Reducer<Text, IntWritable, Text, IntWritable> {
		
	@Override
	public void reduce (Text key,
	   Iterable<IntWritable> values,
		               Context context)
	  throws IOException, InterruptedException {
			int maxValue = Integer.MIN_VALUE;
			for (IntWritable value : values) {
				maxValue = Math.max(maxValue, value.get());
			}
			context.write(key, new IntWritable(maxValue));
		}
}
```
```zsh
vim src/test/java/v1/MaxTemperatureMapperTest.java
```
```java
// pass a weather record as input to the mapper
// and check that the output is the year and temperature reading

package v1;

import java.io.IOException;
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

	@Ignore
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

	@Test
	public void processesMalformedTemperatureRecord () throws IOException, InterruptedException {
		Text value = new Text("0335999999433181957042302005+37950+139117SAO  +0004" +
		                              // year ^^^^
													"RJSN V02011359003150070356999999433201957010100005+353");
													                      // temperature ^^^^^
		new MapDriver<LongWritable, Text, Text, IntWritable>() // configuration
		  .withMapper(new MaxTemperatureMapper())              //   mapper
			.withInput(new LongWritable(0), value)               //   input key and value
			.withOutput(new Text("1957"), new IntWritable(1957)) //   expected output key and value
			.runTest();
	}

}
```
```zsh
vim src/test/java/v1/MaxTemperatureReducerTest.java
```
```java
package v1;

import java.io.IOException;
import java.lang.Object;
import java.util.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.*;

public class MaxTemperatureReducerTest extends Object {
	@Test
	public void returnsMaximumIntegerInValues () throws IOException, InterruptedException {
		new ReduceDriver<Text, IntWritable, Text, IntWritable>()
		  .withReducer(new MaxTemperatureReducer())
			.withInput(new Text("1950"), Arrays.asList(new IntWritable(10), new IntWritable(5)))
			.withOutput(new Text("1950"), new IntWritable(10))
			.runTest();
	}
}
```
```zsh
mvn test
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
[INFO] --- compiler:3.1:compile (default-compile) @ hadoop-book-mr-dev ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 5 source files to .../ch06/target/classes
[INFO]
[INFO] --- resources:3.3.1:testResources (default-testResources) @ hadoop-book-mr-dev ---
[INFO] skip non existing resourceDirectory .../ch06/src/test/resources
[INFO]
[INFO] --- compiler:3.1:testCompile (default-testCompile) @ hadoop-book-mr-dev ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- surefire:3.1.2:test (default-test) @ hadoop-book-mr-dev ---
[INFO] Using auto detected provider org.apache.maven.surefire.junit4.JUnit4Provider
[INFO]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running v1.MaxTemperatureMapperTest
[WARNING] Tests run: 3, Failures: 0, Errors: 0, Skipped: 1, Time elapsed: 0.696 s -- in v1.MaxTemperatureMapperTest
[INFO] Running v1.MaxTemperatureReducerTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.026 s -- in v1.MaxTemperatureReducerTest
[INFO]
[INFO] Results:
[INFO]
[WARNING] Tests run: 4, Failures: 0, Errors: 0, Skipped: 1
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  6.855 s
[INFO] Finished at: 2023-08-19T22:46:37-04:00
[INFO] ------------------------------------------------------------------------
```