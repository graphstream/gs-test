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

package org.miv.graphstream.graph.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.implementations.AdjacencyListGraph;
import org.miv.graphstream.graph.implementations.MultiGraph;
import org.miv.graphstream.graph.implementations.SingleGraph;

/**
 * Synchronisation between Sources and Sinks is not as trivial as it seems, but should looks like
 * it is ! Therefore we need to test it accurately ...
 */
public class TestGraphSynchronisation
{
	@Test
	public void testGraphSyncBase()
	{
		// Simple test with two graphs that mutually listen at themselves.
		//
		// /-------> g2
		// |          |
		// g1 <-------/
		
		testGraphSyncBase( new MultiGraph( "g1" ), new MultiGraph( "g2" ) );
		testGraphSyncBase( new SingleGraph( "g1" ), new SingleGraph( "g2" ) );
//		testGraphSyncBase( new AdjacencyListGraph( "g1" ), new AdjacencyListGraph( "g2" ) );
		
//		testGraphSyncBase( new MultiGraph( "g1" ), new AdjacencyListGraph( "g2" ) );
	}
	
	protected void testGraphSyncBase( Graph g1, Graph g2 )
	{
		g1.addGraphListener( g2 );	// These two lines seem simple but introduce an eventual
		g2.addGraphListener( g1 );	// recursive loop between the two graphs. Graph synchronisation
									// is all about avoiding this loop.
		
		// Test with element addition.
		// We add elements in both graphs alternatively. At the end, the two graphs must be
		// the same.
		
		g1.addNode( "A" );
		g2.addNode( "B" );
		g1.addNode( "C" );
		g2.addEdge( "AB", "A", "B", false );
		g1.addEdge( "BC", "B", "C", true );
		g2.addEdge( "CA", "C", "A", true );
		
		assertEquals( 3, g1.getNodeCount() );
		assertEquals( 3, g2.getNodeCount() );
		assertEquals( 3, g1.getEdgeCount() );
		assertEquals( 3, g2.getEdgeCount() );
		
		assertNotNull( g1.getNode( "A" ) );
		assertNotNull( g2.getNode( "A" ) );
		assertNotNull( g1.getNode( "B" ) );
		assertNotNull( g2.getNode( "B" ) );
		assertNotNull( g1.getNode( "C" ) );
		assertNotNull( g2.getNode( "C" ) );
		
		assertNotNull( g1.getEdge( "AB" ) );
		assertNotNull( g2.getEdge( "AB" ) );
		assertNotNull( g1.getEdge( "BC" ) );
		assertNotNull( g2.getEdge( "BC" ) );
		assertNotNull( g1.getEdge( "CA" ) );
		assertNotNull( g2.getEdge( "CA" ) );
		
		// Test with attribute addition.
		
		g1.getNode("A").addAttribute( "foo", "bar" );
		g2.getEdge( "AB" ).addAttribute( "foo", "bar" );
		
		assertEquals( 1, g1.getNode("A").getAttributeCount() );
		assertEquals( 1, g2.getNode("A").getAttributeCount() );
		assertEquals( 1, g1.getEdge("AB").getAttributeCount() );
		assertEquals( 1, g2.getEdge("AB").getAttributeCount() );
		assertEquals( "bar", g1.getNode("A").getAttribute( "foo" ) );
		assertEquals( "bar", g2.getNode("A").getAttribute( "foo" ) );
		assertEquals( "bar", g1.getEdge("AB").getAttribute( "foo" ) );
		assertEquals( "bar", g2.getEdge("AB").getAttribute( "foo" ) );
	
		// Test attribute change.
		
		g1.getNode("A").setAttribute( "foo", "truc" );
		g2.getEdge("AB").setAttribute( "foo", "truc" );
		
		assertEquals( "truc", g1.getNode("A").getAttribute( "foo" ) );
		assertEquals( "truc", g2.getNode("A").getAttribute( "foo" ) );
		assertEquals( "truc", g1.getEdge("AB").getAttribute( "foo" ) );
		assertEquals( "truc", g2.getEdge("AB").getAttribute( "foo" ) );
		
		// Test attribute removal.
		
		g2.getNode("A").removeAttribute( "foo" );
		g1.getEdge("AB").removeAttribute( "foo" );
		
		assertEquals( 0, g1.getNode("A").getAttributeCount() );
		assertEquals( 0, g2.getNode("A").getAttributeCount() );
		assertEquals( 0, g1.getEdge("AB").getAttributeCount() );
		assertEquals( 0, g2.getEdge("AB").getAttributeCount() );
		assertFalse( g1.getNode("A").hasAttribute( "foo" ) );
		assertFalse( g2.getNode("A").hasAttribute( "foo" ) );
		assertFalse( g1.getEdge("AB").hasAttribute( "foo" ) );
		assertFalse( g2.getEdge("AB").hasAttribute( "foo" ) );
		
		// Test edge removal
		
		g1.removeEdge( "CA" );
		
		assertEquals( 2, g1.getEdgeCount() );
		assertEquals( 2, g2.getEdgeCount() );
		assertNull( g1.getEdge( "CA" ) );
		assertNull( g2.getEdge( "CA" ) );
		
		// Test node removal and automatic edge removal (edge "AB" is automatically removed).
		
		g2.removeNode( "A" );
		
		assertEquals( 2, g2.getNodeCount() );
		assertEquals( 2, g1.getNodeCount() );
		assertEquals( 1, g1.getEdgeCount() );
		assertEquals( 1, g2.getEdgeCount() );
		assertNull( g1.getNode( "A" ) );
		assertNull( g2.getNode( "A" ) );
		assertNull( g1.getEdge( "AB" ) );
		assertNull( g2.getEdge( "AB" ) );
	}
	
