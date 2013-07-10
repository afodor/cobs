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
