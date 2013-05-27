package cobsScripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

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
				
		}
		
		public static void writeROC(String fileSubString, boolean normalized) throws Exception
		{
			
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					ConfigReader.getCleanroom() + File.separator + 
							"bigSummaries" + File.separator + "big" + fileSubString + (normalized ? "normed" : "") 
							+ ".txt")));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					ConfigReader.getCleanroom() + File.separator + 
					"roc" + File.separator + "bigROC" + fileSubString +(normalized ? "normed" : "")+ "_" + 
							".txt"));
			
			int numBelow50=0;
			int numAbove50=0;
			
			writer.write( fileSubString +  "_prediction\t" + 
						fileSubString +  "_numBelow50\t" + 
						fileSubString +	"_numAbove50\n");
								
			
			reader.readLine();
			
			for(String s= reader.readLine(); s != null; s = reader.readLine())
			{
				String[] splits = s.split("\t");
				if( Double.parseDouble(splits[4]) <= 50)
					numBelow50++;
				else
					numAbove50++;
				
				writer.write( (numBelow50 + numAbove50) + "\t" );
				writer.write( numBelow50 + "\t" );
				writer.write( numAbove50+ "\n" );
			}
			
			writer.flush(); writer.close();
			reader.close();
		}
}
