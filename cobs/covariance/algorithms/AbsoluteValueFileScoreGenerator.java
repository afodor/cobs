
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

import java.io.File;

import covariance.datacontainers.Alignment;

/*
 * Just returns the absolute value of a cached file for score generators
 */
public class AbsoluteValueFileScoreGenerator extends FileScoreGenerator
{
	public AbsoluteValueFileScoreGenerator(String name, File file, Alignment a) throws Exception
	{
		super(name,file,a);
	}
	
	@Override
	public Double getScore(Alignment a, int i, int j) throws Exception
	{
		return Math.abs(super.getScore(a, i, j));
	}
	
	@Override
	public String getAnalysisName()
	{
		return "Abs" + super.getAnalysisName();
	}
	
}
