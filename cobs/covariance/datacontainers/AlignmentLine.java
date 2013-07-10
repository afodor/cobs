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

import utils.MapResiduesToIndex;

public class AlignmentLine
{
	public static final Character GAP_CHAR = new Character( '-');
	private final String identifier;
	private final String sequence;
	private final int numValidChars;
	
	public String getIdentifier()
	{
		return identifier;
	}

	public String getSequence()
	{
		return sequence;
	}
	
	public AlignmentLine( String identifier, String sequence )
	{
		this.identifier = identifier;
		this.sequence = sequence;
		
		int vChars = 0;
		
		for ( int x=0; x< sequence.length(); x++ )
			if ( MapResiduesToIndex.isValidResidueChar( sequence.charAt(x) ) )
				vChars++;
		
		numValidChars = vChars;
	}	
	
	public String getUngappedSequence() throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		String capSequence = this.sequence.toUpperCase();
		
		for ( int x=0; x< capSequence.length(); x++ ) 
		{
			char c = capSequence.charAt( x );
			
			if ( MapResiduesToIndex.isValidResidueChar( c ) ) 
				buff.append(c);
		}
		
		return buff.toString();
	}
		
	public String toString()
	{
		return identifier + "\t" + sequence;
	}

	public int getNumValidChars()
	{
		return numValidChars;
	}
}
