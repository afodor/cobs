package parsingGrouping;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import scripts.PdbDownloadFromHTTP;
import utils.ConfigReader;
import utils.ReferenceSequenceUtils;

import covariance.datacontainers.Alignment;
import covariance.datacontainers.PdbFileWrapper;
import covariance.parsers.PfamParser;

public class PfamVsBetaSheets
{
	public static void main(String[] args) throws Exception
	{	
		HashMap<String, PfamToPDB> pdbMap = PfamToPDB.getMapByName();
		
		PfamParser parser = new PfamParser(true);

		int num=0;  int numGot=0;
		
		for(Alignment a=  parser.getNextAlignment();
					a != null;
						a = parser.getNextAlignment())
		{
		
			if( a.getAlignmentLines().size() <= 2000 )
			{
				System.out.println("Staring " + a.getAligmentID());
				try
				{
					PfamToPDB pdbInfo = pdbMap.get(a.getAligmentID());
					char chainHere = pdbInfo.getChainID();
					String pdbHere =  pdbInfo.getPdbID();
					System.out.println( "Chain " +  chainHere + " pdb " + pdbHere );
					System.out.println("CALLING IN");
					
					
					if( pdbInfo != null)
					{
						try
						{
							PdbDownloadFromHTTP.downloadIfNotThere(pdbInfo.getPdbID());
						}
						catch(Exception ex2)
						{
							System.out.println("Could not download " + pdbInfo.getPdbID());
						}
						
						File pdbFile = new File( ConfigReader.getPdbDir() + File.separator + 
								pdbInfo.getPdbID()+ ".txt");
						
						if(pdbFile.exists())
						{
							String bestLine = 
									findBestLine(pdbFile, a, pdbInfo.getChainID(), pdbInfo.getPdbResidueStart(), pdbInfo.getPdbResidueEnd());
							
							
							McBascWithHelixNotations.runOne(pdbFile, a, bestLine, pdbInfo.getChainID(), pdbInfo.getPdbResidueStart(), 
									pdbInfo.getPdbResidueEnd(), a.getAligmentID());
						}
					}
					
				System.out.println("Finished " + a.getAligmentID());
				numGot++;
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
				num++;
				
				System.out.println(num + " " + numGot);
			}
		}
	}
	
	/**  Just some sample code which shows how to find the best matching alignmentLine 
	 *   between an alignment and a strucutre.
	 *   
	 *   This code is very inefficient and can hence be very slow.
	 * 
	 *   It's also more than a little ugly, but it works.
	 *   
	 *   To find the best PDB structure for a given PFAM alignment, use 
	 *   scripts.ScanForBestAlignment
	 */
	private static String findBestLine(File pdbFile, Alignment a, char pdbChainChar, int pdbStartPosition, int pdbEndPos ) throws Exception
	{
		System.out.println("Parsing " + pdbFile.getAbsolutePath());
		PdbFileWrapper wrapper = new PdbFileWrapper( pdbFile );
										
		// a really ugly hack to make this code think your alignment came from Pfam!
		// if you want to check more than just a single chain, add more lines to the list!
		List<String> list = new ArrayList<String>();
		list.add( "#=GF DR   PDB; " + wrapper.getFourCharId() + " " + pdbChainChar
										+ "; " + pdbStartPosition +"; " +pdbEndPos+";");
		a.setPdbIds(list);
		
		return ReferenceSequenceUtils.getBestAlignmentPdbMatch(a, wrapper ).getAlignmentLine().getIdentifier();
	}
}
