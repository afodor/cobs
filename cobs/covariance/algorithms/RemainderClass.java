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

public class RemainderClass implements Comparable
{
	
	private int orginalIndex;
	private float remainder;
	
	public int getOriginalIndex()
	{
		return this.orginalIndex;
	}
	
	public float getRemainder()
	{
		return this.remainder;
	}
	
	public RemainderClass( float remainder, int orginalIndex ) 
	{
		this.remainder = remainder;
		this.orginalIndex = orginalIndex;
	}
	
	/**  Sorts in ascending order
	 */
	public int compareTo(Object o)
	{
		return Float.compare( this.remainder, ((RemainderClass)o).remainder );
	}
	
	public String toString()
	{
		return orginalIndex + " " + remainder;
	}	
}
