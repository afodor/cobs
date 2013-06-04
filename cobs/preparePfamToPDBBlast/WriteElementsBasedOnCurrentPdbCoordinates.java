package preparePfamToPDBBlast;

import gocAlgorithms.HelixSheetGroup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;

import covariance.parsers.PfamToPDBBlastResults;

/**
 * 
 * To be run after ExamineAlignments
 */
public class WriteElementsBasedOnCurrentPdbCoordinates
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				"c:\\temp\\pdbPfamFile3.txt"	)));
		
		writer.write("pdbId\tchainID\tpfamID\tpfamLine\tpfamStart\tpfamEnd\tpdbStart\t" + "" +
				"pdbEnd\tpercentIdentity\tpdbLength\teScore\tnumberOfElements\telements\n");
		
		HashMap<String, PfamToPDBBlastResults> map = PfamToPDBBlastResults.getAsMap();
		
		
		for(PfamToPDBBlastResults toPDB: map.values())
		{
			System.out.println(toPDB.getPfamID());
			List<HelixSheetGroup> helixSheetGroup= 
					HelixSheetGroup.getList(ConfigReader.getPdbDir() + File.separator + toPDB.getPdbID() + ".txt",
							toPDB.getChainId(), toPDB.getPdbStart(), toPDB.getPdbEnd());
			
			writer.write( toPDB.getPdbID() + "\t" );
			writer.write( toPDB.getChainId() + "\t");
			writer.write( toPDB.getPfamID() + "\t");
			writer.write( toPDB.getPfamLine() + "\t");
			writer.write( toPDB.getPfamStart() + "\t");
			writer.write( toPDB.getPfamEnd()  + "\t");
			writer.write( toPDB.getPdbStart() + "\t");
			writer.write( toPDB.getPdbEnd() + "\t");
			writer.write( toPDB.getPercentIdentity() + "\t");
			writer.write( (toPDB.getPdbEnd() - toPDB.getPdbStart()) + "\t");
			writer.write( toPDB.geteScore() + "\t" );
			writer.write( helixSheetGroup.size() + "\t");
			writer.write( helixSheetGroup+ "\n");
		}
		
		writer.flush();  writer.close();
	}
}
