
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
