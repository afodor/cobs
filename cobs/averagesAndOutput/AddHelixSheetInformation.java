package averagesAndOutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import parsingGrouping.HelixSheetGroup;

import utils.ConfigReader;



/*
 * First run kyleCleanroom2.WriteBestHitsForPFAM
 */
public class AddHelixSheetInformation
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getCobsHomeDirectory() + 
			File.separator + 	"bestPfamLines.txt")));
		
		reader.readLine();
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			File pdbFile = new File(ConfigReader.getPdbDir() + File.separator + splits[3] + ".txt");
			
			if( ! pdbFile.exists())
				throw new Exception("No " + pdbFile.getAbsolutePath());
			System.out.println(pdbFile.getAbsolutePath());
			
			List<HelixSheetGroup> list = HelixSheetGroup.getList(pdbFile.getAbsolutePath());
			System.out.println(list);
		}
	}
}
