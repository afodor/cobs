package scripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import covariance.parsers.PfamToPDBBlastResults;



import utils.ConfigReader;

public class WriteTruncatedPfamAlignments
{
	public static void main(String[] args) throws Exception
	{
		System.out.println(ConfigReader.getFullPfamPath());
		boolean zipped = ConfigReader.getFullPfamPath().toLowerCase().endsWith("gz");
		
		BufferedReader reader = 
			zipped ? new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream( ConfigReader.getFullPfamPath()) ) ),100000)
				: new BufferedReader( new FileReader( new File( ConfigReader.getFullPfamPath())),100000);	
			
		HashMap<String, PfamToPDBBlastResults> pdbMap = 
				PfamToPDBBlastResults.getAnnotationsAsMap();
		
		File outFile = new File( ConfigReader.getCobsHomeDirectory()+
				File.separator + "pfamTruncated.txt");
		
		System.out.println(outFile.getAbsolutePath());
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		boolean writeLine = true;
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			if(s.startsWith("#=GF ID"))
			{
				StringTokenizer sToken = new StringTokenizer(s);
				sToken.nextToken(); sToken.nextToken();
				String familyId = sToken.nextToken();
				
				if( pdbMap.containsKey(familyId))
				{
					System.out.println("Writing " + familyId);
					writeLine =true;
				}
				else
				{
					System.out.println("Not Writing " + familyId);
					writeLine =false;
				}
			}
			

			if( writeLine)
				writer.write(s + "\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
}
