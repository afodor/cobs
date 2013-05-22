package averagesAndOutput;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import parsingGrouping.HelixSheetGroup;
import parsingGrouping.PfamToPDB;

import utils.ConfigReader;

import covariance.datacontainers.Alignment;
import covariance.datacontainers.AlignmentLine;
import covariance.datacontainers.PdbFileWrapper;
import covariance.parsers.PfamParser;

public class TestAlphaBetaLocations
{
	public static void main(String[] args) throws Exception
	{

		HashMap<String, PfamToPDBAnnotations> pfamToPdbmap = PfamToPDBAnnotations.getAnnotationsAsMap();
		HashMap<String, PfamToPDB> otherPfamMap = PfamToPDB.getMapByName();
	
		for(String s : pfamToPdbmap.keySet())
			System.out.println(s);
			
		PfamParser parser = new PfamParser(true);

		for(Alignment a=  parser.getNextAlignment();
					a != null;
						a = parser.getNextAlignment())
		{
			if( a.getAligmentID().equals("2OG-FeII_Oxy_5"))
			{
				PdbFileWrapper fileWrapper = new PdbFileWrapper( new File( ConfigReader.getPdbDir() + File.separator + 
						pfamToPdbmap.get(a.getAligmentID()).getPdbID() + ".txt"));
				
				
				PfamToPDBAnnotations toPDB = pfamToPdbmap.get(a.getAligmentID());
				PfamToPDB otherToPdb = otherPfamMap.get(a.getAligmentID());
				
				HashMap<Integer, Integer> pdbToAlignmentNumberMap=  WriteScores.getPdbToAlignmentNumberMap(
						a, pfamToPdbmap.get(a.getAligmentID()), 
						otherPfamMap.get(a.getAligmentID()), fileWrapper);
				
				AlignmentLine aLine = a.getAnAlignmentLine(pfamToPdbmap.get(a.getAligmentID()).getPfamLine());
				String pfamSeq = aLine.getSequence();
				String pdbSeq = fileWrapper.getChain(otherPfamMap.get(a.getAligmentID()).getChainID()).getSequence();
				
				for(Integer i : pdbToAlignmentNumberMap.keySet())
					System.out.println(i + " " + pdbToAlignmentNumberMap.get(i) + " " + pdbSeq.charAt(i-1) + " " + 
															pfamSeq.charAt(pdbToAlignmentNumberMap.get(i)));
				
				List<HelixSheetGroup> helixSheetGroup= 
						HelixSheetGroup.getList(ConfigReader.getPdbDir() + File.separator + toPDB.getPdbID() + ".txt",
								otherToPdb.getChainID(), toPDB.getQueryStart(), toPDB.getQueryEnd());
				for(HelixSheetGroup hsg : helixSheetGroup)
					System.out.println(hsg.toString());
				

				System.out.println(ConfigReader.getPdbDir() + File.separator + 
						pfamToPdbmap.get(a.getAligmentID()).getPdbID() + ".txt");
				System.exit(1);
			}
				
		}
	}
}
