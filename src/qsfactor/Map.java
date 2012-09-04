package qsfactor;

import java.io.IOException;
import java.math.BigInteger;
import java.lang.InterruptedException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*
 * This is the mapper class.
 */
public class Map extends Mapper<LongWritable, Text, LongWritable, Text> 
{
	/*
	 * Setup.
	 */
	protected void setup(Context context) throws IOException 
	{
	    n = new BigInteger(context.getConfiguration().get(Config.N));
	    factorBase = new FactorBase(context.getConfiguration().get(Config.FACTOR_BASE));
	}

	/*
	 * Receives instances of InputArrays as their string representations, performs the sieve and then outputs the sieved InputArray.
	 */
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
	{
		//System.out.println(value.toString());
		//if (1 == 1) return;
		
		InputArray inputArray = new InputArray(value.toString(), 0);
	    InputArray sieved = MathOperations.performSieve(n, inputArray, factorBase);
	    LongWritable outputKey = new LongWritable(0);
	    Text outputValue = new Text(sieved.toString());
	    context.write(outputKey, outputValue);
	}
	
	private BigInteger n;
	private FactorBase factorBase;
}

