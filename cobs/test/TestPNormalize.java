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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import covariance.algorithms.FileScoreGenerator;
import covariance.algorithms.PNormalize;
import covariance.datacontainers.Alignment;
import covariance.parsers.PfamParser;

import utils.ConfigReader;
import junit.framework.TestCase;

public class TestPNormalize extends TestCase
{

	/*
	 * This assumes WriteScripts has been run
	 */
	public void testPNormalize() throws Exception
	{	
		double sum =0;
		int n=0;
		HashMap<Integer, List<Double>> map = new HashMap<Integer,List<Double>>();
		
		PfamParser pfamParser = new PfamParser();
		Alignment a = pfamParser.getAnAlignment("2OG-FeII_Oxy_5");
		int numCols = a.getNumColumnsInAlignment();
		
		FileScoreGenerator fsg = new FileScoreGenerator("blah", ConfigReader.getCleanroom() + File.separator + "results" +
						File.separator + "oneD" + File.separator +"2OG-FeII_Oxy_5_McBASC.txt.gz", a);
	
		for( int i =0; i <=numCols-1; i++)
			for( int j=i+1; j<numCols; j++)
			{
				double score = fsg.getScore(a, i, j);
				sum += score ;
				addOne(map, i, score);
				addOne(map, j,score);
				n++;
			}
		
		double globalAverage = sum / n;
		
		System.out.println("GLOBAL AVERAGE = " +globalAverage);
		
		HashMap<Integer, Double> avMap = getAverageMap(map);
		
		PNormalize pnormal = new PNormalize(fsg);
		
		for( int i =0; i <=numCols -1; i++)
			for( int j=i+1; j<numCols; j++)
		
		{
			
			double expectedNormedScore = fsg.getScore(a, i, j)
					- avMap.get(i) * avMap.get(j)/ globalAverage;
			
			System.out.println(expectedNormedScore + " " +  pnormal.getScore(a, i, j));
			assertEquals(expectedNormedScore ,pnormal.getScore(a, i, j),0.0001);
			
		}
	}
	
	private static HashMap<Integer, Double> getAverageMap( HashMap<Integer, List<Double>> map )
	{
		HashMap<Integer, Double> averageMap = new HashMap<Integer,Double>();
		
		for( Integer key : map.keySet() )
		{
			List<Double> list = map.get(key);
			double sum =0;
			
			for(Double d : list)
				sum+=d;
			
			averageMap.put(key, sum / list.size());
		}
		
		return averageMap;
	}
	
	private static void addOne( HashMap<Integer, List<Double>> map, Integer key, double score )
	{
		List<Double> list = map.get(key);
		
		if( list == null)
		{
			list = new ArrayList<Double>();
			map.put(key,list);
		}
		
		list.add(score);
					
	}
}