	@Test
	public void testGraphSyncCycleSimple()
	{
		// More advanced test where three graphs mutually listen at themselves.
		//
		//             /--------> g3
		//             |           |
		// /--------> g2           |
		// |                       |
		// g1 <--------------------/
		
		testGraphSyncCycleSimple( new MultiGraph( "g1" ), new MultiGraph( "g2" ), new MultiGraph( "g3" ) );
		testGraphSyncCycleSimple( new SingleGraph( "g1"), new SingleGraph( "g2" ), new SingleGraph( "g3" ) );
//		testGraphSyncCycleSimple( new AdjacencyListGraph( "g1" ), new AdjacencyListGraph( "g2" ), new AdjacencyListGraph( "g3" ) );
		
//		testGraphSyncCycleSimple( new MultiGraph( "g1" ), new SingleGraph( "g2" ), new AdjacencyListGraph( "g3" ) );
	}
	
	protected void testGraphSyncCycleSimple( Graph g1, Graph g2, Graph g3 )
	{
		g1.addGraphListener( g2 );
		g2.addGraphListener( g3 );
		g3.addGraphListener( g1 );
		testGraphSyncCycle( g1, g2, g3 );
	}
	
	@Test
	public void testGraphSyncCycleProblem()
	{
		// More advanced test where three graphs mutually listen at themselves.
		//
		//             /--------> g3
		//             |           |
		// /--------> g2 <---------+
		// |                       |
		// g1 <--------------------/

		// XXX This does not yet work !!
		
		// g3 sends AddNode("g3",C) to g2 and g1.
		// g2 receives AN("g3",C) and propagates to g3 that blocks correctly.
		// g1. receive AN("g3",C) and propagates to g2 that already has the node but
		//     has not its id "g2" in the sourceId ==> XXX
		
		testGraphSyncCycleProblem( new MultiGraph( "g1" ), new MultiGraph( "g2" ), new MultiGraph( "g3" ) );
		testGraphSyncCycleProblem( new SingleGraph( "g1" ), new SingleGraph( "g2" ), new SingleGraph( "g3" ) );
	}
	
	protected void testGraphSyncCycleProblem( Graph g1, Graph g2, Graph g3 )
	{
		g1.addGraphListener( g2 );
		g2.addGraphListener( g3 );
		g3.addGraphListener( g1 );
		g3.addGraphListener( g2 );	// Exactly the same test as above with this line added... :-)
		testGraphSyncCycle( g1, g2, g3 );
	}
	
