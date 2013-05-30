package utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.management.openmbean.OpenDataException;

import covariance.datacontainers.SangerPDBpfamMappingSingleLine;

import covariance.parsers.SangerPDBpfamMapping;

public class SangerQueryForBestPDBChain {
	/**
	 * Query PDB
	 */
	String pdb;	/**
	 * Query PFAM
	 */
	String PFAM_CommonName;
	/**
	 * Input Sanger file from ftp://ftp.sanger.ac.uk/pub/databases/Pfam/mappings/pdb_pfam_mapping.txt
	 */
	File sanger;
	/**
	 * This is what we will use INTERNALLY to look at the concatenation of an input of PFAM and PDB
	 */
	private HashMap<String, SangerPDBpfamMappingSingleLine> pFAMpdbBestHit = new HashMap<String, SangerPDBpfamMappingSingleLine>();
	/**
	 * This is what we will use INTERNALLY to look at the concatenation of an input of PFAM and PDB
	 */
	private HashMap<String, List<SangerPDBpfamMappingSingleLine>> sangerByPDB = new HashMap<String, List<SangerPDBpfamMappingSingleLine>>();
	/**
	 * This is what we will use INTERNALLY to look at the concatenation of an input of PFAM and PDB
	 */
	private HashMap<String, List<SangerPDBpfamMappingSingleLine>>  sangerByPFAM = new HashMap<String, List<SangerPDBpfamMappingSingleLine>>();
	private Integer succinctPDBStart;
	private Integer succinctPDBStop;
	/**
	 * @param sanger
	 * @throws IOException 
	 */
	public SangerQueryForBestPDBChain(File sanger) throws IOException {
		this.sanger = sanger;
		SangerPDBpfamMapping spf = new SangerPDBpfamMapping(sanger);
		//Now to organize for queries
		organize(spf);
	}

	private void organize(SangerPDBpfamMapping spf) {
		//First to Map everything by PFAM (common name that we will use) and PDB ID
		//Here we will just initialize the internal lists
		for (Integer line : spf.getSangerLines().keySet()){
			SangerPDBpfamMappingSingleLine tempLine = spf.getSangerLines().get(line);
			String commonPFAMname = tempLine.getpFAMname();
			String pdb = tempLine.getPdbID();
			List<SangerPDBpfamMappingSingleLine> pdbLoopList = new ArrayList<SangerPDBpfamMappingSingleLine>();
			List<SangerPDBpfamMappingSingleLine> pfamLoopList = new ArrayList<SangerPDBpfamMappingSingleLine>();
			//Now to populate the lists
			sangerByPDB.put(pdb.toLowerCase(), pdbLoopList);
			sangerByPFAM.put(commonPFAMname.toLowerCase(), pfamLoopList);
		}
		//Now to actually populate those values from the previous loop
		for (Integer line : spf.getSangerLines().keySet()){
			SangerPDBpfamMappingSingleLine tempLine = spf.getSangerLines().get(line);
			String commonPFAMname = tempLine.getpFAMname().toLowerCase();
			String pdb = tempLine.getPdbID().toLowerCase();
			List<SangerPDBpfamMappingSingleLine> pdbLoopList = sangerByPDB.get(pdb);
			List<SangerPDBpfamMappingSingleLine> pfamLoopList = sangerByPFAM.get(commonPFAMname);
			pdbLoopList.add(tempLine);
			pfamLoopList.add(tempLine);
			//Now to re-populate the lists
			sangerByPDB.put(pdb.toLowerCase(), pdbLoopList);
			sangerByPFAM.put(commonPFAMname.toLowerCase(), pfamLoopList);
		}
		
	}
	
	public List<SangerPDBpfamMappingSingleLine> getLongWindedAnswers (){
		List<SangerPDBpfamMappingSingleLine> answer = new ArrayList<SangerPDBpfamMappingSingleLine>();
		//test that we are ready
		if (
				(pdb == null) || (PFAM_CommonName == null)
			){
			throw new StackOverflowError("We had no input query");
		}
		//Now to find the lists that overlap
		List<SangerPDBpfamMappingSingleLine> pdbList = sangerByPDB.get(pdb);
		List<SangerPDBpfamMappingSingleLine> pfamList = sangerByPFAM.get(PFAM_CommonName);
		//We can now loop through these
		for (SangerPDBpfamMappingSingleLine x : pdbList){
			for (SangerPDBpfamMappingSingleLine y : pfamList){
				//if we have a match, we are great!
				if (x==y){
					answer.add(x);
				}
			}
		}
		
		if (answer.size() < 1){
			throw new ArithmeticException("We didn't find any matches");
		}
		
		return answer;
	}
	
public Character getSuccinctAnswer (){
		List<SangerPDBpfamMappingSingleLine> answer = new ArrayList<SangerPDBpfamMappingSingleLine>();
		//test that we are ready
		if (
				(pdb == null) || (PFAM_CommonName == null)
			){
			throw new StackOverflowError("We had no input query");
		}
		//Now to find the lists that overlap
		List<SangerPDBpfamMappingSingleLine> pdbList = sangerByPDB.get(pdb);
		List<SangerPDBpfamMappingSingleLine> pfamList = sangerByPFAM.get(PFAM_CommonName);
		//We can now loop through these
		try {
			for (SangerPDBpfamMappingSingleLine x : pdbList){
				for (SangerPDBpfamMappingSingleLine y : pfamList){
					//if we have a match, we are great!
					if (x==y){
						answer.add(x);
					}
				}
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.err.println("Problem was found on a lookup of pfam " + PFAM_CommonName + " and pdb " + pdb);
		}
		
		if (answer.size() < 1){
			throw new ArithmeticException("We didn't find any matches");
		}
		
		if (answer.size() == 1){
			this.succinctPDBStart = answer.get(0).getPdbResNumStart();
			this.succinctPDBStop = answer.get(0).getPdbResNumEnd();
			return answer.get(0).getChainID();
		}
		
		Character winningCharacter = null;
		Double evalue = 999999D;
		Integer winningLength = null;
		SangerPDBpfamMappingSingleLine winner = null;
		if (answer.size() > 1){
			System.out.println("We found more than one match, we are going to calculate the best.  With an identical eValue, one will arbitrarily be chosen.");
			for (SangerPDBpfamMappingSingleLine x : answer){
				if (x.geteValue() < evalue){
					winner = x;
				}
			}
		}
		this.succinctPDBStart = winner.getPdbResNumStart();
		this.succinctPDBStop = winner.getPdbResNumEnd();
		return winner.getChainID();
		
	}

	public Integer getSuccinctPDBStart() {
	return this.succinctPDBStart;
}

public Integer getSuccinctPDBStop() {
	return this.succinctPDBStop;
}

	public String getPdb() {
		return pdb;
	}

	public void setPdb(String pdb) throws OpenDataException {
		if (pdb.length() != 4){
			throw new OpenDataException("You must have a 4 letter PDB code");
		}
		this.pdb = pdb.toLowerCase();
	}

	public String getPFAM_CommonName() {
		return PFAM_CommonName;
	}

	public void setPFAM_CommonName(String pFAM_CommonName) throws OpenDataException {
		if (pFAM_CommonName.length() < 2){
			throw new OpenDataException("Unlikely that the PFAM family is this short");
		}
		PFAM_CommonName = pFAM_CommonName.toLowerCase();
	}
	
}
