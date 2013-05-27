package parsingGrouping;

import java.util.HashMap;


import scripts.PdbDownloadFromHTTP;

import covariance.datacontainers.Alignment;
import covariance.parsers.PfamParser;
import covariance.parsers.PfamToPDBBlastResults;

public class DownloadPDBFiles
{
	public static void main(String[] args) throws Exception
	{

		HashMap<String, PfamToPDBBlastResults> pdbMap = PfamToPDBBlastResults.getAnnotationsAsMap();
		
		PfamParser parser = new PfamParser(true);

		for(Alignment a=  parser.getNextAlignment();
					a != null;
						a = parser.getNextAlignment())
		{
			System.out.println(a.getAligmentID());
			
			PfamToPDBBlastResults pdb = pdbMap.get(a.getAligmentID());
			
			if( pdb != null)
			{
				System.out.println(pdb.getPdbID());
				PdbDownloadFromHTTP.downloadIfNotThere(pdb.getPdbID());
			}
		}
	}
}

