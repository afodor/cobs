package parsingGrouping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;


public class PfamToPDB
{ //pfamID	numSequences	numColumns	pdbid	pfamLine	eScore	queryStart	queryEnd	targetStart	targetEnd	percentIdentity	CHAIN_ID
	private String pdbID;
	private char chainID;
	private int pdbResidueStart;
	private int pdbResidueEnd;
	private String pfamAccession;
	private String pfamID;
	private String pfamLine;
	
	public void setPfamBestHit(String pfamBestHit)
	{
		this.pfamLine = pfamBestHit;
	}
	
	public String getPfamBestHit()
	{
		return pfamLine;
	}
	
	public String getPdbID()
	{
		return pdbID;
	}

	public char getChainID()
	{
		return chainID;
	}

	public int getPdbResidueStart()
	{
		return pdbResidueStart;
	}

	public int getPdbResidueEnd()
	{
		return pdbResidueEnd;
	}

	public String getPfamAccession()
	{
		return pfamAccession;
	}

	public String getPfamName()
	{
		return pfamID;
	}

	public static HashMap<String, PfamToPDB> getMapByName() throws Exception
	{
		HashMap<String, PfamToPDB> map = new HashMap<String, PfamToPDB>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getPdbPfamChain())));
		
		reader.readLine();
		
		for(String s = reader.readLine(); 
				s != null; 
					s = reader.readLine())
		{
			//System.out.println(s);
			StringTokenizer sToken = new StringTokenizer(s);
			
			PfamToPDB pdb  = new PfamToPDB();
			
			//Now to figure out the "correct" new order
			//Old was
			//PDB_ID	CHAIN_ID	PdbResNumStart	PdbResNumEnd	PFAM_ACC	PFAM_Name	PFAM_desc	eValue
			//New is
			//pfamID	numSequences	numColumns	pdbid	pfamLine	eScore	queryStart	queryEnd	targetStart	targetEnd	percentIdentity	CHAIN_ID
			
			pdb.pfamID = new String(sToken.nextToken());
			//These next two we will parse but not use right now
			Integer numSequences = Integer.parseInt( sToken.nextToken());
			Integer numColumns = Integer.parseInt( sToken.nextToken());
			
			pdb.pdbID = new String(sToken.nextToken());
			pdb.pfamAccession = new String(sToken.nextToken());
			
			//We are going to ignore this e-value here as well -- it appears this was ignored previously as well, probably to not collide with BLAST scores from other formatted files
			Double eScore = Double.parseDouble(sToken.nextToken());
			
			pdb.pdbResidueStart = Integer.parseInt( sToken.nextToken().replaceAll("[A-Z]", ""));
			pdb.pdbResidueEnd = Integer.parseInt(sToken.nextToken().replaceAll("[A-Z]", ""));
			
			//These next three aren't of interest to us right now either
			Integer startWeIgnore = Integer.parseInt( sToken.nextToken());
			Integer endWeIgnore = Integer.parseInt( sToken.nextToken());
			Double percentIdentity = Double.parseDouble(sToken.nextToken().replaceAll("[A-Z]", ""));
			
			//This entire block parses the CHAIN (which is one of the main purposes here
			String chainString = sToken.nextToken();
			if( chainString.length() != 1)
				throw new Exception("Parsing error");
			pdb.chainID = chainString.charAt(0);
			
			
			map.put(pdb.pfamID, pdb);
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, PfamToPDB> map = PfamToPDB.getMapByName();
		System.out.println(map.size());
		System.out.println(map.containsKey("PID"));
	}
}
