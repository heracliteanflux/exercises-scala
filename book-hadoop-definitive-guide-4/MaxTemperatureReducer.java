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