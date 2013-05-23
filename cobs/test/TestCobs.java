package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utils.MapResiduesToIndex;

import covariance.algorithms.McBASCCovariance;
import covariance.parsers.FastaSequence;
import junit.framework.TestCase;

public class TestCobs extends TestCase
{
	private static Random RANDOM = new Random(3432421);  // seed is there for consistent results
	
	public void testCobs() throws Exception
	{
		int seqLength =100;
		int[][] substitutionMatrix = McBASCCovariance.getMaxhomMetric();
		
		List<FastaSequence> list = new ArrayList<FastaSequence>();
		String s1 = getRandomProtein(seqLength);
		String s2 = getRandomProtein(seqLength);
		
		for( int x=0; x < 5; x++)
			list.add( new FastaSequence(">R1_", getRandomProtein(seqLength)));
		
		for(int x=0; x < 10;x++)
			list.add(new FastaSequence(">S1" + x, s1));
		
		for(int x=11; x < 20;x++)
			list.add(new FastaSequence(">S2" + x, s2));
		
		for( int x=0; x < 5; x++)
			list.add( new FastaSequence(">R2_", getRandomProtein(seqLength)));
		
		
	}
	
	private String getRandomProtein(int length) throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < length;x++)
			buff.append("" + MapResiduesToIndex.getChar(RANDOM.nextInt(20)));
		
		return buff.toString();
	}
	
}
