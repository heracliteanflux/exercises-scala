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