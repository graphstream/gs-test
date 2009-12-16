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

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.io.file.FileSink;
import org.graphstream.io.file.FileSource;
import org.junit.Before;
import org.junit.Test;

/**
 * Base for tests on descendants of {@link org.graphstream.io.file.FileSink}.
 * 
 * <p>This files does all the tests. To implement a test for a specific file
 * format, you have only to implement/override two methods :
 * <ul>
 * 		<li>Override the {@link #aTemporaryGraphFileName()} method that will return the
 * 			name of a file with the correct extension for the file format.</li>
 * 		<li>Implement the {@link #setup()} method that
 * 			initialise the {@link #input} and {@link #output} fields. These
 * 			fields contain an instance of the {@link org.graphstream.io.file.FileSink}
 * 			you want to test and the corresponding {@link org.graphstream.io.file.FileSource}
 * 			for reading back the results of an output and test it.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Some tests may not be appropriate for some graph formats :
 * <ul>
 * 		<li>If your graph format does not support edge identifiers, set {@link #formatHandlesEdgesIDs}
 * 			to false.</li>
 * 		<li>If your graph format does not support dynamic graphs, set {@link #formatHandleDynamics}
 * 			to false.</li>
 * 		<li>If your graph format does not support attributes, set {@link #formatHandlesAttributes}
 * 			to false.</li>
 * </ul>
 * By default all these settings are set to true. You can change them in the {@link #setup()}
 * method.
 * </p>
 */
public abstract class TestFileSinkBase
{
// Attribute
	
	protected Graph outGraph;
	protected Graph inGraph;
	protected FileSource input;
	protected FileSink output;
	
	protected boolean formatHandlesEdgesIDs = true;
	protected boolean formatHandlesAttributes = true;
	protected boolean formatHandleDynamics = true;
	
// To implement or override
	
	/**
	 * Method to implement to create the {@link #input} and {@link #output} fields. These fields
	 * contain the instance of the {@link org.graphstream.io.file.FileSource} and
	 * {@link org.graphstream.io.file.FileSink} to test.
	 */
	@Before
	public abstract void setup();
	
	/**
	 * Return the name of a graph file in the current graph output format. The name of the file
	 * must remain the same.
	 */
	protected abstract String aTemporaryGraphFileName();

// Test
	
	@Before
	public void setup2()
	{
		outGraph = new MultiGraph( "out" );
		inGraph  = new MultiGraph( "in" );
	}
	
	@Test
	public void test_UndirectedTriangle_WriteAll_FileName()
	{
		createUndirectedTriangle();
		
		try
		{
			output.writeAll( outGraph, aTemporaryGraphFileName() );
			input.addGraphListener( inGraph );
			input.readAll( aTemporaryGraphFileName() );
			removeFile( aTemporaryGraphFileName() );
			testUndirectedTriangle();
		}
		catch( IOException e )
		{
			e.printStackTrace();
			assertTrue( "Should not happen !", false );
		}
	}
	
	@Test
	public void test_UndirectedTriangle_WriteAll_Stream()
	{
		createUndirectedTriangle();
		
		try
		{
			output.writeAll( outGraph, new FileOutputStream( aTemporaryGraphFileName() ) );
			input.addGraphListener( inGraph );
			input.readAll( aTemporaryGraphFileName() );
			removeFile( aTemporaryGraphFileName() );
			testUndirectedTriangle();
		}
		catch( IOException e )
		{
			e.printStackTrace();
			assertTrue( "Should not happen !", false );
		}		
	}
	
	@Test
	public void test_UndirectedTriangle_ByEvent()
	{
		try
		{
			output.begin( aTemporaryGraphFileName() );
			output.nodeAdded( "?", 1, "A" );
			output.nodeAdded( "?", 2, "B" );
			output.nodeAdded( "?", 3, "C" );
			output.edgeAdded( "?", 4, "AB", "A", "B", false );
			output.edgeAdded( "?", 5, "BC", "B", "C", false );
			output.edgeAdded( "?", 6, "CA", "C", "A", false );
			output.end();

			input.addGraphListener( inGraph );
			input.readAll( aTemporaryGraphFileName() );
			removeFile( aTemporaryGraphFileName() );
			testUndirectedTriangle();
		}
		catch( IOException e )
		{
			e.printStackTrace();
			assertTrue( "Should not happen !", false );			
		}
	}

