/** 
 * Authors:  anthony.fodor@gmail.com  kylekreth@alumni.nd.edu
 * 
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */


package preparePfamToPDBBlast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

import cobsScripts.WriteScores;

import covariance.datacontainers.Alignment;
import covariance.datacontainers.PdbFileWrapper;
import covariance.parsers.PfamParser;
import covariance.parsers.PfamToPDBBlastResults;

public class ExamineAlignments
{
	/*
	 * Run scratch.SecondFilterOfPfamIDs.
	 * 
	 * If the global alignment comes out overly gapped, we take out the last few alignments
	 */
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
				"c:\\temp\\pdbToPfamForCobsViaBlast2.txt")));
		
		writer.write("pdbId\tchainID\tpfamID\tpfamLine\tpfamStart\tpfamEnd\t" + "" +
				"pdbStart\tpdbEnd\tpercentIdentity\tpdbLength\teScore\tnumberOfElements\telements\n");
		
		PfamParser parser = new PfamParser();
		HashMap<String, PfamToPDBBlastResults> map = PfamToPDBBlastResults.getAsMap();

		int numFailures=0;
		int numSuccesses =0;

		while( ! parser.isFinished())
		{
			Alignment a = parser.getNextAlignment();
			
			if(a.getNumSequencesInAlignment() <200 || a.getNumSequencesInAlignment() > 2000)
				throw new Exception("Unexpected number of sequences ");
			
			PfamToPDBBlastResults toPdb = map.get(a.getAligmentID());
			
			if( toPdb!= null)
			{
				String pdbInQuestion = toPdb.getPdbID();
				
				PdbFileWrapper fileWrapper = new PdbFileWrapper(pdbInQuestion);
			
				try
				{
					WriteScores.getPdbToAlignmentNumberMap(a, toPdb, fileWrapper);		
					numSuccesses++;
					writer.write(toPdb.getOriginalLine() + "\n");
					writer.flush();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					System.err.println("We seemed to have an issue parsing for a particular PDB on this family, skipping");
					System.exit(1);
					numFailures++;
				}
				System.out.println("SUCCESS " + numSuccesses + " FAILURE " + numFailures);
			}
		} 
		
		writer.flush();  writer.close();
	}
}
