package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProcessWrapper
{
	
	public ProcessWrapper( String[] cmdArgs ) throws Exception
	{
		Runtime r = Runtime.getRuntime();
		Process p = r.exec(cmdArgs);
		
		for(String s : cmdArgs)
			System.out.print(s + "  ");
		
		System.out.println();
		
		BufferedReader br = new BufferedReader (new InputStreamReader(p.getInputStream ()));
		
		String s;
		
		while ((s = br.readLine ())!= null)
		{
    		//System.out.println (s);
		}
				
		p.waitFor();
		p.destroy();
		br.close();
	}
}
