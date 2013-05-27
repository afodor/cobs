package parsingGrouping;

import java.util.HashMap;

import averagesAndOutput.PfamToPDBAnnotations;

import scripts.PdbDownloadFromHTTP;

import covariance.datacontainers.Alignment;
import covariance.parsers.PfamParser;

public class DownloadPDBFiles
{
	public static void main(String[] args) throws Exception
	{

		HashMap<String, PfamToPDBAnnotations> pdbMap = PfamToPDBAnnotations.getAnnotationsAsMap();
		
		PfamParser parser = new PfamParser(true);

		for(Alignment a=  parser.getNextAlignment();
					a != null;
						a = parser.getNextAlignment())
		{
			System.out.println(a.getAligmentID());
			
			PfamToPDBAnnotations pdb = pdbMap.get(a.getAligmentID());
			
			if( pdb != null)
			{
				System.out.println(pdb.getPdbID());
				PdbDownloadFromHTTP.downloadIfNotThere(pdb.getPdbID());
			}
		}
	}
}

