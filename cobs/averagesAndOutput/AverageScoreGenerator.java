package averagesAndOutput;

import java.util.HashMap;

import covariance.algorithms.ScoreGenerator;
import covariance.datacontainers.Alignment;

public class AverageScoreGenerator implements GroupOfColumnsInterface
{
	private final ScoreGenerator sg;
	private final HashMap<String, Double> cachedMap = new HashMap<String, Double>();
	
	public AverageScoreGenerator(ScoreGenerator sg) throws Exception
	{
		this.sg = sg;
	}
	
	@Override
	public String getName()
	{
		return "Average" + sg.getAnalysisName();
	}
	
	@Override
	/*
	 * Not thread safe.
	 */
	public double getScore(Alignment alignment, int leftPosStart,
			int leftPosEnd, int rightPosStart, int rightPosEnd)
			throws Exception
	{
		if( alignment != sg.getAlignment())
			throw new Exception("NO");
		
		double sum =0;
		double n =0;
		
		for( int x = leftPosStart; x <= leftPosEnd; x++)
		{
			for(int y= rightPosStart; y <= rightPosEnd; y++)
			{
				String key = x + "@" + y;
				
				Double score = cachedMap.get(key);
				
				if( score == null)
				{
					score = sg.getScore(alignment, x, y);
					cachedMap.put(key, score);
				}
				
				sum += score;
				n++;
			}
		}
		
		return sum / n;
	}
}