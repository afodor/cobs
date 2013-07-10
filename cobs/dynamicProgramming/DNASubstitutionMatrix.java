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


package dynamicProgramming;

public class DNASubstitutionMatrix implements SubstitutionMatrix
{
	public float getScore(char c1, char c2) throws Exception
	{
		
		if( ! isValidDnaChar(c1) || ! isValidDnaChar(c2) )
			return 0;
		
		if( c1 == c2)
			return 1;
		
		return -3;
	}
	
	private static boolean isValidDnaChar(char c)
	{
		if( c== 'A' || c== 'C' || c =='G' || c == 'T' )
			return true;
		
		return false;
	}
	
	public String getSubstitutionMatrixName()
	{
		return "DNA_Matrix";
	}
}
