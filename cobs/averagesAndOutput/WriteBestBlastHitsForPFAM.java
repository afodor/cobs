package averagesAndOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Random;

import parsingGrouping.PfamToPDB;

import utils.ConfigReader;
import utils.ProcessWrapper;

import covariance.datacontainers.Alignment;
import covariance.datacontainers.PdbFileWrapper;
import covariance.parsers.HitScores;
import covariance.parsers.PfamParser;

public class WriteBestBlastHitsForPFAM
{
	private static Random random = new Random();
	
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getCobsHomeDirectory() + File.separator + 
				"bestPfamLines.txt")));
		
		writer.write("pfamID\tnumSequences\tnumColumns\tpdbid\tpfamLine\teScore\tqueryStart\tqueryEnd\ttargetStart\ttargetEnd\tpercentIdentity\n");
		PfamParser parser = new PfamParser(true);

		HashMap<String, PfamToPDB> pfamToPbd = PfamToPDB.getMapByName();
	
		for(Alignment a=  parser.getNextAlignment();
					a != null;
						a = parser.getNextAlignment())
		{
			if( a.getNumSequencesInAlignment() >=200 && a.getNumSequencesInAlignment() <=2000)
			{	
				PfamToPDB pdb = pfamToPbd.get(a.getAligmentID());
				
				if( pdb != null)
				{
					File pdbFile = new File(ConfigReader.getPdbDir() + File.separator + pdb.getPdbID() + ".txt");
					
					try
					{
						if(pdbFile.exists())
						{
							PdbFileWrapper wrapper = new PdbFileWrapper(pdbFile);
							File outFile = new File(ConfigReader.getCobsHomeDirectory() + File.separator + "temp" + File.separator + a.getAligmentID());
							a.writeUngappedAsFasta(outFile.getAbsolutePath());
							System.out.println(a.getAligmentID());
							makeBlastDB(outFile);
							HitScores hit = getTopHit(wrapper,pdb, outFile);
							System.out.println(hit.getTargetId());
							System.out.println(hit.getEScore());
							writer.write(a.getAligmentID() + "\t" + a.getNumSequencesInAlignment() + "\t" + a.getNumColumnsInAlignment() + "\t" +
							  pdb.getPdbID() + "\t" + hit.getTargetId() + "\t" + hit.getEScore() + "\t" +
							hit.getQueryStart() + "\t" + hit.getQueryEnd() + "\t" + hit.getTargetStart() + "\t" +hit.getTargetEnd() + "\t" + hit.getPercentIdentity()
							+ "\n");
							writer.flush();
						}
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}	
				}
			}
		}
		
		writer.flush();  writer.close();

	}
	
	private static HitScores getTopHit(PdbFileWrapper wrapper, PfamToPDB pdb, File inFile) throws Exception
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
		
		String seq = wrapper.getChain(pdb.getChainID()).getSequence();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(queryFile));
		
		writer.write(">" + pdb.getPdbID() + "\n");
		writer.write(seq + "\n");
		writer.flush();  writer.close();
		
		String[] args = new String[11];
		
		args[0] = ConfigReader.getBlastDir() + File.separator + "blastall";
		args[1] = "-p";
		args[2] ="blastp";
		args[3] = "-d";
		args[4] = inFile.getAbsolutePath();
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
