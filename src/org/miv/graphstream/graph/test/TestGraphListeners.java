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

import java.util.ArrayList;

import org.junit.Test;
import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.GraphListener;
import org.miv.graphstream.graph.implementations.AdjacencyListGraph;
import org.miv.graphstream.graph.implementations.MultiGraph;
import org.miv.graphstream.graph.implementations.SingleGraph;

import static org.junit.Assert.*;

public class TestGraphListeners implements GraphListener
{
	/**
	 * The set of events received, in order.
	 */
	protected ArrayList<Event> events = new ArrayList<Event>();
	
	@Test
	public void testBasicListener()
	{
		testBasicListener( new SingleGraph( "G" ) );
		testBasicListener( new MultiGraph( "G" ) );
		testBasicListener( new AdjacencyListGraph( "G" ) );
	}
	
	protected void testBasicListener( Graph graph )
	{
		events.clear();
		graph.addGraphListener( this );
		
		// Some add events.
		
		graph.addNode( "A" );
		graph.addNode( "B" );
		graph.addNode( "C" );
		
		graph.addEdge( "AB", "A", "B" );
		graph.addEdge( "BC", "B", "C" );
		graph.addEdge( "CA", "C", "A" );
		
		assertEquals( 3, graph.getNodeCount() );
		assertEquals( 3, graph.getEdgeCount() );
		
		assertEquals( 6, events.size() );
		assertEquals( ElementEvent.class, events.get(0).getClass() );
		assertEquals( ElementEvent.class, events.get(1).getClass() );
		assertEquals( ElementEvent.class, events.get(2).getClass() );
		assertEquals( ElementEvent.class, events.get(3).getClass() );
		assertEquals( ElementEvent.class, events.get(4).getClass() );
		assertEquals( ElementEvent.class, events.get(5).getClass() );
		
		assertEquals( Event.ElementType.NODE,     ((ElementEvent)events.get(0)).type );
		assertEquals( ElementEvent.EventType.ADD, ((ElementEvent)events.get(0)).event );
		assertEquals( "G",                        ((ElementEvent)events.get(0)).graphId );
		assertEquals( "A",                        ((ElementEvent)events.get(0)).elementId );
		assertEquals( Event.ElementType.NODE,     ((ElementEvent)events.get(1)).type );
		assertEquals( ElementEvent.EventType.ADD, ((ElementEvent)events.get(1)).event );
		assertEquals( "G",                        ((ElementEvent)events.get(1)).graphId );
		assertEquals( "B",                        ((ElementEvent)events.get(1)).elementId );
		assertEquals( Event.ElementType.NODE,     ((ElementEvent)events.get(2)).type );
		assertEquals( ElementEvent.EventType.ADD, ((ElementEvent)events.get(2)).event );
		assertEquals( "G",                        ((ElementEvent)events.get(2)).graphId );
		assertEquals( "C",                        ((ElementEvent)events.get(2)).elementId );
		
		assertEquals( Event.ElementType.EDGE,     ((ElementEvent)events.get(3)).type );
		assertEquals( ElementEvent.EventType.ADD, ((ElementEvent)events.get(3)).event );
		assertEquals( "G",                        ((ElementEvent)events.get(3)).graphId );
		assertEquals( "AB",                       ((ElementEvent)events.get(3)).elementId );
		assertEquals( "A",                        ((ElementEvent)events.get(3)).from );
		assertEquals( "B",                        ((ElementEvent)events.get(3)).to );
		assertFalse(                              ((ElementEvent)events.get(3)).directed );
		assertEquals( Event.ElementType.EDGE,     ((ElementEvent)events.get(4)).type );
		assertEquals( ElementEvent.EventType.ADD, ((ElementEvent)events.get(4)).event );
		assertEquals( "G",                        ((ElementEvent)events.get(4)).graphId );
		assertEquals( "BC",                       ((ElementEvent)events.get(4)).elementId );
		assertEquals( "B",                        ((ElementEvent)events.get(4)).from );
		assertEquals( "C",                        ((ElementEvent)events.get(4)).to );
		assertFalse(                              ((ElementEvent)events.get(4)).directed );
		assertEquals( Event.ElementType.EDGE,     ((ElementEvent)events.get(5)).type );
		assertEquals( ElementEvent.EventType.ADD, ((ElementEvent)events.get(5)).event );
		assertEquals( "G",                        ((ElementEvent)events.get(5)).graphId );
		assertEquals( "CA",                       ((ElementEvent)events.get(5)).elementId );
		assertEquals( "C",                        ((ElementEvent)events.get(5)).from );
		assertEquals( "A",                        ((ElementEvent)events.get(5)).to );
		assertFalse(                              ((ElementEvent)events.get(5)).directed );
		
		// Some remove events

		events.clear();
		graph.removeNode( "A" );
		
		assertEquals( 3, events.size() );
		assertEquals( ElementEvent.class, events.get(0).getClass() );
		assertEquals( ElementEvent.class, events.get(1).getClass() );
		assertEquals( ElementEvent.class, events.get(2).getClass() );
		assertEquals( ElementEvent.EventType.REMOVE, ((ElementEvent)events.get(0)).event );
		assertEquals( ElementEvent.EventType.REMOVE, ((ElementEvent)events.get(1)).event );
		assertEquals( ElementEvent.EventType.REMOVE, ((ElementEvent)events.get(2)).event );
		
		// Some steps events.
		
		events.clear();
		graph.stepBegins( 1 );
		assertEquals( 1, events.size() );
		assertEquals( ElementEvent.class, events.get(0).getClass() );
		assertEquals( Event.ElementType.GRAPH, ((ElementEvent)events.get(0)).type );
	}

