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


package covariance.parsers;

import java.util.StringTokenizer;

public class PFamPdbAnnotationParser
{
	private String id;
	private char chainChar;
	private int startPos;
	private int endPos;
	private String pdbAnnotationString;
	private int length;
	
	public PFamPdbAnnotationParser( String pdbAnnotationString) throws Exception
	{
		this.pdbAnnotationString= pdbAnnotationString;
			
		StringTokenizer sToken = new StringTokenizer( pdbAnnotationString);
			
		if ( ! sToken.nextToken().equals("#=GF")) 
			throw new Exception("Error!  Expecting " + "#=GF" );
			
		if ( ! sToken.nextToken().equals("DR")) 
			throw new Exception("Error!  Expecting " + "DR" );
			
		if ( ! sToken.nextToken().equals("PDB;")) 
			throw new Exception("Error!  Expecting " + "PDB;" );
			
		id = sToken.nextToken();
			
		if ( id.length() != 4 ) 
			throw new Exception("Expecting a pdb id for " + id );
			
		String chainString = sToken.nextToken();
			
		if ( chainString.equals(";" )) 
		{
			chainChar = ' ';
		}
		else
		{
			if ( ! chainString.endsWith(";") || chainString.length() != 2 ) 
				throw new Exception("Error!  Expecting a chain X; for " + chainString);
			
			chainChar = chainString.charAt(0);
		}
			
		String startString = sToken.nextToken();
			
		if ( ! startString.endsWith(";") )
			throw new Exception("Error!  Expecting a terminating semi-colon for " + startString );
			
		startPos = Integer.parseInt( startString.substring( 0, startString.length()-1 ));
			
		String endString = sToken.nextToken();
			
		if ( ! endString.endsWith(";" ) ) 
			throw new Exception("Error!  Expecting a terminating semi-colon for " + endString );
			
		endPos = Integer.parseInt( endString.substring( 0, endString.length()-1 ));
			
		this.length = endPos - startPos;
			
		if ( length < 0 ) 
			throw new Exception("Error!  Negative length for " + endPos + " " + startPos );	
		
	}


	/**  The four char pdb id plus the chain is used for equals() and hashCode()
	 */
	public boolean equals(Object obj) 
	{
		return this.pdbAnnotationString.equals(((PFamPdbAnnotationParser)obj).pdbAnnotationString);
	}

	/**  The four char pdb id plus the chain is used for equals() and hashCode()
	 */		
	public int hashCode() 
	{
		return this.pdbAnnotationString.hashCode();
	}
	
	public int getEndPos()
	{
		return endPos;
	}

	public String getFourCharId()
	{
		return id;
	}

	public int getLength()
	{
		return length;
	}

	public String getPdbAnnotationString()
	{
		return pdbAnnotationString;
	}

	public int getStartPos()
	{
		return startPos;
	}

	public char getChainChar()
	{
		return chainChar;
	}

	public String toString()
	{
		return this.id + " " + this.chainChar + " " + startPos + " " + endPos;
	}

}