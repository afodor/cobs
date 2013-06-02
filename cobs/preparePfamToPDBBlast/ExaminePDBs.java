package preparePfamToPDBBlast;

import gocAlgorithms.HelixSheetGroup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;


public class ExaminePDBs
{
	/*
	 * First run parsers.PfamToPDB.main()
	 * 
	 * This will require at least 7 elements
	 * 
	 */
	public static void main(String[] args) throws Exception
	{
		HashMap<String, PfamToPDB> map = PfamToPDB.getMapByName();
		
		BufferedWriter writer= new BufferedWriter(new FileWriter("c:\\temp\\newLinesFiltered.txt"));
		writer.write("PDB_ID\tCHAIN_ID\tpfamID\tnumberOfElements\telements\n");

		int numDone=0;
		for(PfamToPDB pfamToPdb : map.values())
		{
			try
			{
				File pdbFile = new File(ConfigReader.getPdbDir() + File.separator + pfamToPdb.getPdbID() + ".txt");
				List<HelixSheetGroup> list= HelixSheetGroup.getList(pdbFile.getAbsolutePath(), pfamToPdb.getChainID(), pfamToPdb.getPdbResidueStart(), 
						pfamToPdb.getPdbResidueEnd());
				
				//System.out.println(list.size() + " " + pfamToPdb.getPdbID());
				if(list.size() >=7)
				{
					numDone++;
					writer.write( pfamToPdb.getPdbID() + "\t" + pfamToPdb.getChainID() + "\t" + 
									pfamToPdb.getPfamName() + "\t" + list.size() + "\t" + list + "\n");
					System.out.println( numDone + " " + pfamToPdb.getPdbID() );
					writer.flush();
				}	
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
		writer.flush();  writer.close();
	}
}
