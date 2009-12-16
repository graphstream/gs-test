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
import org.graphstream.io.file.FileSourceDOT;
import org.junit.Before;

public class TestFileSourceDOT extends TestFileSourceBase
{
// Before
	
	@Before
	public void setUp()
	{
		graph = new MultiGraph( "g1" );
		input = new FileSourceDOT();
	}
	
	public static void main( String args[] )
	{
		TestFileSourceDOT fid = new TestFileSourceDOT();
		
		fid.setUp();
		fid.test_Access_ReadAll_URL();
	}
	
// Test
	
	@Override
	public String anUndirectedTriangle() { return TEST1_TRIANGLE; }
	
	protected static String TEST1_TRIANGLE = 
		"graph test1 {\n" +
		"    graph [ id=\"test1\" ];\n" +
		"    A -- B [ id=AB ];\n" +
		"    B -- C [ id=BC ];\n" +
		"    C -- A [ id=CA ];\n" +
		"}\n";
	
	@Override
	public String aDirectedTriangle() { return TEST2_DIRECTED_TRIANGLE; }
	
	protected static String TEST2_DIRECTED_TRIANGLE =
		"graph test2 {\n" +
		"    graph [ id=\"test2\" ];\n" +
		"    A -> B [ id=AB ];\n" +
		"    B -- C [ id=BC ];\n" +
		"    A -> C [ id=CA ];\n" +
		"}\n";
	
	@Override
	public String basicAttributes() { return TEST3_ATTRIBUTES; }
	
	protected static String TEST3_ATTRIBUTES =
		"graph test3 {\n" +
		"    graph [ id=\"test3\" ];\n" +
		"    A [ a=1, b=truc, c=true ];\n" +
		"    B [ aa=\"1,2,3,4\", bb=\"foo\", cc=bar ];\n" +
		"    C [ aaa=1.234 ];\n" +
		"" +
		"    A -- B [ id=AB ];\n" +
		"    B -- C [ id=BC ];\n" +
		"    C -- A [ id=CA ];\n" +
		"}\n";
	
	@Override
	public String anUndirectedTriangleFileName()
	{
		return "src/org/graphstream/io/file/test/data/undirectedTriangle.dot";		
	}
	
	@Override
	public String anUndirectedTriangleHttpURL()
	{
		return "http://graphstream.sourceforge.net/data/undirectedTriangle.dot";
	}
}