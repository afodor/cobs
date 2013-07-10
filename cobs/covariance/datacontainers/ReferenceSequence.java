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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ReferenceSequence
{
	private List referenceSequenceResidues = new ArrayList();
	
	public List getReferenceSequenceResidues()
	{
		return referenceSequenceResidues;
	}
	
	public void addReferenceSequenceResidue( ReferenceSequenceResidue refSequenceResidue ) 
	{
		this.referenceSequenceResidues.add( refSequenceResidue );
	}
	
	public ReferenceSequenceResidue getRefSeqByAlignmentPosition( int alignmentPosition) throws Exception
	{
		for ( Iterator i = referenceSequenceResidues.iterator();
				i.hasNext(); ) 
		{
			ReferenceSequenceResidue rSeqResidue = 
					(ReferenceSequenceResidue) i.next();
					
			if ( rSeqResidue.getAlignmentPosition() == alignmentPosition ) 
				return rSeqResidue;
		}
		
		return null;
	}
	
	public ReferenceSequenceResidue getRefSeqByPdbPosition( char pdbChainChar, int pdbPosition ) 
	{
		for ( Iterator i = referenceSequenceResidues.iterator();
				i.hasNext(); )
		{
			ReferenceSequenceResidue rSeqResidue = (ReferenceSequenceResidue)i.next();
			
			for ( Iterator i2= rSeqResidue.getLinkedPdbResidues().iterator();
					i2.hasNext(); ) 
			{
				PdbResidue pdbResidue = (PdbResidue) i2.next();
				
				if ( pdbResidue.getParentChain().getChainChar() == pdbChainChar && 
						pdbResidue.getPdbPosition() == pdbPosition ) 
						return rSeqResidue;
			}
			
		}
		
		return null;
	}
	
	public void calculateNeighbors(PdbFileWrapper pdbFileWrapper) throws Exception
	{
		for ( Iterator i = referenceSequenceResidues.iterator();
				i.hasNext(); ) 
		{
			ReferenceSequenceResidue rSeqResidue = ( ReferenceSequenceResidue ) i.next();
			
			if ( rSeqResidue.allMatches() ) 
			{
				for ( Iterator i2 = rSeqResidue.getLinkedPdbResidues().iterator();	
						i2.hasNext(); ) 
				{
					PdbResidue linkedResidue = ( PdbResidue )i2.next();
					linkedResidue.generateNeighborList( pdbFileWrapper.getPdbChains() );
				}
			}
		}	
	}
}
