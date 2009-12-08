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

package org.graphstream.ui.viewer.test;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.io.ProxyPipe;
import org.graphstream.io.thread.ThreadProxyPipe;
import org.graphstream.ui2.swingViewer.Viewer;

/**
 * Test the viewer.
 */
public class TestViewerColorInterpolation
{
	public static void main( String args[] )
	{
		new TestViewerColorInterpolation();
	}
	
	public TestViewerColorInterpolation()
	{
		Graph             graph     = new MultiGraph( "main graph" );
		ThreadProxyPipe toSwing   = new ThreadProxyPipe( graph );
		Viewer            viewer    = new Viewer( toSwing );
		ProxyPipe       fromSwing = viewer.getThreadProxyOnGraphicGraph();
		
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
		
		boolean loop  = true;
		float   color = 0;
		float   dir   = 0.01f;
		
		while( loop )
		{
			try { Thread.sleep( 100 ); } catch( InterruptedException e ) { e.printStackTrace(); }
			
			fromSwing.checkEvents();
			
			if( graph.hasAttribute( "ui.viewClosed" ) )
			{
				loop = false;
			}
			else
			{
				color += dir;
				
				if( color > 1 )
				{
					color = 1;
					dir = -dir;
				}
				else if( color < 0 )
				{
					color = 0;
					dir = -dir;
				}
				
				A.setAttribute( "ui.color", color );

				showSelection( graph );
			}
		}
		
		System.out.printf( "Bye bye ...%n" );
		System.exit( 0 );
	}

	protected void showSelection( Graph graph )
	{
		boolean       selection = false;
		StringBuilder sb        = new StringBuilder();
		
		sb.append( "[" );
		
		for( Node node: graph )
		{
			if( node.hasAttribute( "ui.selected" ) )
			{
				sb.append( String.format( " %s", node.getId() ) );
				selection = true;
			}
			if( node.hasAttribute( "ui.clicked" ) )
			{
				System.err.printf( "node %s clicked%n", node.getId() );
			}
		}

		sb.append( " ]" );

		if( selection )
			System.err.printf( "selection = %s%n", sb.toString() );
	}
	
	protected static String styleSheet =
		"graph         { padding: 20px; stroke-width: 0px; }" +
		"node:selected { fill-color: red;  fill-mode: plain; }" +
		"node:clicked  { fill-color: blue; fill-mode: plain; }" +
		"node#A        { fill-color: green, yellow, purple; fill-mode: dyn-plain; }";
}