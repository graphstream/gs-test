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

package org.graphstream.io.file.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.file.FileSource;
import org.junit.Test;

/**
 * Base test class for file inputs.
 * 
 * <p>
 * This test class propose a set of JUnit standard tests that can apply to any file format. 
 * </p>
 */
public abstract class TestFileSourceBase
{
// Attribute
	
	/**
	 * The graph built when reading the file.
	 */
	protected Graph graph;

	/**
	 * The current input tested.
	 */
	protected FileSource input;
	
	/**
	 * If false, edge identifiers are not tested (some format cannot specify edge identifiers).
	 */
	protected boolean testEdgeIds = true;

// Access
	
	/**
	 * Return a string containing a file defining a simple triangle made of three nodes named
	 * "A", "B" and "C", tied with three edges "AB", "BC" and "CA".
	 */
	public abstract String anUndirectedTriangle();
	
	/**
	 * Return a string containing a file defining a simple triangle made of three nodes named
	 * "A", "B" and "C", tied with three edges "AB", "BC" and "CA", with direction "A" toward "B",
	 * undirected between "B" and "C" and directed from "A" to "C".  
	 */
	public abstract String aDirectedTriangle();
	
	/**
	 * Return a string containing the triangle of {@link #anUndirectedTriangle()}, but each
	 * element has attributes :
	 * <ul>
	 * 		<li>node "A" as three attributes</li>
	 * </ul>
	 */
	public abstract String basicAttributes();
	
	/**
	 * Return a string containing the name of a local file pointing at the definition of the
	 * triangle evoked in {@link #anUndirectedTriangle()}.
	 */
	public abstract String anUndirectedTriangleFileName();
	
	/**
	 * Return a string containing an HTTP URL pointing at the definition of the
	 * triangle evoked in {@link #anUndirectedTriangle()}.
	 */
	public abstract String anUndirectedTriangleHttpURL();
	
// Test
	
