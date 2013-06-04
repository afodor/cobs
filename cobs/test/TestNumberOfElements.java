package test;

import java.util.HashMap;
import java.util.StringTokenizer;

import covariance.parsers.PfamToPDBBlastResults;

import junit.framework.TestCase;

public class TestNumberOfElements extends TestCase
{
	public void test() throws Exception
	{
		HashMap<String, PfamToPDBBlastResults> map = PfamToPDBBlastResults.getAsMap();
		
		for(PfamToPDBBlastResults pfamToPdb : map.values())
		{
			System.out.println(pfamToPdb.getOriginalLine());
			
			String[] elements = pfamToPdb.getElements().split(",");
			assertEquals(elements.length, pfamToPdb.getNumberOfElements());
			
			int oldFirstElement =-1;
			int oldSecondElement =-1;
			
			for( String e : elements )
			{
				int firstElement = getFirstElement(e, pfamToPdb.getChainId());
				int secondElement = getSecondElement(e,pfamToPdb.getChainId());
				
				System.out.println(firstElement + " " + secondElement + " " + pfamToPdb.getPdbStart() + " " + pfamToPdb.getPdbEnd() );
				assertTrue(secondElement >= firstElement);
				assertTrue( firstElement >= pfamToPdb.getPdbStart() );
				assertTrue( firstElement <= pfamToPdb.getPdbEnd());
				assertTrue( secondElement >= pfamToPdb.getPdbStart() );
				assertTrue( secondElement <= pfamToPdb.getPdbEnd());
				assertTrue( firstElement > oldFirstElement );
				assertTrue( firstElement >= oldSecondElement);
				assertTrue( secondElement > oldFirstElement);
				assertTrue( secondElement >= oldSecondElement);
				oldFirstElement = firstElement;
				oldSecondElement = secondElement;
			}
		}
	}
	
	private int getFirstElement(String s, char chainID)
	{
		StringTokenizer sToken = new StringTokenizer(s, "-");
		return getPosition(sToken.nextToken(),chainID);
	}
	
	private int getSecondElement(String s, char chainID)
	{
		StringTokenizer sToken = new StringTokenizer(s, "-");
		sToken.nextToken();
		return getPosition(sToken.nextToken(),chainID);
	}
	
	private int getPosition( String s, char chainID)
	{
		StringBuffer buff = new StringBuffer();
		
		for(Character c  : s.toCharArray())
		{
			if( Character.isDigit(c))
				buff.append(c);
		}
		
		if( Character.isDigit(chainID))
			buff = new StringBuffer( buff.toString().substring(1) );
		
		return Integer.parseInt(buff.toString());
	}
}
