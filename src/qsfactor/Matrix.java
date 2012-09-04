package qsfactor;

import java.lang.StringBuilder;
import java.text.ParseException;

/*
 * This class represents a matrix, it is used by the Quadratic Sieve algorithm.
 */
public class Matrix 
{
	public static class EquationException extends Exception 
	{
		public EquationException(String msg) 
		{
			super(msg);
		}
	}
	
	public Matrix(int rows, int columns) 
	{
		if ((rows < 1) || (columns < 1))     
		{
		    throw new IllegalArgumentException();
		}
		
		this.rows = rows;
		this.columns = columns;	
		int numWordsColumns = columns % WORD_SIZE == 0 ? columns / WORD_SIZE : columns / WORD_SIZE + 1;
		m = new int[rows][numWordsColumns];
	}
	
	public int rows() 
	{
		return rows;
	}
	
	public int columns() 
	{
		return columns;
	}
	
	public int get(int row, int column) 	  
	{
		int numWordColumn = column >> WORD_SHIFT;
		int word = m[row][numWordColumn];
		int displacementColumn = column & DISPLACEMENT_MASK;
		
		return (word >> displacementColumn) & 1;
	}
	
	public void set(int row, int column, int value) 
	{  	
		int writeValue = value & 1;
		int numWordColumn = column >> WORD_SHIFT;
		int word = m[row][numWordColumn];
		int displacementColumn = column & DISPLACEMENT_MASK;
		    
		if (writeValue == 0) 	    
		{
			int mask = ~(1 << displacementColumn);  // 1...101...1
		    word = word & mask;		  
		} 
		else 
		{
			int mask = 1 << displacementColumn;
		    word = word | mask;	    
		}
		
		m[row][numWordColumn] = word;
	}
		  
	public Matrix transpose() 	  
	{		    
		Matrix result = new Matrix(columns(), rows());
		    		    
		for (int i = 0; i < rows(); i++) 		    
		{			      
			for (int j = 0; j < columns(); j++) 
			{    
				result.set(j, i, get(i, j));			      
			}		    
		}
		    		    
		return result;	  
	}
		  
	public void exchangeRows(int firstRow, int secondRow, int firstColumn) 	  
	{		    
		int numWordColumn = firstColumn >> WORD_SHIFT;    
		int displacementColumn = firstColumn & DISPLACEMENT_MASK;
		int mask = -1 << displacementColumn;
		int firstWord = m[firstRow][numWordColumn];
		int secondWord = m[secondRow][numWordColumn];
		int firstTemp = firstWord & mask;
		int secondTemp = secondWord & mask;
		firstWord = (firstWord & ~mask) + secondTemp;
		secondWord = (secondWord & ~mask) + firstTemp;
		m[firstRow][numWordColumn] = firstWord;
		m[secondRow][numWordColumn] = secondWord;

		for (int i = numWordColumn + 1; i < m[0].length; i++) 
		{		     
			int temp = m[firstRow][i];
		    m[firstRow][i] = m[secondRow][i];
		    m[secondRow][i] = temp;		    
		}  
	}
	
	public void reduceRow(int pivotRow, int rowToReduce, int firstColumn) 	  
	{
		int numWordColumn = firstColumn >> WORD_SHIFT;
		    
		if (get(rowToReduce, firstColumn) == 1) 
		{
			for (int i = numWordColumn; i < m[0].length; i++) 
			{
		        m[rowToReduce][i] = m[rowToReduce][i] ^ m[pivotRow][i];
		    }	    
		}	  
	}
		  
	private int[] reduceToSuperiorTriangular() 	  
	{
		int rows = rows();
		int columns = columns();
		int[] permutations = new int[columns - 1];
			    
		for (int i = 0; i < permutations.length; i++) 
		{
			permutations[i] = i;
		}
		
		int currentRow = 0;
		int currentColumn = 0;
		int maxColumn = columns - 1;  // Do not permute the last column.
		   
		do 
		{
			int pivotRow = currentRow;
		    int pivotColumn = currentColumn;
		    int pivot = 0;
		    
		    for (int i = currentRow; (i < rows) && (pivot == 0); i++) 
		    {
		        for (int j = currentColumn; (j < maxColumn) && (pivot == 0); j++) 
		        {
		        	if (get(i, j) == 1) 
		        	{
			            pivot = 1;
			            pivotRow = i;
			            pivotColumn = j;
		        	}		        
		        }	      
		    }
		    
		    if (pivot == 0) 
		    {
		        break;
		    }
		
		    if (pivotRow != currentRow) 
		    {
		        exchangeRows(pivotRow, currentRow, currentColumn);
		    }
		
		    if (pivotColumn != currentColumn) 
		    {
		        int temp;
		        for (int k = 0; k < rows; k++) 
		        {
		        	temp = get(k, currentColumn);
			        set(k, currentColumn, get(k, pivotColumn));
			        set(k, pivotColumn, temp);
		        }
		
		        int tempIndex = permutations[currentColumn];
		        permutations[currentColumn] = permutations[pivotColumn];
		        permutations[pivotColumn] = tempIndex;      
		    }
		      
		    for (int i = currentRow + 1; i < rows; i++) 
		    {
		        reduceRow(currentRow, i, currentColumn);
		    }
		
		    currentRow++;
		    currentColumn++;		    
		} 
		while ((currentRow < rows) && (currentColumn < maxColumn));
			 
		return permutations;  
	}
		  
