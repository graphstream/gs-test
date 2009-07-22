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

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.junit.Test;
import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.Node;
import org.miv.graphstream.graph.implementations.MultiGraph;
import org.miv.graphstream.io2.thread.ThreadProxyFilter;
import org.miv.graphstream.ui2.graphicGraph.stylesheet.Style;
import org.miv.graphstream.ui2.graphicGraph.GraphicGraph;
import org.miv.graphstream.ui2.graphicGraph.GraphicSprite;
import org.miv.graphstream.ui2.spriteManager.Sprite;
import org.miv.graphstream.ui2.spriteManager.SpriteManager;

/**
 * Test the bases of the viewer. 
 */
public class TestViewer
{
	@Test
	public void testViewerBase()
	{
		// Here a Graph is created in this thread and
		// another thread is created with a GraphicGraph.
		// The two graphs being in separate threads we use
		// thread proxies filters to pass informations between
		// the two. Once again we will use synchronisation (the
		// two graphs listen at each other).
		// In the direction Graph -> MultiGraph the multigraph
		// listens at ALL the events (elements + attributes). In
		// the direction MultiGraph -> Graph, the graph only listen at
		// attributes since we do not intend to add elements directly
		// in the multi graph.
		
		Graph             graph        = new MultiGraph( "inputGraph" );
		InTheSwingThread  viewerThread = new InTheSwingThread( new ThreadProxyFilter( graph ) );
		ThreadProxyFilter ggProxy       = viewerThread.getProxy();
		
		ggProxy.addGraphAttributesListener( graph );	// Get the graphic graph proxy.
		
		// Now launch the graphic graph in the Swing thread using
		// a Swing Timer.

		viewerThread.start();
		
		// We modify the graph in the main
		// thread.
		
		Node A = graph.addNode( "A" );
		Node B = graph.addNode( "B" );
		Node C = graph.addNode( "C" );
		graph.addEdge( "AB", "A", "B" );
		graph.addEdge( "BC", "B", "C" );
		graph.addEdge( "CA", "C", "A" );
		
		SpriteManager sman = new SpriteManager( graph );
		Sprite S1 = sman.addSprite( "S1" );
		Sprite S2 = sman.addSprite( "S2" );

		A.addAttribute( "ui.foo", "bar" );
		B.addAttribute( "ui.bar", "foo" );
		C.addAttribute( "truc" );			// Not prefixed by UI, will not pass.
		S1.addAttribute( "ui.foo", "bar" );
		
		ggProxy.checkEvents();
		
		// We ask the Swing thread to modify the graphic graph.
		
		graph.addAttribute( "ui.EQUIP" );	// Remember GraphicGraph filters attributes.

		// Wait and stop.
//System.err.printf( "************* EQUIP ****************%n" );
		ggProxy.checkEvents();
		sleep( 1000 );
		ggProxy.checkEvents();
		
//System.err.printf( "************* STOP ****************%n" );
		graph.addAttribute( "ui.STOP" );
		
		ggProxy.checkEvents();
		sleep( 1000 );
		ggProxy.checkEvents();
		
//System.err.printf( "************* END ****************%n" );
		
		// Now we can test
		
		GraphicGraph ggraph = viewerThread.graphicGraph;
		
		assertTrue( viewerThread.isStopped() );
		assertFalse( graph.hasAttribute( "ui.EQUIP" ) );
		assertFalse( ggraph.hasAttribute( "ui.EQUIP" ) );
		assertTrue( graph.hasAttribute( "ui.STOP" ) );
		assertTrue( ggraph.hasAttribute( "ui.STOP" ) );
		
		// Assert all events passed toward the graphic graph. 
		
		assertEquals( 3, ggraph.getNodeCount() );
		assertEquals( 3, ggraph.getEdgeCount() );
		assertEquals( 2, ggraph.getSpriteCount() );
		assertNotNull( ggraph.getNode( "A" ) );
		assertNotNull( ggraph.getNode( "B" ) );
		assertNotNull( ggraph.getNode( "C" ) );
		assertNotNull( ggraph.getEdge( "AB" ) );
		assertNotNull( ggraph.getEdge( "BC" ) );
		assertNotNull( ggraph.getEdge( "CA" ) );
		assertNotNull( ggraph.getSprite( "S1" ) );
		assertNotNull( ggraph.getSprite( "S2" ) );
		assertEquals( "bar", ggraph.getNode("A").getAttribute( "ui.foo" ) );
		assertEquals( "foo", ggraph.getNode("B").getAttribute( "ui.bar" ) );
		assertNull( ggraph.getNode("C").getAttribute( "truc" ) );	// Should not pass the attribute filter.
		assertEquals( "bar", ggraph.getSprite("S1").getAttribute( "ui.foo" ) );
		
		// Assert attributes passed back to the graph from the graphic graph.
		
		Object xyz1[] = { new Float(4), new Float(4), new Float(4) };
		Object xyz2[] = { new Float(2), new Float(2), new Float(2) };
		Object xyz3[] = { new Float(3), new Float(3), new Float(3) };
		
		assertArrayEquals( xyz1, (Object[])graph.getNode("A").getAttribute("xyz") );
		assertArrayEquals( xyz2, (Object[])graph.getNode("B").getAttribute("xyz") );
		assertArrayEquals( xyz3, (Object[])graph.getNode("C").getAttribute("xyz") );

		assertEquals( "foobar", S2.getAttribute( "ui.foobar" ) );
		
		assertEquals( 0.5f, S1.getX() );
		assertEquals( 0,    S1.getY() );
		assertEquals( 0,    S1.getZ() );
		assertEquals( 1,    S2.getX() );
		assertEquals( 2,    S2.getY() );
		assertEquals( 3,    S2.getZ() );
	}

