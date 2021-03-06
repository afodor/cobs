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


import covariance.algorithms.McBASCCovariance;
import covariance.datacontainers.Alignment;

public class AverageMcBASC implements GroupOfColumnsInterface
{
	private final McBASCCovariance mcBasc;
	private final HashMap<String, Double> cachedMap = new HashMap<String, Double>();
	private final Alignment a;
	
	public AverageMcBASC(Alignment a) throws Exception
	{
		this.a = a;
		mcBasc = new McBASCCovariance(a);
	}
	
	@Override
	public String getName()
	{
		return "AverageMcBASC";
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
					score = mcBasc.getScore(alignment, x, y);
					cachedMap.put(key, score);
				}
				
				if( score != mcBasc.NO_SCORE && ! Double.isInfinite(score) && ! Double.isNaN(score))
				{
					sum += score;
					n++;
				}
			}
		}
		
		if( Double.isInfinite(sum) || Double.isNaN(sum) || n==0)
			return mcBasc.NO_SCORE;
		
		return sum / n;
	}
}
