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

import static org.junit.Assert.* ;

import java.util.HashSet;

import org.junit.Test;
import org.miv.graphstream.graph.Edge;
import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.Node;
import org.miv.graphstream.graph.implementations.AdjacencyListGraph;
import org.miv.graphstream.graph.implementations.MultiGraph;
import org.miv.graphstream.graph.implementations.SingleGraph;

public class TestGraph
{
	@Test
	public void testBasic()
	{
		testBasic( new SingleGraph() );
		testBasic( new MultiGraph() );
		testBasic( new AdjacencyListGraph() );
	}
	
	protected void testBasic( Graph graph )
	{
		Node A = graph.addNode( "A" );
		Node B = graph.addNode( "B" );
		Node C = graph.addNode( "C" );
		
		Edge AB = graph.addEdge( "AB", "A", "B" );
		Edge BC = graph.addEdge( "BC", "B", "C" );
		Edge CA = graph.addEdge( "CA", "C", "A" );
		
		assertEquals( 3, graph.getNodeCount() );
		assertEquals( 3, graph.getEdgeCount() );
		
		assertNotNull( A );
		assertNotNull( B );
		assertNotNull( C );
		assertNotNull( AB );
		assertNotNull( BC );
		assertNotNull( CA );
		
		assertEquals( "A", A.getId() );
		assertEquals( "B", B.getId() );
		assertEquals( "C", C.getId() );
		assertEquals( "AB", AB.getId() );
		assertEquals( "BC", BC.getId() );
		assertEquals( "CA", CA.getId() );

		assertEquals( A, graph.getNode( "A" ) );
		assertEquals( B, graph.getNode( "B" ) );
		assertEquals( C, graph.getNode( "C" ) );
		assertEquals( AB, graph.getEdge( "AB" ) );
		assertEquals( BC, graph.getEdge( "BC" ) );
		assertEquals( CA, graph.getEdge( "CA" ) );
		
		assertFalse( AB.isDirected() );
		assertFalse( BC.isDirected() );
		assertFalse( CA.isDirected() );
		
		assertEquals( A, AB.getNode0() );
		assertEquals( B, AB.getNode1() );
		assertEquals( A, AB.getSourceNode() );
		assertEquals( B, AB.getTargetNode() );
		assertEquals( B, BC.getNode0() );
		assertEquals( C, BC.getNode1() );
		assertEquals( B, BC.getSourceNode() );
		assertEquals( C, BC.getTargetNode() );
		assertEquals( C, CA.getNode0() );
		assertEquals( A, CA.getNode1() );
		assertEquals( C, CA.getSourceNode() );
		assertEquals( A, CA.getTargetNode() );
		
		assertEquals( B, AB.getOpposite( A ) );
		assertEquals( A, AB.getOpposite( B ) );
		assertEquals( C, BC.getOpposite( B ) );
		assertEquals( B, BC.getOpposite( C ) );
		assertEquals( A, CA.getOpposite( C ) );
		assertEquals( C, CA.getOpposite( A ) );
		
		assertEquals( 2, A.getDegree() );
		assertEquals( 2, B.getDegree() );
		assertEquals( 2, C.getDegree() );
		
		assertEquals( 2, A.getInDegree() );
		assertEquals( 2, A.getOutDegree() );
		assertEquals( 2, B.getInDegree() );
		assertEquals( 2, B.getOutDegree() );
		assertEquals( 2, C.getInDegree() );
		assertEquals( 2, C.getOutDegree() );
		
		assertTrue( A.hasEdgeFrom( "B" ) );
		assertTrue( A.hasEdgeFrom( "C" ) );
		assertTrue( B.hasEdgeFrom( "A" ) );
		assertTrue( B.hasEdgeFrom( "C" ) );
		assertTrue( C.hasEdgeFrom( "A" ) );
		assertTrue( C.hasEdgeFrom( "B" ) );
		
		assertEquals( AB, A.getEdgeFrom( "B" ) );
		assertEquals( CA, A.getEdgeFrom( "C" ) );
		assertEquals( AB, B.getEdgeFrom( "A" ) );
		assertEquals( BC, B.getEdgeFrom( "C" ) );
		assertEquals( CA, C.getEdgeFrom( "A" ) );
		assertEquals( BC, C.getEdgeFrom( "B" ) );
		
		assertTrue( A.hasEdgeToward( "B" ) );
		assertTrue( A.hasEdgeToward( "C" ) );
		assertTrue( B.hasEdgeToward( "A" ) );
		assertTrue( B.hasEdgeToward( "C" ) );
		assertTrue( C.hasEdgeToward( "A" ) );
		assertTrue( C.hasEdgeToward( "B" ) );
		
		assertEquals( AB, A.getEdgeToward( "B" ) );
		assertEquals( CA, A.getEdgeToward( "C" ) );
		assertEquals( AB, B.getEdgeToward( "A" ) );
		assertEquals( BC, B.getEdgeToward( "C" ) );
		assertEquals( CA, C.getEdgeToward( "A" ) );
		assertEquals( BC, C.getEdgeToward( "B" ) );
		
		assertNull( A.getEdgeFrom( "Z" ) );
		assertNull( B.getEdgeFrom( "Z" ) );
		assertNull( C.getEdgeFrom( "Z" ) );
		assertNull( A.getEdgeToward( "Z" ) );
		assertNull( B.getEdgeToward( "Z" ) );
		assertNull( C.getEdgeToward( "Z" ) );
	}
	
