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

package org.miv.graphstream.ui.graphicGraph.parser.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.miv.graphstream.graph.Edge;
import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.Node;
import org.miv.graphstream.graph.implementations.DefaultGraph;
import org.miv.graphstream.ui2.graphicGraph.StyleGroup;
import org.miv.graphstream.ui2.graphicGraph.StyleGroupSet;
import org.miv.graphstream.ui2.graphicGraph.stylesheet.Rule;
import org.miv.graphstream.ui2.graphicGraph.stylesheet.Style;
import org.miv.graphstream.ui2.graphicGraph.stylesheet.StyleSheet;

import org.junit.* ;
import static org.junit.Assert.* ;

/**
 * Run several tests on the style sheet package. 
 * 
 * @author Antoine Dutot
 */
public class TestStyleSheet
{
	public static void main( String args[] )
	{
		// Instead of JUnit, we can use this main() :
		
		TestStyleSheet tss = new TestStyleSheet();
		tss.setUp();
		tss.testInitialParsing();
		tss.testRuleQuery();
		tss.testStyleGroups();
		tss.testStyleEvents();
	}

	Graph graph;
	Node A, B, C, D;
	Edge AB, BC, CD, DA;
	StyleSheet stylesheet;
	
	@Before
	public void setUp()
	{
		graph = new DefaultGraph();
		
		A  = graph.addNode( "A" );
		B  = graph.addNode( "B" );
		C  = graph.addNode( "C" );
		D  = graph.addNode( "D" );
		AB = graph.addEdge( "AB", "A", "B" );
		BC = graph.addEdge( "BC", "B", "C" );
		CD = graph.addEdge( "CD", "C", "D" );
		DA = graph.addEdge( "DA", "D", "A" );
		
		B.addAttribute( "ui.class", "foo" );
		C.addAttribute( "ui.class", "foo" );
		D.addAttribute( "ui.class", "bar", "foo" );
		
		AB.addAttribute( "ui.class", "foo" );
		BC.addAttribute( "ui.class", "foo" );

		stylesheet = new StyleSheet();
		try { stylesheet.parseFromString( styleSheet1 ); } catch( IOException e ) {}
	}
	
	@Test
	public void testInitialParsing()
	{
		assertTrue( stylesheet.getGraphStyleNameSpace().getIdRulesCount()    == 0 );
		assertTrue( stylesheet.getGraphStyleNameSpace().getClassRulesCount() == 0 );
		
		assertTrue( stylesheet.getNodeStyleNameSpace().getIdRulesCount()    == 1 );
		assertTrue( stylesheet.getNodeStyleNameSpace().getClassRulesCount() == 2 );

		assertTrue( stylesheet.getEdgeStyleNameSpace().getIdRulesCount()    == 1 );
		assertTrue( stylesheet.getEdgeStyleNameSpace().getClassRulesCount() == 0 );

		assertTrue( stylesheet.getSpriteStyleNameSpace().getIdRulesCount()    == 0 );
		assertTrue( stylesheet.getSpriteStyleNameSpace().getClassRulesCount() == 0 );
	}

