package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cobsScripts.ResultsFileLine;

import utils.ConfigReader;

import junit.framework.TestCase;

public class TestAsoluteScoreVsAverageDistance extends TestCase
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
			System.out.println(splits[1] + " " + splits[2]);
		}
		
		reader.close();
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
	
	private static List<ResultsFileLine> getResults( String filename,  HashMap<String, List<ResultsFileLine>> cache ) throws Exception
	{
		List<ResultsFileLine> list= cache.get(filename);
		
		if(list == null)
		{
			list = ResultsFileLine.parseResultsFile(new File(ConfigReader.getCleanroom() + 
					File.separator + "results" + File.separator + filename));
			
			cache.put(filename, list);
		}
		
		
		return list;
	}
}