	@Test
	public void test_Access_ReadAll_Reader()
	{
		try
		{
			input.addSink( graph );
			input.readAll( new StringReader( anUndirectedTriangle() ) );
			undirectedTriangleTests();
		}
		catch( IOException e )
		{
			assertTrue( "IOException, should not happen" + e.getMessage(), false );
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_Access_ByStep_Reader()
	{
		try
		{
			input.addSink( graph );
			input.begin( new StringReader( anUndirectedTriangle() ) );
			while( input.nextEvents() );
			input.end();

			undirectedTriangleTests();
		}
		catch( IOException e )
		{
			assertTrue( "IOException, should not happen" + e.getMessage(), false );
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_Access_ReadAll_Stream()
	{
		try
		{
			input.addSink( graph );
			input.readAll( new FileInputStream( anUndirectedTriangleFileName() ) );
			undirectedTriangleTests();
		}
		catch( IOException e )
		{
			assertTrue( "IOException, should not happen" + e.getMessage(), false );
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_Access_ReadAll_URL()
	{
		try
		{
			URL url = new URL( anUndirectedTriangleHttpURL() );
			
			URLConnection c = url.openConnection();
			c.setDefaultUseCaches( false );
			
			input.addSink( graph );
			input.readAll( url );
			undirectedTriangleTests();
		}
		catch( IOException e )
		{
			e.printStackTrace();
			assertTrue( "IOException, should not happen" + e.getMessage(), false );
		}		
	}
	
	@Test
	public void test_Access_ReadAll_FileName()
	{
		try
		{
			input.addSink( graph );
			input.readAll( anUndirectedTriangleFileName() );
			undirectedTriangleTests();
		}
		catch( IOException e )
		{
			e.printStackTrace();
			assertTrue( "IOException, should not happen" + e.getMessage(), false );
		}
	}
	
	@Test
	public void test_DirectedTriangle()
	{
		try
		{
			input.addSink( graph );
			input.readAll( new StringReader( aDirectedTriangle() ) );
			directedTriangleTests();
		}
		catch( IOException e )
		{
			e.printStackTrace();
			assertTrue( "IOException, should not happen" + e.getMessage(), false );
		}
	}
	
	@Test
	public void test_Attributes()
	{
		try
		{
			input.addSink( graph );
			input.readAll( new StringReader( basicAttributes() ) );
			basicAttributesTests();
		}
		catch( IOException e )
		{
			e.printStackTrace();
			assertTrue( "IOException, should not happen" + e.getMessage(), true );
		}
	}
	
// Test
	
	protected void undirectedTriangleTests()
	{
		assertEquals( 3, graph.getEdgeCount() );
		assertEquals( 3, graph.getNodeCount() );
		assertNotNull( graph.getNode( "A" ) );
		assertNotNull( graph.getNode( "B" ) );
		assertNotNull( graph.getNode( "C" ) );
		
		if( testEdgeIds )
		{
			assertNotNull( graph.getEdge( "AB" ) );
			assertNotNull( graph.getEdge( "BC" ) );
			assertNotNull( graph.getEdge( "CA" ) );
		}
	}
	
	protected void directedTriangleTests()
	{
		assertEquals( 3, graph.getEdgeCount() );
		assertEquals( 3, graph.getNodeCount() );
		
		Node A = graph.getNode( "A" );
		Node B = graph.getNode( "B" );
		Node C = graph.getNode( "C" );
		
		assertNotNull( A );
		assertNotNull( B );
		assertNotNull( C );
		
		if( testEdgeIds )
		{
			Edge AB = graph.getEdge( "AB" );
			Edge BC = graph.getEdge( "BC" );
			Edge CA = graph.getEdge( "CA" );
			
			assertNotNull( AB );
			assertNotNull( BC );
			assertNotNull( CA );
	
			assertTrue( AB.isDirected() );
			assertFalse( BC.isDirected() );
			assertTrue( CA.isDirected() );
			
			assertEquals( "A", AB.getNode0().getId() );
			assertEquals( "B", AB.getNode1().getId() );
			assertEquals( "B", BC.getNode0().getId() );
			assertEquals( "C", BC.getNode1().getId() );
			assertEquals( "A", CA.getNode0().getId() );
			assertEquals( "C", CA.getNode1().getId() );
		}
		
		assertTrue( A.hasEdgeToward( "B" ) );
		assertTrue( A.hasEdgeToward( "C" ) );
		assertTrue( B.hasEdgeToward( "C" ) );
		assertFalse( B.hasEdgeToward( "A" ) );
		assertFalse( C.hasEdgeToward( "A" ) );
		assertTrue( C.hasEdgeToward( "B" ) );
		
		Edge AB = A.getEdgeToward( "B" );
		Edge BC = B.getEdgeToward( "C" );
		Edge CA = A.getEdgeToward( "C" );
		
		assertNotNull( AB );
		assertNotNull( BC );
		assertNotNull( CA );
		
		assertTrue( AB.isDirected() );
		assertFalse( BC.isDirected() );
		assertTrue( CA.isDirected() );
		assertEquals( "B", AB.getNode1().getId() );
		assertEquals( "C", BC.getNode1().getId() );
		assertEquals( "C", CA.getNode1().getId() );
	}
	
	protected void basicAttributesTests()
	{
		assertEquals( 3, graph.getEdgeCount() );
		assertEquals( 3, graph.getNodeCount() );
		
		Node A = graph.getNode( "A" );
		Node B = graph.getNode( "B" );
		Node C = graph.getNode( "C" );
		
		assertNotNull( A );
		assertNotNull( B );
		assertNotNull( C );
		
		assertTrue( A.hasAttribute( "a" ) );
		assertTrue( A.hasAttribute( "b" ) );
		assertTrue( A.hasAttribute( "c" ) );
		assertTrue( B.hasAttribute( "aa" ) );
		assertTrue( B.hasAttribute( "bb" ) );
		assertTrue( B.hasAttribute( "cc" ) );
		assertTrue( C.hasAttribute( "aaa" ) );
		
		assertEquals( 1, A.getAttribute( "a" ) );
		assertEquals( "truc", A.getAttribute( "b" ) );
		assertEquals( "true", A.getAttribute( "c" ) );
		
		assertNotNull( B.getAttribute( "aa" ) );
		assertEquals( "foo", B.getAttribute( "bb" ) );
		assertEquals( "bar", B.getAttribute( "cc" ) );
		
		assertEquals( 1.234, C.getAttribute( "aaa" ) );
	}
}