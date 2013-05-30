package scripts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import covariance.parsers.BestAlignmentResultAppend;

import utils.SangerQueryForBestPDBChain;

public class BestAlignmentResultAppendToNewFile {
	
	static SangerQueryForBestPDBChain q;
	
	public static void main(String [] args) throws Exception
	{
		//First to setup the required files to parse
		File sanger = new File ("/Users/kkreth/Desktop/Sanger_pdb_pfam_mapping.txt");
		q = new SangerQueryForBestPDBChain(sanger);
		createNewFileWithNewChainInfo();
	}
	
	
	public static void createNewFileWithNewChainInfo() throws Exception {
		String inputBestPFAM = "/Users/kkreth/workspace/git/cobs/cobs/bestPfamLines_pfamToPDB.txt";
		File tester = new File(inputBestPFAM);
		BufferedWriter targetScript = new BufferedWriter(new FileWriter(inputBestPFAM + "corrected.txt"));
		//First write the header
		targetScript.write("pfamID\tnumSequences\tnumColumns\tpdbid\tpfamLine\teScore\tqueryStart\tqueryEnd\ttargetStart\ttargetEnd\tpercentIdentity\tCHAIN_ID\n");
		BestAlignmentResultAppend b = new BestAlignmentResultAppend(tester);
		List<BestAlignmentResultAppend> allFromQuestionableFile = b.getResultingList();
		for (BestAlignmentResultAppend x : allFromQuestionableFile){
			String pdb = x.getPdbID();
			String pfam = x.getpFamId();
			q.setPdb(pdb);
			q.setPFAM_CommonName(pfam);
			Character answer = q.getSuccinctAnswer();
			System.out.println("Here is the answer for " + pfam + " and " + pdb + " " + answer);
			//Now to output the data
			targetScript.write(x.getpFamId());
			targetScript.write("\t");
			targetScript.write(x.getNumSequences());
			targetScript.write("\t");
			targetScript.write(x.getNumColumns());
			targetScript.write("\t");
			targetScript.write(x.getPdbID());
			targetScript.write("\t");
			targetScript.write(x.getAlignmentLineId());
			targetScript.write("\t");
			targetScript.write(x.geteScore()+ "\t");
			targetScript.write(x.getqStart());
			targetScript.write("\t");
			targetScript.write(x.getqStop());
			targetScript.write("\t");
			targetScript.write(q.getSuccinctPDBStart().toString());
			targetScript.write("\t");
			targetScript.write(q.getSuccinctPDBStop().toString());
			targetScript.write("\t");
			targetScript.write(x.getPercentIdentity() + "\t");
			targetScript.write(answer + "\n");
			targetScript.flush();

		}
		targetScript.close();
	}

}
