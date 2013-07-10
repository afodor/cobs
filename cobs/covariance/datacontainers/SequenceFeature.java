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

public class SequenceFeature
{
	private int startInclusive;
	private int endInclusive;
	private String color;
	int sequenceNum;
	
	public SequenceFeature( int start, int end, String color, int sequenceNum ) 
	{
		this.startInclusive = start;
		this.endInclusive = end;
		this.color = color;
		this.sequenceNum = sequenceNum;
	}
	
	
	public String getColor()
	{
		return color;
	}

	public int getEndInclusive()
	{
		return endInclusive;
	}

	public int getStartInclusive()
	{
		return startInclusive;
	}

	public int getSequenceNum()
	{
		return sequenceNum;
	}

}
