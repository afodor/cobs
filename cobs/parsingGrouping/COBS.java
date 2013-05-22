package parsingGrouping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import averagesAndOutput.GroupOfColumnsInterface;

import utils.ConfigReader;
import utils.MapResiduesToIndex;
import utils.Pearson;


import covariance.algorithms.McBASCCovariance;
import covariance.datacontainers.Alignment;
import covariance.parsers.FastaSequence;

public class COBS implements GroupOfColumnsInterface
{


	
	private static int[][] substitutionMatrix;

	static
	{
		try
		{
			substitutionMatrix = McBASCCovariance.getMaxhomMetric();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.exit(1);
		}

	}



	public static void main(String[] args) throws Exception
	{
		
	    File relative = new File("delete.me");
	    int correctLengthOfPath = relative.getAbsolutePath().length() - 10 ;
	    String testedFasta = relative.getAbsolutePath().substring(0,correctLengthOfPath) + File.separator + "src" + File.separator + "AdoHcyase.fasta";
		
		List<FastaSequence> alignment = FastaSequence.readFastaFile(testedFasta);

		int length = alignment.get(0).getSequence().length();

		for( FastaSequence fs : alignment)
			if (fs.getSequence().length() != length)
				throw new Exception("No");			

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
				ConfigReader.getCleanroom() + File.separator + 
				"comparison.txt")));

		writer.write("COBS\tscoreFromGlobalAlignment\n");

		BufferedReader reader = new BufferedReader(new FileReader(new File( 
				relative.getAbsolutePath().substring(0,correctLengthOfPath) + File.separator + "src" + File.separator +
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
							substitutionMatrix);

			writer.write(splits[9] + "\t");
			writer.write(sum + "\n");
			writer.flush();
			System.out.println(splits[1] + " " + splits[2]);
		}

		reader.readLine();

		reader.close();
	}

	private static double getSubstitutionMatrixSum( String s1, String s2 , int[][] substitutionMatrix)
			throws Exception
			{
		if(s1.length() != s2.length())
			throw new Exception("No");

		double sum = 0D;


		for( int x=0; x < s1.length(); x++)
		{
			int index1 = MapResiduesToIndex.getIndexOrNegativeOne(s1.charAt(x));

			if( index1 != -1)
			{
				int index2 = MapResiduesToIndex.getIndexOrNegativeOne(s2.charAt(x));

				if( index2 != -1)
					sum += substitutionMatrix[index1][index2];
			}
		}

		return sum;
			}

	@Override
	public String getName()
	{
		return "COBS_UNCORRECTED";
	}

	@Override
	public double getScore(Alignment a, int leftPosStart,
			int leftPosEnd, int rightPosStart, int rightPosEnd) throws Exception
			{
		return getScoreFromGlobalAlignment(a.getAsFastaSequenceList(), 
				leftPosStart, leftPosEnd, rightPosStart, rightPosEnd, substitutionMatrix);
			}

	private static double getScoreFromGlobalAlignment( List<FastaSequence> alignment,
			int leftPosStart, int leftPosEnd, int rightPosStart, int rightPosEnd,
			int[][] substitutionMatrix) 
					throws Exception
					{
		/**
		 * Many comparisons as we "walk" down the permutations in a given set of columns will be identical.  We can save at LEAST
		 * 10x of computations if we cache the results as a simple concatenation (with a comma) of the strings and use those scores
		 * instead of recomputing the lookups etc.  This uses more memory, but is a worthy tradeoff.
		 */
		HashMap<String,Double> previouslyCalculatedScores = new HashMap<String,Double>();
		
		List<Double> list1 = new ArrayList<Double>();
		List<Double> list2 = new ArrayList<Double>();

		leftPosStart--;;
		rightPosStart--;

		for(int x=0; x < alignment.size()-1; x++)
		{
			FastaSequence fs1 = alignment.get(x);
			String leftFrag1 = fs1.getSequence().substring(leftPosStart, leftPosEnd );
			String rightFrag1 =fs1.getSequence().substring(rightPosStart, rightPosEnd);

			for( int y=x+1; y < alignment.size(); y++)
			{
				FastaSequence fs2 = alignment.get(y);
				String leftFrag2 = fs2.getSequence().substring(leftPosStart, leftPosEnd );
				String rightFrag2 =fs2.getSequence().substring(rightPosStart, rightPosEnd);

				String concatLookupOne = leftFrag1 + "," + leftFrag2;
				String concatLookupTwo = rightFrag1 + "," + rightFrag2;
				
				Double matrixOneSum, matrixTwoSum;

				if (previouslyCalculatedScores.containsKey(concatLookupOne)){
					matrixOneSum = previouslyCalculatedScores.get(concatLookupOne);
					//System.err.println("We found matrixONE.");
				}
				else{
					matrixOneSum = getSubstitutionMatrixSum(leftFrag1, leftFrag2, substitutionMatrix);
					previouslyCalculatedScores.put(concatLookupOne, matrixOneSum);
				}

				if (previouslyCalculatedScores.containsKey(concatLookupTwo)){
					matrixTwoSum = previouslyCalculatedScores.get(concatLookupTwo);
					//System.err.println("We found matrixTWO.");
					if ((matrixOneSum == null) || (matrixTwoSum == null)){
						continue;
					}
					list1.add(matrixOneSum);
					list2.add(matrixTwoSum);
				}
				else{
					matrixTwoSum = getSubstitutionMatrixSum(rightFrag1, rightFrag2, substitutionMatrix);
					if ((matrixOneSum == null) || (matrixTwoSum == null)){
						continue;
					}
					list1.add(matrixOneSum);
					list2.add(matrixTwoSum);
					previouslyCalculatedScores.put(concatLookupTwo, matrixTwoSum);
				}

			}
		}

		if ((list1.size() < 1) || (list2.size() < 1)){
			throw new IndexOutOfBoundsException("Unsure what happened, but we don't have functional lists to perform a Pearson Correlation.");
		}

		return Pearson.getPearsonR(list1, list2);
					}
}
