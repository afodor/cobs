package covariance.parsers;

import java.io.CharConversionException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.omg.IOP.CodecPackage.TypeMismatch;

import covariance.datacontainers.SangerPDBpfamMappingSingleLine;

public class SangerPDBpfamMapping {
	private HashMap<Integer, SangerPDBpfamMappingSingleLine> sangerLines = new HashMap<Integer, SangerPDBpfamMappingSingleLine>();
	Integer badlines = 0;

	/**
	 * Only requires a the file normally found at ftp://ftp.sanger.ac.uk/pub/databases/Pfam/mappings/pdb_pfam_mapping.txt
	 * @throws IOException 
	 * @throws TypeMismatch 
	 */
	public SangerPDBpfamMapping(File originalSangerFile) throws IOException {

		System.out.println("Reading in file: " + originalSangerFile.getAbsolutePath() );

		LineNumberReader lnr = new LineNumberReader(new FileReader(originalSangerFile));

		runWithTheFile(lnr);

	}


	private void runWithTheFile(LineNumberReader lnr) throws IOException  {
		//kind of our test string to make sure there is more data
		String l;

		while ( 
				((l = lnr.readLine()) != null)  &&
				(l.length() >=1) 
				)  {

			Integer currentLine = lnr.getLineNumber();
			Scanner reader = new Scanner(l).useDelimiter("\t");
			try {
				SangerPDBpfamMappingSingleLine aLine = new SangerPDBpfamMappingSingleLine(reader);
				sangerLines.put(currentLine, aLine);
			}
			catch (StringIndexOutOfBoundsException e){
		//		System.out.println(e.getMessage() + ".  This caused us to drop this line " + currentLine + "\t" + l.toString());
				badlines++;
			}
			catch (InputMismatchException e) {
		//		System.out.println("There was a conversion issue, bad data was in stream.  This caused us to drop this line " + currentLine + "\t" + l.toString());
				badlines++;
			}
			catch (IndexOutOfBoundsException e){
		//		System.out.println(e.getMessage() + ".  This caused us to drop this line " + currentLine + "\t" + l.toString());
				badlines++;
			}
			catch (IOException e){
		//		System.out.println("There was a conversion issue, bad data was in stream.  This caused us to drop this line " + currentLine + "\t" + l.toString());
				badlines++;
			}
			catch (TypeMismatch e){
		//		System.out.println(e.getMessage() + ".  This caused us to drop this line " + currentLine + "\t" + l.toString());
				badlines++;
			}
			finally {

			}
		}
		System.out.println("Finished reading file, we found " + sangerLines.size() + " lines that were valid.");
		System.out.println("We had " + badlines + " rejected.");
		
	}


	public HashMap<Integer, SangerPDBpfamMappingSingleLine> getSangerLines() {
		return sangerLines;
	}


	public Integer getBadlines() {
		return badlines;
	}
	

}
