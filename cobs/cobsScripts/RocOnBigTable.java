package cobsScripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Annotated;

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
		
		/*"AverageMcBASC_PNormalInitial", 
		"AverageMcBASC", 
		"COBS_UNCORRECTED",
		"random_PNormalInitial",
		"AverageConservationSum_PNormalInitial",
		"AverageConservationSum",
		"AverageMI_PNormalInitial",
		"AverageMI"
		 * 
		 */
		private static String annotateColumn(String fileSubString,boolean normalized)
		{
			StringTokenizer sToken = new StringTokenizer(fileSubString, "_");
			
			String firstToken = sToken.nextToken();
			
			String suffix = normalized ? "late" : "";
			
			if( ! sToken.hasMoreTokens())
				return firstToken + "_" + suffix;
			
			return firstToken + "_" +  sToken.nextToken().replace("PNormalInitial", "early") + "_" + suffix;
		}
		
		public static void writeROC(String fileSubString, boolean normalized) throws Exception
		{
			
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					ConfigReader.getCleanroom() + File.separator + 
							"bigSummaries" + File.separator + "big" + fileSubString + (normalized ? "normedLate" : "") 
							+ ".txt")));
			
			File rocDir = new File(ConfigReader.getCleanroom() + File.separator + 
					"roc" );
			
			rocDir.mkdirs();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					rocDir.getAbsolutePath() + File.separator 
					+ "bigROC" + fileSubString +(normalized ? "normedLate" : "")+ "_" + 
							".txt"));
			
			int numBelow50=0;
			int numAbove50=0;
			
			String prefix = annotateColumn(fileSubString, normalized);
			writer.write( prefix +  "_prediction\t" + 
						prefix +  "_numBelow50\t" + 
						prefix +	"_numAbove50\n");
			
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
