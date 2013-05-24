package test;

import java.util.ArrayList;
import java.util.List;

import utils.MapResiduesToIndex;
import utils.Pearson;
import utils.TTest;
import covariance.algorithms.McBASCCovariance;
import covariance.datacontainers.Alignment;
import covariance.datacontainers.AlignmentLine;

import junit.framework.TestCase;

public class TestMcBasc extends TestCase
{

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
		
		Alignment a = new Alignment( "a",  list );

		McBASCCovariance mcBasc = new McBASCCovariance( a );
		
		System.out.println( new Double (metric[ MapResiduesToIndex.getIndex('A')][MapResiduesToIndex.getIndex('G')]));
		System.out.println( mcBasc.getAverages()[0] );
		System.out.println( mcBasc.getSds()[0] );
		
		Double finalScore = mcBasc.getScore(a, 0, 1);
		
		System.out.println("Final score for this perfectly conserved algorithm and the two test columns is: " + finalScore);

		
		Double expectedScore = 1.0;
		
		assertEquals(expectedScore,finalScore);
	}
	
	public void testAgainstReimplementation(  ) throws Exception
	{
		int[][] metric = McBASCCovariance.getMaxhomMetric();
		List<AlignmentLine> list = new ArrayList<>();
		
		for( int x=0; x < 50; x++)
			list.add(new AlignmentLine("" +  x, TestCobs.getRandomProtein(2)));
		
		Alignment a= new Alignment("Test", list);
		
		double fromReimplementation = 
				getMcBasc(a.getColumnAsString(0), 
						a.getColumnAsString(1), metric);
		System.out.println("From reimplementation" + fromReimplementation );
		
		McBASCCovariance mcbascCovariane = new McBASCCovariance(a);
		
		double fromPackage = mcbascCovariane.getScore(a, 0, 1);
		
		System.out.println( "From package " + fromPackage  );
		
		assertEquals(fromPackage, fromReimplementation,0.01);
	}
	
	private static double getMcBasc(String s1, String s2, int [][] sMatrix) throws Exception
	{
		if( s1.length() != s2.length())
			throw new Exception("No");
		
		List<Double> listI = new ArrayList<>();
		List<Double> listJ = new ArrayList<>();
		
		for( int k=0; k < s1.length()-1; k++)
		{
			int iCharForK = MapResiduesToIndex.getIndex(s1.charAt(k));
			int jCharForK = MapResiduesToIndex.getIndex(s2.charAt(k));
			
			for(int l = k+1; l < s1.length(); l++ )
			{
				int iCharForL = MapResiduesToIndex.getIndex(s1.charAt(l));
				int jCharForL = MapResiduesToIndex.getIndex(s2.charAt(l));
				
				listI.add( (double) sMatrix[iCharForK][iCharForL]);
				listJ.add( (double) sMatrix[jCharForK][jCharForL]);
			}
		}
		
		return Pearson.getPearsonR(listI, listJ);
	}
}
