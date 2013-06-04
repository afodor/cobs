package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import cobsScripts.ResultsFileLine;

import utils.ConfigReader;

import junit.framework.TestCase;

public class TestResultsFileLine extends TestCase
{
	/*
	 * This assumes WriteScripts has been run
	 */
	public void testFileParse() throws Exception
	{
		File file = new File(ConfigReader.getCleanroom() + File.separator + "results" +
								File.separator + "2OG-FeII_Oxy_5_COBS_UNCORRECTED.txt.gz");
		
		List<ResultsFileLine> list =  ResultsFileLine.parseResultsFile(file);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader( 
				new GZIPInputStream( new FileInputStream( file ))));
		
		reader.readLine();
		
		for( int x=0; x < list.size(); x++)
		{
			ResultsFileLine rfl = list.get(x);
			
			String s= reader.readLine();
			
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			
			assertEquals(rfl.getRegion1(), sToken.nextToken());
			assertEquals(rfl.getRegion2(), sToken.nextToken());
			assertEquals(rfl.getCombinedType(), sToken.nextToken());
			assertEquals(rfl.getScore(), Double.parseDouble(sToken.nextToken()),0.00001);
			assertEquals(rfl.getAverageDistance(), Double.parseDouble(sToken.nextToken()),0.00001);
			assertEquals(rfl.getMinDistance(), Double.parseDouble(sToken.nextToken()),0.00001);
		}
		
		reader.close();
	}
}