	@Test
	public void testDirected()
	{
		testDirected( new SingleGraph() );
		testDirected( new MultiGraph() );
		testDirected( new AdjacencyListGraph() );
	}
	
	protected void testDirected( Graph graph )
	{
		Node A = graph.addNode( "A" );
		Node B = graph.addNode( "B" );
		Node C = graph.addNode( "C" );
		
		Edge AB = graph.addEdge( "AB", "A", "B" );
		Edge BC = graph.addEdge( "BC", "B", "C", true );
		Edge CA = graph.addEdge( "CA", "C", "A", false );
	
		//   A
		//   |\
		//   | \
		//   |  \
		//   |   \
		//   B--->C
		
		assertFalse( AB.isDirected() );
		assertTrue(  BC.isDirected() );
		assertFalse( CA.isDirected() );
		
		assertEquals( 2, A.getDegree() );
		assertEquals( 2, B.getDegree() );
		assertEquals( 2, C.getDegree() );
		
		assertEquals( 2, A.getInDegree() );
		assertEquals( 2, A.getOutDegree() );
		assertEquals( 1, B.getInDegree() );
		assertEquals( 2, B.getOutDegree() );
		assertEquals( 2, C.getInDegree() );
		assertEquals( 1, C.getOutDegree() );
		
		assertEquals( AB, A.getEdgeFrom( "B" ) );
		assertEquals( CA, A.getEdgeFrom( "C" ) );
		assertEquals( AB, B.getEdgeFrom( "A" ) );
		assertNull(       B.getEdgeFrom( "C" ) );
		assertEquals( CA, C.getEdgeFrom( "A" ) );
		assertEquals( BC, C.getEdgeFrom( "B" ) );
		
		assertEquals( AB, A.getEdgeToward( "B" ) );
		assertEquals( CA, A.getEdgeToward( "C" ) );
		assertEquals( AB, B.getEdgeToward( "A" ) );
		assertEquals( BC, B.getEdgeToward( "C" ) );
		assertEquals( CA, C.getEdgeToward( "A" ) );
		assertNull(       C.getEdgeToward( "B" ) );	
		
		// Now change things a little :
		//
		//   A
		//   |\
		//   | \
		//   |  \
		//   v   \
		//   B<---C
		//
		// BC changes its direction, and AB becomes directed. 
		
		BC.switchDirection();
		AB.setDirected( true );

		assertTrue(  AB.isDirected() );
		assertTrue(  BC.isDirected() );
		assertFalse( CA.isDirected() );
		
		assertEquals( 2, A.getDegree() );
		assertEquals( 2, B.getDegree() );
		assertEquals( 2, C.getDegree() );
		
		assertEquals( 1, A.getInDegree() );
		assertEquals( 2, A.getOutDegree() );
		assertEquals( 2, B.getInDegree() );
		assertEquals( 0, B.getOutDegree() );
		assertEquals( 1, C.getInDegree() );
		assertEquals( 2, C.getOutDegree() );
		
		assertNull(       A.getEdgeFrom( "B" ) );
		assertEquals( CA, A.getEdgeFrom( "C" ) );
		assertEquals( AB, B.getEdgeFrom( "A" ) );
		assertEquals( BC, B.getEdgeFrom( "C" ) );
		assertEquals( CA, C.getEdgeFrom( "A" ) );
		assertNull(       C.getEdgeFrom( "B" ) );
		
		assertEquals( AB, A.getEdgeToward( "B" ) );
		assertEquals( CA, A.getEdgeToward( "C" ) );
		assertNull(       B.getEdgeToward( "A" ) );
		assertNull(       B.getEdgeToward( "C" ) );
		assertEquals( CA, C.getEdgeToward( "A" ) );
		assertEquals( BC, C.getEdgeToward( "B" ) );
	}
	
