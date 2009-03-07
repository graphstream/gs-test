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
 */

package org.miv.graphstream.algorithm.test;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.miv.graphstream.algorithm.AStar;
import org.miv.graphstream.graph.Edge;
import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.Node;
import org.miv.graphstream.graph.Path;
import org.miv.graphstream.graph.implementations.DefaultGraph;

//import org.junit.* ;
import static org.junit.Assert.* ;

/**
 * Simple test of the A* algorithm.
 * 
 * @author Antoine Dutot
 */
public class TestAStar
{
	public static void main( String args[] )
	{
		TestAStar tas = new TestAStar();
		
		tas.setUp();
//		tas.testAStarNoWeights();
//		tas.testAStarWeighted1();
		tas.testAStarWeighted2();
		tas.graph.display();
	}

	public Graph graph;
	Node A, B, C, D, E, F;
	Edge AB, BC, CD, DE, EF, BF;
	AStar astar;
	
	@Before
	public void setUp()
	{
		graph = new DefaultGraph( false, true );
		
		AB = graph.addEdge( "AB", "A", "B" );
		BC = graph.addEdge( "BC", "B", "C" );
		CD = graph.addEdge( "CD", "C", "D" );
		DE = graph.addEdge( "DE", "D", "E" );
		EF = graph.addEdge( "EF", "E", "F" );
		BF = graph.addEdge( "BF", "B", "F" );
		
		A = graph.getNode( "A" ); A.addAttribute( "label", "A" );
		B = graph.getNode( "B" ); B.addAttribute( "label", "B" );
		C = graph.getNode( "C" ); C.addAttribute( "label", "C" );
		D = graph.getNode( "D" ); D.addAttribute( "label", "D" );
		E = graph.getNode( "E" ); E.addAttribute( "label", "E" );
		F = graph.getNode( "F" ); F.addAttribute( "label", "F" );
		
		//         C --- D
		//        /      |
		// A --- B       |
		//        \      |
		//         F --- E
		//
		// The shortest path between A and F is therefore A -> B -> F.
		
		astar = new AStar();
		astar.setGraph( graph );
		astar.setCosts( new AStar.DefaultCosts( "weight" ) );
	}

	@Test
	public void testAStarNoWeights()
	{
		astar.compute( "A", "F" );
		
		Path path = astar.getShortestPath();
		
		List<Edge> edges = path.getEdgePath();
		
		for( Edge edge: edges )
			edge.addAttribute( "color", "red" );
		
		Iterator<? extends Edge> i = edges.iterator();
		
		Edge e = i.next();
		assertTrue( e != null );
		assertTrue( e.getId().equals( "AB" ) );
		e = i.next();
		assertTrue( e != null );
		assertTrue( e.getId().equals( "BF" ) );
		assertTrue( ! i.hasNext() );
	}
	
	@Test
	public void testAStarWeighted1()
	{
		AB.setAttribute( "weight", 1 );
		BC.setAttribute( "weight", 1 );
		BF.setAttribute( "weight", 1.5f );	// First orient the algorithm toward BC.
		CD.setAttribute( "weight", 1 );
		DE.setAttribute( "weight", 1 );
		EF.setAttribute( "weight", 1 );
		
		astar.compute( "A", "F" );
		
		Path path = astar.getShortestPath();
		
		List<Edge> edges = path.getEdgePath();
		
		for( Edge edge: edges )
			edge.addAttribute( "color", "red" );
		
		Iterator<? extends Edge> i = edges.iterator();
		
		Edge e = i.next();
		assertTrue( e != null );
		assertTrue( e.getId().equals( "AB" ) );
		e = i.next();
		assertTrue( e != null );
		assertTrue( e.getId().equals( "BF" ) );
		assertTrue( ! i.hasNext() );		
	}
	
	@Test
	public void testAStarWeighted2()
	{
		AB.setAttribute( "weight", 1.0f );
		BC.setAttribute( "weight", 0.1f );
		BF.setAttribute( "weight", 1.0f );
		CD.setAttribute( "weight", 0.1f );
		DE.setAttribute( "weight", 0.1f );
		EF.setAttribute( "weight", 0.1f );
		
		// Therefore the shortest path is A -> B -> C -> D -> E -> F, sum = 1.4.
		// Whereas path A -> B -> F sum = 2.
		
		astar.compute( "A", "F" );
		
		Path path = astar.getShortestPath();
		
		List<Edge> edges = path.getEdgePath();
		
		for( Edge edge: edges )
			edge.addAttribute( "color", "red" );
		
		Iterator<? extends Edge> i = edges.iterator();
		
		Edge e = i.next();
		assertTrue( e != null );
		assertTrue( e.getId().equals( "AB" ) );
		e = i.next();
		assertTrue( e != null );
		assertTrue( e.getId().equals( "BC" ) );
		e = i.next();
		assertTrue( e != null );
		assertTrue( e.getId().equals( "CD" ) );
		e = i.next();
		assertTrue( e != null );
		assertTrue( e.getId().equals( "DE" ) );
		e = i.next();
		assertTrue( e != null );
		assertTrue( e.getId().equals( "EF" ) );
		assertTrue( ! i.hasNext() );		
	}
}