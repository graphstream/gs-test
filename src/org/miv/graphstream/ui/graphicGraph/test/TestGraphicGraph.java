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

package org.miv.graphstream.ui.graphicGraph.test;

import java.awt.Color;
import java.util.HashSet;

import org.junit.Test;
import org.miv.graphstream.graph.Element;
import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.implementations.MultiGraph;
import org.miv.graphstream.ui2.spriteManager.Sprite;
import org.miv.graphstream.ui2.spriteManager.SpriteManager;
import org.miv.graphstream.ui2.graphicGraph.GraphicGraph;
import org.miv.graphstream.ui2.graphicGraph.GraphicSprite;
import org.miv.graphstream.ui2.graphicGraph.stylesheet.Style;
import org.miv.graphstream.ui2.graphicGraph.stylesheet.StyleConstants;

import static org.junit.Assert.* ;

public class TestGraphicGraph
{
// Attribute
	
	/**
	 * a graph that can server as input to send events to the graphic graph.
	 */
	protected Graph inGraph;
	
	/**
	 * The graphic graph to test.
	 */
	protected GraphicGraph outGraph;
	
// Tests
	
	@Test
	public void basicTest()
	{
		// Test the class alone.
		
		outGraph = new GraphicGraph();
		
		// The usual triangle test.
		
		outGraph.addNode( "A" );
		outGraph.addNode( "B" );
		outGraph.addNode( "C" );
		outGraph.addEdge( "AB", "A", "B", false );
		outGraph.addEdge( "BC", "B", "C", true );
		outGraph.addEdge( "CA", "C", "A", false );
		
		assertEquals( 3, outGraph.getNodeCount() );
		assertEquals( 3, outGraph.getEdgeCount() );
		assertEquals( 0, outGraph.getSpriteCount() );

		assertFalse( outGraph.getEdge("AB").isDirected() );
		assertTrue(  outGraph.getEdge("BC").isDirected() );
		assertFalse( outGraph.getEdge("CA").isDirected() );
		
		// Test the case of multi-graphs.
		
		outGraph.addEdge( "AB2", "A", "B", true );
		
		assertEquals( 4, outGraph.getEdgeCount() );
		assertFalse( outGraph.getEdge("AB" ).isDirected() );
		assertTrue(  outGraph.getEdge("AB2").isDirected() );
		
		outGraph.addEdge( "CA2", "C", "A" );
		outGraph.removeEdge( "CA" );
		
		assertEquals( 4, outGraph.getEdgeCount() );
		assertEquals( null, outGraph.getEdge( "CA" ) );
		assertTrue( outGraph.getEdge( "CA2" ) != null );
		
		outGraph.removeNode( "C" );
		
		assertEquals( 2, outGraph.getNodeCount() );
		assertEquals( 2, outGraph.getEdgeCount() );
		assertEquals( null, outGraph.getNode( "C" ) );
		assertEquals( null, outGraph.getEdge( "BC" ) );
		assertEquals( null, outGraph.getEdge( "CA" ) );
		
		outGraph.removeNode( "A" );
		
		assertEquals( 1, outGraph.getNodeCount() );
		assertEquals( 0, outGraph.getEdgeCount() );
		assertEquals( null, outGraph.getNode( "A" ) );
		assertEquals( null, outGraph.getEdge( "AB" ) );
		assertEquals( null, outGraph.getEdge( "AB2" ) );

		// And finally...
		
		outGraph.clear();

		assertEquals( 0, outGraph.getNodeCount() );
		assertEquals( 0, outGraph.getEdgeCount() );
		assertEquals( 0, outGraph.getSpriteCount() );
	}
	
	protected static String styleSheet1 =
		"graph  { fill-color: black; }" +
		"node   { fill-color: white; }" +
		"edge   { fill-color: white; }" +
		"node#A { fill-color: red;   }" +
		"node#B { fill-color: blue;  }";
	
