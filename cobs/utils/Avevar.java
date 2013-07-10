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


package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**  Calculates mean and variance from an array of doubles
 */
public class Avevar
{
	double ave;
	double var;
	
	public double getAve()
	{
	    return ave;
	}
	
	public double getVar()
	{
	    return var;
	}
	
	public double getSqrt()
	{
		return Math.sqrt(var);
	}
	
	public double getSD()
	{
		return Math.sqrt(var);
	}
	
	@Override
	public String toString()
	{
		return this.getAve() + " " + this.getSD();
	}
	
	public Avevar( double[] data ) 
	{
		double s, ep;
		int j;
		
		ave = 0.0;
		for ( j=0; j < data.length; j++ ) ave += data[j];
		ave /= data.length;
		var = ep = 00;
		
		for ( j = 0; j < data.length; j++) {
			s = data[j] - ave;
			ep += s;
			var += s*s;
		}
		
		var = ( var - ep * ep / data.length ) / ( data.length - 1);
	}
	
	
	public Avevar( List<? extends Number> data ) 
	{
		double s, ep;
		int j;
		
		ave = 0.0;
		for ( j=0; j < data.size(); j++ ) ave += (data.get(j)).doubleValue();
		ave /= data.size();
		var = ep = 00;
		
		for ( j = 0; j < data.size(); j++) {
			s = (data.get(j)).doubleValue() - ave;
			ep += s;
			var += s*s;
		}
		
		var = ( var - ep * ep / data.size() ) / ( data.size()- 1);
	}
    
	
    public Avevar( float[] data ) 
	{
        double s, ep;
		int j;
		
		ave = 0.0;
		for ( j=0; j < data.length; j++ ) ave += data[j];
		ave /= data.length;
		var = ep = 00;
		
		for ( j = 0; j < data.length; j++) {
			s = data[j] - ave;
			ep += s;
			var += s*s;
		}
		
		var = ( var - ep * ep / data.length ) / ( data.length - 1);
	}
    

	public static double getMedian(List<Double> list)
	{
		List<Double> newList = new ArrayList<Double>();
		
		for(Double d : list)
			newList.add(d);
		
		Collections.sort(newList);
		
		if( list.size() % 2== 1)
		{
			return newList.get(newList.size()/2);
		}
		
		return ( newList.get(newList.size()/2) + newList.get(newList.size()/2 -1))/2 ;
	}
	
}