	@Test
	public void testRuleQuery()
	{
        ArrayList<Rule> rulesA = stylesheet.getRulesFor( A );
        String          idA    = stylesheet.getStyleGroupIdFor( A, rulesA );
        ArrayList<Rule> rulesB = stylesheet.getRulesFor( B );
        String          idB    = stylesheet.getStyleGroupIdFor( B, rulesB );
        ArrayList<Rule> rulesC = stylesheet.getRulesFor( C );
        String          idC    = stylesheet.getStyleGroupIdFor( C, rulesC );
        ArrayList<Rule> rulesD = stylesheet.getRulesFor( D );
        String          idD    = stylesheet.getStyleGroupIdFor( D, rulesD );

        ArrayList<Rule> rulesAB = stylesheet.getRulesFor( AB );
        String          idAB    = stylesheet.getStyleGroupIdFor( AB, rulesAB );
        ArrayList<Rule> rulesBC = stylesheet.getRulesFor( BC );
        String          idBC    = stylesheet.getStyleGroupIdFor( BC, rulesBC );
        ArrayList<Rule> rulesCD = stylesheet.getRulesFor( CD );
        String          idCD    = stylesheet.getStyleGroupIdFor( CD, rulesCD );
        ArrayList<Rule> rulesDA = stylesheet.getRulesFor( DA );
        String          idDA    = stylesheet.getStyleGroupIdFor( DA, rulesDA );
        
        assertTrue( idA.equals( "n_A" ) );
        assertTrue( idB.equals( "n(foo)" ) );
        assertTrue( idC.equals( "n(foo)" ) );
        assertTrue( idD.equals( "n(bar,foo)" ) );
        assertTrue( idAB.equals( "e_AB" ) );
        assertTrue( idBC.equals( "e" ) );
        assertTrue( idCD.equals( "e" ) );
        assertTrue( idDA.equals( "e" ) );
    
        System.err.printf( "----%n" );
        System.err.printf( "A %s%n", displayGroup( idA, rulesA ) );
        System.err.printf( "B %s%n", displayGroup( idB, rulesB ) );
        System.err.printf( "C %s%n", displayGroup( idC, rulesC ) );
        System.err.printf( "D %s%n", displayGroup( idD, rulesD ) );
        System.err.printf( "----%n" );
        System.err.printf( "AB %s%n", displayGroup( idAB, rulesAB ) );
        System.err.printf( "BC %s%n", displayGroup( idBC, rulesBC ) );
        System.err.printf( "CD %s%n", displayGroup( idCD, rulesCD ) );
        System.err.printf( "DA %s%n", displayGroup( idDA, rulesDA ) );
	}
	
	@Test
	public void testStyleGroups()
	{
        StyleGroupSet sgs = new StyleGroupSet( stylesheet );

        populateGroupSet( sgs );

        System.err.printf( "There are %d groups !!%n", sgs.getGroupCount() );
        Iterator<?extends StyleGroup> i = sgs.getGroupIterator();
        while( i.hasNext() )
        	System.err.printf( "  %s", i.next().toString() );
        
        assertTrue( sgs.getGroupCount() == 6 );
        
        System.err.printf( "----%n" );
        System.err.printf( sgs.toString() );
        
        Style sG = sgs.getStyleForElement( graph );
        Style sA = sgs.getStyleForElement( A );
        Style sB = sgs.getStyleForElement( B );
        Style sC = sgs.getStyleForElement( C );
        Style sD = sgs.getStyleForElement( D );
        
        Style sAB = sgs.getStyleForElement( AB );
        Style sBC = sgs.getStyleForElement( BC );
        Style sCD = sgs.getStyleForElement( CD );
        Style sDA = sgs.getStyleForElement( DA );

        assertTrue( sG.getColor().getRed() == 255 && sG.getColor().getGreen() ==   0 && sG.getColor().getBlue() ==   0 );
        assertTrue( sA.getColor().getRed() == 255 && sA.getColor().getGreen() ==   0 && sA.getColor().getBlue() == 255 );
        assertTrue( sB.getColor().getRed() == 255 && sB.getColor().getGreen() == 165 && sB.getColor().getBlue() ==   0 );
        assertTrue( sC.getColor().getRed() == 255 && sC.getColor().getGreen() == 165 && sC.getColor().getBlue() ==   0 );
        assertTrue( sD.getColor().getRed() == 190 && sD.getColor().getGreen() == 190 && sD.getColor().getBlue() == 190 );
        
        assertTrue( sA.getBorderWidth().value == 0 );
        assertTrue( sB.getBorderWidth().value == 0 );
        assertTrue( sC.getBorderWidth().value == 0 );
        assertTrue( sD.getBorderWidth().value == 0 );

        assertTrue( sAB.getColor().getRed() == 255 && sAB.getColor().getGreen() == 255 && sAB.getColor().getBlue() ==   0 );
        assertTrue( sBC.getColor().getRed() ==   0 && sBC.getColor().getGreen() == 255 && sBC.getColor().getBlue() ==   0 );
        assertTrue( sCD.getColor().getRed() ==   0 && sCD.getColor().getGreen() == 255 && sCD.getColor().getBlue() ==   0 );
        assertTrue( sDA.getColor().getRed() ==   0 && sDA.getColor().getGreen() == 255 && sDA.getColor().getBlue() ==   0 );
        
        sgs.release();
	}
	
