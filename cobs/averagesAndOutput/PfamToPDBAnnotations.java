package averagesAndOutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import utils.ConfigReader;


public class PfamToPDBAnnotations
{
	//pfamID	numSequences	numColumns	pdbid	pfamLine	eScore	queryStart	queryEnd	targetStart	targetEnd	percentIdentity

	private String pfamID;
	private int numSequences;
	private int numColumns;
	private String pdbID;
	private String pfamLine;
	private String eScore;
	private int queryStart;
	private int queryEnd;
	private int targetStart;
	private int targetEnd;
	private double percentIdentity;
	private char chainId;
	
	public String getPfamID()
	{
		return pfamID;
	}
	
	public char getChainId()
	{
		return chainId;
	}

	public int getNumSequences()
	{
		return numSequences;
	}

	public int getNumColumns()
	{
		return numColumns;
	}

	public String getPdbID()
	{
		return pdbID;
	}

	public String getPfamLine()
	{
		return pfamLine;
	}

	public String geteScore()
	{
		return eScore;
	}

	public int getQueryStart()
	{
		return queryStart;
	}



	public int getQueryEnd()
	{
		return queryEnd;
	}



	public int getTargetStart()
	{
		return targetStart;
	}



	public int getTargetEnd()
	{
		return targetEnd;
	}



	public double getPercentIdentity()
	{
		return percentIdentity;
	}


	/*
	 * First run kyleCleanroom2.WriteBestHitsForPFAM
	 * 
	 * The key is the pfamID
	 */
	public static HashMap<String, PfamToPDBAnnotations> getAnnotationsAsMap() throws Exception
	{
		HashMap<String, PfamToPDBAnnotations> map = new LinkedHashMap<String, PfamToPDBAnnotations>();
		
		List<PfamToPDBAnnotations> list = getAnnotations();
		
		for(PfamToPDBAnnotations p : list)
		{
			if(map.containsKey(p.getPfamID()))
				throw new Exception("No");
			
			map.put(p.getPfamID(), p);
		}
		
		return map;
	}

	/*
	 * First run kyleCleanroom2.WriteBestHitsForPFAM
	 */
	public static List<PfamToPDBAnnotations> getAnnotations() throws Exception
	{
		List<PfamToPDBAnnotations> list = new ArrayList<PfamToPDBAnnotations>();
		
//		File f = new File("delete.txt");
//		System.out.println(f.getAbsolutePath());
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getPdbPfamChain())));
			
			reader.readLine();
			for(String s= reader.readLine(); s != null; s= reader.readLine())
			{
				String[] splits = s.split("\t");
				PfamToPDBAnnotations a = new PfamToPDBAnnotations();
				a.pfamID = splits[0];
				a.numSequences = Integer.parseInt(splits[1]);
				a.numColumns = Integer.parseInt(splits[2]);
				a.pdbID = splits[3];
				a.pfamLine = splits[4];
				a.eScore = splits[5];
				a.queryStart = Integer.parseInt(splits[6]);
				a.queryEnd = Integer.parseInt(splits[7]);
				a.targetStart = Integer.parseInt(splits[8]);
				a.targetEnd = Integer.parseInt(splits[9]);
				a.percentIdentity = Double.parseDouble(splits[10]);
				
				if( a.queryEnd < a.queryStart)
					throw new Exception("No");
				
				if( a.targetEnd < a.targetStart)
					throw new Exception("No");
				
				String chainToken = splits[11];
				
				if(chainToken.length() > 1)
					throw new Exception("Unexpected token");
				
				
				
				list.add(a);
				
			}
			
			List<PfamToPDBAnnotations> answer = Collections.unmodifiableList(list);
			if (answer.size() < 1){
				throw new IndexOutOfBoundsException("No parsing worked!");
			}
			
		return answer;
	}
	
	public static void main(String[] args) throws Exception
	{
		getAnnotations();
	}
}
