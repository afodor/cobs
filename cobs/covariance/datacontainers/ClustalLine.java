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


package covariance.datacontainers;

public class ClustalLine
{
	private int positionInOriginatingSequence;
	private int clustalIndex;
	private boolean isAlignmentSequence;
	private String lineName;
		
		
	public ClustalLine( 
					 String lineName,
		 			 int clustalIndex, 
					 int positionInOriginatingSequence, 
					 boolean isAlignmentSequence ) 
	{
		this.lineName = lineName;
		this.clustalIndex = clustalIndex;
		this.positionInOriginatingSequence = positionInOriginatingSequence;
		this.isAlignmentSequence = isAlignmentSequence;
	}		
		
	public int getClustalIndex()
	{
		return clustalIndex;
	}

	public boolean isAlignmentSequence()
	{
		return isAlignmentSequence;
	}

	public String getLineName()
	{
		return lineName;
	}

	public int getPositionInOriginatingSequence()
	{
		return positionInOriginatingSequence;
	}

	public void incrementPositionInOriginalSequence()
	{
		this.positionInOriginatingSequence++;
	}
}
