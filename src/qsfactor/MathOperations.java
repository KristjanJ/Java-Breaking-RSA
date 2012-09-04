package qsfactor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;

/*
 * This class contains different math operations and also the complex parts of the Quadratic Sieve algorithm.
 */
public class MathOperations 
{
	public static int[] smoothFactor(BigInteger bigInteger, FactorBase factorBase) 
	{
		int[] exponents = new int[factorBase.size()];
		  
		for (int i = 0; i < exponents.length; i++) 
		{
			exponents[i] = 0;
		}
	
		for (int i = 0; i < factorBase.size(); i++) 
		{
			BigInteger factor = factorBase.factor(i);
		    int j = 1;
		      
		    while (bigInteger.remainder(factor.pow(j)).equals(BigInteger.ZERO)) 
		    {
		    	exponents[i]++;
		    	j++;
		    }
		}
	
		return exponents;
	}
	
	public static Matrix buildSystemMatrix(InputArray inputArray, FactorBase factorBase) 
	{
		int rows = factorBase.size();
	    int columns = inputArray.size() + 1;
	    Matrix result = new Matrix(rows, columns);
	      
	    for (int i = 0; i < rows; i++) 
	    {
	    	for (int j = 0; j < columns; j++) 
	    	{
	    		result.set(i, j, 0);
	    	}
	    }
	
	    for (int j = 0; j < columns - 1; j++) 
	    {
	    	int[] exponents = MathOperations.smoothFactor(inputArray.evaluation(j), factorBase);
	
		    for (int i = 0; i < rows; i++) 
		    {
		    	int exponentMod2 = exponents[i] % 2;
		        result.set(i, j, exponentMod2);
		    }
	    }
	
	    return result;
	}
	
	public static Matrix getIndeterminatesFromMask(int mask) 
	{
		if (mask == 0) 
		{
			Matrix result = new Matrix(1, 1);
		    result.set(0, 0, 0);
		    
		    return result;
	    }
	
	    BigInteger binaryMask = BigInteger.valueOf(mask);
	    int numIdeterminates = binaryMask.bitLength();
	    Matrix indeterminates = new Matrix(numIdeterminates, 1);
	    
	    for (int i = 0; i < numIdeterminates; i++) 
	    {
	    	indeterminates.set(i, 0, binaryMask.testBit(i) ? 1 : 0);
	    }
	
	    return indeterminates;
	}
	
	public static InputArray findSquare(InputArray allFactors, int indeterminatesMask, FactorBase factorBase) throws Matrix.EquationException 
	{
		Matrix system = MathOperations.buildSystemMatrix(allFactors, factorBase);
		Matrix indeterminates = MathOperations.getIndeterminatesFromMask(indeterminatesMask);
		Matrix result = system.solve(indeterminates);
		InputArray squareFactors = new InputArray();
		BigInteger product = BigInteger.ONE;
		    
		for (int i = 0; i < result.rows(); i++) 
		{
			if (result.get(i, 0) == 1) 
			{
				BigInteger integer = allFactors.integer(i);
		        BigInteger evaluation = allFactors.evaluation(i);
		        squareFactors.add(integer, evaluation);
		        product = product.multiply(evaluation);
		    }
		}
		
		if (!perfectSquare(product)) 
		{
			return null;
		}
		
		return squareFactors;
	}
	
	public static int getFirstMultipleIndex(BigInteger n, BigInteger prime, InputArray inputArray, int solutionIndex) 
	{
	    int result = -1;
	    int i = 0;
	    
	    for (i = 0; i < inputArray.size(); i++) 
	    {
	    	if (inputArray.evaluation(i).remainder(prime).equals(BigInteger.ZERO)) 
	    	{
	    		result = i;
	    		break;
	    	}
	    }
	
	    if (solutionIndex == 0) 
	    {
	    	return result;
	    }
	
	    int secondResult = -1;
	    int j = 0;
	    
	    for (j = i + 1; j < inputArray.size(); j++) 
	    {
	    	if (inputArray.evaluation(j).remainder(prime).equals(BigInteger.ZERO)) 
	    	{
		        secondResult = j;
		        break;
	    	}
	    }
	    
	    BigInteger difference = BigInteger.valueOf(secondResult - result);
	    
	    if (difference.remainder(prime).equals(BigInteger.ZERO)) 
	    {
	    	return result;
	    } 
	    else 
	    {
	    	return secondResult;
	    }
	}
	
	public static BigInteger tryFactor(BigInteger N, InputArray squareFactors) 
	{
		BigInteger productInts = BigInteger.ONE;
		BigInteger productEvals = BigInteger.ONE;
		    
		for (int i = 0; i < squareFactors.size(); i++) 
		{  	
			productInts = productInts.multiply(squareFactors.integer(i));
		    productEvals = productEvals.multiply(squareFactors.evaluation(i));
		}
		
		BigInteger productEvalsRoot = MathOperations.sqrt(productEvals);
		BigInteger factor = N.gcd(productEvalsRoot.subtract(productInts));
		    
		if (!factor.equals(BigInteger.ONE) && !factor.equals(N)) 	    
		{	
			return factor;
		}
		    
		factor = N.gcd(productEvalsRoot.add(productInts));
		    
		if (!factor.equals(BigInteger.ONE) && !factor.equals(N)) 
		{
		    return factor;
		}
		
		return null;
	}

