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


import junit.framework.Test;
import junit.framework.TestSuite;

public class TestSuite1
{

	/*
	 * Manually synched with all test cases.  These should all pass
	 * although TestMcBasc will only pass with a certain probability
	 * (since our implementation of McBASC covariance is approximate).
	 */
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for covariance.test");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(AlignmentFilterTest.class));
		suite.addTest(new TestSuite(AlignmentLineTest.class));
		suite.addTest(new TestSuite(AlignmentTest.class));
		suite.addTest(new TestSuite(EntropyConservationTest.class));
		suite.addTest(new TestSuite(FactorialsTest.class));
		suite.addTest(new TestSuite(TestMcBasc2.class));
		suite.addTest(new TestSuite(TestMi.class));
		suite.addTest(new TestSuite(OmesCovarianceTest.class));
		suite.addTest(new TestSuite(TestCobs.class));
		suite.addTest(new TestSuite(TestConservationSum.class));
		suite.addTest(new TestSuite(TestConservationSumAverage.class));
		suite.addTest(new TestSuite(TestMcBasc.class));
		suite.addTest(new TestSuite(TestMcBascAverage.class));
		suite.addTest(new TestSuite(OmesCovarianceTest.class));
		suite.addTest(new TestSuite(FactorialsTest.class));
		suite.addTest(new TestSuite( TestNumberOfElements.class ));
		//$JUnit-END$
		return suite;
	}
}
