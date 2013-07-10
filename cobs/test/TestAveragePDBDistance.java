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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import covariance.datacontainers.PdbChain;
import covariance.datacontainers.PdbFileWrapper;
import covariance.datacontainers.PdbResidue;

import junit.framework.TestCase;

import utils.ConfigReader;

import cobsScripts.ResultsFileLine;

public class TestAveragePDBDistance extends TestCase
{
	/*
	 * This assumes that writeScores has been run
	 */
	public void test() throws Exception
	{
		List<ResultsFileLine> innerList= ResultsFileLine.parseResultsFile( new File( ConfigReader.getCleanroom() + File.separator + 
				"results" + File.separator + 
				"2OG-FeII_Oxy_5_COBS_UNCORRECTED.txt.gz"));
		
		char chainID = 'A';
		String pdbID = "3BVC";
		HashMap<Integer, Point> pdbMap = getPdbAsLines(pdbID, chainID);
		subAgainstPdbParser(pdbMap, pdbID, chainID);
		
		for(ResultsFileLine rfl : innerList)
		{
			int leftElementStart = TestNumberOfElements.getFirstElement(rfl.getRegion1(), chainID);
			int leftElementEnd = TestNumberOfElements.getSecondElement(rfl.getRegion1(), chainID);
			
			int rightElementStart = TestNumberOfElements.getFirstElement(rfl.getRegion2(), chainID);
			int rightElementEnd = TestNumberOfElements.getSecondElement(rfl.getRegion2(), chainID);
			
			double sum =0;
			double n =0;
			
			System.out.println(rfl.getRegion1() +  " " + rfl.getRegion2());
			System.out.println(leftElementStart + " " +leftElementEnd);
			System.out.println(rightElementStart + "  " + rightElementEnd);
			for( int x=leftElementStart; x <= leftElementEnd; x++)
				for( int y=rightElementStart; y<=rightElementEnd; y++)
				{
					sum += getDistance(pdbMap.get(x), pdbMap.get(y));
					n++;
				}
		
			System.out.println(sum + " " + n);
			System.out.println("TEST " + sum/n + " " + rfl.getAverageDistance());
			assertEquals(sum/n, rfl.getAverageDistance(), 0.0001);
			System.out.println("Passed " + rfl.getAverageDistance() );
			
		}
	}
	
	private void subAgainstPdbParser(HashMap<Integer, Point> pointMap, String pdbID, char chainID ) throws Exception
	{
		PdbFileWrapper fileWrapper = new PdbFileWrapper(pdbID);
		PdbChain chain = fileWrapper.getChain(chainID);
		
		for( Integer i : pointMap.keySet() )
		{
			PdbResidue residue = chain.getPdbResidueByPdbPosition(i);
			System.out.println(i + " "+ residue.getPdbChar());
			Point point = pointMap.get(i);
			
			assertEquals(residue.getCbAtom().getX(), point.x,0.0001);
			assertEquals(residue.getCbAtom().getY(), point.y,0.0001);
			assertEquals(residue.getCbAtom().getZ(), point.z,0.0001);
		}
	}
	
	
	private static class Point
	{
		double x, y,z;
	}
	
	public double getDistance(Point p1, Point p2)
	{
		
		double sum =0;
		
		sum+= (p1.x - p2.x) * (p1.x - p2.x);
		sum+= (p1.y - p2.y) * (p1.y- p2.y);
		sum+= (p1.z - p2.z) * (p1.z - p2.z);
		
		return Math.sqrt(sum);
	}
	
	private static HashMap<Integer, Point> getPdbAsLines(String fourCharId , char chainID) throws Exception
	{
		HashMap<Integer, Point> map = new LinkedHashMap<Integer,Point>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getPdbDir() + File.separator + fourCharId + ".txt")));
		
		for(String s= reader.readLine(); s != null ; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			
			if(sToken.nextToken().equals("ATOM"))
			{
				sToken.nextToken();
				String atomType = sToken.nextToken();
				String residueID =  sToken.nextToken();
				String chainIDString = sToken.nextToken();
				
				assertEquals(chainIDString.length(),1);
				
				char pdbChar = chainIDString.charAt(0);
				
				int position = Integer.parseInt(sToken.nextToken());
				
				if(pdbChar == chainID)
					if( ( residueID.equals("GLY") && atomType.equals("CA") ) ||  atomType.equals("CB") )
					{
						//System.out.println(atomType+ " " + residueID + " " + position);
						assertFalse(map.containsKey(position));
						Point p = new Point();
						p.x = Double.parseDouble(sToken.nextToken());
						p.y= Double.parseDouble(sToken.nextToken());
						p.z = Double.parseDouble(sToken.nextToken());
						map.put(position, p);
					}
			}
		}
		
		reader.close();
		
		return map;
	}
}
