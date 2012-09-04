package qsfactor;

import java.io.Serializable;
import java.lang.StringBuilder;
import java.math.BigInteger;
import java.util.ArrayList;

/*
 * This class is used to hold the primes of the factor base.
 */
public class FactorBase implements Serializable 
{
	/*
	 * Default constructor.
	 */
	public FactorBase()
	{
		
	}
	
	/*
	 * Constructs a factor base from an arraylist.
	 */
	public FactorBase(ArrayList<BigInteger> factorArray) 
	{
		this.factorArray = factorArray;
	}
	
	/*
	 * Constructs a factor base from a string.
	 */
	public FactorBase(String stringRepresentation)
	{
		factorArray = new ArrayList<BigInteger>();
	    String temp = stringRepresentation.substring(1, stringRepresentation.length() - 1);
	    String[] integerArray = temp.split(",");
	    
	    for (int i = 0; i < integerArray.length; i++) 
	    {
	    	factorArray.add(new BigInteger(integerArray[i]));
	    }
	}
	
	/*
	 * Returns the size of this factor base.
	 */
	public int size() 
	{
		return factorArray.size();
	}
	
	/*
	 * Returns a factor at position index in this factor base.
	 */
	public BigInteger factor(int index) 
	{
		return factorArray.get(index);
	}

	/*
	 * Returns the string representation of this factor base.
	 */
	public String toString() 
	{
		StringBuilder stringRepresentation = new StringBuilder();
		stringRepresentation.append("[");
	
		for (int i = 0; i < size(); i++) 
		{
			stringRepresentation.append(factor(i));
	
			if (i < size() - 1) 
			{
				stringRepresentation.append(",");
			}
		}
	
	    stringRepresentation.append("]");
	    
	    return stringRepresentation.toString();
	}
	
	private ArrayList<BigInteger> factorArray;
}

