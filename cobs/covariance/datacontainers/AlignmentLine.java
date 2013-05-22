package covariance.datacontainers;

import utils.MapResiduesToIndex;

public class AlignmentLine
{
	public static final Character GAP_CHAR = new Character( '-');
	private final String identifier;
	private final String sequence;
	private final int numValidChars;
	
	public String getIdentifier()
	{
		return identifier;
	}

	public String getSequence()
	{
		return sequence;
	}
	
	public AlignmentLine( String identifier, String sequence )
	{
		this.identifier = identifier;
		sequence = sequence.toUpperCase();
		sequence = sequence.replaceAll("X", "A");
		sequence = sequence.replaceAll("N", "L");
		sequence = sequence.replaceAll("B", "D");
		sequence = sequence.replaceAll("Z", "E");
		this.sequence = sequence;
		
		int vChars = 0;
		
		for ( int x=0; x< sequence.length(); x++ )
			if ( MapResiduesToIndex.isValidResidueChar( sequence.charAt(x) ) )
				vChars++;
		
		numValidChars = vChars;
	}	
	
	public String getUngappedSequence() throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		String capSequence = this.sequence.toUpperCase();
		
		for ( int x=0; x< capSequence.length(); x++ ) 
		{
			char c = capSequence.charAt( x );
			
			if ( MapResiduesToIndex.isValidResidueChar( c ) ) 
				buff.append(c);
		}
		
		return buff.toString();
	}
		
	public String toString()
	{
		return identifier + "\t" + sequence;
	}

	public int getNumValidChars()
	{
		return numValidChars;
	}
}
