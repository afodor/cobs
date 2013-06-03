package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utils.MapResiduesToIndex;

import covariance.algorithms.ConservationSum;
import covariance.datacontainers.Alignment;
import covariance.datacontainers.AlignmentLine;

import junit.framework.TestCase;

public class TestConservationSum extends TestCase
{
	private Alignment getThreeColsEachWith20Aas() throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for( Character c : MapResiduesToIndex.charResidues)
			buff.append(c);
		
		String s = buff.toString();
		System.out.println(s);
		
		for( int x=0; x< 20; x++)
			System.out.println( x + " "+ s.charAt(x));
		
		String sReversed = new StringBuffer(s).reverse().toString();
		
		List<Character> list = new ArrayList<Character>();
		
		for( Character c : sReversed.toCharArray())
			list.add(c);
		
		Collections.shuffle(list);
		
		String sShuffled = "";
		
		for( Character c : list)
			sShuffled += c;
		
		List<AlignmentLine> lines= new ArrayList<AlignmentLine>();
		
		for( int x=0; x < 20; x++)
		{
			String seq ="" + s.charAt(x) + sReversed.charAt(x) + sShuffled.charAt(x); 
			System.out.println("Adding " + seq);
			lines.add( new AlignmentLine( ">" + x, seq));
		}
		
		Alignment a = new Alignment( "a",  lines);
		a.dumpAlignmentToConsole();
		
		int index=0;
		for( AlignmentLine aLine : a.getAlignmentLines() )
		{
			assertEquals(aLine.getSequence(), lines.get(index).getSequence());
			System.out.println("PASSED " + aLine.getSequence() + " "+ lines.get(index).getSequence());
			index++;
		}
		
		return a;
		
	}
	
	public void testAgainstOneKindOfEachResiude() throws Exception
	{
		
		double expectedVal = 0;
		
		for( int x=0; x< 20; x++)
			expectedVal += .05 * Math.log(0.05);
		
		expectedVal = -expectedVal;
		
		Alignment a = getThreeColsEachWith20Aas();
		ConservationSum cSum= new ConservationSum( a );
		assertEquals(cSum.getScore(0),expectedVal, 0.00001);
		assertEquals(cSum.getScore(1),expectedVal, 0.00001);
		assertEquals(cSum.getScore(2),expectedVal, 0.00001);
		
		assertEquals(cSum.getScore(a,0,1), expectedVal,0.00001);
		assertEquals(cSum.getScore(a,0,2), expectedVal,0.00001);
		assertEquals(cSum.getScore(a,1,2), expectedVal,0.00001);
	}
	
	public void testPerfectConservation() throws Exception
	{
		List<AlignmentLine> list = new ArrayList<AlignmentLine>();
		
		list.add(new AlignmentLine("1", "AGT") );
		list.add(new AlignmentLine("2", "AGT") );
		list.add(new AlignmentLine("3", "AGT") );
		
	
		Alignment a = new Alignment( "a",  list );

		ConservationSum cSum= new ConservationSum( a );
		
		assertEquals(cSum.getScore(0),0, 0.00001);
		assertEquals(cSum.getScore(1),0, 0.00001);
		assertEquals(cSum.getScore(2),0, 0.00001);
		
		assertEquals(cSum.getScore(a,0,1), 0, 0.00001);
		assertEquals(cSum.getScore(a,0,2), 0, 0.00001);
		assertEquals(cSum.getScore(a,1,2), 0, 0.00001);
	}
}
