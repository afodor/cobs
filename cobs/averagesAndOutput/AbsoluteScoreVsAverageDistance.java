package averagesAndOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;


public class AbsoluteScoreVsAverageDistance
{
	private static final class ListData
	{
		double medianDistance;
		double listSize;
	}
	
	public static final String[]  FILE_NAMES = {"random_PNormalInitial",
		"AverageConservationSum_PNormalInitial",
		"AverageConservationSum",
		"AverageMcBASC",  
		"AverageMI_PNormalInitial",
		"AverageMI", 
		"COBS_UNCORRECTED"
		};
		
	public static void main(String[] args) throws Exception
	{
		for(String s : FILE_NAMES)
		{
			writeASummaryFile(s,true);
			writeASummaryFile(s,false);
		}
			
	}
	
	public static void writeASummaryFile(String fileSubString, boolean normalized) throws Exception
	{
		HashMap<String, ListData> mapOfFiles = new HashMap<String, AbsoluteScoreVsAverageDistance.ListData>();
		
		List<ResultsFileLine> bigList = getCombinedList(mapOfFiles, fileSubString,normalized);
		
		Collections.sort(bigList, new ResultsFileLine.SortByScore());
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getCleanroom() + File.separator + 
				"bigSummaries" + File.separator + "big" + fileSubString + (normalized ? "normedLate" : "") +".txt")
		  ));
		
		writer.write("family\tscore\tregion1\tregion2\tpercentile\tavgDistance\ttype\tlistSize\tmedianDistance\n");
		
		for( ResultsFileLine rfl : bigList)
		{
			writer.write(rfl.getParentFileName() + "\t");
			writer.write(rfl.getScore() + "\t");
			writer.write(rfl.getRegion1() + "\t");
			writer.write(rfl.getRegion2() + "\t");
			writer.write(rfl.getPercentile() + "\t");
			writer.write(rfl.getAverageDistance() + "\t");
			writer.write(rfl.getCombinedType() + "\t");
			
			//Debug only
			//System.out.println("Find " + rfl.getParentFileName());
			ListData listData = mapOfFiles.get(rfl.getParentFileName());
			writer.write(listData.listSize + "\t");
			writer.write(listData.medianDistance + "\n");
			writer.flush();
		}
		
		writer.flush();  
		
		writer.close();
	}
	
	public static List<ResultsFileLine> getCombinedList(HashMap<String, ListData> mapOfFiles, String fileSubString,
			boolean normalized) throws Exception
	{
		List<ResultsFileLine> bigList = new ArrayList<ResultsFileLine>();
		
		File dataDir = new File(ConfigReader.getCleanroom() + File.separator + "results");
		String[] files = dataDir.list();

		for(String s : files)
		{	
			if(s.contains(fileSubString + "."))
			{
				//Debug only
				//System.out.println(s);
				
				File fileToRead = new File(dataDir.getAbsolutePath() +
						File.separator + s);
				
				List<ResultsFileLine> innerList= ResultsFileLine.parseResultsFile(fileToRead);
				
				if(normalized)
					innerList= ResultsFileLine.getNormalizedList(innerList);
				
				if( innerList.size() > 3)
				{
					ListData ld = new ListData();
					ld.listSize = innerList.size();
					ld.medianDistance = ResultsFileLine.getMedianAverageDistance(innerList);
					
				
					// list is sorted by call to getMedianAverageDistance(...)
					for(int x=0;x  < innerList.size(); x++)
					{
						if (x == (innerList.size() -1 )){
							innerList.get(x).setPercentile(100.0);
						}
						else {
							innerList.get(x).setPercentile(100.0 * x / innerList.size());
						}
						
					}
					
					mapOfFiles.put(s, ld);
					System.out.println(s + " " + innerList.get(0).getParentFileName());
					bigList.addAll(innerList);
				}	
			}
		}
		
		return bigList;
	}
}
