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
import java.util.List;

import covariance.algorithms.McBASCCovariance;
import covariance.datacontainers.Alignment;
import covariance.datacontainers.AlignmentLine;
import utils.MapResiduesToIndex;
import utils.TTest;

import junit.framework.TestCase;

public class TestMcBasc2 extends TestCase
{

	public TestMcBasc2(String arg0)
	{
		super(arg0);
	}

	public void test1() throws Exception
	{
		List list = new ArrayList();
		
		list.add(new AlignmentLine("1", "AA") );
		list.add(new AlignmentLine("2", "GG") );
		list.add(new AlignmentLine("3", "HH") );
		
		int[][] metric = McBASCCovariance.getMaxhomMetric();
		
		List sumList = new ArrayList();
		
		
		sumList.add( new Double (metric[ MapResiduesToIndex.getIndex('A')][MapResiduesToIndex.getIndex('A')]));
		sumList.add( new Double (metric[ MapResiduesToIndex.getIndex('A')][MapResiduesToIndex.getIndex('G')]));
		sumList.add( new Double (metric[ MapResiduesToIndex.getIndex('A')][MapResiduesToIndex.getIndex('H')]));
		sumList.add( new Double (metric[ MapResiduesToIndex.getIndex('G')][MapResiduesToIndex.getIndex('A')]));
		sumList.add( new Double (metric[ MapResiduesToIndex.getIndex('G')][MapResiduesToIndex.getIndex('G')]));
		sumList.add( new Double (metric[ MapResiduesToIndex.getIndex('G')][MapResiduesToIndex.getIndex('H')]));
		sumList.add( new Double (metric[ MapResiduesToIndex.getIndex('H')][MapResiduesToIndex.getIndex('A')]));
		sumList.add( new Double (metric[ MapResiduesToIndex.getIndex('H')][MapResiduesToIndex.getIndex('G')]));
		sumList.add( new Double (metric[ MapResiduesToIndex.getIndex('H')][MapResiduesToIndex.getIndex('H')]));
		Alignment a = new Alignment( "a",  list );
		
		McBASCCovariance mcBasc = new McBASCCovariance( a );
		
		System.out.println( new Double (metric[ MapResiduesToIndex.getIndex('A')][MapResiduesToIndex.getIndex('G')]));
		System.out.println( mcBasc.getAverages()[0] );
		System.out.println( mcBasc.getSds()[0] );

		
		assertEquals( mcBasc.getAverages()[0], TTest.getAverage(sumList), 0.001);
		assertEquals( mcBasc.getAverages()[1], TTest.getAverage(sumList), 0.001);
		
		assertEquals( mcBasc.getSds()[0], TTest.getStDev(sumList), 0.001);
		assertEquals( mcBasc.getSds()[1], TTest.getStDev(sumList), 0.001);
	}
}
