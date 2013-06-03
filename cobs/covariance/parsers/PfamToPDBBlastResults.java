package covariance.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import utils.ConfigReader;

public class PfamToPDBBlastResults
{
	//pdbId	chainID	pfamID	pfamLine	pfamStart	pfamEnd	pdbStart	pdbEnd	percentIdentity	
	//pdbLength	eScore	numberOfElements	elements

	private final String pdbID;
	private final char chainId;
	private final String pfamID;
	private final String pfamLine;
	private final int pfamStart;
	private final int pfamEnd;
	private final int pdbStart;
	private final int pdbEnd;
	private double percentIdentity;
	private final int pdbLength;
	private final double eScore;
	private final int numberOfElements;
	private final String elements;
	private final String originalLine;
	
	public String getOriginalLine()
	{
		return originalLine;
	}
	
	public String getPdbID()
	{
		return pdbID;
	}

	public char getChainId()
	{
		return chainId;
	}

	public String getPfamLine()
	{
		return pfamLine;
	}

	public int getPfamStart()
	{
		return pfamStart;
	}

	public int getPfamEnd()
	{
		return pfamEnd;
	}

	public int getPdbStart()
	{
		return pdbStart;
	}

	public int getPdbEnd()
	{
		return pdbEnd;
	}

	public double getPercentIdentity()
	{
		return percentIdentity;
	}

	public int getPdbLength()
	{
		return pdbLength;
	}

	public double geteScore()
	{
		return eScore;
	}

	public int getNumberOfElements()
	{
		return numberOfElements;
	}

	public String getElements()
	{
		return elements;
	}
	
	public String getPfamID()
	{
		return pfamID;
	}
	

	private PfamToPDBBlastResults(String s) throws Exception
	{
		StringTokenizer sToken = new StringTokenizer(s, "\t");
		
		this.pdbID = sToken.nextToken();
		this.chainId= sToken.nextToken().charAt(0);
		this.pfamID = sToken.nextToken();
		this.pfamLine = sToken.nextToken();
		this.pfamStart = Integer.parseInt(sToken.nextToken());
		this.pfamEnd = Integer.parseInt(sToken.nextToken());
		this.pdbStart = Integer.parseInt(sToken.nextToken());
		this.pdbEnd = Integer.parseInt(sToken.nextToken());
		this.percentIdentity = Double.parseDouble(sToken.nextToken());
		this.pdbLength = Integer.parseInt(sToken.nextToken());
		this.eScore = Double.parseDouble(sToken.nextToken());
		this.numberOfElements = Integer.parseInt(sToken.nextToken());
		this.elements = sToken.nextToken();
		this.originalLine=s;
		
		if(sToken.hasMoreTokens()) 
			throw new Exception("Unexpected token " + sToken.nextToken());
	}
	
	public static HashMap<String, PfamToPDBBlastResults> getAsMap() throws Exception
	{
		HashMap<String, PfamToPDBBlastResults>  map = new HashMap<String, PfamToPDBBlastResults>();
		
		File file = new File(ConfigReader.getPdbPfamChain());
		
		BufferedReader reader = 
		file.getName().toLowerCase().endsWith("gz") ? 
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream( file )))) :  
				new BufferedReader(new FileReader(file)) ;
		
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			PfamToPDBBlastResults results = new PfamToPDBBlastResults(s);
			
			if( map.containsKey(results.getPfamID())) 
				throw new Exception("Duplicate pfam id " + results.getPfamID());
			
			map.put(results.getPfamID(), results);
		}
		
		reader.close();
		return map;
	}
}
