package qsfactor;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.lang.StringBuilder;

/*
 * This class is used to hold sieve inputs.
 */
public class InputArray implements Serializable 
{
	/*
	 * Default constructor. 
	 */
	public InputArray() 
	{
		integerArray = new ArrayList<BigInteger>();
	    evaluationArray = new ArrayList<BigInteger>();
	}
	
	/*
	 * Constructs an InputArray from a string.
	 */
	public InputArray(String stringRepresentation, int size)
	{
	    if (stringRepresentation.equals("[]")) 
	    {
	    	integerArray = new ArrayList<BigInteger>();
	    	evaluationArray = new ArrayList<BigInteger>();
	    }
	    else
	    {
		    String temp = stringRepresentation.substring(2, stringRepresentation.length() - 2);
		    int startSize = size > 0 ? size : 10;
		    integerArray = new ArrayList<BigInteger>(startSize);
		    evaluationArray = new ArrayList<BigInteger>(startSize);
		    String[] pairs = temp.split("\\],\\[");
		    
		    for (int i = 0; i < pairs.length; i++) 
		    {
		    	String pair = pairs[i];
			    String[] pairIntegers = pair.split(",");
		
			    integerArray.add(new BigInteger(pairIntegers[0]));
			    evaluationArray.add(new BigInteger(pairIntegers[1]));
		    }
	
		    integerArray.trimToSize();
		    evaluationArray.trimToSize();
	    }
	}
	
	/*
	 * Constructs an InputArray from ArrayLists.
	 */
	public InputArray(ArrayList<BigInteger> integerArray, ArrayList<BigInteger> evaluationArray)
	{
		this.integerArray = integerArray;
		this.evaluationArray = evaluationArray;
		this.integerArray.trimToSize();
		this.evaluationArray.trimToSize();
	}

	/*
	 * Returns the size of this InputArray.
	 */
	public int size() 
	{
		return integerArray.size();
	}

	/*
	 * Returns an integer at position index in this InputArray.
	 */
	public BigInteger integer(int index) 
	{
		return integerArray.get(index);
	}

	/*
	 * Returns an evaluation at position index in this InputArray.
	 */
	public BigInteger evaluation(int index) 
	{
		return evaluationArray.get(index);
	}
	
	/*
	 * Adds elements from another InputArray into this InputArray.
	 */
	public void add(InputArray other) 
	{
	    integerArray.addAll(other.integerArray);
	    evaluationArray.addAll(other.evaluationArray);
	    integerArray.trimToSize();
	    integerArray.trimToSize();
	}

	/*
	 * Adds an InputArray element into this InputArray.
	 */
	public void add(BigInteger integer, BigInteger evaluation) 
	{
	    integerArray.add(integer);
	    evaluationArray.add(evaluation);
	}

	/*
	 * Returns the string representation of this InputArray.
	 */
	public String toString() 
	{
	    StringBuilder stringRepresentation = new StringBuilder();
	    stringRepresentation.append("[");
	
	    for (int i = 0; i < size(); i++) 
	    {
	    	stringRepresentation.append("[");
	    	stringRepresentation.append(integer(i));
	    	stringRepresentation.append(",");
	    	stringRepresentation.append(evaluation(i));
	    	stringRepresentation.append("]");
	
	    	if (i < size() - 1) 
	    	{
	    		stringRepresentation.append(",");
	    	}
	    }
	
	    stringRepresentation.append("]");
	    
	    return stringRepresentation.toString();
	}
	
	private ArrayList<BigInteger> integerArray;
	private ArrayList<BigInteger> evaluationArray;
}

