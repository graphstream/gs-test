package org.graphstream.graph.test;

import static org.junit.Assert.* ;
import org.junit.Test;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.NodeFactory;
import org.graphstream.graph.Edge;
import org.graphstream.graph.EdgeFactory;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.AdjacencyListGraph;
import org.graphstream.graph.implementations.AdjacencyListNode;
import org.graphstream.graph.implementations.AdjacencyListEdge;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.implementations.SingleNode;
import org.graphstream.graph.implementations.SingleEdge;

public class TestGenericity
{
	protected static class MyALGNode
		extends AdjacencyListNode
	{
		public MyALGNode(Graph graph, String id)
		{
			super(graph, id);
		}
	}
	
	protected static class MyALGNodeFactory
		implements NodeFactory<MyALGNode>
	{
		public MyALGNode newInstance(String id, Graph graph)
		{
			return new MyALGNode(graph,id);
		}
	}
	
	protected static class MyALGEdge
		extends AdjacencyListEdge
	{
		protected MyALGEdge(String id, Node src, Node dst, boolean directed)
		{
			super(id, src, dst, directed);
		}
	}
	
	protected static class MyALGEdgeFactory
		implements EdgeFactory<MyALGEdge>
	{
		public MyALGEdge newInstance(String id, Node src, Node dst,
				boolean directed)
		{
			return new MyALGEdge(id,src,dst,directed);
		}
	}
	
	protected static class MySingleNode
		extends SingleNode
	{
		public MySingleNode(Graph graph, String id)
		{
			super(graph, id);
		}
	}
	
	protected static class MySingleEdge
		extends SingleEdge
	{
		protected MySingleEdge(String id, Node src, Node dst, boolean directed)
		{
			super(id, src, dst, directed);
		}
	}
	
	@Test
	public void checkAdjacencyListGraph()
	{
		Graph g = new AdjacencyListGraph("g");
		
		g.setNodeFactory(new MyALGNodeFactory());
		g.setEdgeFactory(new MyALGEdgeFactory());
		
		Node n;
		MyALGNode m;
		MySingleNode o;
		
		Edge e1;
		MyALGEdge e2;
		MySingleEdge e3;
		
		g.addNode("0");
		n = g.addNode("1");
		m = g.addNode("2");
		
		boolean test = false;
		
		try
		{
			test = true;
			o = g.addNode("3");
		}
		catch( ClassCastException e )
		{
			test = false;
			e.printStackTrace();
		}
		finally
		{
			g.addNode("3");
		}
		
		assertFalse(test);
		
		for( Node tmp : g.getEachNode() )
		{
			
		}
		
		for( MyALGNode tmp : g.<MyALGNode>getEachNode() )
		{
			
		}
		
		try
		{
			test = true;
			
			for( MySingleNode tmp : g.<MySingleNode>getEachNode() )
			{
				
			}
		}
		catch( ClassCastException e )
		{
			test = false;
		}
		
		assertFalse(test);
		
		g.addEdge("0","0","1");
		e1 = g.addEdge("1","0","2");
		e2 = g.addEdge("2","1","2");
		
		try
		{
			test = true;
			
			e3 = g.addEdge("3","0","3");
		}
		catch( ClassCastException e )
		{
			test = false;
		}
		
		assertFalse(test);
		
		for( Edge tmp : g.getEachEdge() )
		{
			
		}
		
		for( MyALGEdge tmp : g.<MyALGEdge>getEachEdge() )
		{
			
		}
		
		try
		{
			test = true;
			
			for( MySingleEdge tmp : g.<MySingleEdge>getEachEdge() )
			{
				
			}
		}
		catch( ClassCastException e )
		{
			test = false;
		}
		
		assertFalse(test);
	}
}
