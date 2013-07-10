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
