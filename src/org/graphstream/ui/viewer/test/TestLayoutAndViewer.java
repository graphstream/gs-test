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

import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.io.thread.ThreadProxyPipe;
import org.graphstream.ui2.layout.Layout;
import org.graphstream.ui2.layout.springbox.SpringBox;
import org.graphstream.ui2.swingViewer.Viewer;

public class TestLayoutAndViewer
{
	public static void main( String args[] )
	{
		new TestLayoutAndViewer();
	}
	
	public TestLayoutAndViewer()
	{
		boolean           loop       = true;
		Graph             graph      = new MultiGraph( "g1" );
		Viewer            viewer     = new Viewer( new ThreadProxyPipe( graph ) );
		ThreadProxyPipe fromViewer = viewer.getThreadProxyOnGraphicGraph();
		Layout            layout     = new SpringBox( false );
		
		graph.addAttribute( "ui.stylesheet", styleSheet );
		
		fromViewer.addGraphListener( graph );
		viewer.addDefaultView( true );
		graph.addGraphListener( layout );
		layout.addGraphAttributesListener( graph );

		Generator gen = new DorogovtsevMendesGenerator();
		
		gen.addGraphListener( graph );
		gen.begin();
		for( int i=0; i<1000; i++ )
			gen.nextElement();
		gen.end();
		
		while( loop )
		{
			fromViewer.checkEvents();
			
			if( graph.hasAttribute( "ui.viewClosed" ) )
			{
				loop = false;
			}
			else
			{
				try { Thread.sleep( 20 ); } catch(Exception e) {}
				layout.compute();
			}
		}
		
		System.exit( 0 );
	}
	
	protected static String styleSheet =
		"node { size: 3px; fill-color: rgb(150,150,150); }" +
		"edge { fill-color: rgb(100,100,100); }";
}