	@Test
	public void testRecursiveListener()
	{
		// TODO
	}
	
	@Test
	public void testAttributeEvents()
	{
		// TODO
	}
	
// Nested classes that represent events.
	
	public static class Event
	{
		public static enum ElementType { GRAPH, NODE, EDGE };
		public String graphId;
		public String elementId;
		public ElementType type;
		public Event( String graphId, String elementId, ElementType type )
		{ this.graphId = graphId; this.elementId = elementId; this.type = type; }
	}
	
	public static class ElementEvent extends Event
	{
		public static enum EventType { ADD, REMOVE, STEP };
		public EventType event;
		public ElementEvent( String graphId, String elementId, ElementType type, EventType event )
		{ super( graphId, elementId, type ); this.event = event; }
		public String from, to;
		public boolean directed;
		public ElementEvent( String graphId, String elementId, ElementType type, EventType event,
				String from, String to, boolean directed )
		{ super( graphId, elementId, type );
		  this.event = event;
		  this.from  = from;
		  this.to    = to;
		  this.directed = directed;
		}
	}
	
	public static class AttributeEvent extends Event
	{
		public static enum EventType { ADD, CHANGE, REMOVE };
		public String attribute;
		public EventType event;
		public Object value;
		public AttributeEvent( String graphId, String elementId, ElementType type, String attribute,
				EventType event, Object value )
		{ super( graphId, elementId, type );
		  this.attribute = attribute;
		  this.event = event;
		  this.value = value;
		}
	}

// Graph Listener
	
	public void edgeAttributeAdded( String graphId, String edgeId, String attribute,
            Object value )
    {
		events.add( new AttributeEvent( graphId, edgeId, Event.ElementType.EDGE, attribute,
				AttributeEvent.EventType.ADD, value) );
    }

	public void edgeAttributeChanged( String graphId, String edgeId, String attribute,
            Object oldValue, Object value )
    {
		events.add( new AttributeEvent( graphId, edgeId, Event.ElementType.EDGE, attribute,
				AttributeEvent.EventType.CHANGE, value) );
    }

	public void edgeAttributeRemoved( String graphId, String edgeId, String attribute )
    {
		events.add( new AttributeEvent( graphId, edgeId, Event.ElementType.EDGE, attribute,
				AttributeEvent.EventType.REMOVE, null ) );
    }

	public void graphAttributeAdded( String graphId, String attribute, Object value )
    {
		events.add( new AttributeEvent( graphId, graphId, Event.ElementType.GRAPH, attribute,
				AttributeEvent.EventType.ADD, value) );
    }

	public void graphAttributeChanged( String graphId, String attribute, Object oldValue, Object value )
    {
		events.add( new AttributeEvent( graphId, graphId, Event.ElementType.GRAPH, attribute,
				AttributeEvent.EventType.CHANGE, value) );
    }

	public void graphAttributeRemoved( String graphId, String attribute )
    {
		events.add( new AttributeEvent( graphId, graphId, Event.ElementType.GRAPH, attribute,
				AttributeEvent.EventType.REMOVE, null) );
    }

	public void nodeAttributeAdded( String graphId, String nodeId, String attribute,
            Object value )
    {
		events.add( new AttributeEvent( graphId, nodeId, Event.ElementType.NODE, attribute,
				AttributeEvent.EventType.ADD, value) );
    }

	public void nodeAttributeChanged( String graphId, String nodeId, String attribute,
            Object oldValue, Object value )
    {
		events.add( new AttributeEvent( graphId, nodeId, Event.ElementType.NODE, attribute,
				AttributeEvent.EventType.CHANGE, value) );
    }

	public void nodeAttributeRemoved( String graphId, String nodeId, String attribute )
    {
		events.add( new AttributeEvent( graphId, nodeId, Event.ElementType.NODE, attribute,
				AttributeEvent.EventType.REMOVE, null) );
    }

	public void edgeAdded( String graphId, String edgeId, String fromNodeId, String toNodeId,
            boolean directed )
    {
		events.add( new ElementEvent( graphId, edgeId, Event.ElementType.EDGE,
				ElementEvent.EventType.ADD, fromNodeId, toNodeId, directed ) );
    }

	public void edgeRemoved( String graphId, String edgeId )
    {
		events.add( new ElementEvent( graphId, edgeId, Event.ElementType.EDGE,
				ElementEvent.EventType.REMOVE ) );
    }

	public void nodeAdded( String graphId, String nodeId )
    {
		events.add( new ElementEvent( graphId, nodeId, Event.ElementType.NODE,
				ElementEvent.EventType.ADD ) );
    }

	public void nodeRemoved( String graphId, String nodeId )
    {
		events.add( new ElementEvent( graphId, nodeId, Event.ElementType.NODE,
				ElementEvent.EventType.REMOVE ) );
    }
	
	public void graphCleared( String graphId )
	{
	}

	public void stepBegins( String graphId, double time )
    {
		events.add( new ElementEvent( graphId, graphId, Event.ElementType.GRAPH,
				ElementEvent.EventType.STEP ) );
    }
}