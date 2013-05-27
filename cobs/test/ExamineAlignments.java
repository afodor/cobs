package test;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cobsScripts.WriteScores;

import parsingGrouping.HelixSheetGroup;
import utils.ConfigReader;
import covariance.datacontainers.Alignment;
import covariance.datacontainers.PdbFileWrapper;
import covariance.parsers.PfamParser;
import covariance.parsers.PfamToPDBBlastResults;

public class ExamineAlignments
{
	public static void main(String[] args) throws Exception
	{
		PfamParser parser = new PfamParser();
		HashMap<String, PfamToPDBBlastResults> map = PfamToPDBBlastResults.getAnnotationsAsMap();
		
		while(true)
		{
			Alignment a = parser.getNextAlignment();
			System.out.println(a.getAligmentID());
			
			//if( a.equals("Arch_ATPase"))
			{
				PfamToPDBBlastResults toPdb = map.get(a.getAligmentID());
				
				PdbFileWrapper fileWrapper = new PdbFileWrapper(toPdb.getPdbID());
				
				HashMap<Integer, Integer> pdbToAlignmentNumberMap=  WriteScores.getPdbToAlignmentNumberMap(a, toPdb, fileWrapper);
				List<HelixSheetGroup> helixSheetGroup= 
						HelixSheetGroup.getList(ConfigReader.getPdbDir() + File.separator + toPdb.getPdbID() + ".txt",
								toPdb.getChainId(), toPdb.getQueryStart(), toPdb.getQueryEnd());
				
				
				
			}
		}
	}
		
		
	//}
}
