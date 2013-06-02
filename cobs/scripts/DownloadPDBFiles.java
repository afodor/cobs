package scripts;

import java.util.HashMap;

import covariance.parsers.PfamToPDBBlastResults;


public class DownloadPDBFiles
{
	public static void main(String[] args) throws Exception
	{

		HashMap<String, PfamToPDBBlastResults> pdbMap = PfamToPDBBlastResults.getAsMap();
		
		int numDone=0;
		for(PfamToPDBBlastResults pdb : pdbMap.values())
		{
			numDone++;
			System.out.println( pdb.getPdbID() + " " +  pdb.getPdbID() + " " + numDone + " " + pdbMap.values().size());
			PdbDownloadFromHTTP.downloadIfNotThere(pdb.getPdbID());
		}
	}
}

