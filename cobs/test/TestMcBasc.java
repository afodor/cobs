package test;

import java.util.ArrayList;
import java.util.List;

import utils.MapResiduesToIndex;
import utils.TTest;
import covariance.algorithms.McBASCCovariance;
import covariance.datacontainers.Alignment;
import covariance.datacontainers.AlignmentLine;

import junit.framework.TestCase;

public class TestMcBasc extends TestCase
{

	public TestMcBasc(String arg0)
	{
		super(arg0);
	}

	public void test1() throws Exception
	{
		List<AlignmentLine> list = new ArrayList<AlignmentLine>();
		
		list.add(new AlignmentLine("1", "AA") );
		list.add(new AlignmentLine("2", "GG") );
		list.add(new AlignmentLine("3", "HH") );
		
		int[][] metric = McBASCCovariance.getMaxhomMetric();
		
		List<Double> sumList = new ArrayList<Double>();
		
		
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
		
		System.out.println("Final score for this algorithm and the two test columns is: " + mcBasc.getScore(a, 0, 1));

		
		assertEquals( mcBasc.getAverages()[0], TTest.getAverage(sumList), 0.001);
		assertEquals( mcBasc.getAverages()[1], TTest.getAverage(sumList), 0.001);
		
		assertEquals( mcBasc.getSds()[0], TTest.getStDev(sumList), 0.001);
		assertEquals( mcBasc.getSds()[1], TTest.getStDev(sumList), 0.001);
	}
	
	public void testPerfectConservation() throws Exception
	{
		List<AlignmentLine> list = new ArrayList<AlignmentLine>();
		
		list.add(new AlignmentLine("1", "AG") );
		list.add(new AlignmentLine("2", "AG") );
		list.add(new AlignmentLine("3", "AG") );
		
		int[][] metric = McBASCCovariance.getMaxhomMetric();
		
		List<Double> sumList = new ArrayList<Double>();
		
		
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
		
		Double finalScore = mcBasc.getScore(a, 0, 1);
		
		System.out.println("Final score for this perfectly conserved algorithm and the two test columns is: " + finalScore);

		
		Double expectedScore = 0.0;
		
		assertEquals(expectedScore,finalScore);

		

	}
}
