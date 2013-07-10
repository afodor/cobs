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

import java.io.CharConversionException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.omg.IOP.CodecPackage.TypeMismatch;


public class SangerPDBpfamMappingSingleLine 
{
	final private String pdbID;
	final private Character chainID;
	final private Integer pdbResNumStart;
	final private Integer pdbResNumEnd;
	final private String pFAMacc;
	final private String pFAMname;
	final private String pFAMdesc;
	final private Double eValue;

	/**
	 * @param pdbID
	 * @param chainID
	 * @param pdbResNumStart
	 * @param pdbResNumEnd
	 * @param pFAMacc
	 * @param pFAMname
	 * @param pFAMdesc
	 * @param eValue
	 * @throws CharConversionException 
	 * @throws TypeMismatch 
	 */
	public SangerPDBpfamMappingSingleLine(Scanner reader ) throws CharConversionException, TypeMismatch {
		this.pdbID = reader.next();
		if (pdbID.startsWith("PDB_I")){
			throw new StringIndexOutOfBoundsException("We have found the first header line, skipping the parse as we should.");
		}
		if (pdbID.length() !=4){
			throw new IndexOutOfBoundsException("PDB Wrong length");
		}
		String tempChain = reader.next();
		if (tempChain.length() != 1) {
			throw new TypeMismatch("Chain is too long");
		}
		this.chainID = tempChain.charAt(0);
		if (Character.isDigit(chainID)){
			throw new CharConversionException("We had a digit in place of a char for chainID");
		}
		this.pdbResNumStart = reader.nextInt();
		if (pdbResNumStart < 0) {
			throw new IndexOutOfBoundsException("PDB supposedly starts before any sensible first sequence");
		}
		this.pdbResNumEnd = reader.nextInt();
		if (pdbResNumEnd <= pdbResNumStart){
			throw new IndexOutOfBoundsException("Last residue is before the first residue");
		}
		this.pFAMacc = reader.next();
		this.pFAMname = reader.next();
		this.pFAMdesc = reader.next();
		
		Double tempEvalue = -99D;
		//Having issues with consistent behavior of Scanner for parsing scientific values.  This might help us debug, or might allow us to just move forward.
		try {
			String eValueS = reader.next();
			tempEvalue = Double.parseDouble(eValueS);
		} catch (InputMismatchException e) {
			throw new InputMismatchException("Problem with parsing the double for e-value.");
		}
		finally {}
		
		if (tempEvalue == -99D){
			throw new InputMismatchException("We couldn't convert the e-value correctly from a String (Workaround)");
		}
		
		this.eValue = tempEvalue;
	}

	public String getPdbID() {
		return pdbID;
	}

	public Character getChainID() {
		return chainID;
	}

	public Integer getPdbResNumStart() {
		return pdbResNumStart;
	}

	public Integer getPdbResNumEnd() {
		return pdbResNumEnd;
	}

	public String getpFAMacc() {
		return pFAMacc;
	}

	public String getpFAMname() {
		return pFAMname;
	}

	public String getpFAMdesc() {
		return pFAMdesc;
	}

	public Double geteValue() {
		return eValue;
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