	protected void sleep( int millis )
	{
		try{ Thread.sleep( millis ); } catch( InterruptedException e ) {}
	}
	
/**
 * The graphic graph in the Swing thread.
 */
public static class InTheSwingThread implements ActionListener
{
	protected ThreadProxyFilter inputProxy;
	
	protected GraphicGraph graphicGraph;
	
	protected Timer timer;
	
	public InTheSwingThread( ThreadProxyFilter input )
	{
		inputProxy   = input;
		graphicGraph = new GraphicGraph();
		timer        = new Timer( 40, this ); 
		
		timer.setRepeats( true );
		timer.setCoalesce( true );
		input.addGraphListener( graphicGraph );
	}

	public void start()
	{
		timer.start();
	}
	
	public boolean isStopped()
	{
		return( ! timer.isRunning() );
	}
	
	public void actionPerformed( ActionEvent e )
    {
		inputProxy.checkEvents();
		
		if( graphicGraph.hasAttribute( "ui.EQUIP" ) )
		{
			Node A = graphicGraph.getNode( "A" );
			Node B = graphicGraph.getNode( "B" );
			Node C = graphicGraph.getNode( "C" );
			
			if( A != null )
				A.addAttribute( "xyz", 4, 4, 4 );
			if( B != null )
				B.addAttribute( "xyz", 2, 2, 2 );
			if( C != null )
				C.addAttribute( "xyz", 3, 3, 3 );
			
			GraphicSprite S1 = graphicGraph.getSprite( "S1" );
			GraphicSprite S2 = graphicGraph.getSprite( "S2" );
			
			if( S2 != null )
			{
				S2.addAttribute( "ui.foobar", "foobar" );
				S2.setPosition( 1, 2, 3, Style.Units.GU );
			}

			if( S1 != null )
				S1.setPosition( 0.5f );
			
			graphicGraph.removeAttribute( "ui.EQUIP" );
		}
		else if( graphicGraph.hasAttribute( "ui.STOP" ) )
		{
			timer.stop();
		}
    }
	
	public ThreadProxyFilter getProxy()
	{
		return new ThreadProxyFilter( graphicGraph );
	}
}
}