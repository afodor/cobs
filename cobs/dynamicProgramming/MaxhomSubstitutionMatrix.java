package dynamicProgramming;

import utils.MapResiduesToIndex;
import covariance.algorithms.McBASCCovariance;

public class MaxhomSubstitutionMatrix implements SubstitutionMatrix
{
	private final int[][] matrix;
	
	public MaxhomSubstitutionMatrix() throws Exception
	{
		this.matrix = McBASCCovariance.getMaxhomMetric();
	}
	
	@Override
	public float getScore(char c1, char c2) throws Exception
	{
		int index1 = MapResiduesToIndex.getIndexOrNegativeOne(c1);
		
		if( index1 != -1)
		{
			int index2 = MapResiduesToIndex.getIndexOrNegativeOne(c2);
			
			if( index2 != -1)
				return  this.matrix[index1][index2];
		}
		
		return 0;
	}
	
	@Override
	public String getSubstitutionMatrixName()
	{
		return "Maxhom";
	}
}
