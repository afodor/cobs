package test;

import gocAlgorithms.COBS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
