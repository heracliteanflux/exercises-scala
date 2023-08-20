// pass a weather record as input to the mapper
// and check that the output is the year and temperature reading

package v2;

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

	@Test
	public void processesPositiveTemperatureRecord () throws IOException, InterruptedException {
		Text value = new Text("0043011990999991950051518004+68750+023550FM-12+0382" +
		                              // year ^^^^
													"99999V0203201N00261220001CN9999999N9+00111+99999999999");
													                      // temperature ^^^^^
		new MapDriver<LongWritable, Text, Text, IntWritable>() // configuration
		  .withMapper(new MaxTemperatureMapper())              //   mapper
			.withInput(new LongWritable(0), value)               //   input key and value
			.withOutput(new Text("1950"), new IntWritable(11))   //   expected output key and value
			.runTest();
	}

	//@Ignore
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
	public void ignoresSuspectQualityRecord () throws IOException, InterruptedException {
		Text value = new Text("0335999999433181957042302005+37950+139117SAO  +0004" +
		                              // year ^^^^
													"99999V0203201N00261220001CN9999999N9+00112+99999999999");
													                      // temperature ^^^^^
																								// suspect quality  ^
		new MapDriver<LongWritable, Text, Text, IntWritable>() // configuration
		  .withMapper(new MaxTemperatureMapper())              //   mapper
			.withInput(new LongWritable(0), value)               //   input key and value
			.runTest();
	}

}