	@Test
	public void testStyleSheetLoading()
	{
		// Test the style sheet loading capabilities of the graphic graph.
		
		outGraph = new GraphicGraph();

		outGraph.addNode( "A" );
		outGraph.addNode( "B" );
		outGraph.addNode( "C" );
		outGraph.addEdge( "AB", "A", "B" );
		outGraph.addEdge( "BC", "B", "C" );
		outGraph.addEdge( "CA", "C", "A" );
		
		// Look at the default style sheet.
		
		assertNotNull( outGraph.getStyle() );
		assertNotNull( outGraph.getNode("A").getStyle() );
		assertNotNull( outGraph.getNode("B").getStyle() );
		assertNotNull( outGraph.getNode("C").getStyle() );
		
		testStyle( outGraph.getStyle(), Color.WHITE );
		testStyle( outGraph.getNode("A").getStyle(), Color.BLACK );
		testStyle( outGraph.getNode("B").getStyle(), Color.BLACK );
		testStyle( outGraph.getNode("C").getStyle(), Color.BLACK );
		
		// Load a style sheet by URL.

		outGraph.addAttribute( "stylesheet", styleSheet1 );
		
		assertNotNull( outGraph.getStyle() );
		assertNotNull( outGraph.getNode("A").getStyle() );
		assertNotNull( outGraph.getNode("B").getStyle() );
		assertNotNull( outGraph.getNode("C").getStyle() );
		
		testStyle( outGraph.getStyle(), Color.BLACK );
		testStyle( outGraph.getNode("A").getStyle(), Color.RED );
		testStyle( outGraph.getNode("B").getStyle(), Color.BLUE );
		testStyle( outGraph.getNode("C").getStyle(), Color.WHITE );
		
		// Cascade a style sheet by string.
		
		outGraph.addAttribute( "stylesheet", "node#A { fill-color: green; }" );
		
		assertNotNull( outGraph.getStyle() );
		assertNotNull( outGraph.getNode("A").getStyle() );
		assertNotNull( outGraph.getNode("B").getStyle() );
		assertNotNull( outGraph.getNode("C").getStyle() );
		
		testStyle( outGraph.getStyle(), Color.BLACK );
		testStyle( outGraph.getNode("A").getStyle(), Color.GREEN );
		testStyle( outGraph.getNode("B").getStyle(), Color.BLUE );
		testStyle( outGraph.getNode("C").getStyle(), Color.WHITE );

		// Cascade individual styles on elements.
		
		outGraph.getNode("A").addAttribute( "ui.style", "fill-color: blue;" );
		
		assertNotNull( outGraph.getNode("A").getStyle() );
		testStyle( outGraph.getNode("A").getStyle(), Color.BLUE );
		
		// Clear style.
		
		outGraph.getStyleSheet().clear();

		assertNotNull( outGraph.getStyle() );
		assertNotNull( outGraph.getNode("A").getStyle() );
		assertNotNull( outGraph.getNode("B").getStyle() );
		assertNotNull( outGraph.getNode("C").getStyle() );
		
		testStyle( outGraph.getStyle(), Color.WHITE );
		testStyle( outGraph.getNode("A").getStyle(), Color.BLACK );
		testStyle( outGraph.getNode("B").getStyle(), Color.BLACK );
		testStyle( outGraph.getNode("C").getStyle(), Color.BLACK );
	}
	
	protected void testStyle( Style style, Color colorBase )
	{
		assertTrue( style.getFillColors() != null && style.getFillColors().size() == 1 );
		Color color = style.getFillColor( 0 );
		assertEquals( StyleConstants.FillMode.PLAIN,  style.getFillMode() );
		assertEquals( StyleConstants.StrokeMode.NONE, style.getStrokeMode() );
		assertEquals( colorBase, color );
	}
	
	@Test
	public void testAsOutput()
	{
		// Test the GraphicGraph as an output for another graph.
		
		inGraph  = new MultiGraph( "inputGraph" );
		outGraph = new GraphicGraph();
		
		// Simply put the graphic graph as listener of the input graph.
		
		inGraph.addGraphListener( outGraph );
		
		// The usual triangle test : add some nodes and edges.
		
		inGraph.addNode( "A" );
		inGraph.addNode( "B" );
		inGraph.addNode( "C" );
		inGraph.addEdge( "AB", "A", "B", false );
		inGraph.addEdge( "BC", "B", "C", true );
		inGraph.addEdge( "CA", "C", "A", false );
		
		// Are they in the output graph ?
		
		assertEquals( 3, outGraph.getNodeCount() );
		assertEquals( 3, outGraph.getEdgeCount() );
		assertEquals( 0, outGraph.getSpriteCount() );

		assertFalse( outGraph.getEdge("AB").isDirected() );
		assertTrue(  outGraph.getEdge("BC").isDirected() );
		assertFalse( outGraph.getEdge("CA").isDirected() );
		
		// Now try to remove some nodes and edges in the in graph.
		
		inGraph.removeNode( "A" );	// This also removes edge "AB" and "CA".
		inGraph.removeEdge( "BC" );
		
		// Are they removed from the out graph ?
		
		assertEquals( 2, outGraph.getNodeCount() );
		assertEquals( 0, outGraph.getEdgeCount() );
		assertNull( outGraph.getNode( "A" ) );
		assertNotNull( outGraph.getNode( "B" ) );
		assertNotNull( outGraph.getNode( "C" ) );
		assertNull( outGraph.getEdge( "AB" ) );
		assertNull( outGraph.getEdge( "BC" ) );
		assertNull( outGraph.getEdge( "CA" ) );
	}
	
