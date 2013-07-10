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

import gocAlgorithms.AverageMcBASC;
import gocAlgorithms.AverageScoreGenerator;

import java.io.File;
import java.util.Random;



import junit.framework.TestCase;

import covariance.algorithms.McBASCCovariance;
import covariance.datacontainers.Alignment;

public class TestMcBascAverage extends TestCase
{
	public void testAverage() throws Exception
	{
		Random random = new Random();

		String runningDirectory = TestConservationSumAverage.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		System.out.println(runningDirectory);
		Alignment a = new Alignment("1", new File(runningDirectory + "pnase.txt"), true );
		a = a.getFilteredAlignment(90);
		
		McBASCCovariance mcBasc  = new McBASCCovariance(a);
		
		AverageMcBASC aMcBasc = new AverageMcBASC(a);
		
		AverageScoreGenerator asg = new AverageScoreGenerator(mcBasc);
		
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
			assertEquals(score, reimpScore,0.0001);
			assertEquals(score, asg.getScore(a, startLeft, endLeft, startRight, endRight),0.0001);
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
