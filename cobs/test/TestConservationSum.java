package test;

import java.util.ArrayList;
import java.util.List;

import covariance.algorithms.ConservationSum;
import covariance.datacontainers.Alignment;
import covariance.datacontainers.AlignmentLine;

import junit.framework.TestCase;

public class TestConservationSum extends TestCase
{
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