	@Test
	public void test_DirectedTriangle()
	{
		createDirectedTriangle();
		
		try
		{
			output.writeAll( outGraph, new FileOutputStream( aTemporaryGraphFileName() ) );
			input.addGraphListener( inGraph );
			input.readAll( aTemporaryGraphFileName() );
			removeFile( aTemporaryGraphFileName() );
			testDirectedTriangle();
		}
		catch( IOException e )
		{
			e.printStackTrace();
			assertTrue( "Should not happen !", false );
		}				
	}
	
	@Test
	public void test_Attributes()
	{
		if( formatHandlesAttributes )
		{
			createAttributedTriangle();
			
			try
			{
				output.writeAll( outGraph, new FileOutputStream( aTemporaryGraphFileName() ) );
				input.addGraphListener( inGraph );
				input.readAll( aTemporaryGraphFileName() );
				removeFile( aTemporaryGraphFileName() );
				testAttributedTriangle();
			}
			catch( IOException e )
			{
				e.printStackTrace();
				assertTrue( "Should not happen !", false );
			}							
		}
	}
	
	@Test
	public void test_Dynamic()
	{
		if( formatHandleDynamics )
		{
			try
			{
				output.begin( new FileOutputStream( aTemporaryGraphFileName() ) );
				outGraph.addGraphListener( output );
				outGraph.stepBegins( 0 );
				outGraph.addNode( "A" );
				outGraph.addNode( "B" );
				outGraph.addNode( "C" );
				outGraph.stepBegins( 1 );
				outGraph.addEdge( "AB", "A", "B" );
				outGraph.addEdge( "BC", "B", "C" );
				outGraph.addEdge( "CA", "C", "A" );
				outGraph.stepBegins( 2 );
				outGraph.addAttribute( "a", 1 );
				outGraph.addAttribute( "b", "foo" );
				outGraph.getNode("A").addAttribute( "a", 1 );
				outGraph.getNode("B").addAttribute( "b", "foo" );
				outGraph.getNode("C").addAttribute( "c", "bar" );
				outGraph.stepBegins( 3 );
				outGraph.removeNode( "A" );
				outGraph.stepBegins( 4 );
				outGraph.removeEdge( "BC" );
				output.end();
				
				input.addGraphListener( inGraph );
				input.begin( aTemporaryGraphFileName() );
				testDynamicTriangleStep0();
				input.nextStep();
				testDynamicTriangleStep0_1();
				input.nextStep();
				testDynamicTriangleStep1_2();
				input.nextStep();
				testDynamicTriangleStep2_3();
				input.nextStep();
				testDynamicTriangleStep3_4();
				input.nextStep();
				testDynamicTriangleStep4();
				input.end();
				removeFile( aTemporaryGraphFileName() );
			}
			catch( IOException e )
			{
				e.printStackTrace();
				assertTrue( "Should not happen !", false );
			}							
		}
	}

	/**
	 * Create a simple undirected graph triangle (A--B--C--A).
	 */
	protected void createUndirectedTriangle()
	{
		outGraph.addNode( "A" );
		outGraph.addNode( "B" );
		outGraph.addNode( "C" );
		outGraph.addEdge( "AB", "A", "B", false );
		outGraph.addEdge( "BC", "B", "C", false );
		outGraph.addEdge( "CA", "C", "A", false );
	}
	
	/**
	 * Create a directed triangle (A->B--C<-A).
	 */
	protected void createDirectedTriangle()
	{
		outGraph.addNode( "A" );
		outGraph.addNode( "B" );
		outGraph.addNode( "C" );
		outGraph.addEdge( "AB", "A", "B", true );
		outGraph.addEdge( "BC", "B", "C", false );
		outGraph.addEdge( "CA", "A", "C", true );		
	}
	
