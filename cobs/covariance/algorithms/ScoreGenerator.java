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


package covariance.algorithms;

import covariance.datacontainers.*;


public interface ScoreGenerator
{
	//Original 2004 paper method of excluding McBasc perfect conservation columns
	public static final double NO_SCORE = -2;
	
	//Because of the way that Pearson's is calculated for COBS (using a "TINY" double value)
	//McBasc will now need to score a 0 so that we are making the same assumptions in the code base.
	public static final double COBS_CONSISTENT = 1.0;
	
	public Double getScore( Alignment a, int i, int j ) throws Exception;
	
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
