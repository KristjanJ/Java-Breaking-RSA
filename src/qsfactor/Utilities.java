package qsfactor;

import java.math.BigInteger;
import java.util.ArrayList;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;

/*
 * This class contains methods to access the HDFS.
 */
public class Utilities 
{
	/*
	 * Writes sieve ranges that are used by the mapper.
	 * Sieve range has the size of SIEVE_RANGE_SIZE or less.
	 */
	public static void writeSieveRanges(BigInteger n) throws Exception 
	{
		Configuration configuration = MapReduceFactorizer.configuration();
		FileSystem fileSystem = FileSystem.get(configuration);	
		Path inputFilePath = new Path(Config.FACTORIZER_INPUT_DIR + "/" + Config.FACTORIZER_INPUT_FILE);
		SequenceFile.Writer writer = SequenceFile.createWriter(fileSystem, configuration, inputFilePath, LongWritable.class, Text.class, SequenceFile.CompressionType.NONE);
		
		BigInteger totalSieveRangeSize = MathOperations.totalSieveRangeSize(n);
		BigInteger squareRoot = MathOperations.sqrt(n);
		BigInteger start = squareRoot.subtract(totalSieveRangeSize.divide(BigInteger.valueOf(2)));
		int sieveRangeSize = Config.SIEVE_RANGE_SIZE;
		ArrayList<BigInteger> integerArray = new ArrayList<BigInteger>(sieveRangeSize);
		ArrayList<BigInteger> evaluationArray = new ArrayList<BigInteger>(sieveRangeSize);
		BigInteger factorIndex = BigInteger.ZERO;
		int rangeIndex = 0;	
		LongWritable key = new LongWritable(0);
		Text value = new Text();
		
		do 
		{
			BigInteger integer = start.add(factorIndex);
			BigInteger evaluation = integer.pow(2).subtract(n);
			integerArray.add(integer);
			evaluationArray.add(evaluation);      
			factorIndex = factorIndex.add(BigInteger.ONE);
			rangeIndex++;

			if (rangeIndex == sieveRangeSize) 
			{
				InputArray inputArray = new InputArray(integerArray, evaluationArray); 
				value.set(inputArray.toString());
				writer.append(key, value);
				integerArray.clear();
				evaluationArray.clear();
				rangeIndex = 0;
			} 
			else if (factorIndex.compareTo(totalSieveRangeSize) == 0) 
			{
				InputArray inputArray = new InputArray(integerArray, evaluationArray);
				value.set(inputArray.toString());
				writer.append(key, value);
			}
		} 
		while (factorIndex.compareTo(totalSieveRangeSize) < 0);

		writer.close();
	}
	
	/*
	 * Reads result factors from reducer's output.
	 */
	public static ArrayList<BigInteger> readFactors() throws Exception
	{
		Configuration configuration = MapReduceFactorizer.configuration();
		FileSystem fileSystem = FileSystem.get(configuration);
		Path path = new Path(Config.FACTORIZER_OUTPUT_DIR);
		
		ArrayList<BigInteger> factors = new ArrayList<BigInteger>();
		
		if(!fileSystem.isFile(path))
		{
			FileStatus[] listFiles = fileSystem.listStatus(path);
			
			for(int i = 0; i < listFiles.length; i++)
			{
				try
				{
					SequenceFile.Reader reader = new SequenceFile.Reader(fileSystem, listFiles[i].getPath(), configuration);
					Text key = new Text();
					Text value  = new Text();
					
					while (reader.next(key, value)) 
					{
						factors.add(new BigInteger(value.toString()));
					}
					
					reader.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return factors;	
	}
}
