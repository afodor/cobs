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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import cobsScripts.ResultsFileLine;

import utils.ConfigReader;

import junit.framework.TestCase;

public class TestAbsoluteScoreVsAverageDistance extends TestCase
{
	/*
	 * This assumes that AbsoluteScoreVsAverageDistance has been run and the results
	 * in COBS_CLEANROOM/bigSummaries reflects the files in COBS_CLEANROOM results
	 */
	public void test() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getCleanroom() +
				File.separator + "bigSummaries" + File.separator +  "bigAverageMI.txt" )));
		
		reader.readLine();
		
		 HashMap<String, List<ResultsFileLine>> cache = new HashMap<String, List<ResultsFileLine>>();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			List<ResultsFileLine> list = getResults(splits[0], cache);
			assertEquals(list.size(), (int) Double.parseDouble(splits[7]));
			ResultsFileLine rfl = getExactlyOne(list, splits[2], splits[3]);
			assertEquals(rfl.getAverageDistance(), Double.parseDouble(splits[5]), 0.0001);
			
			int index = getExactlyOneIndex(list, splits[2], splits[3]);
			
			double percentile =100;
			
			if( index < list.size() -1 )
				percentile = 100.0 *  ((double)index) / list.size();
			
			System.out.println("Expected percentile " + percentile);
			
			assertEquals(percentile, Double.parseDouble(splits[4]),0.0001);
			
			System.out.println(splits[1] + " " + splits[2]);
		}
		
		reader.close();
	}
	
	private static class SortByAverageDistance implements Comparator<ResultsFileLine>
	{
		@Override
		public int compare(ResultsFileLine arg0, ResultsFileLine arg1)
		{
			return Double.compare(arg0.getAverageDistance(), arg1.getAverageDistance());
		}
	}
	
	private static ResultsFileLine getExactlyOne(List<ResultsFileLine> list, String region1,String region2)
	{
		ResultsFileLine returnVal =null;
		
		for(ResultsFileLine rfl : list)
		{
			if(rfl.getRegion1().equals(region1) && rfl.getRegion2().equals(region2))
			{
				assertTrue(returnVal == null);
				returnVal = rfl;
			}
		}
		
		assertTrue(returnVal != null);
		return returnVal;
	}
	

	private static int getExactlyOneIndex(List<ResultsFileLine> list, String region1,String region2)
	{
		Integer returnVal =null;
		
		for(int x=0; x < list.size(); x++)
		{
			ResultsFileLine rfl = list.get(x);
			
			if(rfl.getRegion1().equals(region1) && rfl.getRegion2().equals(region2))
			{
				assertTrue(returnVal == null);
				returnVal = x;
			}
		}
		
		assertTrue(returnVal != null);
		return returnVal;
	}
	
	private static List<ResultsFileLine> getResults( String filename,  HashMap<String, List<ResultsFileLine>> cache ) throws Exception
	{
		List<ResultsFileLine> list= cache.get(filename);
		
		if(list == null)
		{
			list = ResultsFileLine.parseResultsFile(new File(ConfigReader.getCleanroom() + 
					File.separator + "results" + File.separator + filename));
			
			java.util.Collections.sort(list, new SortByAverageDistance());
			
			double init =-1;
			
			for( ResultsFileLine rfl : list )
			{
				assertTrue(rfl.getAverageDistance() >= init);
				init = rfl.getAverageDistance();
			}
			
			cache.put(filename, list);
		}
		
		
		return list;
	}
}