	@Test
	public void testIterables()
	{
		testIterables( new SingleGraph() );
		testIterables( new MultiGraph() );
		testIterables( new AdjacencyListGraph() );		
	}
	
	protected void testIterables( Graph graph )
	{
		Node A = graph.addNode( "A" );
		Node B = graph.addNode( "B" );
		Node C = graph.addNode( "C" );
		
		Edge AB = graph.addEdge( "AB", "A", "B" );
		Edge BC = graph.addEdge( "BC", "B", "C" );
		Edge CA = graph.addEdge( "CA", "C", "A" );

		// Test graph iterables.
		
		HashSet<Node> nodes = new HashSet<Node>();
		HashSet<Edge> edges = new HashSet<Edge>();
		
		for( Node node: graph )
			nodes.add( node );
		
		assertEquals( 3, nodes.size() );
		assertTrue( nodes.contains( A ) );
		assertTrue( nodes.contains( B ) );
		assertTrue( nodes.contains( C ) );
		nodes.clear();
		
		for( Node node: graph.getNodeSet() )
			nodes.add( node );
		
		assertEquals( 3, nodes.size() );
		assertTrue( nodes.contains( A ) );
		assertTrue( nodes.contains( B ) );
		assertTrue( nodes.contains( C ) );
		nodes.clear();
		
		for( Edge edge: graph.getEdgeSet() )
			edges.add( edge );
		
		assertEquals( 3, edges.size() );
		assertTrue( edges.contains( AB ) );
		assertTrue( edges.contains( BC ) );
		assertTrue( edges.contains( CA ) );
		edges.clear();

		// Test node iterables.

		for( Edge edge: A )
			edges.add( edge );
			
		assertEquals( 2, edges.size() );
		assertTrue( edges.contains( AB ) );
		assertTrue( edges.contains( CA ) );
		edges.clear();
		
		for( Edge edge: A.getEdgeSet() )
			edges.add( edge );
			
		assertEquals( 2, edges.size() );
		assertTrue( edges.contains( AB ) );
		assertTrue( edges.contains( CA ) );
		edges.clear();		
		
		for( Edge edge: B.getEdgeSet() )
			edges.add( edge );
			
		assertEquals( 2, edges.size() );
		assertTrue( edges.contains( AB ) );
		assertTrue( edges.contains( BC ) );
		edges.clear();

		for( Edge edge: C.getEdgeSet() )
			edges.add( edge );
			
		assertEquals( 2, edges.size() );
		assertTrue( edges.contains( BC ) );
		assertTrue( edges.contains( CA ) );
		edges.clear();

		AB.setDirected( true );
		BC.setDirected( true );
		//   A
		//   |\
		//   | \
		//   |  \
		//   v   \
		//   B--->C

		for( Edge edge: A.getEnteringEdgeSet() )
			edges.add( edge );
		
		assertEquals( 1, edges.size() );
		assertTrue( edges.contains( CA ) );
		edges.clear();

		for( Edge edge: B.getEnteringEdgeSet() )
			edges.add( edge );
		
		assertEquals( 1, edges.size() );
		assertTrue( edges.contains( AB ) );
		edges.clear();

		for( Edge edge: C.getEnteringEdgeSet() )
			edges.add( edge );
		
		assertEquals( 2, edges.size() );
		assertTrue( edges.contains( BC ) );
		assertTrue( edges.contains( CA ) );
		edges.clear();
		
		for( Edge edge: A.getLeavingEdgeSet() )
			edges.add( edge );
		
		assertEquals( 2, edges.size() );
		assertTrue( edges.contains( AB ) );
		assertTrue( edges.contains( CA ) );
		edges.clear();

		for( Edge edge: B.getLeavingEdgeSet() )
			edges.add( edge );
		
		assertEquals( 1, edges.size() );
		assertTrue( edges.contains( BC ) );
		edges.clear();

		for( Edge edge: C.getLeavingEdgeSet() )
			edges.add( edge );

		assertEquals( 1, edges.size() );
		assertTrue( edges.contains( CA ) );
		edges.clear();
	}
	
