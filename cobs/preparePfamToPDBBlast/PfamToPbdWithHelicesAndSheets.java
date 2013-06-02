package preparePfamToPDBBlast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;


/*
 * First run parsers.PfamToPDB.main()
 *  then scratch.ExaminePDBs
 *  then scratch.BlastPDBToPfam
 */
class PfamToPbdWithHelicesAndSheets
{
	private String pdbID;
	private char chainId;
	private String pfamID;
	private int numberOfElements;
	private String elements;
	
	public String getPdbID()
	{
		return pdbID;
	}

	public void setPdbID(String pdbID)
	{
		this.pdbID = pdbID;
	}

	public char getChainId()
	{
		return chainId;
	}

	public void setChainId(char chainId)
	{
		this.chainId = chainId;
	}

	public String getPfamID()
	{
		return pfamID;
	}

	public void setPfamID(String pfamID)
	{
		this.pfamID = pfamID;
	}

	public int getNumberOfElements()
	{
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements)
	{
		this.numberOfElements = numberOfElements;
	}

	public String getElements()
	{
		return elements;
	}

	public void setElements(String elements)
	{
		this.elements = elements;
	}

	private PfamToPbdWithHelicesAndSheets(String s) throws Exception
	{
		StringTokenizer sToken = new StringTokenizer(s, "\t");
		this.pdbID = sToken.nextToken();
		this.chainId = sToken.nextToken().charAt(0);
		this.pfamID = sToken.nextToken();
		this.numberOfElements = Integer.parseInt(sToken.nextToken());
		this.elements = sToken.nextToken();
		
		if( sToken.hasMoreTokens())
			throw new Exception("Unexpeced token " + sToken.nextToken());
	}
	
	public static HashMap<String, PfamToPbdWithHelicesAndSheets> getAsMap() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File("c:\\temp\\newLinesFiltered.txt")));
		
		reader.readLine();
		
		HashMap<String, PfamToPbdWithHelicesAndSheets> map = new HashMap<>();
		
		for( String s= reader.readLine(); s!= null; s= reader.readLine())
		{
			PfamToPbdWithHelicesAndSheets blastResults = new PfamToPbdWithHelicesAndSheets(s);
			
			if( map.containsKey(blastResults.getPfamID()))
				throw new Exception("Duplicate id");
			
			map.put(blastResults.getPfamID(), blastResults);
			
		}
		
		reader.close();
		
		return map;
	}

}
