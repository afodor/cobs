package test;


// test comment

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestSuite1
{

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
		//$JUnit-END$
		return suite;
	}
}
