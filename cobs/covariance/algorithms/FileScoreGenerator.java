package covariance.algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;

import covariance.datacontainers.Alignment;

public class FileScoreGenerator implements ScoreGenerator
{
	private final String name;
	private final ConcurrentHashMap<String, Double> scores = new ConcurrentHashMap<String,Double>();
	private final Alignment a;
	private final boolean isSymmetrical;
	private final boolean reverseSort;
	private int lastI;
	private int lastJ;
	
	@Override
	/*
	 * Assumes i < j
	 */
	public Double getScore(Alignment a, int i, int j) throws Exception
	{
		
	
		Double score = scores.get(i + "@" + j);
		
		if( score == null)
		{
			throw new Exception("Could not find " + i + " " + j + " last vals= " + lastI + "  " + lastJ);
		}
			
		
		return score;
	}
	
	public int getNumScores()
	{
		return scores.size();
	}
	
	@Override
	public Alignment getAlignment()
	{
		return a;
	}

	@Override
	public String getAnalysisName()
	{
		return name;
	}
	
	@Override
	public boolean isSymmetrical()
	{
		return isSymmetrical;
	}
	
	@Override
	public boolean reverseSort()
	{
		return reverseSort;
	}
	
	public FileScoreGenerator(String name, File file, Alignment a) throws Exception
	{
		this(name,file,a,true,false);
	}
	
	public FileScoreGenerator(String name, File file, Alignment a, boolean isSymmetrical, boolean reverseSort) throws Exception
	{
		this.name = name;
		
		BufferedReader reader = file.getName().toLowerCase().endsWith("gz") ? 
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream( file )))) :  
				new BufferedReader(new FileReader(file)) ;
		
		reader.readLine();
		
		for(String s= reader.readLine(); s!= null; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			
			if( sToken.countTokens() > 3)
				throw new Exception("Parsing error");
			
			if( sToken.countTokens() == 3)
			{
				int i = Integer.parseInt(sToken.nextToken());
				int j = Integer.parseInt(sToken.nextToken());
				double score = Double.parseDouble(sToken.nextToken());
				
				String key = i + "@" + j;
				
				if( scores.containsKey(key) )
					throw new Exception("Parsing error");
				
				scores.put(key,score);
				lastI = i;
				lastJ = j;
			}
		}
		
		this.a = a;
		this.isSymmetrical = isSymmetrical;
		this.reverseSort = reverseSort;
		reader.close();
	}
}
