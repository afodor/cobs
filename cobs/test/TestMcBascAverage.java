package test;

import java.io.File;
import java.util.Random;

import averagesAndOutput.AverageMcBASC;

import junit.framework.TestCase;

import utils.ConfigReader;

import covariance.algorithms.McBASCCovariance;
import covariance.datacontainers.Alignment;

public class TestMcBascAverage extends TestCase
{
	public void testAverage() throws Exception
	{
		Random random = new Random();

		Alignment a = new Alignment("1", new File( ConfigReader.getCobsHomeDirectory() + File.separator + 
				"pnase.txt"), true );
		a = a.getFilteredAlignment(90);
		
		McBASCCovariance mcBasc  = new McBASCCovariance(a);
		
		AverageMcBASC aMcBasc = new AverageMcBASC(a);
		
		for( int x=0; x< 100; x++)
		{
			int startLeft = a.getNumColumnsInAlignment() / 4;
			int endLeft= startLeft + random.nextInt(10) + 2;
			
			int startRight = endLeft + random.nextInt(15);
			int endRight = startRight +  random.nextInt(10) + 2;
			System.out.println(startLeft + " " + endLeft + " " + startRight + " " + endRight);
			
			double score= aMcBasc.getScore(a, startLeft, endLeft, startRight, endRight);
			
			double reimpScore = getReimplementedAverage(mcBasc, a, startLeft, endLeft, startRight, endRight);
			
			System.out.println(score + " " + reimpScore);
			assertEquals(score, reimpScore);
		}
	}
	
	private static double getReimplementedAverage( McBASCCovariance mcBascm, Alignment a,
			int startLeft, int endLeft, int startRight, int endRight ) throws Exception
	{
		double sum =0; 
		int n=0;
		
		for( int x= startLeft; x <=endLeft; x++)
		{
			for( int y=startRight; y <=endRight; y++)
			{
				double score=mcBascm.getScore(a, x, y); 
				
				//if( score != McBASCCovariance.NO_SCORE && ! Double.isInfinite(score) && ! Double.isNaN(score))
				{
					sum += score ;
					n++;
				}
				
				
			}
		}
		
		return sum / n;
	}
}
