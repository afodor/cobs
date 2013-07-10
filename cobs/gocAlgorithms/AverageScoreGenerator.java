/** 
 * Authors:  anthony.fodor@gmail.com  kylekreth@alumni.nd.edu
 * 
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */


package gocAlgorithms;


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
		//System.out.println("CHECKING " + alignment.getAligmentID() + " " + 
		//leftPosStart + " " + leftPosEnd + " " + rightPosStart + " " + rightPosEnd);
		
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
					if( x== y)
						System.out.println("Querying " + x + " " + y);
					
					
					score = sg.getScore(alignment, x, y);
				}
				
				cachedMap.put(key, score);
				sum += score;
				n++;
			}
		}
		
		return sum / n;
	}
}