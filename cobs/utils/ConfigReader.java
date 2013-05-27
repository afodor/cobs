package utils;

import java.io.*;
import java.util.*;

public class ConfigReader
{
	public static final String ENERGETICS_PROPERTIES_FILE="Energetics.properties";
	
	public static final String CLUSTAL_EXECUTABLE="CLUSTAL_EXECUTABLE";
	public static final String CLUSTAL_DIRECTORY="CLUSTAL_DIRECTORY";
	public static final String COBS_HOME_DIRECTORY="COBS_HOME_DIRECTORY";
	public static final String FULL_PFAM_PATH="FULL_PFAM_PATH";
	public static final String OUT_DATA_DIR="OUT_DATA_DIR";
	public static final String GZIP_FULL_PATH="GZIP_FULL_PATH";
	public static final String COBS_CLEANROOM = "COBS_CLEANROOM";
	public static final String PDB_DIR = "PDB_DIR";
	public static final String BLAST_DIR = "BLAST_DIR";
	public static final String PDB_PFAM_CHAIN = "PDB_PFAM_CHAIN";
	
	public static final String TRUE="TRUE";
	public static final String YES="YES";
	
	private static ConfigReader configReader = null;
	private static Properties props = new Properties();
	
	private static String getAProperty(String namedProperty ) throws Exception
	{
		Object obj = props.get( namedProperty );
		
		if ( obj == null ) 
			throw new Exception("Error!  Could not find " + namedProperty + " in " + ENERGETICS_PROPERTIES_FILE );
		
		return obj.toString();
	}
	
	private ConfigReader() throws Exception
	{
		Object o = new Object();
		
		InputStream in = o.getClass().getClassLoader().getSystemResourceAsStream( ENERGETICS_PROPERTIES_FILE );
		
		if ( in == null )
			throw new Exception("Error!  Could not find " + ENERGETICS_PROPERTIES_FILE + " anywhere on the current classpath");		
		
		BufferedReader reader = new BufferedReader( new InputStreamReader( in ));
		props = new Properties();
		
		String nextLine = reader.readLine();
		
		while ( nextLine != null ) 
		{
			nextLine= nextLine.trim();
			
			if ( nextLine.length() > 0 && ! nextLine.startsWith("!") && ! nextLine.startsWith("#") ) 
			{
				StringTokenizer sToken = new StringTokenizer( nextLine, "=" );
				
				if ( sToken.hasMoreTokens() ) 
				{
					String key = sToken.nextToken().trim();
					
					String value = "";
					
					if ( sToken.hasMoreTokens() ) 
					{
						value = sToken.nextToken().trim();
					}
					
					props.put( key, value );
					
				}	
			}	
			nextLine = reader.readLine();
		}	
	}
	
	private static synchronized ConfigReader getConfigReader() throws Exception
	{
		if ( configReader == null ) 
		{
			configReader = new ConfigReader();
		}
		
		return configReader;
	}
	
	public static String getCobsHomeDirectory() throws Exception
	{
		return getConfigReader().getAProperty( COBS_HOME_DIRECTORY );
	}
	
	public static String getFullPfamPath() throws Exception
	{
		return getConfigReader().getAProperty( FULL_PFAM_PATH );
	}
	
	public static String getPdbDir() throws Exception
	{
		return getConfigReader().getAProperty( PDB_DIR);
	}
	
	
	public static String getOutDataDir() throws Exception
	{
		return getConfigReader().getAProperty( OUT_DATA_DIR );
	}
	
	public static String getClustalExecutable() throws Exception
	{
		return getConfigReader().getAProperty( CLUSTAL_EXECUTABLE);
	}
	
	public static String getClustalDirectory() throws Exception
	{
		return getConfigReader().getAProperty( CLUSTAL_DIRECTORY);
	}
	
	public static String getBlastDir() throws Exception
	{
		return getConfigReader().getAProperty(BLAST_DIR);
	}
	
	
	public static String getCleanroom() throws Exception
	{
		return getConfigReader().getAProperty(COBS_CLEANROOM);
	}

	public static String getPdbPfamChain() throws Exception {
		return getConfigReader().getAProperty(PDB_PFAM_CHAIN);
	}
}

