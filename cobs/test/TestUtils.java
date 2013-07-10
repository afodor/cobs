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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import covariance.datacontainers.Alignment;
import covariance.datacontainers.AlignmentLine;
import utils.MapResiduesToIndex;

public class TestUtils
{
	/**  Static methods only
	 */
	private TestUtils()
	{
		
	}
	
	/**  Don't change this.  Alot of the tests depend on the sequence being in this format
	 */
	public static String getTestSequence() throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for ( int x=0; x < MapResiduesToIndex.NUM_VALID_RESIDUES; x++ ) 
			buff.append( "" + MapResiduesToIndex.getChararcter(x) + MapResiduesToIndex.getChararcter(x) );
			
		return buff.toString();

	}
	
	public static List getTestSequences() throws Exception
	{
		List list = new ArrayList();
		
		String prefix = "------Evlis";
		String postfix = "E--l--v--s-";
		
		list.add( prefix + TestUtils.getTestSequence() + postfix );
		list.add( postfix + TestUtils.getTestSequence() + prefix);
		list.add( prefix + postfix + TestUtils.getTestSequence());
		list.add( TestUtils.getTestSequence() + prefix + prefix);
		list.add( postfix + TestUtils.getTestSequence() + postfix);
		list.add( prefix + prefix + TestUtils.getTestSequence() );
		
		return list;
		
	}
	
	public static Alignment getTestAlignment( String alignmentId ) throws Exception
	{
		return getTestAlignment(alignmentId, getTestSequences());
	}
	
	public static Alignment getTestAlignment(String alignmentId, List testSequences) throws Exception
	{
		List alignemntLines = new ArrayList();
		
		int x=0;
		
		for ( Iterator i = testSequences.iterator();
				i.hasNext(); ) 
		{
			x++;
			alignemntLines.add( new AlignmentLine( "" + x, i.next().toString() ));
		}
		
		Alignment a= new Alignment(alignmentId, alignemntLines );
		
		return a;
	}
}
