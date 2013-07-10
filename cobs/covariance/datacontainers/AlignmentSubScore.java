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


package covariance.datacontainers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;



public class AlignmentSubScore
{
	private char xChar;
	private char yChar;
	private int numObserved;
	private float numExpected;
	private float score;
	
	public static AlignmentSubScore getASubScore( Collection subScores, char xChar, char yChar ) 
	{
		for ( Iterator i = subScores.iterator();
				i.hasNext(); ) 
		{
			AlignmentSubScore aSubScore = (AlignmentSubScore) i.next();
			
			if ( aSubScore.getXChar() == xChar && aSubScore.getYChar() == yChar ) 
				return aSubScore;		
		}
		
		return null;
	}
	
	public AlignmentSubScore( char xChar, char yChar, int numObserved, 
									float numExpected, float score )
	{
		this.xChar = xChar;
		this.yChar = yChar;
		this.numObserved = numObserved;
		this.numExpected = numExpected;
		this.score = score;	
	}
	
	public float getNumExpected()
	{
		return numExpected;
	}

	public int getNumObserved()
	{
		return numObserved;
	}

	public float getScore()
	{
		return score;
	}

	public char getXChar()
	{
		return xChar;
	}

	public char getYChar()
	{
		return yChar;
	}
	
	public static String getHeader()
	{
		return "xChar\tyChar\tnumObserved\tnumExpected\tscore\tPositveCovariance\n";
	}
	
	public String getTabbedLine()
	{
		StringBuffer buff = new StringBuffer();
		
		buff.append( this.xChar + "\t" );
		buff.append( this.yChar + "\t" );
		buff.append( this.numObserved + "\t" );
		buff.append( this.numExpected + "\t" );
		buff.append( this.score + "\t");
		
		if ( this.numObserved > this.numExpected ) 
			buff.append( "positive\n" );
		else if ( this.numExpected > this.numObserved ) 
			buff.append( "negative\n" );
		else
			buff.append("equal\n");
		
		return buff.toString();
	}
					
	
	public static void writeToFile ( File file, List subScores ) throws Exception
	{
		BufferedWriter writer = new BufferedWriter( new FileWriter( file ));	
		writer.write( getHeader() );
		
		for ( int x=0; x < subScores.size(); x++ ) 
		{
			AlignmentSubScore aSubScore = (AlignmentSubScore ) subScores.get(x);
			writer.write( aSubScore.getTabbedLine() );
		}
		
		writer.flush();  writer.close();
	}

}
