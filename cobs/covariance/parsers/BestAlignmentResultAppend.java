package covariance.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;


public class BestAlignmentResultAppend implements Comparable
{
	private String pFamId;  //pfamID
	private String numSequences;
	private String numColumns;
	private String pdbID;  //pdbid
	private String alignmentLineId;  //pfamLine
	private String eScore;  //eScore
	private String qStart;
	private String qStop;
	private String tStart;
	private String tEnd;
	private String percentIdentity;  //% Identity
	private Character pdbChain; //CHAIN_ID
	
	/**
	 * 
	 */
	List<BestAlignmentResultAppend> resultingList;
				  							  
	
	public BestAlignmentResultAppend( String fileLine ) throws Exception
	{
		//pfamID	numSequences	numColumns	pdbid	pfamLine	eScore	queryStart	queryEnd	targetStart	targetEnd	percentIdentity	CHAIN_ID
		//pfamID	pdbid	pfamLine	eScore	queryStart	queryEnd	targetStart	targetEnd	percentIdentity	CHAIN_ID
		StringTokenizer sToken = new StringTokenizer( fileLine, "\t" );
//		this.pFamId= sToken.nextToken();
//		this.alignmentNum = Integer.parseInt( sToken.nextToken()); 
//		sToken.nextToken(); // numColumnsInAlignment
//		this.numSequencesInAlignment = Integer.parseInt( sToken.nextToken()); // numSequencesInAlignment
		
		if ( sToken.hasMoreElements() ) 
		{
			this.pFamId = sToken.nextToken();
			this.numSequences = sToken.nextToken();
			this.numColumns = sToken.nextToken();
			this.pdbID = sToken.nextToken();
			this.alignmentLineId = sToken.nextToken();
			this.eScore = sToken.nextToken();	
			this.qStart = sToken.nextToken();
			this.qStop = sToken.nextToken();
			this.tStart = sToken.nextToken();  
			this.tEnd = sToken.nextToken();
			this.percentIdentity = sToken.nextToken();
			this.pdbChain = sToken.nextToken().charAt(0);
			
			
			if ( sToken.hasMoreTokens() ) 
				throw new Exception("Parsing error");		
		}
		
	}


	
	
	public static HashSet<String> getPfamIdsAsSet(List<BestAlignmentResultAppend> list) throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		for ( BestAlignmentResultAppend rFileLine : list )
			set.add(rFileLine.getpFamId());
		
		return set;
	}
	
	
	public BestAlignmentResultAppend(File fileToParse) throws Exception
	{
		List<BestAlignmentResultAppend> list = new ArrayList<BestAlignmentResultAppend>();
		
		System.out.println("Opening "  + fileToParse.getAbsolutePath() );
		BufferedReader reader = new BufferedReader( new FileReader( fileToParse));
		
		reader.readLine();
		
		String nextLine = reader.readLine();
		
		while ( nextLine != null && nextLine.trim().length() != 0 ) 
		{
			list.add( new BestAlignmentResultAppend( nextLine ));
			nextLine = reader.readLine();
		}	
		
		resultingList = list;
	}
	
	public static BestAlignmentResultAppend getOneById(List<BestAlignmentResultAppend> list,
			String id) throws Exception
	{
		for ( BestAlignmentResultAppend rFileLine : list )
			if ( rFileLine.getpFamId().equals(id))
				return rFileLine;
			
		throw new Exception("Could not find" + id);
	}
	
	
	public int compareTo(Object o)
	{
		return this.pFamId.toUpperCase().compareTo( ((BestAlignmentResultAppend)o).pFamId.toUpperCase() );
	}

	public void setPdbID(String pdbID)
	{
		this.pdbID = pdbID;
	}

	public void setPdbChain(char pdbChain)
	{
		this.pdbChain = pdbChain;
	}

	public String getpFamId() {
		return pFamId;
	}

	

	public List<BestAlignmentResultAppend> getResultingList() {
		return resultingList;
	}




	public String getNumSequences() {
		return numSequences;
	}




	public String getNumColumns() {
		return numColumns;
	}




	public String getPdbID() {
		return pdbID;
	}




	public String getAlignmentLineId() {
		return alignmentLineId;
	}




	public String geteScore() {
		return eScore;
	}




	public String getqStart() {
		return qStart;
	}




	public String getqStop() {
		return qStop;
	}




	public String gettStart() {
		return tStart;
	}




	public String gettEnd() {
		return tEnd;
	}




	public String getPercentIdentity() {
		return percentIdentity;
	}




	public Character getPdbChain() {
		return pdbChain;
	}

}
