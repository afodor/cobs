package averagesAndOutput;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;


/* Run WriteScores
   then AbsoluteScoreVsAverageDistance
 */
public class RocOnBigTable
{
	public static void main(String[] args) throws Exception
	{
		for(String s: AbsoluteScoreVsAverageDistance.FILE_NAMES)
			{
				writeROC(s,true);
				writeROC(s, false);
			}
		System.out.println("All files completed.");
	}
	
	public static void writeROC(String fileSubString, boolean normalized) throws Exception
	{
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCleanroom() + File.separator + 
						"bigSummaries" + File.separator + "big" + fileSubString + (normalized ? "normed" : "") + 
						".txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				ConfigReader.getCleanroom() + File.separator + 
				"roc" + File.separator + "bigROC" + fileSubString +(normalized ? "normed" : "")+ "_" + 
						 ".txt"));
		
		int numBelow50=0;
		int numAbove50=0;
		
		writer.write("family\t" + "thisFamilyTotal\t" +
					"PerPFAMUnit" + fileSubString +  "_prediction\t" + 
					"PerPFAMUnit" + fileSubString +  "_numBelow50\t" + 
					"PerPFAMUnit" + fileSubString +	"_numAbove50\t" +
					fileSubString +  "_prediction\t" + 
					fileSubString +  "_numBelow50\t" + 
					fileSubString +	"_numAbove50\n");
							
		
		reader.readLine();
		//Looks like we need a re-usable List here
		List<String> theReadLines = new ArrayList<String>();
		for(String s= reader.readLine(); s != null; s = reader.readLine()){
			theReadLines.add(s);
		}
			
		
		//Hopefully you can do this twice with Reader!  We will iterate through to get the PerPFAM counts necessary above
		//And create a HashMap of that Data for use later
		//With a HashMap of the below and a HashMap of the Above
		HashMap<String,Integer> pFAMtotalHits = new HashMap<String, Integer>();
		HashMap<String,Integer> pFAMsubTotal = new HashMap<String, Integer>();
		HashMap<String,Integer> pFAMabove = new HashMap<String, Integer>();
		
		//This first read-through will create many "redundant" keys, but that won't be a problem, they have to be unique
		//for(String s : theReadLines.subList( 1, theReadLines.size() )) 
		for(String s : theReadLines)
		{
			String[] splits = s.split("\t");
			String family = (splits[0]);
			pFAMtotalHits.put(family, 0);
			pFAMsubTotal.put(family, 0);
			pFAMabove.put(family, 0);
		}
		
		//Now we will record the Total
		for(String s : theReadLines)
		{
			String[] splits = s.split("\t");
			String family = (splits[0]);
			Integer currentTotal = pFAMtotalHits.get(family);
			currentTotal++;
			pFAMtotalHits.put(family, currentTotal);
		}
		
		
		for(String s : theReadLines)
		{
			String[] splits = s.split("\t");
			
			String family = (splits[0]);
			
			//Now to track the running total
			Integer subTotal = pFAMsubTotal.get(family);
			subTotal++;
			pFAMsubTotal.put(family, subTotal);
		
			if( Double.parseDouble(splits[4])  < 50){
				numBelow50++;
			}
				
			else{
				numAbove50++;
				Integer perFamilyAbove = pFAMabove.get(family);
				perFamilyAbove++;
				pFAMabove.put(family,perFamilyAbove);
			}
				
				
			//Now to carry the family name (without proper parsing...but good enough...forward)
			writer.write(family + "\t");
			
			//Now to always carry the total for the family through
			writer.write(pFAMtotalHits.get(family) + "\t");
			
			//Now the three additional columns we require, one of which is "derived", which we use here as a sort of
			//check digit
			int targetPFAMhighWaterMark = pFAMsubTotal.get(family);
			writer.write(targetPFAMhighWaterMark + "\t");
			int targetPFAMaboveCount = pFAMabove.get(family);
			writer.write(targetPFAMaboveCount + "\t");
			int whatIsLeftForThisPFAMToAchieve = targetPFAMhighWaterMark - targetPFAMaboveCount;
			writer.write(whatIsLeftForThisPFAMToAchieve + "\t");
			
			writer.write( (numBelow50 + numAbove50) + "\t" );

			writer.write( numBelow50 + "\t" );
			writer.write( numAbove50+ "\n" );
			writer.flush();
		}
		
		writer.flush(); writer.close();
		reader.close();
	}
	
	
}