	/*
	 * Performs the sieve.
	 */
	public static InputArray performSieve(BigInteger n, InputArray inputArray, FactorBase factorBase) 
	{
	    ArrayList<BigInteger> quotients = new ArrayList<BigInteger>(inputArray.size());
	    
	    for (int i = 0; i < inputArray.size(); i++) 
	    {
	    	quotients.add(inputArray.evaluation(i));
	    }
	
	    for (int i = 0; i < factorBase.size(); i++) 
	    {
	    	BigInteger prime = factorBase.factor(i);
	
		    for (int numSolution = 0; numSolution < 2; numSolution++) 
		    {
		        int sieveStart = getFirstMultipleIndex(n, prime, inputArray, numSolution);
		        
		        if (sieveStart == -1) 
		        {
		        	continue;
		        }
		
		        for (int j = sieveStart; j < inputArray.size(); j += prime.intValue()) 
		        {
		        	BigInteger numberToSieve = quotients.get(j);
		
			        while (numberToSieve.remainder(prime).equals(BigInteger.ZERO)) 
			        {
			        	numberToSieve = numberToSieve.divide(prime);
			        }
		
			        quotients.set(j, numberToSieve);
		        }
		    }
	    }
	
	    int numResults = 0;
	    
	    for (int i = 0; i < quotients.size(); i++) 
	    {
	    	if (quotients.get(i).abs().equals(BigInteger.ONE)) 
	    	{
	    		numResults++;
	    	}
	    }
	
	    ArrayList<BigInteger> integerArray = new ArrayList<BigInteger>(numResults);
	    ArrayList<BigInteger> evaluationArray = new ArrayList<BigInteger>(numResults);
	    
	    for (int i = 0; i < inputArray.size(); i++) 
	    {
	    	if (quotients.get(i).abs().equals(BigInteger.ONE)) 
	    	{
		        integerArray.add(inputArray.integer(i));
		        evaluationArray.add(inputArray.evaluation(i));
	    	}
	    }
	    
	    InputArray sieved = new InputArray(integerArray, evaluationArray);
	    
	    return sieved;
	}
	
	/*
	 * Returns the square root of bigInteger.
	 */
	public static BigInteger sqrt(BigInteger bigInteger) 
	{
	    if (bigInteger.compareTo(BigInteger.ZERO) < 0) 
	    {
	    	throw new IllegalArgumentException();
	    }

	    BigDecimal square = new BigDecimal(bigInteger);
	    BigDecimal squareRoot = new BigDecimal(BigInteger.valueOf(2).pow(bigInteger.bitLength() / 2));
	    BigDecimal adjustment = null;
	    
	    do 
	    {
	    	adjustment = square.subtract(squareRoot.pow(2));
	    	adjustment = adjustment.divide(squareRoot, 10, RoundingMode.HALF_EVEN);
	    	adjustment = adjustment.divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_EVEN);
	    	squareRoot = squareRoot.add(adjustment);
	    } 
	    while (Math.abs(adjustment.doubleValue()) > 1);

	    return squareRoot.toBigInteger();
	}
	
	/*
	 * Returns true if bigInteger is a prime.
	 */
	public static boolean prime(BigInteger bigInteger) 
	{
	    for (BigInteger i = BigInteger.valueOf(2); i.compareTo(bigInteger) < 0; i = i.add(BigInteger.ONE)) 
	    {
	    	if (bigInteger.remainder(i).equals(BigInteger.ZERO)) 
	    	{
	    		return false;
	    	}
	    }
	    
	    return true;
	}
	
	/*
	 * Returns true if bigInteger is a perfect square.
	 */
	public static boolean perfectSquare(BigInteger bigInteger) 
	{
		if (bigInteger.compareTo(BigInteger.ZERO) < 0) 
		{
		     return false;
		}

		BigInteger root = sqrt(bigInteger);
		
		return root.multiply(root).equals(bigInteger);
	}
	
	/*
	 * Returns the size of the factor base.
	 */
	public static int factorBaseSize(BigInteger n) 
	{
		int log2N = n.bitLength();
		double lnN = log2N * Math.log(2.0);
		double lnlnN = Math.log(lnN);
		double base = Math.exp(Math.sqrt(lnN * lnlnN));
		double exponent = Math.sqrt(2) / 4;
	    double size = Math.pow(base, exponent);
	    
	    return (int) Math.ceil(size);
	}
	
	/*
	 * Returns the factor base.
	 */
	public static FactorBase factorBase(BigInteger n) 
	{
		int size = factorBaseSize(n);

	    ArrayList<BigInteger> factorArray = new ArrayList<BigInteger>(size);
	    int primes = 0;
	    BigInteger bigInteger = BigInteger.valueOf(2);
	    
	    do 
	    {
	    	if (MathOperations.prime(bigInteger) && legendreSymbol(n, bigInteger) == 1) 
	    	{
		        factorArray.add(bigInteger);
		        primes++;
	    	}

	    	bigInteger = bigInteger.add(BigInteger.ONE);
	    } 
	    while (primes < size);

	    return new FactorBase(factorArray);
	}
	
	/*
	 * Calculates the legendre symbol.
	 */
	public static int legendreSymbol(BigInteger bigInteger, BigInteger prime) 
	{
	    if (bigInteger.remainder(prime).equals(BigInteger.ZERO)) 
	    {
	    	return 0;
	    }

	    BigInteger exponent = prime.subtract(BigInteger.ONE);
	    exponent = exponent.divide(BigInteger.valueOf(2));
	    BigInteger result = bigInteger.modPow(exponent, prime);

	    if (result.equals(BigInteger.ONE)) 
	    {
	    	return 1;
	    } 
	    else if (result.equals(prime.subtract(BigInteger.ONE))) 
	    {
	    	return -1;
	    } 
	    else 
	    {
	    	throw new ArithmeticException("Error calculating the result of Legendre symbol.");
	    }
	}
	
	/*
	 * Returns the size of the total sieve range.
	 */
	public static BigInteger totalSieveRangeSize(BigInteger n) 
	{
		return BigInteger.valueOf(factorBaseSize(n)).pow(3);
	}
}
