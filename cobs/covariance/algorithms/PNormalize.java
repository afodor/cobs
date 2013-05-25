package covariance.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.Avevar;

import covariance.datacontainers.Alignment;

public class PNormalize implements ScoreGenerator
{
	private final HashMap<Integer, Double> averages = new HashMap<Integer, Double>();
	private final double grandAverage;
	private final ScoreGenerator wrappedMetric;
	
	public PNormalize(ScoreGenerator wrappedMetric) throws Exception
	{
		if(! wrappedMetric.isSymmetrical())
			throw new Exception("underlying algorithm must be symmetrical");
		
		this.wrappedMetric = wrappedMetric;		
		
		double sum =0;
		int n=0;
		
		HashMap<Integer, List<Double>> map = new HashMap<Integer, List<Double>>();
		
		for( int x=0; x < wrappedMetric.getAlignment().getNumColumnsInAlignment() -1; x++)
			for(int y=x +1; y < wrappedMetric.getAlignment().getNumColumnsInAlignment();y++)
			{
				double score = wrappedMetric.getScore(wrappedMetric.getAlignment(), x, y);
				n++;
				sum += score;
				addToMap(map, x, score);
				addToMap(map, y, score);
			}
		
		grandAverage = sum / n;
		
		for( Integer i : map.keySet() )
			averages.put(i, new Avevar(map.get(i)).getAve());

	}
	
	private static void addToMap( HashMap<Integer, List<Double>> map, int col, double score  )
	{
		List<Double> list = map.get(col);
		
		if(list==null)
		{
			list = new ArrayList<Double>();
			map.put(col, list);
		}
		
		list.add(score);
	}

	@Override
	public Alignment getAlignment()
	{
		return wrappedMetric.getAlignment();
	}
	
	@Override
	public double getScore(Alignment a, int i, int j) throws Exception
	{
		return wrappedMetric.getScore(a, i, j) - averages.get(i) * averages.get(j) / grandAverage;
	}

	@Override
	public String getAnalysisName()
	{
		return wrappedMetric.getAnalysisName() + "_PNormalInitial";
	}

	@Override
	public boolean isSymmetrical()
	{
		return true;
	}

	@Override
	public boolean reverseSort()
	{
		return wrappedMetric.reverseSort();
	}
}
