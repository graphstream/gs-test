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

import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.file.FileSourceGML;
import org.junit.Before;

public class TestFileSourceGML extends TestFileSourceBase
{
// Before
	
	@Before
	public void setUp()
	{
		graph = new MultiGraph( "g1" );
		input = new FileSourceGML();
	}
	
// Test
	
	@Override
	public String anUndirectedTriangle() { return TEST1_TRIANGLE; }
	
	protected static String TEST1_TRIANGLE = 
		"graph [\n" +
		"    id \"test1\"\n" +
		"    node [ id \"A\" ]\n" +
		"    node [ id \"B\" ]\n" +
		"    node [ id \"C\" ]\n" +
		"" +
		"    edge [ id \"AB\" source \"A\" target \"B\" ]\n" +
		"    edge [ id \"BC\" source \"B\" target \"C\" ]\n" +
		"    edge [ id \"CA\" source \"C\" target \"A\" ]\n" +
		"]\n";
	
	@Override
	public String aDirectedTriangle() { return TEST2_DIRECTED_TRIANGLE; }
	
	protected static String TEST2_DIRECTED_TRIANGLE =
		"graph [\n" +
		"    id \"test1\"\n" +
		"    node [ id \"A\" ]\n" +
		"    node [ id \"B\" ]\n" +
		"    node [ id \"C\" ]\n" +
		"" +
		"    edge [ id \"AB\" source \"A\" target \"B\" directed 1 ]\n" +
		"    edge [ id \"BC\" source \"B\" target \"C\" directed 0 ]\n" +
		"    edge [ id \"CA\" source \"A\" target \"C\" directed true ]\n" +
		"]\n";
	
	@Override
	public String basicAttributes() { return TEST3_ATTRIBUTES; }
	
	protected static String TEST3_ATTRIBUTES =
		"graph [\n" +
		"    id \"test1\"\n" +
		"    node [ id \"A\" a 1 b truc c true ]\n" +
		"    node [ id \"B\" aa \"1,2,3,4\" bb \"foo\" cc bar ]\n" +
		"    node [ id \"C\" aaa 1.234 ]\n" +
		"" +
		"    edge [ id \"AB\" source \"A\" target \"B\" ]\n" +
		"    edge [ id \"BC\" source \"B\" target \"C\" ]\n" +
		"    edge [ id \"CA\" source \"C\" target \"A\" ]\n" +
		"]\n";
	
	@Override
	public String anUndirectedTriangleFileName()
	{
		return "src/org/graphstream/io/file/test/data/undirectedTriangle.gml";		
	}
	
	@Override
	public String anUndirectedTriangleHttpURL()
	{
		return "http://graphstream.sourceforge.net/data/undirectedTriangle.gml";
	}
}