package qsfactor;

import java.util.Iterator;
import java.io.IOException;
import java.math.BigInteger;
import java.lang.InterruptedException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

/*
 * This is the reducer class.
 */
public class Reduce extends Reducer<LongWritable, Text, Text, Text> 
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
	 * Performs the factorization of n using the sieved InputArrays(from Map tasks).
	 * There is only one reducer task since this part is part(sieving is slow).
	 * If factorization succeeds results will be written to the output file.
	 */
	public void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException  
	{   
		InputArray allFactors = null;
		Iterator<Text> valuesIterator = values.iterator();
		  
		while (valuesIterator.hasNext()) 
		{      
			String value = valuesIterator.next().toString();
			InputArray sievedIntegers = new InputArray(value, 0);
	
		    if (allFactors == null) 
		    {	    	  
		    	allFactors = sievedIntegers;
		    } 
		    else 
		    {
		    	allFactors.add(sievedIntegers);
		    }
		}

		BigInteger factor = null;
		int attemptsLimit = Config.FIND_SQUARE_ATTEMPTS_LIMIT;
		  
		for (int attempt = 1; attempt < attemptsLimit; attempt++) 
		{
			InputArray squareFactors = null;
			try 
			{
				squareFactors = MathOperations.findSquare(allFactors, attempt, factorBase);
			} 
			catch (Matrix.EquationException e) 
			{
				continue;
			}
			if (squareFactors == null) 
			{
				continue;
			}
	
			factor = MathOperations.tryFactor(n, squareFactors);
	      
			if (factor != null) 
			{ 
				break;
			}   
		}
	
		if (factor == null) 
		{
			
			System.out.println("Factorization of n failed after " + + attemptsLimit + " attempts.");
		}
		else
		{	  
			System.out.println("Successfully factorized n. Results will be written to the output file.");
			Text factor1Key = new Text("P");
			Text factor2Key = new Text("Q");
			Text factor1Value = new Text(factor.toString());
			Text factor2Value = new Text(n.divide(factor).toString());
		    context.write(factor1Key, factor1Value);
			context.write(factor2Key, factor2Value); 
		}	  
	}
	  
	private BigInteger n;
	private FactorBase factorBase;
}

