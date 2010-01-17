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

package org.graphstream.stream.file.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.file.FileSourceEdge;
import org.junit.Before;
import org.junit.Test;

public class TestFileSourceEdge extends TestFileSourceBase
{
// Before
	
	@Before
	public void setUp()
	{
		graph = new MultiGraph( "g1" );
		input = new FileSourceEdge();
		testEdgeIds = false;
	}
	
	public static void main( String args[] )
	{
		TestFileSourceEdge fid = new TestFileSourceEdge();
		
		fid.setUp();
		fid.test_Access_ReadAll_Stream();
	}
	
// Test
	
	@Override
	public String anUndirectedTriangle() { return TEST1_TRIANGLE; }
	
	protected static String TEST1_TRIANGLE =
		"A B\n" +
		"B C\n" +
		"C A\n";
	
	@Override
	public String aDirectedTriangle() { return TEST2_DIRECTED_TRIANGLE; }
	
	protected static String TEST2_DIRECTED_TRIANGLE =
		"A B\n" +
		"B C\n" +
		"A C\n";
	
	@Override
	public String basicAttributes() { return ""; }

	@Test
	@Override
	@SuppressWarnings("deprecation")
	public void test_DirectedTriangle()
	{
		input = new FileSourceEdge( true );
		
		try
		{
			input.addSink( graph );
			input.readAll( new StringReader( aDirectedTriangle() ) );
			graph.getNode("B").getEdgeToward("C").setDirected( false );
			directedTriangleTests();
		}
		catch( IOException e )
		{
			e.printStackTrace();
			assertTrue( "IOException, should not happen" + e.getMessage(), false );
		}
	}
	
	@Test
	@Override
	public void test_Attributes()
	{
		// NOP, edge format does not allow attributes.
	}
	
	@Override
	public String anUndirectedTriangleFileName()
	{
		return "src/org/graphstream/stream/file/test/data/undirectedTriangle.edge";		
	}
	
	@Override
	public String anUndirectedTriangleHttpURL()
	{
		return "http://graphstream.sourceforge.net/data/undirectedTriangle.edge";
	}
}