package averagesAndOutput;

import java.util.HashMap;

import covariance.algorithms.McBASCCovariance;
import covariance.datacontainers.Alignment;

public class MaxMcBASC implements GroupOfColumnsInterface
{
	private final McBASCCovariance mcBasc;
	private final HashMap<String, Double> cachedMap = new HashMap<String, Double>();
	private final Alignment a;
		
	public MaxMcBASC(Alignment a) throws Exception
	{
			this.a = a;
			mcBasc = new McBASCCovariance(a);
	}
		
	@Override
	public String getName()
	{
		return "MaxMcBASC";
	}
		
	@Override
	/*
	* Not thread safe.  Ignores columns with noScore..
	*/
	public double getScore(Alignment alignment, int leftPosStart,
				int leftPosEnd, int rightPosStart, int rightPosEnd)
				throws Exception
	{
		if( a != alignment)
			throw new Exception("NO");
			
		double val= Double.MIN_VALUE;
		
		for( int x = leftPosStart; x <= leftPosEnd; x++)
		{
			for(int y= rightPosStart; y <= rightPosEnd; y++)
			{
				String key = x + "@" + y;
				
				Double score = cachedMap.get(key);
				
				if( score == null)
				{
					score = mcBasc.getScore(alignment, x, y);
					cachedMap.put(key,score);
				}
				
				if( score != mcBasc.NO_SCORE && !Double.isInfinite(val) && ! Double.isNaN(val) )
				{
					val = Math.max(score, val);
				}
			}
		}
		
		if( val == Double.MIN_VALUE|| Double.isInfinite(val) || Double.isNaN(val))
			return mcBasc.NO_SCORE;
			
		return val;
	}
}
