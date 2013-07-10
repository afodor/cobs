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


package test;

import covariance.datacontainers.AlignmentLine;

import junit.framework.TestCase;

public class AlignmentLineTest extends TestCase
{

	public AlignmentLineTest(String arg0)
	{
		super(arg0);
	}

	public void testGetUngappedSequence() throws Exception
	{
	
		String testSequence = TestUtils.getTestSequence();	
		System.out.println( TestUtils.getTestSequence() );
		String prefix = "----El---vI-s------";
		String id = "someId";
		
		AlignmentLine aLine = new AlignmentLine( id, testSequence + prefix );
		assertEquals(aLine.getIdentifier(), id);
		assertEquals( aLine.getSequence(), testSequence + prefix );
		
		assertEquals( aLine.getUngappedSequence(), testSequence + "ELVIS" );
			
	}
	
	public void testNumValidChars() throws Exception
	{
		AlignmentLine aLine = new AlignmentLine("1","AAA---AA");
		assertEquals(aLine.getNumValidChars(), 5);
		
		aLine = new AlignmentLine("1", "abcdefg----------".toUpperCase());
		assertEquals(aLine.getNumValidChars(), 6);
		
		aLine = new AlignmentLine("1", "abcdefg----------");
		assertEquals(aLine.getNumValidChars(), 0);	
		
		aLine = new AlignmentLine("1", "FFFFFFFFFF" );
		assertEquals(aLine.getNumValidChars(), 10);
	}

}
