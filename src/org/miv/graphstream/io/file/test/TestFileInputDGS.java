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

package org.miv.graphstream.io.file.test;

import org.junit.*;
import org.miv.graphstream.graph.implementations.MultiGraph;
import org.miv.graphstream.io2.file.FileInputDGS;

/**
 * Test the file input in DGS format.
 */
public class TestFileInputDGS extends TestFileInputBase
{
// Before
	
	@Before
	public void setUp()
	{
		graph = new MultiGraph();
		input = new FileInputDGS();
	}
	
// Test
	
	@Override
	public String anUndirectedTriangle() { return TEST1_TRIANGLE; }
	
	protected static String TEST1_TRIANGLE = 
		"DGS004\n" +
		"\"test1\" 0 0\n" +
		"an A\n" +
		"an B\n" +
		"an C\n" +
		"ae AB A B\n" +
		"ae BC B C\n" +
		"ae CA C A\n";
	
	@Override
	public String aDirectedTriangle() { return TEST2_DIRECTED_TRIANGLE; }
	
	protected static String TEST2_DIRECTED_TRIANGLE =
		"DGS004\n" +
		"\"test2\" 0 0\n" +
		"an A\n" +
		"an B\n" +
		"an C\n" +
		"ae AB A > B\n" +
		"ae BC B C\n" +
		"ae CA C < A\n";
	
	@Override
	public String basicAttributes() { return TEST3_ATTRIBUTES; }
	
	protected static String TEST3_ATTRIBUTES =
		"DGS004\n" +
		"\"test3\" 0 0\n" +
		"an A a:1 b:\"truc\" c:true\n" +
		"an B aa:1,2,3,4 bb:foo cc:bar\n" +
		"an C aaa=1.234\n" +
		"ae AB A B\n" +
		"ae BC B C\n" +
		"ae CA C A\n";
	
	@Override
	public String anUndirectedTriangleFileName()
	{
		return "src/org/miv/graphstream/io/file/test/data/undirectedTriangle.dgs";		
	}
	
	@Override
	public String anUndirectedTriangleHttpURL()
	{
		return "http://graphstream.sourceforge.net/data/undirectedTriangle.dgs";
	}
}