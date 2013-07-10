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


package covariance.datacontainers;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import utils.ConfigReader;

import covariance.parsers.PdbParser;

public class PdbFileWrapper
{
	private String pdbId;
	private List pdbChains = new ArrayList();
	
	private String experimentMethod;
	
	
	
	public PdbFileWrapper( File pdbFile ) throws Exception
	{
		new PdbParser(pdbFile.getAbsolutePath() , this);
	}
	
	public PdbFileWrapper( String fourCharId ) throws Exception
	{
		this(new File( ConfigReader.getPdbDir()+ 
							File.separator + fourCharId + ".txt") );
	}
	
	public List getPdbChains()
	{
		return pdbChains;
	}

	public String getFourCharId()
	{
		return pdbId;
	}

	public void setPdbId(String pdbId)
	{
		this.pdbId = pdbId;
	}
	
	public void addChain(PdbChain pdbChain ) 
	{
		pdbChains.add(pdbChain);
	}
	
	public PdbChain getChain( Character chainChar ) 
	{
		return getChain( chainChar.charValue());
	}
	
	public PdbChain getChain( char chainChar ) 
	{
		for ( Iterator i = this.pdbChains.iterator();
					i.hasNext(); ) 
		{
			PdbChain pdbChain = ( PdbChain ) i.next();
			
			if ( pdbChain.getChainChar() == chainChar ) 
				return pdbChain;
		}
		
		return null;
	}
	
	public int getLongestLength() 
	{
		int longestLength = -1;
		
		for ( int x=0; x < this.pdbChains.size(); x++ ) 
		{
			PdbChain chain= (PdbChain) this.pdbChains.get(x);
			
			longestLength = Math.max( longestLength, chain.getPdbResidues().size() );	
		}
		
		return longestLength;
	}

	public String getExperimentMethod()
	{
		return experimentMethod;
	}

	public void setExperimentMethod(String experimentMethod)
	{
		this.experimentMethod = experimentMethod;
	}

}
