package covariance.algorithms;

import covariance.datacontainers.*;


public interface ScoreGenerator
{
	//Original 2004 paper method of excluding McBasc perfect conservation columns
	public static final double NO_SCORE = -2;
	
	//Because of the way that Pearson's is calculated for COBS (using a "TINY" double value)
	//McBasc will now need to score a 0 so that we are making the same assumptions in the code base.
	public static final double COBS_CONSISTENT = 1.0;
	
	public double getScore( Alignment a, int i, int j ) throws Exception;
	
	public String getAnalysisName();
	
	/** return true is getScore(a,i,j) == getScore( a, j, i )
	 */
	public boolean isSymmetrical();
	
	/** return true if a low score indicates high covariance or conservation.
	 *  return false if a high score indicates high covariance or conservation.
	*/
	public boolean reverseSort();
	
	public Alignment getAlignment();
	
}
