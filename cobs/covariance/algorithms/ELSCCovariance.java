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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import covariance.datacontainers.Alignment;

public class ELSCCovariance implements ScoreGenerator
{
	private ELSCCovarianceSubAlignment lastSubAlignment = null;
	private int lastI = -999;
	private Alignment a;
	char[] mostFrequentResidues;
	String analysisID;
	
	@Override
	public Alignment getAlignment()
	{
		return a;
	}

	public ELSCCovariance(Alignment a) throws Exception
	{
		this(a, "ELSC");
	}

	public ELSCCovariance(Alignment a, String analysisID) throws Exception
	{
		this.a = a;
		this.mostFrequentResidues= Alignment.getMostFrequentResidues(a);	
		this.analysisID = analysisID;	
	}
	
	public String getAnalysisName()
	{
		return analysisID;
	}

	/**  This will work a lot faster if you call all the j's for the same i since the last
	 *   called subalignment i is cached
	 */
	public Double getScore(Alignment a, int i, int j) throws Exception
	{
		if ( this.a != a ) 
			throw new Exception("Unexpected alignment");
			
		if ( lastSubAlignment == null || lastI != i ) 
		{
			lastSubAlignment = new ELSCCovarianceSubAlignment( a, i, mostFrequentResidues[i]);	
			lastI = i;		
		}
			
		return lastSubAlignment.getCovarianceScores()[j];
	}

	public boolean isSymmetrical()
	{
		return false;
	}

	public boolean reverseSort()
	{
		return false;
	}
	
	public static void main(String[] args) throws Exception
	{
		if ( args.length != 2 ) 
		{
			System.out.println( "Usage ELSCCovariance inAlignment outFile" );
			return;
		}	
		
		Alignment a = new Alignment("1", new File( args[0]), false );
		
		ELSCCovariance elsc  = new ELSCCovariance(a);
		
		BufferedWriter writer = new BufferedWriter( new FileWriter( new File(
						args[1] )));
		
		writer.write( "i\tj\tscore\n");
		
		for ( int i =0; i < a.getNumColumnsInAlignment(); i++ ) 
			if ( a.columnHasValidResidue(i) ) 
				for ( int j = i + 1; j < a.getNumColumnsInAlignment(); j++ )
					if ( a.columnHasValidResidue(j) ) 
					writer.write( i + "\t" + j + "\t" + elsc.getScore(a, i, j) + "\n" );				
		
		writer.flush();  writer.close();			
	}
}