	@Test
	public void testAsOutputSprites()
	{
		inGraph  = new MultiGraph( "inputGraph" );
		outGraph = new GraphicGraph();
		
		inGraph.addGraphListener( outGraph );

		SpriteManager sman = new SpriteManager( inGraph );
		
		inGraph.addNode( "A" );
		inGraph.addNode( "B" );
		inGraph.addNode( "C" );
		inGraph.addEdge( "AB", "A", "B", false );
		inGraph.addEdge( "BC", "B", "C", true );
		inGraph.addEdge( "CA", "C", "A", false );
		
		assertEquals( 3, outGraph.getNodeCount() );
		assertEquals( 3, outGraph.getEdgeCount() );
		assertEquals( 0, outGraph.getSpriteCount() );
		
		// Now test sprites.
		
		Sprite s1 = sman.addSprite( "S1" );
		Sprite s2 = sman.addSprite( "S2" );
		
		// Test the sprite manager.
		
		HashSet<String> spriteIds = new HashSet<String>();
		
		assertTrue( sman.hasSprite( "S1" ) );
		assertTrue( sman.hasSprite( "S2" ) );
		assertEquals( s1, sman.getSprite( "S1" ) );
		assertEquals( s2, sman.getSprite( "S2" ) );
		assertEquals( 2, sman.getSpriteCount() );
		
		spriteIds.add( "S1" );
		spriteIds.add( "S2" );
		
		for( Sprite sprite: sman )
		{
			if( spriteIds.contains( sprite.getId() ) )
				spriteIds.remove( sprite.getId() );
		}
		
		assertTrue( spriteIds.isEmpty() );
		
		// Test the out graph for corresponding sprites.
		
		assertEquals( 2, outGraph.getSpriteCount() );

		spriteIds.add( "S1" );
		spriteIds.add( "S2" );
		
		for( GraphicSprite sprite: outGraph.spriteSet() )
		{
			if( spriteIds.contains( sprite.getId() ) )
				spriteIds.remove( sprite.getId() );
		}
		
		assertTrue( spriteIds.isEmpty() );
		
		// Now remove a sprite.
		
		sman.removeSprite( "S2" );
		
		assertEquals( 1, sman.getSpriteCount() );
		assertEquals( 1, outGraph.getSpriteCount() );
		assertNotNull( outGraph.getSprite( "S1" ) );
		assertNull( outGraph.getSprite( "S2" ) );
		
		// Now test adding attributes to a sprite.
		// Look if they are transfered in the out graph.
		
		s1.addAttribute( "foo", "bar" );
		s1.addAttribute( "foo1", 1, 2, 3 );
		
		GraphicSprite gs1 = outGraph.getSprite( "S1" );
		
		testSprite1( s1 );
		testSprite1( gs1 );
		
		// Now removing some attributes to a sprite.
		
		s1.removeAttribute( "foo1" );
		
		assertFalse( s1.hasAttribute( "foo1" ) );
		assertFalse( gs1.hasAttribute( "foo1" ) );
	}
	
	protected void testSprite1( Element e )
	{
		Object values[] = { 1, 2, 3 };

		assertTrue( e.hasLabel( "foo" ) );
		assertTrue( e.hasAttribute( "foo" ) );
		assertEquals( "bar", e.getLabel( "foo" ) );
		assertEquals( "bar", e.getAttribute( "foo" ) );
		assertTrue( e.hasArray( "foo1" ) );
		assertTrue( e.hasAttribute( "foo1" ) );
		assertArrayEquals( values, e.getArray( "foo1" ) );		
	}
}