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

import gocAlgorithms.COBS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import covariance.datacontainers.Alignment;
import covariance.datacontainers.PdbFileWrapper;
import covariance.parsers.PfamParser;
import covariance.parsers.PfamToPDBBlastResults;

import cobsScripts.ResultsFileLine;
import cobsScripts.WriteScores;

import utils.ConfigReader;

import junit.framework.TestCase;

public class TestResultsFileLine extends TestCase
{

	/*
	 * This assumes WriteScripts has been run
	 */
	public void testPNormalize() throws Exception
	{
		File file = new File(ConfigReader.getCleanroom() + File.separator + "results" +
				File.separator + "2OG-FeII_Oxy_5_COBS_UNCORRECTED.txt.gz");
		List<ResultsFileLine> list =  ResultsFileLine.parseResultsFile(file);
		
		double sum =0;
		
		HashMap<String, List<Double>> map = new HashMap<String,List<Double>>();
		
		for( ResultsFileLine rfl : list )
		{
			sum += rfl.getScore();
			addOne(map, rfl.getRegion1(), rfl.getScore());
			addOne(map, rfl.getRegion2(), rfl.getScore());
		}
		
		double globalAverage = sum / list.size();
		System.out.println("GLOBAL AVERAGE = " +globalAverage);
		
		HashMap<String, Double> avMap = getAverageMap(map);
		HashMap<String, Double> anotherAvMap = ResultsFileLine.breakByColumn(list);
		
		assertEquals(anotherAvMap.size(), avMap.size());
		
		for(String s : avMap.keySet())
		{
			System.out.println("Comparing " + s);
			assertEquals(avMap.get(s), anotherAvMap.get(s),0.0001);
			
		}
			
		List<ResultsFileLine> normList = ResultsFileLine.getNormalizedList(list);
		
		assertEquals(normList.size(), list.size());
		assertEquals(
			ResultsFileLine.getMedianAverageDistance(normList), ResultsFileLine.getMedianAverageDistance(list) );
		
		for( int x=0; x < list.size(); x++)
		{
			ResultsFileLine rfl = list.get(x);
			ResultsFileLine normedRFL = normList.get(x);
			
			assertEquals(rfl.getRegion1(), normedRFL.getRegion1());
			assertEquals(rfl.getRegion2(), normedRFL.getRegion2());
			assertEquals(rfl.getAverageDistance(), normedRFL.getAverageDistance());
			assertEquals(rfl.getMinDistance(), normedRFL.getMinDistance());
			
			double expectedNormedScore = rfl.getScore() 
					- avMap.get(rfl.getRegion1()) * avMap.get(rfl.getRegion2())/ globalAverage;
			
			System.out.println(expectedNormedScore + " " +  normedRFL.getScore());
			assertEquals(expectedNormedScore, normedRFL.getScore(), 0.00001);
		}
	}
	
	private static HashMap<String, Double> getAverageMap( HashMap<String, List<Double>> map )
	{
		HashMap<String, Double> averageMap = new HashMap<String,Double>();
		
		for( String key : map.keySet() )
		{
			List<Double> list = map.get(key);
			double sum =0;
			
			for(Double d : list)
				sum+=d;
			
			averageMap.put(key, sum / list.size());
		}
		
		return averageMap;
	}
	
	private static void addOne( HashMap<String, List<Double>> map, String key, double score )
	{
		List<Double> list = map.get(key);
		
		if( list == null)
		{
			list = new ArrayList<Double>();
			map.put(key,list);
		}
		
		list.add(score);
					
	}
	
	/*
	 * This assumes WriteScripts has been run
	 */
	public void testCobsScore() throws Exception
	{
		
		File file = new File(ConfigReader.getCleanroom() + File.separator + "results" +
				File.separator + "2OG-FeII_Oxy_5_COBS_UNCORRECTED.txt.gz");
		COBS cobs = new COBS();
		
		List<ResultsFileLine> list =  ResultsFileLine.parseResultsFile(file);
		
		PfamParser parser =new PfamParser();
		Alignment a= parser.getAnAlignment("2OG-FeII_Oxy_5");
		
		HashMap<String, PfamToPDBBlastResults> pfamToPdbMap = PfamToPDBBlastResults.getAsMap();
		PfamToPDBBlastResults toPDB = pfamToPdbMap.get("2OG-FeII_Oxy_5");
		
		PdbFileWrapper fileWrapper = new PdbFileWrapper( new File( ConfigReader.getPdbDir() + File.separator + toPDB.getPdbID() + ".txt"));
		HashMap<Integer, Integer> pdbToAlignmentNumberMap=  WriteScores.getPdbToAlignmentNumberMap(a, toPDB, fileWrapper);
		
		
		for(ResultsFileLine rfl : list)
		{
			double score = 
					cobs.getScore(a, 
							pdbToAlignmentNumberMap.get(TestNumberOfElements.getFirstElement(rfl.getRegion1(), toPDB.getChainId())), 
							pdbToAlignmentNumberMap.get(TestNumberOfElements.getSecondElement(rfl.getRegion1(), toPDB.getChainId())),
							pdbToAlignmentNumberMap.get(TestNumberOfElements.getFirstElement(rfl.getRegion2(), toPDB.getChainId())),
							pdbToAlignmentNumberMap.get(TestNumberOfElements.getSecondElement(rfl.getRegion2(), toPDB.getChainId())));
			
			assertEquals(score, rfl.getScore(), 0.0001);
		}
		
		System.out.println("passed");
	}
	
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
