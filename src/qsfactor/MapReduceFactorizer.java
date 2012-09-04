package qsfactor;

import java.math.BigInteger;
import java.util.ArrayList;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

/*
 * This is the controller class that controls the MapReduce job.
 */
public class MapReduceFactorizer 
{
	/*
	 * Constructor. Takes the input parameter n and saves it into configuration.
	 */
	public MapReduceFactorizer(BigInteger n)
	{
		configuration = new Configuration();
		configuration.set(Config.N, n.toString());
	}
	
	/*
	 * Run method. Writes the InputArrays(sieve ranges) for the mapper. In the end gets results from reducer's output.
	 */
	public int run() throws Exception
	{
		BigInteger n = new BigInteger(configuration.get(Config.N));	
		FactorBase factorBase = MathOperations.factorBase(n);
		configuration.set(Config.FACTOR_BASE, factorBase.toString());	
		Utilities.writeSieveRanges(n);
		
		Job job = new Job(configuration, "Factorizer");
	    job.setJarByClass(MapReduceFactorizer.class);
	    job.setMapperClass(Map.class);
	    job.setReducerClass(Reduce.class);  
	    job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);	
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Text.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);    
	    FileInputFormat.addInputPath(job, new Path(Config.FACTORIZER_INPUT_DIR));
	    FileOutputFormat.setOutputPath(job, new Path(Config.FACTORIZER_OUTPUT_DIR));
	    
		boolean success = job.waitForCompletion(true);
		
		ArrayList<BigInteger> factors = Utilities.readFactors();
		
		for (BigInteger factor : factors)
		{
			System.out.println("FACTOR: " + factor.toString());
		}
		
		return success ? 1 : 0;
	}
	
	/*
	 * Returns the configuration.
	 */
	public static Configuration configuration()
	{
		return configuration;
	}
	
	static
	{
		configuration = null;
	}
	
	private static Configuration configuration;
}
