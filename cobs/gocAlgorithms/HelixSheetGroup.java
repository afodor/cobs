package gocAlgorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;


public class HelixSheetGroup implements Comparable<HelixSheetGroup>
{
	public static final String HELIX = "HELIX";
	public static final String SHEET = "SHEET";
	public static final String NONE = "NONE";
	
	private String element;
	private int startPos;
	private int endPos;
	private char startChain;
	private char endChain;

	@Override
	public int compareTo(HelixSheetGroup o)
	{
		return this.startPos - o.startPos;
	}
	
	@Override
	public String toString()
	{
		return element + ":" + startChain + "" +  startPos + "-" + endChain + ""  +  endPos;
	}
	
	public char getStartChain()
	{
		return startChain;
	}
	
	public char getEndChain()
	{
		return endChain;
	}
	
	public String getElement()
	{
		return element;
	}

	public int getStartPos()
	{
		return startPos;
	}

	public int getEndPos()
	{
		return endPos;
	}
	
	public static String getGroup(int pos, List<HelixSheetGroup> list)
	{
		for( HelixSheetGroup hsg : list )
			if( pos >= hsg.startPos && pos <= hsg.endPos)
				return hsg.getElement();
		
		return NONE;
	}

	public static List<HelixSheetGroup> getList(String pdbFile, char pdbChain, int pdbStart, int pdbEnd) throws Exception
	{
		List<HelixSheetGroup> aList = new ArrayList<HelixSheetGroup>();
		
		for( HelixSheetGroup hsg : getList(pdbFile))
			if( hsg.startChain == pdbChain && hsg.endChain == pdbChain && hsg.startPos >= pdbStart && hsg.endPos <= pdbEnd)
				aList.add(hsg);
		
		List<HelixSheetGroup> returnList = new ArrayList<HelixSheetGroup>();
		
		for(HelixSheetGroup hsg : aList)
			if( ! alreadyThere(hsg, returnList))
				returnList.add(hsg);
		
		for(HelixSheetGroup hsg : returnList)
			trimMatchingEnds(hsg, returnList);
		
		return returnList;
	}
	
	private static boolean alreadyThere(  HelixSheetGroup hsg, List<HelixSheetGroup> list )
	{
		for( HelixSheetGroup aHsg : list )
		{
			//System.out.println( hsg + " vs " + aHsg);
			
			if( hsg.startPos >= aHsg.startPos && hsg.startPos<= aHsg.endPos)
				return true;
			
			if( aHsg.startPos >= hsg.startPos && aHsg.startPos <= hsg.endPos)
				return true;
			
			//System.out.println("OK\n\n");
		}
			
		return false;
	}
	
	
	private static void trimMatchingEnds(  HelixSheetGroup hsg, List<HelixSheetGroup> list )
	{
		for( HelixSheetGroup aHsg : list )
		{
			if( aHsg != hsg)			
			{
				//System.out.println("hsg " + hsg);
				//System.out.println("ahsg " + aHsg);
			
				//trim if overlapping to avoid residues being compared to themselves
				if( aHsg.endPos==hsg.startPos)
					aHsg.endPos =  --aHsg.endPos;
				
				if( hsg.endPos == aHsg.startPos)
					hsg.endPos= --hsg.endPos;
				
				//System.out.println("Post " + aHsg + "\n\n\n");
			}			
		}
	}
	
	public static List<HelixSheetGroup> getList(String pdbFile) throws Exception
	{
		List<HelixSheetGroup> list = new ArrayList<HelixSheetGroup>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(pdbFile)));
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			
			String firstToken = sToken.nextToken();
			
			if( firstToken.equals(HELIX) )
			{
				HelixSheetGroup hsg = new HelixSheetGroup();
				hsg.element = new String(firstToken);
				
				hsg.startPos = Integer.parseInt(s.substring(21,25).replaceAll("[A-Z]", "").trim());
				hsg.endPos = Integer.parseInt(s.substring(33,37).replaceAll("[A-Z]", "").trim());
				hsg.startChain= s.charAt(19);
				hsg.endChain = s.charAt(31);
				list.add(hsg);
			}
			else if( firstToken.equals(SHEET) )
			{
				HelixSheetGroup hsg = new HelixSheetGroup();
				hsg.element = new String(firstToken);
				
				hsg.startPos = Integer.parseInt(s.substring(22,26).replaceAll("[A-Z]", "").trim());
				hsg.endPos = Integer.parseInt(s.substring(33,37).replaceAll("[A-Z]", "").trim());
				hsg.startChain = s.charAt(21);
				hsg.endChain = s.charAt(32);
				list.add(hsg);
			}
		}
		
		reader.close();
		Collections.sort(list);
		return list;
	}
	
	public static void main(String[] args) throws Exception
	{
		List<HelixSheetGroup> list = getList(
				ConfigReader.getPdbDir() + File.separator + "1DXJ.txt",'A',1,230);
		
		for( HelixSheetGroup hsg : list)
				System.out.println(hsg.element + " " +  hsg.startPos + " " + hsg.endPos);
	}
}
