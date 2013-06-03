package test;

import java.io.File;

import utils.ConfigReader;
import covariance.algorithms.AbsoluteValueFileScoreGenerator;
import covariance.algorithms.FileScoreGenerator;
import covariance.algorithms.McBASCCovariance;
import covariance.datacontainers.Alignment;
import covariance.parsers.PfamParser;
import junit.framework.TestCase;

public class TestMcBascVsAbsoluteMcBASC extends TestCase
{
	/*
	 * This assumes that cobsScripts.WriteOneDScores 
	 * has been run..
	 */
	public void test() throws Exception
	{
		
		File file = new File(ConfigReader.getCleanroom() + File.separator + "results" + File.separator + "oneD" + 
				File.separator + "3-HAO_McBASC.txt.gz");
		
		System.out.println(file.getAbsolutePath());

		Alignment a = new PfamParser().getAnAlignment("3-HAO");
		FileScoreGenerator fsg = new FileScoreGenerator("mcbas", file, a);
		FileScoreGenerator afsg = new AbsoluteValueFileScoreGenerator("absmcbas", file, a);
		
		McBASCCovariance mcbasc =new McBASCCovariance(a); 
		
		for( int i = 0; i < 354; i++)
		{
			for( int j=i+1; j < 355; j++ )
			{
				assertEquals(fsg.getScore(a, i, j), mcbasc.getScore(a, i, j),0.00001);
				assertEquals(Math.abs(fsg.getScore(a, i, j)), afsg.getScore(a, i, j),0.00001);
				//System.out.println(i + " " + j + " " + fsg.getScore(a, i, j) + " " + mcbasc.getScore(a, i, j) );
			}
			System.out.println(i);
		}
			
		
		System.out.println("passed");
	}
}