	protected void createAttributedTriangle()
	{
		outGraph.addNode( "A" );
		outGraph.addNode( "B" );
		outGraph.addNode( "C" );
		outGraph.addEdge( "AB", "A", "B", true );
		outGraph.addEdge( "BC", "B", "C", false );
		outGraph.addEdge( "CA", "A", "C", true );
		outGraph.addAttribute( "a", 1 );
		outGraph.addAttribute( "b", "foo" );
		outGraph.getNode( "A" ).addAttribute( "a", 1 );
		outGraph.getNode( "B" ).addAttribute( "b", "foo" );
		outGraph.getNode( "C" ).addAttribute( "c", "bar" );
	}
	
	protected void testUndirectedTriangle()
	{
		assertEquals( 3, inGraph.getNodeCount() );
		assertEquals( 3, inGraph.getEdgeCount() );
		
		Node A = inGraph.getNode( "A" );
		Node B = inGraph.getNode( "B" );
		Node C = inGraph.getNode( "C" );
		
		assertNotNull( A );
		assertNotNull( B );
		assertNotNull( C );
		
		if( formatHandlesEdgesIDs )
		{
			assertNotNull( inGraph.getEdge( "AB" ) );
			assertNotNull( inGraph.getEdge( "BC" ) );
			assertNotNull( inGraph.getEdge( "CA" ) );			
		}
		
		assertTrue( A.hasEdgeToward( "B" ) );
		assertTrue( B.hasEdgeToward( "C" ) );
		assertTrue( C.hasEdgeToward( "A" ) );
		assertTrue( A.hasEdgeToward( "C" ) );
		assertTrue( B.hasEdgeToward( "A" ) );
		assertTrue( C.hasEdgeToward( "B" ) );
	}
	
	protected void testDirectedTriangle()
	{
		assertEquals( 3, inGraph.getNodeCount() );
		assertEquals( 3, inGraph.getEdgeCount() );
		
		Node A = inGraph.getNode( "A" );
		Node B = inGraph.getNode( "B" );
		Node C = inGraph.getNode( "C" );
		
		assertNotNull( A );
		assertNotNull( B );
		assertNotNull( C );
		
		assertTrue( A.hasEdgeToward( "B" ) );
		assertTrue( A.hasEdgeToward( "C" ) );
		assertFalse( B.hasEdgeToward( "A" ) );
		assertTrue( B.hasEdgeToward( "C" ) );
		assertFalse( C.hasEdgeToward( "A" ) );
		assertTrue( C.hasEdgeToward( "B" ) );

		Edge AB = A.getEdgeToward( "B" );
		Edge BC = B.getEdgeToward( "C" );
		Edge CA = A.getEdgeToward( "C" );
		
		assertTrue( AB.isDirected() );
		assertFalse( BC.isDirected() );
		assertTrue( CA.isDirected() );
	}
	
	protected void testAttributedTriangle()
	{
		assertEquals( 3, inGraph.getNodeCount() );
		assertEquals( 3, inGraph.getEdgeCount() );
		
		Node A = inGraph.getNode( "A" );
		Node B = inGraph.getNode( "B" );
		Node C = inGraph.getNode( "C" );
		
		assertNotNull( A );
		assertNotNull( B );
		assertNotNull( C );

		assertEquals( 1, inGraph.getAttribute( "a" ) );
		assertEquals( "foo", inGraph.getAttribute( "b" ) );
		
		assertEquals( 1, A.getAttribute( "a" ) );
		assertEquals( "foo", B.getAttribute( "b" ) );
		assertEquals( "bar", C.getAttribute( "c" ) );
	}
	
	protected void testDynamicTriangleStep0()
	{
		assertEquals( 0, inGraph.getNodeCount() );
		assertEquals( 0, inGraph.getEdgeCount() );
	}
	
