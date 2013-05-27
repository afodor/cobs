package cobsScripts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import parsingGrouping.HelixSheetGroup;
import parsingGrouping.COBS;

import utils.ConfigReader;

import covariance.algorithms.ConservationSum;
import covariance.algorithms.FileScoreGenerator;
import covariance.algorithms.MICovariance;
import covariance.algorithms.PNormalize;
import covariance.algorithms.RandomScore;
import covariance.datacontainers.Alignment;
import covariance.datacontainers.AlignmentLine;
import covariance.datacontainers.PdbFileWrapper;
import covariance.datacontainers.PdbResidue;
import covariance.parsers.PfamParser;
import covariance.parsers.PfamToPDBBlastResults;
import dynamicProgramming.MaxhomSubstitutionMatrix;
import dynamicProgramming.NeedlemanWunsch;
import dynamicProgramming.PairedAlignment;

public class WriteScores
{
	public static final int MIN_PDB_LENGTH = 80;
	public static final double MIN_PERCENT_IDENTITY= 90;
	public static MaxhomSubstitutionMatrix substitutionMatrix;
	public static final int NUM_THREADS = 1;
	
	static
	{
		try
		{
			substitutionMatrix = new MaxhomSubstitutionMatrix();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.exit(1);
		}
		
	}
	
	private static File getMcBascFileName(Alignment a) throws Exception
	{
		return new File(ConfigReader.getCleanroom() + File.separator + "results" + File.separator + 
				"oneD" + File.separator + 
				a.getAligmentID() + "_" + "McBASC"+ ".txt");
	}
	

	private static File getRandomFileName(Alignment a) throws Exception
	{
		return new File(ConfigReader.getCleanroom() + File.separator + "results" + File.separator + 
				"oneD" + File.separator + 
				a.getAligmentID() + "_" + "random"+ ".txt");
	}
	
	
	private static FileScoreGenerator getMcBascFSGorNull(Alignment a) throws Exception
	{
		File file = getMcBascFileName(a);
		
		if (! file.exists())
		{
			System.out.println("Could not find " + file.getAbsolutePath());
			return null;
			
		}
			
		FileScoreGenerator fsg = new FileScoreGenerator("McBASC", file, a);
		
		if( a.getNumColumnsInAlignment() * (a.getNumColumnsInAlignment()-1) / 2 != fsg.getNumScores() )
		{
			System.out.println("Truncated " + file.getAbsolutePath());			
			return null;
			
		}
			
		return fsg;
	}
	
	private static FileScoreGenerator getRandomOrNull(Alignment a) throws Exception
	{
		File file = getRandomFileName(a);
		
		if (! file.exists())
		{
			System.out.println("Could not find " + file.getAbsolutePath());
			return null;
			
		}
			
		FileScoreGenerator fsg = new FileScoreGenerator("random", file, a);
		
		if( a.getNumColumnsInAlignment() * (a.getNumColumnsInAlignment()-1) / 2 != fsg.getNumScores() )
		{
			System.out.println("Truncated " + file.getAbsolutePath());			
			return null;
			
		}
			
		return fsg;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, PfamToPDBBlastResults> pfamToPdbmap = PfamToPDBBlastResults.getAnnotationsAsMap();
		
		System.out.println(pfamToPdbmap.keySet());
		
		Semaphore semaphore = new Semaphore(NUM_THREADS);
		
		PfamParser parser = new PfamParser();

		for(Alignment a=  parser.getNextAlignment();
					a != null;
						a = parser.getNextAlignment())
		{
			PfamToPDBBlastResults toPdb = pfamToPdbmap.get(a.getAligmentID());
			
			System.out.println("Trying " + a.getAligmentID());
			
			if( toPdb != null && (toPdb.getQueryEnd() - toPdb.getQueryStart()) >= MIN_PDB_LENGTH 
						&& toPdb.getPercentIdentity() >= MIN_PERCENT_IDENTITY  )
			{
				//FileScoreGenerator mcbascFSG = getMcBascFSGorNull(a);
				
				if(true)//if( mcbascFSG != null)
				{
					System.out.println("Starting " + a.getAligmentID());
					
					/*
					kickOneOffIfFileDoesNotExist(semaphore, a, toPdb, otherPdb, 
							new AverageScoreGenerator(mcbascFSG));
					
					
					kickOneOffIfFileDoesNotExist(semaphore, a, toPdb, otherPdb, new COBS());
					*/
					
					kickOneOffIfFileDoesNotExist(semaphore, a, toPdb,
						new AverageScoreGenerator(getRandomOrNull(a)));
					
					/*
					kickOneOffIfFileDoesNotExist(semaphore, a, toPdb, otherPdb, 
							new AverageScoreGenerator(new PNormalize(new MICovariance(a))));
					
					kickOneOffIfFileDoesNotExist(semaphore, a, toPdb, otherPdb, 
							new AverageScoreGenerator(new MICovariance(a)));											
					
					kickOneOffIfFileDoesNotExist(semaphore, a, toPdb, otherPdb, 
							new AverageScoreGenerator(new ConservationSum(a)));
					
					kickOneOffIfFileDoesNotExist(semaphore, a, toPdb, otherPdb, 
							new AverageScoreGenerator(new PNormalize(new ConservationSum(a))));
					
					kickOneOffIfFileDoesNotExist(semaphore, a, toPdb, otherPdb, 
							new AverageScoreGenerator( new PNormalize(mcbascFSG)));
							*/
				}
				else
				{	
					System.out.println("Skipping " + a.getAligmentID());
				}
			}
		}
		
		int num = NUM_THREADS;
		
		while( num > 0)
		{
			semaphore.acquire();
			num--;
		}
		
		System.out.println("Finished");

	}
	
