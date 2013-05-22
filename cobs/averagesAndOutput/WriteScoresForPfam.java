package averagesAndOutput;

import java.util.HashMap;

import parsingGrouping.PfamToPDB;

import covariance.algorithms.McBASCCovariance;
import covariance.datacontainers.Alignment;
import covariance.parsers.PfamParser;

public class WriteScoresForPfam
{
	public static void main(String[] args) throws Exception
	{
		int[][] substitutionMatrix = McBASCCovariance.getMaxhomMetric();
		
		PfamParser parser = new PfamParser(true);

		HashMap<String, PfamToPDB> pfamToPbd = PfamToPDB.getMapByName();
	
		
		for(Alignment a=  parser.getNextAlignment();
					a != null;
						a = parser.getNextAlignment())
		{
			if( a.getNumSequencesInAlignment() <2000)
			{
				PfamToPDB pdb = pfamToPbd.get(a.getAligmentID());
				System.out.println(a.getAligmentID() + " " + ((pdb == null) ? "none" : (pdb.getPdbID() + " " + pdb.getPfamAccession())  )) ;
			}
		}

	}
}
