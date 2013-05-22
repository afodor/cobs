package scripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import utils.ConfigReader;


public class PdbDownloadFromHTTP
{
	public static void downloadIfNotThere(String fourChar) throws Exception
	{
		File pdbFile = new File(ConfigReader.getPdbDir() + File.separator + 
				fourChar + ".txt");
		
		if(pdbFile.exists())
			return;
		
		URL url = new URL( "http://www.rcsb.org/pdb/files/" + fourChar + ".pdb.gz");
		
		BufferedReader reader = 
		new BufferedReader(new InputStreamReader( 
				new GZIPInputStream( url.openStream())));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(pdbFile));
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			writer.write(s + "\n");
		}
			
		reader.close();
		writer.flush();  writer.close();
	}
	
}