	/*
	 * The syncrhonized is just to force all threads to the most up-to-date view of the data
	 */
	private static synchronized void kickOneOffIfFileDoesNotExist(Semaphore semaphore, Alignment a, PfamToPDBBlastResults toPdb, 
			GroupOfColumnsInterface gci) throws Exception
	{
		File outFile = getOutputFile(a, gci);
		
		if(! outFile.exists())
		{
			semaphore.acquire();
			Worker w = new Worker(a,toPdb,gci, semaphore);
				new Thread(w).start();
		}
		else
		{
			Date date = new Date();
			String stringDate = date.toString();
			System.out.println(outFile.getAbsolutePath() + "exists. skipping at " + stringDate);
		}
	}
	
	private static File getOutputFile(Alignment a, GroupOfColumnsInterface gci) throws Exception
	{
		return new File(ConfigReader.getCleanroom() + File.separator + "results" + File.separator + 
				a.getAligmentID() + "_" + gci.getName() + ".txt");
	}
	
	private static class Worker implements Runnable
	{
		private final Alignment a;
		private final PfamToPDBBlastResults toPDB;
		private final GroupOfColumnsInterface gci;
		private final Semaphore semaphore;
		
		private Worker(Alignment a, PfamToPDBBlastResults toPDB,
				GroupOfColumnsInterface gci, Semaphore semaphore)
		{
			this.a = a;
			this.toPDB = toPDB;
			this.gci = gci;
			this.semaphore = semaphore;
		}
		
