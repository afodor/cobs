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
import java.util.Random;

import utils.ConfigReader;
import utils.ProcessWrapper;

import covariance.datacontainers.Alignment;
import covariance.datacontainers.PdbFileWrapper;
import covariance.parsers.HitScores;
import covariance.parsers.PfamParser;

/*
 * First run scratch.ExaminePDBs
 */
public class BlastPDBToPfam
{
	private static Random random = new Random();
	
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("c:\\temp\\newLineWithBlast.txt")));
		
		writer.write("pdbId\tchainID\tpfamID\tpfamLine\tpfamStart\tpfamEnd\tqueryStart\tqueryEnd\tpercentIdentity\tpdbLength\teScore\tnumberOfElements\telements\n");

		HashMap<String, PfamToPbdWithHelicesAndSheets> blastMap = PfamToPbdWithHelicesAndSheets.getAsMap();
		
		PfamParser parser = new PfamParser();
		
		for( Alignment a = parser.getNextAlignment(); a != null; a = parser.getNextAlignment())
		{
			PfamToPbdWithHelicesAndSheets blastResults = blastMap.get(a.getAligmentID());
			
			if(blastResults != null)
			{
				try
				{
					File databaseFile = new File("c:\\temp\\temp" + File.separator + a.getAligmentID());
					a.writeUngappedAsFasta(databaseFile.getAbsolutePath());
					System.out.println(a.getAligmentID());
					makeBlastDB(databaseFile);
					PdbFileWrapper wrapper = new PdbFileWrapper(blastResults.getPdbID());
					HitScores hs = getTopHit(blastResults, wrapper, databaseFile);
					writer.write( blastResults.getPdbID() + "\t" + blastResults.getChainId() + "\t" + 
										blastResults.getPfamID() + "\t" + hs.getTargetId() + "\t" + 
							hs.getTargetStart() + "\t" + hs.getTargetEnd() + "\t" + 
							hs.getQueryStart() + "\t" + hs.getQueryEnd() + "\t" + 
							hs.getPercentIdentity() + "\t" + (hs.getQueryEnd() -hs.getQueryStart()) 
										+ "\t" +  hs.getEScore() + "\t" + blastResults.getNumberOfElements() + "\t" + 
											blastResults.getElements() + "\n");
					writer.flush();

					System.out.println("Wrote " + a.getAligmentID());

					
				}
				catch(Exception ex)
				{
					// the pdb files can sometimes throw.
					ex.printStackTrace();
				}
							
			}
		}
		
		writer.flush(); writer.close();
	}
	
	
	private static HitScores getTopHit(PfamToPbdWithHelicesAndSheets blastResults, PdbFileWrapper wrapper, File databaseFile) throws Exception
	{
		File outFile = new File(ConfigReader.getBlastDir() + File.separator + "PfamTEMP.txt" + "_" + System.currentTimeMillis() + 
				random.nextLong()
				);
		
		outFile.delete();
		
		if(outFile.exists())
			throw new Exception("out File exists");
		
		File queryFile = new File(ConfigReader.getBlastDir() + File.separator + "queryFileTEMP.txt");
		
		queryFile.delete();
		
		if( queryFile.exists())
			throw new Exception("query File exits");
		
		String seq = wrapper.getChain(blastResults.getChainId()).getSequence();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(queryFile));
		
		writer.write(">" + blastResults.getPdbID() + "\n");
		writer.write(seq + "\n");
		writer.flush();  writer.close();
		
		String[] args = new String[11];
		
		args[0] = ConfigReader.getBlastDir() + File.separator + "blastall";
		args[1] = "-p";
		args[2] ="blastp";
		args[3] = "-d";
		args[4] = databaseFile.getAbsolutePath();
		args[5] = "-o";
		args[6] = outFile.getAbsolutePath();
		args[7] = "-i";
		args[8] = queryFile.getAbsolutePath();
		args[9] = "-m";
		args[10] = "8";
		
		new ProcessWrapper(args);
		
		HitScores hitScore = HitScores.getTopHits(outFile.getAbsolutePath()).get(0);
		
		outFile.delete();
		
		return hitScore;
	}
	
	private static void makeBlastDB(File inFile) throws Exception
	{
		String[] args = new String[5];
		
		args[0] = ConfigReader.getBlastDir() + File.separator + "formatdb";
		args[1] = "-p";
		args[2] = "t";
		args[3] = "-i";
		args[4] = inFile.getAbsolutePath();
		
		new ProcessWrapper(args);
	}

}
