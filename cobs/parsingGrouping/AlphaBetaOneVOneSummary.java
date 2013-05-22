package parsingGrouping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;


public class AlphaBetaOneVOneSummary
{
	public static final int NUM_SIGNIFICANT =25;
	
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
				ConfigReader.getCobsHomeDirectory()  + File.separator + "alphaBetaSummary.txt")));
		
		writer.write("pfamID\tnumberSheetToSheet\tnumberSheetToHelx\tnumberHelixToHelix\tnumFirst" + NUM_SIGNIFICANT +  "SheetToSheet\t" +
							"numFirst" + NUM_SIGNIFICANT + "HelixToHelix\tnumFirst" + NUM_SIGNIFICANT + "SheetToHelix\n");
		
		File topDir = new File(ConfigReader.getCobsHomeDirectory() + File.separator + "pfamRun");
		
		String[] files = topDir.list();
		
		for(String s : files)
		{
			File fileToParse = new File(topDir.getAbsoluteFile() + File.separator + s);
			addOne(writer, fileToParse);
		}
		
		writer.flush();  writer.close();
	}
	
	private static class Holder implements Comparable<Holder>
	{
		double score;
		double distance;
		
		@Override
		public int compareTo(Holder arg0)
		{
			return Double.compare(arg0.score, this.score);
		}
	}
	
	private static void addOne(BufferedWriter writer, File fileToParse) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(fileToParse));
		reader.readLine();
		
		List<Holder> sheetToSheet = new ArrayList<Holder>();
		List<Holder> helixToHelix = new ArrayList<Holder>();
		List<Holder> helixToSheet = new ArrayList<Holder>();
		
		String pFamID = fileToParse.getName().replaceAll("_results.txt", "");
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer( s );
			
			for( int x=0;x <6; x++)
				sToken.nextToken();
			
			String ss = sToken.nextToken();
			
			Holder h = new Holder();
			h.distance = Double.parseDouble(sToken.nextToken());
			h.score = Double.parseDouble(sToken.nextToken());
			
			if(ss.equals("SHEET_SHEET"))
				sheetToSheet.add(h);
			else if ( ss.equals("HELIX_HELIX"))
				helixToHelix.add(h);
			else if( ss.equals("HELIX_SHEET"))
				helixToSheet.add(h);
		}
		
		reader.close();
		
		if( sheetToSheet.size() >= 1000 && helixToHelix.size() >=1000 )
		{
		
			writer.write(pFamID +"\t");
			writer.write(sheetToSheet.size() + "\t");
			writer.write(helixToHelix.size() + "\t");
			writer.write(helixToSheet.size() + "\t");
			writer.write(getNumBelowDistance(sheetToSheet) + "\t");
			writer.write(getNumBelowDistance(helixToHelix) + "\t");
			writer.write(getNumBelowDistance(helixToSheet) + "\n");
			writer.flush();
		}
		System.out.println(fileToParse);
	}
	
	private static int getNumBelowDistance( List<Holder> list )
	{
		Collections.sort(list);
		
		double distToBeat = getMedianDistance(list);
		
		int num =0;
		
		for( int x=0; x < NUM_SIGNIFICANT; x++)
		{
			if( list.get(x).distance <= distToBeat )
				num++;
		}
		
		return num;
	}
	
	private static double getMedianDistance(List<Holder> list)
	{
		List<Double> dList = new ArrayList<Double>();
		
		for(Holder h : list)
			dList.add(h.distance);
		
		Collections.sort(dList);
		
		return dList.get(dList.size() /2);
	}
}
