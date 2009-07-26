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

package org.miv.graphstream.ui.viewer.test;

import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.Node;
import org.miv.graphstream.graph.implementations.MultiGraph;
import org.miv.graphstream.io2.ProxyFilter;
import org.miv.graphstream.io2.thread.ThreadProxyFilter;
import org.miv.graphstream.ui2.swingViewer.Viewer;

/**
 * Test the viewer.
 */
public class TestViewer
{
	public static void main( String args[] )
	{
		new TestViewer();
	}
	
	public TestViewer()
	{
		Graph       graph     = new MultiGraph( "main graph" );
		Viewer      viewer    = new Viewer( new ThreadProxyFilter( graph ) );
		ProxyFilter fromSwing = viewer.getThreadProxyOnGraphicGraph();
		
		fromSwing.addGraphAttributesListener( graph );
		viewer.addDefaultView( true );

		Node A = graph.addNode( "A" );
		Node B = graph.addNode( "B" );
		Node C = graph.addNode( "C" );

		graph.addEdge( "AB", "A", "B" );
		graph.addEdge( "BC", "B", "C" );
		graph.addEdge( "CA", "C", "A" );
		
		A.addAttribute( "xyz", 0, 1, 0 );
		B.addAttribute( "xyz", 1, 0, 0 );
		C.addAttribute( "xyz",-1, 0, 0 );
		
		graph.addAttribute( "ui.stylesheet", styleSheet );
		
		boolean loop = true;
		
		while( loop )
		{
			try { Thread.sleep( 200 ); } catch( InterruptedException e ) { e.printStackTrace(); }
			
			fromSwing.checkEvents();
			
			if( graph.hasAttribute( "ui.viewClosed" ) )
				loop  = false;
		}
		
		System.out.printf( "Bye bye ...%n" );
		System.exit( 0 );
	}
	
	protected static String styleSheet =
		"graph { padding : 20px; stroke-width: 0px; }";
}