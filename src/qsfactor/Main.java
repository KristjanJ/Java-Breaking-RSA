package qsfactor;

import java.math.BigInteger;

/*
 * This is the Main class. It contains the entry point of the QSFactor program.
 */
public class Main 
{
	/*
	 * Main method.
	 */
	public static void main(String[] args)
	{
		try
		{
			if (args.length >= 1)
			{
				if (args.length == 3)
				{
					Config.FACTORIZER_INPUT_DIR = args[1];
					Config.FACTORIZER_OUTPUT_DIR = args[2];
				}
				
				BigInteger n = new BigInteger(args[0]);
				System.out.println("FACTORIZING n: " + n.toString());
				System.out.println("FACTORIZER_INPUT_DIR: " + Config.FACTORIZER_INPUT_DIR);
				System.out.println("FACTORIZER_INPUT_FILE: " + Config.FACTORIZER_INPUT_FILE);
				System.out.println("FACTORIZER_OUTPUT_DIR: " + Config.FACTORIZER_OUTPUT_DIR);
				System.out.println();
				
				MapReduceFactorizer mapReduceFactorizer = new MapReduceFactorizer(n);
				mapReduceFactorizer.run();
			}
			else
			{
				System.out.println("ERROR: Invalid number of arguments.");
				System.out.println("Usage: java Main [n] [input_dir (optional)] [output_dir (optional)]");
				System.out.println("input_dir_default = " + Config.FACTORIZER_INPUT_DIR + " output_dir_default = " + Config.FACTORIZER_OUTPUT_DIR);
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