	protected String displayGroup( String id, ArrayList<Rule> rules )
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append( "(" );
		builder.append( id );
		builder.append( ") " );
		builder.append( rules.size() );
		builder.append( " [ " );
		for( int i=0; i<rules.size(); ++i )
		{
			builder.append( rules.get(i).selector.toString() );
			builder.append( " " );
		}
		builder.append( "]" );
		return builder.toString();
	}
	
	@Test
	public void testStyleEvents()
	{
        StyleGroupSet sgs = new StyleGroupSet( stylesheet );
        
        populateGroupSet( sgs );

        Style sA = sgs.getStyleForElement( A );
        Style sB = sgs.getStyleForElement( B );
        Style sC = sgs.getStyleForElement( C );
        Style sD = sgs.getStyleForElement( D );
        
        assertTrue( sA.getBorderWidth().value == 0 );
        assertTrue( sB.getBorderWidth().value == 0 );
        assertTrue( sC.getBorderWidth().value == 0 );
        assertTrue( sD.getBorderWidth().value == 0 );
       
        sgs.pushEvent( "clicked" );

        sA = sgs.getStyleForElement( A );
        sB = sgs.getStyleForElement( B );
        sC = sgs.getStyleForElement( C );
        sD = sgs.getStyleForElement( D );
        
        assertTrue( sA.getBorderWidth().value == 2 );
        assertTrue( sB.getBorderWidth().value == 3 );
        assertTrue( sC.getBorderWidth().value == 3 );
        assertTrue( sD.getBorderWidth().value == 3 );
        
        sgs.popEvent( "clicked" );

        sA = sgs.getStyleForElement( A );
        sB = sgs.getStyleForElement( B );
        sC = sgs.getStyleForElement( C );
        sD = sgs.getStyleForElement( D );
        
        assertTrue( sA.getBorderWidth().value == 0 );
        assertTrue( sB.getBorderWidth().value == 0 );
        assertTrue( sC.getBorderWidth().value == 0 );
        assertTrue( sD.getBorderWidth().value == 0 );
        
        sgs.pushEvent( "clicked" );
        sgs.pushEvent( "selected" );

        sA = sgs.getStyleForElement( A );
        sB = sgs.getStyleForElement( B );
        sC = sgs.getStyleForElement( C );
        sD = sgs.getStyleForElement( D );
        
        assertTrue( sA.getBorderWidth().value == 4 );
        assertTrue( sB.getBorderWidth().value == 3 );
        assertTrue( sC.getBorderWidth().value == 3 );
        assertTrue( sD.getBorderWidth().value == 3 );
        
        sgs.release();
	}
	
	@Test
	public void testStyleChange()
		throws IOException
	{
        StyleGroupSet sgs = new StyleGroupSet( stylesheet );
        
        populateGroupSet( sgs );

        assertTrue( sgs.getGroupCount() == 6 );
        
		// Augment the style sheet a new style sheet that change an existing style.
		
		stylesheet.parseFromString( styleSheet2 );

        assertTrue( sgs.getGroupCount() == 6 );
		assertTrue( stylesheet.getGraphStyleNameSpace().getIdRulesCount()     == 0 );
		assertTrue( stylesheet.getGraphStyleNameSpace().getClassRulesCount()  == 0 );
		assertTrue( stylesheet.getNodeStyleNameSpace().getIdRulesCount()      == 1 );
		assertTrue( stylesheet.getNodeStyleNameSpace().getClassRulesCount()   == 2 );
		assertTrue( stylesheet.getEdgeStyleNameSpace().getIdRulesCount()      == 1 );
		assertTrue( stylesheet.getEdgeStyleNameSpace().getClassRulesCount()   == 0 );
		assertTrue( stylesheet.getSpriteStyleNameSpace().getIdRulesCount()    == 0 );
		assertTrue( stylesheet.getSpriteStyleNameSpace().getClassRulesCount() == 0 );
		
		// All nodes should have a border of 10px except the clicked ones.
        
		Style sA = sgs.getStyleForElement( A );
        Style sB = sgs.getStyleForElement( B );
        Style sC = sgs.getStyleForElement( C );
        Style sD = sgs.getStyleForElement( D );
        
        assertTrue( sA.getBorderWidth().value == 10 );
        assertTrue( sB.getBorderWidth().value == 10 );
        assertTrue( sC.getBorderWidth().value == 10 );
        assertTrue( sD.getBorderWidth().value == 10 );
        
        sgs.pushEvent( "clicked" );
		sA = sgs.getStyleForElement( A );
        sB = sgs.getStyleForElement( B );
        sC = sgs.getStyleForElement( C );
        sD = sgs.getStyleForElement( D );

        assertTrue( sA.getBorderWidth().value == 2 );
        assertTrue( sB.getBorderWidth().value == 3 );
        assertTrue( sC.getBorderWidth().value == 3 );
        assertTrue( sD.getBorderWidth().value == 3 );
        
        sgs.popEvent( "clicked" );
		sA = sgs.getStyleForElement( A );
        sB = sgs.getStyleForElement( B );
        sC = sgs.getStyleForElement( C );
        sD = sgs.getStyleForElement( D );
        
        assertTrue( sA.getBorderWidth().value == 10 );
        assertTrue( sB.getBorderWidth().value == 10 );
        assertTrue( sC.getBorderWidth().value == 10 );
        assertTrue( sD.getBorderWidth().value == 10 );
        
        // Now augment the style sheet with a change that applies only to node B.
        
        stylesheet.parseFromString( styleSheet3 );

        assertTrue( sgs.getGroupCount() == 7 );
		assertTrue( stylesheet.getGraphStyleNameSpace().getIdRulesCount()     == 0 );
		assertTrue( stylesheet.getGraphStyleNameSpace().getClassRulesCount()  == 0 );
		assertTrue( stylesheet.getNodeStyleNameSpace().getIdRulesCount()      == 2 ); // <-- +1
		assertTrue( stylesheet.getNodeStyleNameSpace().getClassRulesCount()   == 2 );
		assertTrue( stylesheet.getEdgeStyleNameSpace().getIdRulesCount()      == 1 );
		assertTrue( stylesheet.getEdgeStyleNameSpace().getClassRulesCount()   == 0 );
		assertTrue( stylesheet.getSpriteStyleNameSpace().getIdRulesCount()    == 0 );
		assertTrue( stylesheet.getSpriteStyleNameSpace().getClassRulesCount() == 0 );
		
		sA = sgs.getStyleForElement( A );
        sB = sgs.getStyleForElement( B );
        sC = sgs.getStyleForElement( C );
        sD = sgs.getStyleForElement( D );
        
        assertTrue( sA.getBorderWidth().value == 10 );
        assertTrue( sB.getBorderWidth().value ==  5 );	// <-- The specific style changed.
        assertTrue( sC.getBorderWidth().value == 10 );
        assertTrue( sD.getBorderWidth().value == 10 );
        
        // Now augment the style sheet with a change that applies to all edges with the ".foo" class.
        
        stylesheet.parseFromString( styleSheet4 );
        
        assertTrue( sgs.getGroupCount() == 8 );	// (e_AB disappears, e_AB(foo) and e(foo) appear)
		assertTrue( stylesheet.getGraphStyleNameSpace().getIdRulesCount()     == 0 );
		assertTrue( stylesheet.getGraphStyleNameSpace().getClassRulesCount()  == 0 );
		assertTrue( stylesheet.getNodeStyleNameSpace().getIdRulesCount()      == 2 );
		assertTrue( stylesheet.getNodeStyleNameSpace().getClassRulesCount()   == 2 );
		assertTrue( stylesheet.getEdgeStyleNameSpace().getIdRulesCount()      == 1 );
		assertTrue( stylesheet.getEdgeStyleNameSpace().getClassRulesCount()   == 1 ); // <-- +1
		assertTrue( stylesheet.getSpriteStyleNameSpace().getIdRulesCount()    == 0 );
		assertTrue( stylesheet.getSpriteStyleNameSpace().getClassRulesCount() == 0 );
		
		Style sAB = sgs.getStyleForElement( AB );
        Style sBC = sgs.getStyleForElement( BC );
        Style sCD = sgs.getStyleForElement( CD );
        Style sDA = sgs.getStyleForElement( DA );
        
        assertTrue( sAB.getBorderWidth().value == 1 );
        assertTrue( sBC.getBorderWidth().value == 1 );
        assertTrue( sCD.getBorderWidth().value == 0 );
        assertTrue( sDA.getBorderWidth().value == 0 );
     
        System.err.printf( "After adding new style sheets, there are %d groups !!%n", sgs.getGroupCount() );
        Iterator<?extends StyleGroup> i = sgs.getGroupIterator();
        while( i.hasNext() )
        	System.err.printf( "  %s", i.next().toString() );
        
        sgs.release();
	}
	
	@Test
	public void testZIndex()
		throws IOException
	{
		StyleGroupSet sgs = new StyleGroupSet( stylesheet );
        
        populateGroupSet( sgs );

        assertTrue( sgs.getGroupCount() == 6 );

        // Now test the default Z index
        
        Iterator<HashSet<StyleGroup>> zIndex = sgs.getZIterator();
        
        // The groups we expect in order.
        HashSet<String> groups1 = new HashSet<String>();
        HashSet<String> groups2 = new HashSet<String>();
        HashSet<String> groups3 = new HashSet<String>();
        HashSet<String> groups4 = new HashSet<String>();
        HashSet<String> groups5 = new HashSet<String>();
        groups1.add( "g" );
        groups2.add( "e" ); groups2.add( "e_AB" );
        groups3.add( "n_A" ); groups3.add( "n(foo)" ); groups3.add( "n(bar,foo)" );
        
        System.err.printf( "---- zIndex ----%n" );

        assertTrue( zIndex.hasNext() );
        HashSet<StyleGroup> cell = zIndex.next();
        for( StyleGroup g: cell )
        {
        	assertTrue( groups1.contains( g.getId() ) );
        	assertTrue( g.getZIndex() == 0 );
        }

        assertTrue( zIndex.hasNext() );
        cell = zIndex.next();
        for( StyleGroup g: cell )
        {
        	assertTrue( groups2.contains( g.getId() ) );
        	assertTrue( g.getZIndex() == 1 );
        }

        assertTrue( zIndex.hasNext() );
        cell = zIndex.next();
        for( StyleGroup g: cell )
        {
        	assertTrue( groups3.contains( g.getId() ) );
        	assertTrue( g.getZIndex() == 2 );
        }
        
        assertTrue( ! zIndex.hasNext() );

        System.err.printf( "The Z index is :%n" );
        System.err.printf( "%s", sgs.getZIndex().toString() );
        
        // Now test the way the z-index is kept up to date when changing the style.
        // The change affects styles that already exist.

        System.err.printf( "---- zIndex 2 ----%n" );

        stylesheet.parseFromString( styleSheet5 );

        assertTrue( sgs.getGroupCount() == 6 );

        zIndex = sgs.getZIterator();
        assertTrue( zIndex.hasNext() );
        cell = zIndex.next();
        for( StyleGroup g: cell )
        {
        	assertTrue( groups1.contains( g.getId() ) );
        	assertTrue( g.getZIndex() == 0 );
        }

        assertTrue( zIndex.hasNext() );
        cell = zIndex.next();
        for( StyleGroup g: cell )
        {
        	assertTrue( groups3.contains( g.getId() ) );
        	assertTrue( g.getZIndex() == 1 );
        }

        assertTrue( zIndex.hasNext() );
        cell = zIndex.next();
        for( StyleGroup g: cell )
        {
        	assertTrue( groups2.contains( g.getId() ) );
        	assertTrue( g.getZIndex() == 2 );
        }

        assertTrue( ! zIndex.hasNext() );
        
        System.err.printf( "The Z index is : %n" );
        System.err.printf( "%s", sgs.getZIndex().toString() );
        
        // Now change only one specific (id) style.
        
        System.err.printf( "---- zIndex 3 ----%n" );
        stylesheet.parseFromString( styleSheet6 );
        
        assertTrue( sgs.getGroupCount() == 6 );
        groups2.clear();
        groups3.clear();
        groups2.add( "n_A" );
        groups3.add( "e" ); groups3.add( "e_AB" );
        groups4.add( "n(bar,foo)" ); groups4.add( "n(foo)" );
        
        zIndex = sgs.getZIterator();
        assertTrue( zIndex.hasNext() );
        cell = zIndex.next();
        for( StyleGroup g: cell )
        {
        	assertTrue( groups1.contains( g.getId() ) );
        	assertTrue( g.getZIndex() == 0 );
        }

        assertTrue( zIndex.hasNext() );
        cell = zIndex.next();
        for( StyleGroup g: cell )
        {
        	assertTrue( groups2.contains( g.getId() ) );
        	assertTrue( g.getZIndex() == 1 );
        }

        assertTrue( zIndex.hasNext() );
        cell = zIndex.next();
        for( StyleGroup g: cell )
        {
        	assertTrue( groups3.contains( g.getId() ) );
        	assertTrue( g.getZIndex() == 2 );
        }

        assertTrue( zIndex.hasNext() );
        cell = zIndex.next();
        for( StyleGroup g: cell )
        {
        	assertTrue( groups4.contains( g.getId() ) );
        	assertTrue( g.getZIndex() == 5 );
        }

        assertTrue( ! zIndex.hasNext() );
        
        System.err.printf( "The Z index is : %n" );
        System.err.printf( "%s", sgs.getZIndex().toString() );
        
        // Now add a style with a Z index.s

        System.err.printf( "---- zIndex 4 ----%n" );
        stylesheet.parseFromString( styleSheet7 );
        
        assertTrue( sgs.getGroupCount() == 7 );
        groups5.add( "e_DA" );

        zIndex = sgs.getZIterator();
        assertTrue( zIndex.hasNext() );
        cell = zIndex.next();
        for( StyleGroup g: cell )
        {
        	assertTrue( groups1.contains( g.getId() ) );
        	assertTrue( g.getZIndex() == 0 );
        }

        assertTrue( zIndex.hasNext() );
        cell = zIndex.next();
        for( StyleGroup g: cell )
        {
        	assertTrue( groups2.contains( g.getId() ) );
        	assertTrue( g.getZIndex() == 1 );
        }

        assertTrue( zIndex.hasNext() );
        cell = zIndex.next();
        for( StyleGroup g: cell )
        {
        	assertTrue( groups3.contains( g.getId() ) );
        	assertTrue( g.getZIndex() == 2 );
        }

        assertTrue( zIndex.hasNext() );
        cell = zIndex.next();
        for( StyleGroup g: cell )
        {
        	assertTrue( groups4.contains( g.getId() ) );
        	assertTrue( g.getZIndex() == 5 );
        }

        assertTrue( zIndex.hasNext() );
        cell = zIndex.next();
        for( StyleGroup g: cell )
        {
        	assertTrue( groups5.contains( g.getId() ) );
        	assertTrue( g.getZIndex() == 7 );
        }
        
        System.err.printf( "The Z index is : %n" );
        System.err.printf( "%s", sgs.getZIndex().toString() );
	}
	
	@Test
	public void testShadow()
		throws IOException
	{
        StyleGroupSet sgs = new StyleGroupSet( stylesheet );
        
        populateGroupSet( sgs );

        // First test with the default style sheet, no shadows.
        
        Iterator<StyleGroup> shadow = sgs.getShadowIterator();
        int count = 0;
        
        while( shadow.hasNext() )
        {
        	shadow.next();
        	count ++;
        }
        
        assertTrue( count == 0 );
        
        // Then we add a style that adds shadows to all nodes.
        
        stylesheet.parseFromString( styleSheet8 );
        assertTrue( sgs.getGroupCount() == 6 );
        HashSet<String> groups = new HashSet<String>();
        groups.add( "n_A" ); groups.add( "n(bar,foo)" ); groups.add( "n(foo)" );
        
        shadow = sgs.getShadowIterator();
        count  = 0;
        
        while( shadow.hasNext() )
        {
        	StyleGroup g = shadow.next();
        	assertTrue( groups.contains( g.getId() ) );
        	count++;
        }
        
        assertTrue( count == 3 );	// There are three node groups.
        
        // Then we add a style that adds shadows to a specific edge.
        
        stylesheet.parseFromString( styleSheet9 );
        assertTrue( sgs.getGroupCount() == 6 );
        groups.add( "e_AB" );
       
        shadow = sgs.getShadowIterator();
        count  = 0;
        
        while( shadow.hasNext() )
        {
        	StyleGroup g = shadow.next();
        	assertTrue( groups.contains( g.getId() ) );
        	count++;
        }
        
        assertTrue( count == 4 );	// Three node groups, plus one edge group (e_AB)
	}
	
	protected void populateGroupSet( StyleGroupSet sgs )
	{
        sgs.addElement( graph );
        sgs.addElement( A );
        sgs.addElement( B );
        sgs.addElement( C );
        sgs.addElement( D );
        sgs.addElement( AB );
        sgs.addElement( BC );
        sgs.addElement( CD );
        sgs.addElement( DA );
	}
	
	public static String styleSheet1 = 
		"graph    { color: red;     }" +
		"node     { color: blue;    }" +
		"edge     { color: green;   }" +
		"sprite   { color: cyan;    }" +
		"node#A   { color: magenta; }" +
		"edge#AB  { color: yellow;  }" +
		"node.foo { color: orange;  }" +
		"node.bar { color: grey;    }" +
		"node:clicked     { border-width: 1px; }" +
		"node#A:clicked   { border-width: 2px; }" +
		"node.foo:clicked { border-width: 3px; }" +
		"node#A:selected  { border-width: 4px; }";
	
	public static String styleSheet2 =
		"node { color: yellow; border-width: 10px; }";
	
	public static String styleSheet3 =
		"node#B { border-width: 5px; }";
	
	public static String styleSheet4 = 
		"edge.foo { border-width: 1px; }";
	
	public static String styleSheet5 =
		"node { z-index: 1; }" +
		"edge { z-index: 2; }";
	
	public static String styleSheet6 = 
		"node.foo { z-index: 5; }";
	
	public static String styleSheet7 =
		"edge#DA { z-index: 7; }";
	
	public static String styleSheet8 =
		"node { shadow-style: plain; }";
	
	public static String styleSheet9 =
		"edge#AB { shadow-style: plain; }";
}