	protected void testGraphSyncCycle( Graph g1, Graph g2, Graph g3 )
	{
		g1.addNode( "A" );
		g2.addNode( "B" );
		g3.addNode( "C" );
		
		assertEquals( 3, g1.getNodeCount() );
		assertEquals( 3, g2.getNodeCount() );
		assertEquals( 3, g3.getNodeCount() );
		
		assertNotNull( g1.getNode( "A" ) );
		assertNotNull( g2.getNode( "A" ) );
		assertNotNull( g3.getNode( "A" ) );
		assertNotNull( g1.getNode( "B" ) );
		assertNotNull( g2.getNode( "B" ) );
		assertNotNull( g3.getNode( "B" ) );
		assertNotNull( g1.getNode( "C" ) );
		assertNotNull( g2.getNode( "C" ) );
		assertNotNull( g3.getNode( "C" ) );
		
		g1.addEdge( "AB", "A", "B" );
		g2.addEdge( "BC", "B", "C", true );
		g3.addEdge( "CA", "C", "A", false );
		
		assertEquals( 3, g1.getEdgeCount() );
		assertEquals( 3, g2.getEdgeCount() );
		assertEquals( 3, g3.getEdgeCount() );
		
		assertNotNull( g1.getEdge( "AB" ) );
		assertNotNull( g2.getEdge( "AB" ) );
		assertNotNull( g3.getEdge( "AB" ) );
		assertNotNull( g1.getEdge( "BC" ) );
		assertNotNull( g2.getEdge( "BC" ) );
		assertNotNull( g3.getEdge( "BC" ) );
		assertNotNull( g1.getEdge( "CA" ) );
		assertNotNull( g2.getEdge( "CA" ) );
		assertNotNull( g3.getEdge( "CA" ) );
		
		// Now attributes.
		
		g1.addAttribute( "foo", "bar" );
		g2.getNode("A").addAttribute( "foo", "bar" );
		g3.getEdge("AB").addAttribute( "foo", "bar" );
		
		assertEquals( "bar", g1.getAttribute( "foo" ) );
		assertEquals( "bar", g2.getAttribute( "foo" ) );
		assertEquals( "bar", g3.getAttribute( "foo" ) );
		assertEquals( "bar", g1.getNode("A").getAttribute( "foo" ) );
		assertEquals( "bar", g2.getNode("A").getAttribute( "foo" ) );
		assertEquals( "bar", g3.getNode("A").getAttribute( "foo" ) );
		assertEquals( "bar", g1.getEdge("AB").getAttribute( "foo" ) );
		assertEquals( "bar", g2.getEdge("AB").getAttribute( "foo" ) );
		assertEquals( "bar", g3.getEdge("AB").getAttribute( "foo" ) );
		
		// Attributes change.
		
		g1.setAttribute( "foo", "truc" );
		g2.getNode("A").setAttribute( "foo", "truc" );
		g3.getEdge("AB").setAttribute( "foo", "truc" );
		
		assertEquals( "truc", g1.getAttribute( "foo" ) );
		assertEquals( "truc", g2.getAttribute( "foo" ) );
		assertEquals( "truc", g3.getAttribute( "foo" ) );
		assertEquals( "truc", g1.getNode("A").getAttribute( "foo" ) );
		assertEquals( "truc", g2.getNode("A").getAttribute( "foo" ) );
		assertEquals( "truc", g3.getNode("A").getAttribute( "foo" ) );
		assertEquals( "truc", g1.getEdge("AB").getAttribute( "foo" ) );
		assertEquals( "truc", g2.getEdge("AB").getAttribute( "foo" ) );
		assertEquals( "truc", g3.getEdge("AB").getAttribute( "foo" ) );
		
		// Attribute removal.
		
		g1.removeAttribute( "foo" );
		g2.getNode("A").removeAttribute( "foo" );
		g3.getEdge("AB").removeAttribute( "foo" );
		
		assertFalse( g1.hasAttribute( "foo" ) );
		assertFalse( g2.hasAttribute( "foo" ) );
		assertFalse( g3.hasAttribute( "foo" ) );
		assertFalse( g1.getNode("A").hasAttribute( "foo" ) );
		assertFalse( g2.getNode("A").hasAttribute( "foo" ) );
		assertFalse( g3.getNode("A").hasAttribute( "foo" ) );
		assertFalse( g1.getEdge("AB").hasAttribute( "foo" ) );
		assertFalse( g2.getEdge("AB").hasAttribute( "foo" ) );
		assertFalse( g3.getEdge("AB").hasAttribute( "foo" ) );
		
		// Edge removal.
		
		g1.removeEdge( "AB" );
		
		assertEquals( 2, g1.getEdgeCount() );
		assertEquals( 2, g2.getEdgeCount() );
		assertEquals( 2, g3.getEdgeCount() );
		assertNull( g1.getEdge( "AB" ) );
		assertNull( g2.getEdge( "AB" ) );
		assertNull( g3.getEdge( "AB" ) );
		
		// Node removal and automatic edge removal.
		
		g2.removeNode( "A" );
		
		assertEquals( 2, g1.getNodeCount() );
		assertEquals( 2, g2.getNodeCount() );
		assertEquals( 2, g3.getNodeCount() );
		assertEquals( 1, g1.getEdgeCount() );
		assertEquals( 1, g2.getEdgeCount() );
		assertEquals( 1, g3.getEdgeCount() );
		assertNull( g1.getNode( "A" ) );
		assertNull( g2.getNode( "A" ) );
		assertNull( g3.getNode( "A" ) );
		assertNull( g1.getEdge( "CA" ) );
		assertNull( g2.getEdge( "CA" ) );
		assertNull( g3.getEdge( "CA" ) );
	}
}