	@Test
	public void testRemoval()
	{
		testRemoval( new SingleGraph() );
		testRemoval( new MultiGraph() );
		testRemoval( new AdjacencyListGraph() );				
	}
	
	public void testRemoval( Graph graph )
	{
		Node A = graph.addNode( "A" );
		graph.addNode( "B" );
		graph.addNode( "C" );
		
		Edge AB = graph.addEdge( "AB", "A", "B" );
		graph.addEdge( "BC", "B", "C" );
		Edge CA = graph.addEdge( "CA", "C", "A" );

		assertEquals( 3, graph.getNodeCount() );
		assertEquals( 3, graph.getEdgeCount() );
		
		// Remove a node. This should also remove two edges.
		
		Node n = graph.removeNode( "A" );

		assertEquals( 2, graph.getNodeCount() );
		assertEquals( 1, graph.getEdgeCount() );
		
		assertEquals( n, A );
		assertNull( graph.getEdge( "AB" ) );
		assertNull( graph.getEdge( "CA" ) );
		assertNull( graph.getNode( "A" ) );
		
		assertNotNull( graph.getEdge( "BC" ) );
		assertNotNull( graph.getNode( "B" ) );
		assertNotNull( graph.getNode( "C" ) );

		// Rebuild the graph and remove an edge.
		
		A  = graph.addNode( "A" );
		AB = graph.addEdge( "AB", "A", "B" );
		CA = graph.addEdge( "CA", "C", "A" );
		
		assertEquals( 3, graph.getNodeCount() );
		assertEquals( 3, graph.getEdgeCount() );

		Edge e = graph.removeEdge( "A", "B" );

		assertEquals( 3, graph.getNodeCount() );
		assertEquals( 2, graph.getEdgeCount() );
		
		assertEquals( e, AB );
		assertNull( graph.getEdge( "AB" ) );
		
		assertNotNull( graph.getNode( "A" ) );
		assertNotNull( graph.getNode( "B" ) );
		assertNotNull( graph.getNode( "C" ) );
		assertNotNull( graph.getEdge( "BC" ) );
		assertNotNull( graph.getEdge( "CA" ) );
		
		// Now remove another edge in another way.
		
		e = graph.removeEdge( "CA" );
		
		assertEquals( 3, graph.getNodeCount() );
		assertEquals( 1, graph.getEdgeCount() );
		
		assertEquals( e, CA );
		assertNull( graph.getEdge( "AB" ) );
		assertNull( graph.getEdge( "CA" ) );
		
		assertNotNull( graph.getNode( "A" ) );
		assertNotNull( graph.getNode( "B" ) );
		assertNotNull( graph.getNode( "C" ) );
		assertNotNull( graph.getEdge( "BC" ) );

		// Test the whole graph erasing.
		
		graph.clear();
		
		assertEquals( 0, graph.getNodeCount() );
		assertEquals( 0, graph.getEdgeCount() );
	}
}