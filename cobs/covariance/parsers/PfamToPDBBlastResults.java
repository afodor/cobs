package covariance.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import utils.ConfigReader;


public class PfamToPDBBlastResults
{
	//pfamID	numSequences	numColumns	pdbid	pfamLine	eScore	queryStart	queryEnd	targetStart	targetEnd	percentIdentity

	private String pfamID;
	private int numSequences;
	private int numColumns;
	private String pdbID;
	private String pfamLine;
	private String eScore;
	private int queryStart;
	private int queryEnd;
	private int targetStart;
	private int targetEnd;
	private double percentIdentity;
	private char chainId;
	
	public String getPfamID()
	{
		return pfamID;
	}
	
	public char getChainId()
	{
		return chainId;
	}

	public int getNumSequences()
	{
		return numSequences;
	}

	public int getNumColumns()
	{
		return numColumns;
	}

	public String getPdbID()
	{
		return pdbID;
	}

	public String getPfamLine()
	{
		return pfamLine;
	}

	public String geteScore()
	{
		return eScore;
	}

	public int getQueryStart()
	{
		return queryStart;
	}



	public int getQueryEnd()
	{
		return queryEnd;
	}



	public int getTargetStart()
	{
		return targetStart;
	}



	public int getTargetEnd()
	{
		return targetEnd;
	}



	public double getPercentIdentity()
	{
		return percentIdentity;
	}


	/*
	 * First run kyleCleanroom2.WriteBestHitsForPFAM
	 * 
	 * The key is the pfamID
	 */
	public static HashMap<String, PfamToPDBBlastResults> getAnnotationsAsMap() throws Exception
	{
		HashMap<String, PfamToPDBBlastResults> map = new LinkedHashMap<String, PfamToPDBBlastResults>();
		
		List<PfamToPDBBlastResults> list = getAnnotations();
		
		for(PfamToPDBBlastResults p : list)
		{
			if(map.containsKey(p.getPfamID()))
				throw new Exception("No");
			
			map.put(p.getPfamID(), p);
		}
		
		return map;
	}

	/*
	 * First run kyleCleanroom2.WriteBestHitsForPFAM
	 */
	public static List<PfamToPDBBlastResults> getAnnotations() throws Exception
	{
		List<PfamToPDBBlastResults> list = new ArrayList<PfamToPDBBlastResults>();
		
//		File f = new File("delete.txt");
//		System.out.println(f.getAbsolutePath());
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getPdbPfamChain())));
			
			reader.readLine();
			for(String s= reader.readLine(); s != null; s= reader.readLine())
			{
				String[] splits = s.split("\t");
				PfamToPDBBlastResults a = new PfamToPDBBlastResults();
				a.pfamID = splits[0];
				a.numSequences = Integer.parseInt(splits[1]);
				a.numColumns = Integer.parseInt(splits[2]);
				a.pdbID = splits[3];
				a.pfamLine = splits[4];
				a.eScore = splits[5];
				a.queryStart = Integer.parseInt(splits[6]);
				a.queryEnd = Integer.parseInt(splits[7]);
				a.targetStart = Integer.parseInt(splits[8]);
				a.targetEnd = Integer.parseInt(splits[9]);
				a.percentIdentity = Double.parseDouble(splits[10]);
				
				if( a.queryEnd < a.queryStart)
					throw new Exception("No");
				
				if( a.targetEnd < a.targetStart)
					throw new Exception("No");
				
				String chainToken = splits[11];
				
				if(chainToken.length() > 1)
					throw new Exception("Unexpected token");
				
				a.chainId = chainToken.charAt(0);
				
				list.add(a);
				
			}
			
			List<PfamToPDBBlastResults> answer = Collections.unmodifiableList(list);
			if (answer.size() < 1){
				throw new IndexOutOfBoundsException("No parsing worked!");
			}
			
		return answer;
	}
	
	public static void main(String[] args) throws Exception
	{
		List<PfamToPDBBlastResults> theAnnotations = getAnnotations();
		//Now to prove we are actually reading this information
		for (PfamToPDBBlastResults x : theAnnotations){
			System.out.println(x);
		}
	}
	
	@Override
	public String toString() {
	    java.lang.reflect.Field[] fields = getClass().getDeclaredFields();
	    AccessibleObject.setAccessible(fields,true);

	    StringBuffer sb = new StringBuffer();
	    sb.append("Class: " + this.getClass().getName() + "\n");

	    for (java.lang.reflect.Field field : fields) {
	        Object value = null;
	        try {
	            value = field.get(this);
	        } catch (IllegalAccessException e) {continue;}

	        sb.append("\tField \"" + field.getName() + "\"\n");

	        Class fieldType = field.getType();
	        sb.append("\t\tType:  ");

	        if (fieldType.isArray()) {
	            Class subType = fieldType.getComponentType();
	            int length = Array.getLength(value);
	            sb.append(subType.getName() + "[" + length + "]" + "\n");

	            for (int i = 0; i < length; i ++) {
	                Object obj = Array.get(value,i);
	                sb.append("\t\tValue " + i + ":  " + obj + "\n");
	            }
	        } else {
	            sb.append(fieldType.getName() + "\n");
	            sb.append("\t\tValue: ");
	            sb.append((value == null) ? "NULL" : value.toString());
	            sb.append("\n");
	        }
	    }
	    return sb.toString();
	}
}