		public void run()
		{
			try
			{
				PdbFileWrapper fileWrapper = new PdbFileWrapper( new File( ConfigReader.getPdbDir() + File.separator + toPDB.getPdbID() + ".txt"));
				
				File outputFile = getOutputFile(a, gci);
				
				if(outputFile.exists())
					throw new Exception(outputFile.getAbsolutePath() + " already exists ");
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
				
				writer.write("region1\tregion2\tcombinedType\tscore\taverageDistance\tminDistance\n");
				writer.flush();
				
				System.out.println(a.getAligmentID());
				HashMap<Integer, Integer> pdbToAlignmentNumberMap=  getPdbToAlignmentNumberMap(a, toPDB, fileWrapper);
				List<HelixSheetGroup> helixSheetGroup= 
						HelixSheetGroup.getList(ConfigReader.getPdbDir() + File.separator + toPDB.getPdbID() + ".txt",
								toPDB.getChainId(), toPDB.getQueryStart(), toPDB.getQueryEnd());
				System.out.println(helixSheetGroup);
				
				for(HelixSheetGroup hsg : helixSheetGroup)
				{
					System.out.println( hsg.getStartPos() + "-" + hsg.getEndPos() + " " +  
							pdbToAlignmentNumberMap.get(hsg.getStartPos()) +  "-" + 
							pdbToAlignmentNumberMap.get(hsg.getEndPos()));
				}
					
				
				for(int x=0; x< helixSheetGroup.size() -1; x++)
				{
					HelixSheetGroup xHSG = helixSheetGroup.get(x);
					
					for( int y=x+1; y < helixSheetGroup.size(); y++)
					{
						HelixSheetGroup yHSG = helixSheetGroup.get(y);
						
						writer.write(xHSG.toString() + "\t");
						writer.write(yHSG.toString() + "\t");
						List<String> aList = new ArrayList<String>();
						
						aList.add(xHSG.getElement());
						aList.add(yHSG.getElement());
						Collections.sort(aList);
						writer.write(aList.get(0) + "_TO_" + aList.get(1) + "\t");
						
						double score =
								gci.getScore(a, pdbToAlignmentNumberMap.get(xHSG.getStartPos()), 
										   pdbToAlignmentNumberMap.get(xHSG.getEndPos()), 
										   pdbToAlignmentNumberMap.get(yHSG.getStartPos()),
										    pdbToAlignmentNumberMap.get(yHSG.getEndPos()));
						
						writer.write(score+ "\t");
						
						double distance = 
								getAverageDistance(fileWrapper, xHSG, yHSG, toPDB.getChainId());
						
						writer.write(distance + "\t");
						
						double minDistance = 
								getMinDistance(fileWrapper, xHSG, yHSG, toPDB.getChainId());
						
						writer.write(minDistance + "\n");
						writer.flush();
					}
				}
				
				writer.flush();  
				writer.close();
				Date date = new Date();
				String stringDate = date.toString();
				System.out.println("Finished " + a.getAligmentID() + "_" + gci.getName() + " at " + stringDate);
				semaphore.release();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	private static double getAverageDistance( PdbFileWrapper wrapper, HelixSheetGroup xGroup, HelixSheetGroup yGroup, char chain )
		throws Exception
	{
		double n=0;
		double sum=0;
		
		for( int x=xGroup.getStartPos(); x <= xGroup.getEndPos(); x++ )
		{
			PdbResidue xResidue = wrapper.getChain(chain).getPdbResidueByPdbPosition(x);
			
			if( xResidue == null || xResidue.getCbAtom() == null){
				//Too verbose -- debug only use
				System.out.println("WARNING:  " + wrapper.getFourCharId() + " " + chain + "  " +  x);
			}
				
			else
			{
				for( int y= yGroup.getStartPos(); y <= yGroup.getEndPos(); y++)
				{
					PdbResidue yResidue = wrapper.getChain(chain).getPdbResidueByPdbPosition(y);
					
					if( yResidue == null || yResidue.getCbAtom() == null)
					{
						//Too verbose -- debug only use
						//System.out.println("WARNING: NO " + wrapper.getFourCharId() + " " + chain + "  " +  y);
					}
					else
					{
						sum += xResidue.getCbAtom().getDistance(yResidue.getCbAtom());
						n++;
					}		
				}
			}
		}
		
		return sum / n;
	}
	
	private static double getMinDistance( PdbFileWrapper wrapper, HelixSheetGroup xGroup, HelixSheetGroup yGroup, char chain )
			throws Exception
		{
			double val = Double.MAX_VALUE;
			
			for( int x=xGroup.getStartPos(); x <= xGroup.getEndPos(); x++ )
			{
				PdbResidue xResidue = wrapper.getChain(chain).getPdbResidueByPdbPosition(x);
				
				if( xResidue == null || xResidue.getCbAtom() == null ){
					//Too verbose -- debug only use
					//System.out.println("WARNING: No " + wrapper.getFourCharId() + " " + chain + "  " +  x);
				}
					
				else
				{
					for( int y= yGroup.getStartPos(); y <= yGroup.getEndPos(); y++)
					{
						PdbResidue yResidue = wrapper.getChain(chain).getPdbResidueByPdbPosition(y);
						
						if( yResidue == null || yResidue.getCbAtom() == null)
						{
							//Too verbose, removed
							//System.out.println("WARNING: NO " + wrapper.getFourCharId() + " " + chain + "  " +  y);
						}
						else
						{
							val = Math.min(xResidue.getCbAtom().getDistance(yResidue.getCbAtom()), val);
						}		
					}
				}
			}
			
			return val;
		}
	
	public static double getFractionIdentity(String pdbString, String pfamString) throws Exception
	{
		double num =0;
		double numMatch=0;
		
		for( int x=0; x < pdbString.length(); x++)
		{
			char c = pdbString.charAt(x);
			
			if ( c != '-')
			{
				num++;
				
				if( c == pfamString.charAt(x))
					numMatch++;
			}
		}
		
		return numMatch / num;
	}
	
	public static HashMap<Integer, Integer> getPdbToAlignmentNumberMap( Alignment a, PfamToPDBBlastResults toPDB,
			 PdbFileWrapper fileWrapper) throws Exception
	{
		HashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
		AlignmentLine aLine = a.getAnAlignmentLine(toPDB.getPfamLine());
		String alignmentSeq = aLine.getSequence().toUpperCase();
		
		alignmentSeq = alignmentSeq.replaceAll("-", ".");
		
		if(alignmentSeq.indexOf("-") != -1)
			throw new Exception("No " + alignmentSeq);
		
		String pdbSeq = fileWrapper.getChain(toPDB.getChainId()).getSequence().substring(toPDB.getQueryStart()-1,
																		toPDB.getQueryEnd());
		
		pdbSeq = pdbSeq.toUpperCase();
		
		PairedAlignment pa = 
				NeedlemanWunsch.globalAlignTwoSequences(
						pdbSeq, alignmentSeq, substitutionMatrix, -3, -1, false);
		
		System.out.println(pa.getFirstSequence());
		System.out.println(pa.getSecondSequence());
		
		double fractionMatch = getFractionIdentity(pa.getFirstSequence(), pa.getSecondSequence());
		
		System.out.println("fractionMatch = " + fractionMatch);
		
		if( fractionMatch < 0.9)
			throw new Exception("Alignment failure");
		
		int x=-1;
		int alignmentPos =-1;
		int pdbNumber = toPDB.getQueryStart() -1;
		
		while(pdbNumber < toPDB.getQueryEnd())
		{
			x++;
			
			if( pa.getSecondSequence().charAt(x) != '-')
				alignmentPos++;
			
			if( pa.getFirstSequence().charAt(x) != '-' )
			{
				pdbNumber++;
				
				map.put(pdbNumber,Math.max(0, alignmentPos));
			}
			
		}
		
		return map;
	}
}