	public Matrix solve(Matrix indeterminates) throws EquationException 	  
	{	    
		if (indeterminates == null) 
		{
			throw new IllegalArgumentException();
		}
		    
		int[] permutations = reduceToSuperiorTriangular();	
		int rows = rows();
		int columns = columns();	    
		int range = 0;
		    
		for (int i = 0; (i < rows) && (i < columns); i++) 
		{
			if (get(i, i) != 0) 
			{
				range++;
		    }
		}
		    
		for (int i = range; i < rows; i++) 
		{	      
			if (get(i, columns - 1) == 1) 
			{
				throw new EquationException("The range of the expanded system" + "is greater than the range of the original system");
		    }
		}
		
		Matrix result = new Matrix(columns - 1, 1);
		
		for (int i = 0; i < range; i++) 
		{     
			result.set(i, 1, 0);
		}
		    
		for (int i = range; i < result.rows(); i++) 
		{
			if (i - range < indeterminates.rows()) 
			{
		        result.set(i, 0, indeterminates.get(i - range, 0));
		    } 
			else 
		    {
		        result.set(i, 0, 0);
		    }
		}
			    
		for (int i = range - 1; i >= 0; i--) 
		{
			int otherFactors = 0;
		    
			for (int j = i + 1; j < columns - 1; j++) 
		    {
		        otherFactors = otherFactors + (get(i, j) * result.get(j, 0));
		    }
		      
			otherFactors = otherFactors % 2;	
		    result.set(i, 0, get(i, columns - 1) ^ otherFactors);	   
		}
		
		for (int newPosition = 0; newPosition < columns - 1; newPosition++) 
		{	      
			int oldPosition = 0;
		      
			for (int k = 0; k < permutations.length; k++) 
			{
				if (permutations[k] == newPosition) 
				{
					oldPosition = k;
					break;
		        }
		    }
		
		    if (newPosition != oldPosition) 
		    {
		        int tempValue = result.get(oldPosition, 0);
		        result.set(oldPosition, 0, result.get(newPosition, 0));
		        result.set(newPosition, 0, tempValue);
		
		        int tempIndex = permutations[oldPosition];
		        permutations[oldPosition] = permutations[newPosition];
		        permutations[newPosition] = tempIndex;
		    }	    
		}
		
		return result;
	}
  
	public String toString() 
	{
		StringBuilder builder = new StringBuilder();
	    
		for (int i = 0; i < rows(); i++) 
		{
			builder.append("[");
			
			for (int j = 0; j < columns(); j++) 
			{
				builder.append(get(i,j));
			}
			
			builder.append("]\n");
	    }
	    
		return builder.toString();	  
	}
	
	public static Matrix fromString(String s) throws ParseException 
	{
	    String[] splits = s.split("[\\[\\]]");
	    
	    if (splits.length < 1) 
	    {
	    	throw new ParseException("The number of rows is 0", -1);
	    }
	
	    int rows = 0;
	    String[] validSplits = new String[splits.length];
	    
	    for (int i = 0; i < splits.length; i++) 
	    {
	    	if (splits[i].length() > 1) 
	    	{
	    		validSplits[rows] = splits[i];
	    		rows++;
	    	}
	    }
	
	    int columns = validSplits[0].length();
	    
	    for (int i = 0; i < rows; i++) 
	    {  
	    	if ((validSplits[i].length() > 1) && (validSplits[i].length() != columns)) 
	    	{
	    		throw new ParseException("Row: " + String.valueOf(i) + " does not have the same length.", -1);
	    	}
	    }
	
	    Matrix result = new Matrix(rows, columns);
	    
	    for (int i = 0; i < rows; i++) 
	    {
	    	for (int j = 0; j < columns; j++) 
	    	{
	    		if (validSplits[i].charAt(j) == '0') 
	    		{
	    			result.set(i, j, 0);
	    		} 
	    		else if (validSplits[i].charAt(j) == '1') 
	    		{
	    			result.set(i, j, 1);
	    		} 
	    		else 
	    		{
	    			throw new ParseException("Row: " + String.valueOf(i) + " column: " + String.valueOf(j) + " contains an invalid character (not 0 or 1).", -1);
	    		}
	    		
	    	}
	    }
	
	    return result;
	}
	
	private int[][] m;
	private final int rows;
	private final int columns;
	private static final int WORD_SIZE = 32;
	private static final int WORD_SHIFT = 5;
	private static final int DISPLACEMENT_MASK = 31;
}
