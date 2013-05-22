package parsingGrouping;

import java.util.HashMap;

import scripts.PdbDownloadFromHTTP;

import covariance.datacontainers.Alignment;
import covariance.parsers.PfamParser;

public class DownloadPDBFiles
{
	public static void main(String[] args) throws Exception
	{

		HashMap<String, PfamToPDB> pdbMap = PfamToPDB.getMapByName();
		
		PfamParser parser = new PfamParser(true);

		int numGot=0;  int num=0;
		for(Alignment a=  parser.getNextAlignment();
					a != null;
						a = parser.getNextAlignment())
		{
			System.out.println(a.getAligmentID());
			
			PfamToPDB pdb = pdbMap.get(a.getAligmentID());
			
			if( pdb != null)
			{
				numGot++;
				System.out.println(pdb.getPdbID());
				PdbDownloadFromHTTP.downloadIfNotThere(pdb.getPdbID());
			}
			
			num++;
			
			
		}
	}
}

