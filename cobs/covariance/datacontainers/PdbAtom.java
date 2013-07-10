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

public class PdbAtom
{
	private int serialNumber;
	private String atomName;
	private String residueName;
	private char chainID;
	private int residueSequenceNumber;
	private float x, y, z;
	private float occupancy;
	private float temperature;
	
	public int getSerialNumber()
	{
		return this.serialNumber;
	}
	
	public float getTemperature()
	{
		return this.temperature;
	}
	
	public double getDistance( PdbAtom otherAtom) 
	{
		float xDiff = this.getX() - otherAtom.getX();
		xDiff = xDiff * xDiff;
		
		float yDiff = this.getY() - otherAtom.getY();
		yDiff = yDiff * yDiff;
		
		float zDiff = this.getZ() - otherAtom.getZ();
		zDiff = zDiff * zDiff;
		
		return Math.sqrt( xDiff + yDiff + zDiff ) ;	
	}
	
	public String getAtomName()
	{
		return this.atomName;
	}
	
	public String getResidueName()
	{
		return this.residueName;
	}
	
	public char getChainId()
	{
		return this.chainID;
	}
	
	public float getOccupancy()
	{
		return this.occupancy;
	}
	
	public int getResidueSequenceNumber()
	{
		return this.residueSequenceNumber;
	}
	
	public float getX()
	{
		return this.x;
	}
	
	public float getY()
	{
		return this.y;
	}
	
	public float getZ()
	{
		return this.z;
	}
	
	public PdbAtom( String pdbAtomLine ) throws Exception
	{
		if ( ! pdbAtomLine.startsWith("ATOM") ) 
			throw new Exception("Error!  Expecting the atom line to start with ATOM");
		
		this.serialNumber = Integer.parseInt( pdbAtomLine.substring(6,11).trim() );
		
		this.atomName = pdbAtomLine.substring( 12, 16).trim();
		this.residueName = pdbAtomLine.substring(17,20).trim();
		
		this.chainID = pdbAtomLine.substring( 21, 22 ).charAt(0);
		
		this.residueSequenceNumber = Integer.parseInt( pdbAtomLine.substring(22,26).trim() );
		
		this.x = Float.parseFloat(pdbAtomLine.substring(30,38).trim() );
		this.y = Float.parseFloat( pdbAtomLine.substring(38,46).trim() );
		this.z = Float.parseFloat( pdbAtomLine.substring(46,54).trim());
		
		this.occupancy = Float.parseFloat( pdbAtomLine.substring(54, 60).trim() );
		this.temperature = Float.parseFloat( pdbAtomLine.substring(60,66).trim() );
																					 
	}
}
