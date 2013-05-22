package parsingGrouping;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utils.ConfigReader;
import utils.RefSequenceWrapper;
import utils.RunOne;

import covariance.algorithms.McBASCCovariance;
import covariance.datacontainers.Alignment;
import covariance.datacontainers.AlignmentLine;
import covariance.datacontainers.PdbFileWrapper;
import covariance.datacontainers.PdbResidue;
import covariance.datacontainers.ReferenceSequence;
import covariance.datacontainers.ReferenceSequenceResidue;

public class McBascWithHelixNotations
{
	public static void runOne(File pdbFile, Alignment a, String bestAlignmentLine, char aPdbChar, int startPos, int endPos, String pfamID ) throws Exception
	{	
		List<HelixSheetGroup> helixList = HelixSheetGroup.getList(pdbFile.getAbsolutePath());
		
		File resultsFile = new File( ConfigReader.getCobsHomeDirectory() + File.separator + 
				"pfamRun" + File.separator + 
							pfamID+  "_results.txt" );
		System.out.println("Writing file " + resultsFile.getAbsolutePath());
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(resultsFile));
		
		writer.write("i\tj\tpdbI\tpdbJ\tssI\tssJ\tssToss\tdistance\tmcBasc\n");
		

		// this pdbFileWrapper is pretty fragile
		// for example, it throws if there are any duplicate atoms
		// or if the pdb file is in some weird format
	    // this may cause you problems, depending on what you are doing
		PdbFileWrapper pdbFileWrapper = new PdbFileWrapper( pdbFile);
		
		AlignmentLine aLine = a.getAnAlignmentLine(bestAlignmentLine);
		
		if ( aLine == null ) 
			throw new Exception( "Could not find " + bestAlignmentLine + " in the alignment " );
		
		float filter = 90;
									
		RefSequenceWrapper wrapper= 
			new RefSequenceWrapper(pdbFileWrapper,
							aPdbChar, 
							startPos,
							endPos, 
							a,
							aLine,
							filter
							);		

		ReferenceSequence rSeq = wrapper.getReferenceSequence();
		Alignment a2 = wrapper.getFilteredAlignment();
		char pdbChar = wrapper.getPdbChain().getChainChar();
		
		McBASCCovariance mcBasc = new McBASCCovariance(a2);
		
		for ( int x=0; x< rSeq.getReferenceSequenceResidues().size(); x++ ) 
		{
			ReferenceSequenceResidue xResidue = 
				(ReferenceSequenceResidue) rSeq.getReferenceSequenceResidues().get(x);
			
			int xAlignmentPos = xResidue.getAlignmentPosition();
				
			if ( xResidue.allMatches() && a.getRatioValid(xAlignmentPos) > RunOne.MAX_RATIO_GAPS  )
			{
				PdbResidue xPdbResidue = xResidue.getLinkedPdbResidue(pdbChar);
					
				System.out.println( "Crunching i pdb position " +  xPdbResidue.getPdbPosition());
				
				for ( int y= x+1; y < rSeq.getReferenceSequenceResidues().size(); y++ ) 
				{
					
					ReferenceSequenceResidue yResidue = 
						(ReferenceSequenceResidue) rSeq.getReferenceSequenceResidues().get(y);
						
					int yAlignmentPosition = yResidue.getAlignmentPosition();
						
					if ( yResidue.allMatches() && a.getRatioValid(yAlignmentPosition) > RunOne.MAX_RATIO_GAPS ) 
					{
						PdbResidue yPdbResidue = yResidue.getLinkedPdbResidue(pdbChar);
						

						String ssI = HelixSheetGroup.getGroup(xPdbResidue.getPdbPosition(), helixList);
						String ssJ = HelixSheetGroup.getGroup(yPdbResidue.getPdbPosition(), helixList);
						
						if( Math.abs(xPdbResidue.getPdbPosition() -yPdbResidue.getPdbPosition()) >5 
								&& ssI != ssJ	)
						{
							writer.write( xAlignmentPos + "\t");					
							writer.write( yAlignmentPosition + "\t");
							
							writer.write( xPdbResidue.getPdbPosition() + "\t" );
							writer.write( yPdbResidue.getPdbPosition() + "\t" );
							
							writer.write(ssI + "\t");
							writer.write(ssJ + "\t");
							
							List<String> sorter = new ArrayList<String>();
							sorter.add(ssI);  sorter.add(ssJ); 
							Collections.sort(sorter);
							writer.write(sorter.get(0) + "_" + sorter.get(1) + "\t");
							
							// write the min distance between all chains
							writer.write( xResidue.getMinCbDistance(yResidue) + "\t" );	
							writer.write( mcBasc.getScore(a2, xAlignmentPos , yAlignmentPosition) + "\n" );
						}
					}
					
					writer.flush();
				}
				
			}
		}
		
		writer.flush();  writer.close();
	}
}
