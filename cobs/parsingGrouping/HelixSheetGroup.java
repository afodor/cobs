package parsingGrouping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;


public class HelixSheetGroup
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
		
		return returnList;
	}
	
	private static boolean alreadyThere(  HelixSheetGroup hsg, List<HelixSheetGroup> list )
	{
		for( HelixSheetGroup aHsg : list )
			if( hsg.startPos ==  aHsg.startPos && hsg.endPos == aHsg.endPos)
				return true;
		
		return false;
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
		return list;
	}
	
	public static void main(String[] args) throws Exception
	{
		for( HelixSheetGroup hsg : getList(ConfigReader.getCobsHomeDirectory() + File.separator + "2sns.pdb"))
				System.out.println(hsg.element + " " +  hsg.startPos + " " + hsg.endPos);
	}
}
