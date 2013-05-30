package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import covariance.datacontainers.SangerPDBpfamMappingSingleLine;
import covariance.parsers.SangerPDBpfamMapping;

public class SangerPDBpfamMappingTest {
	private static SangerPDBpfamMapping spf;
	
    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
		File sanger = new File ("/Users/kkreth/Desktop/Sanger_pdb_pfam_mapping.txt");
		spf = new SangerPDBpfamMapping(sanger);
    }
    
	@Test
	public void testNumLines() throws IOException {
		Integer goodLines = 195180;
		Integer badLines = 3606;
		assertEquals(badLines,spf.getBadlines());
		HashMap<Integer, SangerPDBpfamMappingSingleLine> sangerLines = spf.getSangerLines();
		Integer sizeOfSanger = sangerLines.size();
		assertEquals(goodLines,sizeOfSanger);
	}
	
	@Test
	public void testParticularLine() throws IOException {
		HashMap<Integer, SangerPDBpfamMappingSingleLine> allLines = spf.getSangerLines();
		SangerPDBpfamMappingSingleLine FourteenFourteenThree = allLines.get(73449);
		System.out.println(FourteenFourteenThree);
		Integer start = FourteenFourteenThree.getPdbResNumStart();
		Integer stop = FourteenFourteenThree.getPdbResNumEnd();
		System.out.println("Start " + start + " stop " + stop);
	}
}
