/** 
 * Authors:  anthony.fodor@gmail.com  kylekreth@alumni.nd.edu
 * 
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */


package utils;

import java.util.*;

public class MapResiduesToIndex
{
	public static Random random = new Random(System.currentTimeMillis());
	
	public static final char[] residues = new char[] { 'A', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T',
						 'V', 'W', 'Y' };
	
	
	public static final HashMap<Character,Integer> residueHashMap = new HashMap<Character,Integer>(){
        {
            put('A', 0);
            put('C', 1);
            put('D', 2);
            put('E', 3);
            put('F', 4);
            put('G', 5);
            put('H', 6);
            put('I', 7);
            put('K', 8);
            put('L', 9);
            put('M', 10);
            put('N', 11);
            put('P', 12);
            put('Q', 13);
            put('R', 14);
            put('S', 15);
            put('T', 16);
            put('V', 17);
            put('W', 18);
            put('Y', 19);
        }
    };
	
	public static int NUM_VALID_RESIDUES = residues.length;	
	
	public static final Character[] charResidues = new Character[]
			{
				new Character('A'), 
				new Character('C'), 
				new Character('D'), 
				new Character('E'), 
			    new Character('F'),
				new Character('G'), 
				new Character('H'),
				new Character('I'), 
				new Character('K'), 
				new Character('L'), 
				new Character('M'), 
				new Character('N'), 
				new Character('P'),
				new Character('Q'), 
				new Character('R'), 
				new Character('S'), 
				new Character('T'),
				new Character('V'),
				new Character('W'),
				new Character('Y')			 
			};
	
	public static char getChar( int i ) throws Exception
	{
		return residues[i];
	}
	
	public static char getRandomChar() throws Exception
	{
		return residues[ random.nextInt( residues.length ) ];
	}
	
	public static char getRandomCharFromDistribution(float[] distribution ) throws Exception
	{
		if ( distribution.length != MapResiduesToIndex.NUM_VALID_RESIDUES ) 
			throw new Exception("Unexpected length");
		
		float sum = 0f;
		
		float randomFloat = random.nextFloat();
		
		for ( int x=0; x< distribution.length; x++)
		{
			sum+= distribution[x];
			
			if ( randomFloat <= sum ) 
				return residues[x];
		}
		
		return residues[MapResiduesToIndex.NUM_VALID_RESIDUES -1];
	}
	
	public static Character getChararcter( int i ) throws Exception
	{
		return charResidues[i];
	}
	
	public static int getIndex(char c) throws Exception
	{
		for (int x=0; x<residues.length; x++ ) 
			if ( residues[x] == c ) 
				return x;
		
		throw new Exception("Unknown character " + c );
	}
	
	public static int getIndexOrNegativeOne(char c) throws Exception
	{
		Character query = c;
		query = Character.toUpperCase(query);
		Integer answer = null;
		answer = residueHashMap.get(query);
		if (query=='-' || query=='.'){
			return -1;
		}
		if (answer == null ){
			//System.out.print("We did not find the amino acid code in question: " + query + " \n");
		}
		else{
			return answer;
		}
	
			
		return -1;
	}
	
	public static int getIndex(String c) throws Exception
	{
		if ( c.length() != 1) 
			throw new Exception("Error!  Expecting a length of one");
		
		for (int x=0; x<residues.length; x++ ) 
			if ( residues[x] == c.charAt(0) ) 
				return x;
		
		throw new Exception("Unknown character " + c );
	}
	
	
	public static boolean isValidResidueChar( char c ) 
	{
		for (int x=0; x<residues.length; x++ ) 
			if ( residues[x] == c ) 
				return true;
		
		return false;
	}
	
	public static boolean isVaildThreeResidueString( String aString ) 
	{
		try
		{
			SequenceUtils.threeToOne(aString);		
		}
		catch(Exception e ) 
		{
			return false;
		}
		
		return true;
	}
}
