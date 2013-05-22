package parsingGrouping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import utils.ConfigReader;
import utils.Pearson;

import covariance.parsers.FastaSequence;
import dynamicProgramming.MaxhomSubstitutionMatrix;
import dynamicProgramming.NeedlemanWunsch;
import dynamicProgramming.PairedAlignment;

public class COBSFromNW
{
	private static int[] getTranslationArray( List<FastaSequence> alignment ) 
	{
		FastaSequence fs = null;
		
		for(FastaSequence fSeq : alignment)
			if(fSeq.getFirstTokenOfHeader().equals("SAHH_PLAF7"))
				fs = fSeq;
		
		String strippedString = fs.getSequence().replaceAll("-", "");
		
		System.out.println(strippedString);
		System.out.println(strippedString.length());
		
		int[] returnArray = new int[strippedString.length()];
		int index =0;
		
		for( int x=0; x < fs.getSequence().length(); x++)
		{
			char c= fs.getSequence().charAt(x);
			
			if( c != '-')
			{
				returnArray[index] = x;
				index++;
			}
		}
		
		for( int x=0; x < returnArray.length; x++)
			System.out.println( x + " " + returnArray[x]);
		
		return returnArray;
	}
	
	public static void main(String[] args) throws Exception
	{
		List<FastaSequence> alignment = 
			FastaSequence.readFastaFile(ConfigReader.getCleanroom() + File.separator + 
					"AdoHcyase.fasta");
		
		int[] translationArray = getTranslationArray(alignment);
		
		MaxhomSubstitutionMatrix substitutionMatrix = new MaxhomSubstitutionMatrix();
		
		int length = alignment.get(0).getSequence().length();
		
		for( FastaSequence fs : alignment)
			if (fs.getSequence().length() != length)
				throw new Exception("No");			
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
				ConfigReader.getCleanroom() + File.separator + 
				"comparison.txt")));
		
		writer.write("kyleScore\tscoreFromGlobalAlignment\n");
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
				ConfigReader.getCleanroom() + File.separator + 
				"AdoHcyase.fasta.output.csv")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); 
					s != null;
						s = reader.readLine())
		{
			String[] splits = s.split(",");
			double sum = 
				getScoreFromGlobalAlignment(
						alignment,
						Integer.parseInt(splits[1]), 
						Integer.parseInt(splits[2]), 
						Integer.parseInt(splits[3]), 
						Integer.parseInt(splits[4]),
						substitutionMatrix, 
						translationArray);
			
			writer.write(splits[9] + "\t");
			writer.write(sum + "\n");
			writer.flush();
		}
		
		reader.readLine();
		
		reader.close();
	}
	
	
	private static double getScoreFromGlobalAlignment( List<FastaSequence> alignment,
			int leftPosStart, int leftPosEnd, int rightPosStart, int rightPosEnd,
			MaxhomSubstitutionMatrix substitutionMatrix, int[] translationArray) 
					throws Exception
	{
		List<Double> list1 = new ArrayList<Double>();
		List<Double> list2 = new ArrayList<Double>();
		
		leftPosStart--;
		rightPosStart--;
		leftPosEnd--;
		rightPosEnd--;
		
		for(int x=0; x < alignment.size()-1; x++)
		{
			if( x %100 ==0 )
				System.out.println("\t\t\t" + x);
			FastaSequence fs1 = alignment.get(x);
			String leftFrag1 = fs1.getSequence().substring( translationArray[leftPosStart], 
							 translationArray[leftPosEnd] + 1 );
			
			String rightFrag1 =fs1.getSequence().substring( 
					translationArray[rightPosStart], 
					translationArray[ rightPosEnd] +1 );
			
			for( int y=x+1; y < alignment.size(); y++)
			{
				FastaSequence fs2 = alignment.get(y);
				
				String leftFrag2 = fs2.getSequence().substring(
						translationArray[leftPosStart], translationArray[ leftPosEnd ] + 1 );
				String rightFrag2 =fs2.getSequence().substring( 
						translationArray[ rightPosStart], translationArray[ rightPosEnd] + 1);
				
				PairedAlignment leftPa = 
				NeedlemanWunsch.globalAlignTwoSequences(
						leftFrag1, leftFrag2, substitutionMatrix, -3, -1, false);
				
				list1.add((double)leftPa.getAlignmentScore());
				
				PairedAlignment rightPa = 
					NeedlemanWunsch.globalAlignTwoSequences(
							rightFrag1, rightFrag2, substitutionMatrix, -3, -1, false);
				
				list2.add((double) rightPa.getAlignmentScore()); 
					
			}
		}
		
		return Pearson.getPearsonR(list1, list2);
	}
}
