package gocAlgorithms;


import covariance.datacontainers.Alignment;

public interface GroupOfColumnsInterface
{
	public double getScore( Alignment alignment,int leftPosStart, int leftPosEnd, int rightPosStart, int rightPosEnd)
		throws Exception;
	
	public String getName();
}
