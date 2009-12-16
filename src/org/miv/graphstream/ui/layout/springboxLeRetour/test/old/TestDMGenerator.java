/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * Copyright 2006 - 2009
 * 	Julien Baudry
 * 	Antoine Dutot
 * 	Yoann Pigné
 * 	Guilhelm Savin
 */

package org.miv.graphstream.ui.layout.springboxLeRetour.test.old;

import java.io.IOException;

import org.miv.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.miv.graphstream.algorithm.generator.Generator;
import org.miv.graphstream.algorithm.generator.GridGenerator;
import org.miv.graphstream.algorithm.generator.PreferentialAttachmentGenerator;
import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.Node;
import org.miv.graphstream.graph.implementations.MultiGraph;
import org.miv.graphstream.io.GraphParseException;
import org.miv.graphstream.ui.GraphViewerRemote;
import org.miv.graphstream.ui.layout.LayoutRunner.LayoutRemote;
import org.miv.graphstream.ui.layout.springboxLeRetour.SpringBox;
import org.miv.graphstream.ui.swing.SwingGraphViewer;
import org.miv.util.NotFoundException;

public class TestDMGenerator
{
	public static void main( String args[] ) {
		try
		{
			new TestDMGenerator( args );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	public TestDMGenerator( String args[] ) throws NotFoundException, IOException, GraphParseException {
		Graph graph = new MultiGraph( "Graph" );
		SwingGraphViewer viewer = new SwingGraphViewer( graph, new SpringBox( false ), false );
		
		GraphViewerRemote remote = viewer.newViewerRemote();
		LayoutRemote     layout = remote.getLayoutRemote();
		
		layout.setQuality( 1 );
		remote.setQuality( 0 );

//		Generator dmg = new DorogovtsevMendesGenerator();
//		Generator dmg = new GridGenerator( true, false );
		Generator dmg = new PreferentialAttachmentGenerator();
		
		dmg.begin( graph );
		for( int i=0; i<3000; i++ )
			dmg.nextElement();
		dmg.end();
		
		int degreeMax = 0;
		
		for( Node node:graph )
		{
			if( node.getDegree() > degreeMax )
				degreeMax = node.getDegree();
		}
		System.err.printf( "Degree Max = %d%n", degreeMax );
/*		if( degreeMax > 25 )
		{
			degreeMax -= 25;
			float maxDegree = 100f;

			if( degreeMax >= maxDegree )
				degreeMax = (int)maxDegree;
			
			float force = 1f - ( degreeMax / maxDegree );
			
			System.err.printf( "Setting forfce to %f%n", force );
			graph.addAttribute( "layout.force", force );
		}
*/	
		
		System.out.printf( "OK%n" );
		System.out.flush();
	}
}