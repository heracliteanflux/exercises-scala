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