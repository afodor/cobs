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
