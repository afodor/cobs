/** 
 * Authors:  anthony.fodor@gmail.com  kylekreth@alumni.nd.edu
 * 
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */


package scripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import utils.ConfigReader;


public class PdbDownloadFromHTTP
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getPdbPfamChain() )));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String pdbID =  s.split("\t")[3];
			
			downloadIfNotThere(pdbID);
			
		}
		
		reader.close();
	}
	
	public static void downloadIfNotThere(String fourChar) throws Exception
	{
		File pdbFile = new File(ConfigReader.getPdbDir() + File.separator + 
				fourChar + ".txt");
		
		if(pdbFile.exists())
		{
			System.out.println(pdbFile.getAbsolutePath() + " exists!");
			return;
			
		}
			
		URL url = new URL( "http://www.rcsb.org/pdb/files/" + fourChar + ".pdb.gz");
		System.out.println(url.toString());
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
		System.out.println("Got" + fourChar);
	}
	
}
