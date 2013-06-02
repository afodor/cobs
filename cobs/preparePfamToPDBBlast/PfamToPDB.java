package preparePfamToPDBBlast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.zip.GZIPInputStream;

import cobsScripts.WriteScores;

import covariance.datacontainers.Alignment;
import covariance.parsers.PfamParser;

import utils.ConfigReader;

class PfamToPDB
{
	private String pdbID;
	private char chainID;
	private int pdbResidueStart;
	private int pdbResidueEnd;
	private String pfamAccession;
	private String pfamName;
	private String pfamBestHit;
	private double eValue;
	private String originalLine;
	
	public String getOriginalLine()
	{
		return originalLine;
	}
	
	public void setPfamBestHit(String pfamBestHit)
	{
		this.pfamBestHit = pfamBestHit;
	}
	
	public double getEValue()
	{
		return eValue;
	}
	
	public String getPfamBestHit()
	{
		return pfamBestHit;
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
		return pfamName;
	}

	public static HashMap<String, PfamToPDB> getMapByName() throws Exception
	{
		
		HashMap<String, PfamToPDB> map = new LinkedHashMap<String, PfamToPDB>();
		
		File file = new File(ConfigReader.getPdbPfamChain());
		
		BufferedReader reader = file.getName().toLowerCase().endsWith("gz") ? 
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream( file )))) :  
				new BufferedReader(new FileReader(file)) ;
		
		reader.readLine();
		
		for(String s = reader.readLine(); 
				s != null; 
					s = reader.readLine())
		{
			//System.out.println(s);
			
			String[] splits = s.split("\t");
			
			PfamToPDB pdb  = new PfamToPDB();
			pdb.pdbID = new String(splits[0]);
			
			String chainString = splits[1];
			
			if( chainString.length() != 1)
				throw new Exception("Parsing error");
			
			pdb.chainID = chainString.charAt(0);
			pdb.originalLine = s;
			pdb.pdbResidueStart = Integer.parseInt( splits[2].replaceAll("[A-Z]", ""));
			pdb.pdbResidueEnd = Integer.parseInt(splits[3].replaceAll("[A-Z]", ""));
			
			if( pdb.pdbResidueEnd < pdb.pdbResidueStart)
			{
				int temp = pdb.pdbResidueEnd;
				pdb.pdbResidueEnd = pdb.pdbResidueStart;
				pdb.pdbResidueStart = temp;
			}
			
			pdb.pfamAccession = new String(splits[4]);
			pdb.pfamName = new String(splits[5]);
			pdb.eValue = Double.parseDouble(splits[7]);
			
			if( splits.length != 8)
				throw new Exception("Unexpected token " + s);
			
			PfamToPDB oldPdb = map.get(pdb.pfamName);
			
			if( pdb.pdbID.length() == 4 && ( oldPdb == null || pdb.eValue < oldPdb.eValue)) 
				map.put(pdb.pfamName, pdb);
		}
		
		reader.close();
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("c:\\temp\\newLines.txt"));
		HashMap<String, PfamToPDB> map = PfamToPDB.getMapByName();
		System.out.println(map.size());
		System.out.println(map.containsKey("1-cysPrx_C"));
		
		PfamParser parser = new PfamParser();
		
		while( ! parser.isFinished())
		{
			Alignment a = parser.getNextAlignment();
			
			PfamToPDB aPDB = map.get(a.getAligmentID());
			
			if( aPDB != null && a.getNumSequencesInAlignment() >= 200 && a.getNumSequencesInAlignment() <= 2000 &&
					aPDB.pdbResidueEnd - aPDB.pdbResidueStart >= WriteScores.MIN_PDB_LENGTH )
			{
				System.out.println(a.getAligmentID());
				writer.write( aPDB.originalLine + "\n" );
				writer.flush();
			}
		}
		
		writer.flush();  writer.close();
	}
}