	protected void testDynamicTriangleStep0_1()
	{
		assertEquals( 3, inGraph.getNodeCount() );
		assertEquals( 0, inGraph.getEdgeCount() );
		
		Node A = inGraph.getNode( "A" );
		Node B = inGraph.getNode( "B" );
		Node C = inGraph.getNode( "C" );
		
		assertNotNull( A );
		assertNotNull( B );
		assertNotNull( C );
		
		assertEquals( 0, A.getAttributeCount() );
		assertEquals( 0, B.getAttributeCount() );
		assertEquals( 0, C.getAttributeCount() );
	}
	
	protected void testDynamicTriangleStep1_2()
	{
		assertEquals( 3, inGraph.getNodeCount() );
		assertEquals( 3, inGraph.getEdgeCount() );

		Node A = inGraph.getNode( "A" );
		Node B = inGraph.getNode( "B" );
		Node C = inGraph.getNode( "C" );
		
		assertNotNull( A );
		assertNotNull( B );
		assertNotNull( C );
		
		assertTrue( A.hasEdgeToward( "B" ) );
		assertTrue( A.hasEdgeToward( "C" ) );
		assertTrue( B.hasEdgeToward( "A" ) );
		assertTrue( B.hasEdgeToward( "C" ) );
		assertTrue( C.hasEdgeToward( "A" ) );
		assertTrue( C.hasEdgeToward( "B" ) );
	}
	
	protected void testDynamicTriangleStep2_3()
	{
		assertEquals( 3, inGraph.getNodeCount() );
		assertEquals( 3, inGraph.getEdgeCount() );
		
		Node A = inGraph.getNode( "A" );
		Node B = inGraph.getNode( "B" );
		Node C = inGraph.getNode( "C" );
		
		assertNotNull( A );
		assertNotNull( B );
		assertNotNull( C );
		
		assertTrue( inGraph.hasAttribute( "a" ) );
		assertTrue( inGraph.hasAttribute( "b" ) );
		assertTrue( A.hasAttribute( "a" ) );
		assertTrue( B.hasAttribute( "b" ) );
		assertTrue( C.hasAttribute( "c" ) );
		
		assertEquals( 1, inGraph.getAttribute( "a" ) );
		assertEquals( "foo", inGraph.getAttribute( "b" ) );
		assertEquals( 1, A.getAttribute( "a" ) );
		assertEquals( "foo", B.getAttribute( "b" ) );
		assertEquals( "bar", C.getAttribute( "c" ) );
	}

	protected void testDynamicTriangleStep3_4()
	{
		assertEquals( 2, inGraph.getNodeCount() );
		assertEquals( 1, inGraph.getEdgeCount() );
		
		Node A = inGraph.getNode( "A" );
		Node B = inGraph.getNode( "B" );
		Node C = inGraph.getNode( "C" );
		
		assertNull( A );
		assertNotNull( B );
		assertNotNull( C );

		assertFalse( B.hasEdgeToward( "A" ) );
		assertTrue( B.hasEdgeToward( "C" ) );
		assertFalse( C.hasEdgeToward( "A" ) );
		assertTrue( C.hasEdgeToward( "B" ) );
	}

	protected void testDynamicTriangleStep4()
	{
		assertEquals( 2, inGraph.getNodeCount() );
		assertEquals( 0, inGraph.getEdgeCount() );
		
		Node A = inGraph.getNode( "A" );
		Node B = inGraph.getNode( "B" );
		Node C = inGraph.getNode( "C" );
		
		assertNull( A );
		assertNotNull( B );
		assertNotNull( C );

		assertFalse( B.hasEdgeToward( "A" ) );
		assertFalse( B.hasEdgeToward( "C" ) );
		assertFalse( C.hasEdgeToward( "A" ) );
		assertFalse( C.hasEdgeToward( "B" ) );		
	}
	
	protected void removeFile( String fileName )
	{
		File file = new File( aTemporaryGraphFileName() );
		boolean ok = file.delete();
		
		if( ! ok )
			System.err.printf( "Cannot remove file %s !!%n", fileName );
	}
}