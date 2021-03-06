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


package covariance.algorithms;

import java.io.*;
import java.util.*;

import covariance.datacontainers.*;

public class RandomScore implements ScoreGenerator
{
	public static final String RANDOM_NAME = "random";
	
	Random random = new Random();
	String idString;
	private final Alignment a;
	
	@Override
	public Alignment getAlignment()
	{
		return a;
	}
	
	
	public static void main(String[] args) throws Exception
	{
		if ( args.length != 2 ) 
		{
			System.out.println( "Usage RandomScore inAlignment outFile" );
			return;
		}	
		
		
		Alignment a = new Alignment("1", new File( args[0]), false );
		
		RandomScore rScore = new RandomScore();
		
		BufferedWriter writer = new BufferedWriter( new FileWriter( new File(
						args[1] )));
		
		writer.write( "i\tj\tscore\n");
		
		for ( int i =0; i < a.getNumColumnsInAlignment(); i++ ) 
			if ( a.columnHasValidResidue(i) ) 
				for ( int j = i + 1; j < a.getNumColumnsInAlignment(); j++ )
					if ( a.columnHasValidResidue(j) ) 
						writer.write( i + "\t" + j + "\t" + rScore.getScore(a, i, j) + "\n" );				
		
		writer.flush();  writer.close();			
	}
	
	public Double getScore( Alignment a, int i, int j ) throws Exception
	{
		return random.nextDouble();
	}
	
	public RandomScore()
	{
		this(RANDOM_NAME, null);
	}
	
	public RandomScore(String idString, Alignment a)
	{
		this.idString = idString;
		this.a = a;
	}
	
	public String getAnalysisName()
	{
		return idString;
	}
	
	public boolean isSymmetrical()
	{
		return true;
	}
	
	public boolean reverseSort()
	{
		return true;
	}
}
