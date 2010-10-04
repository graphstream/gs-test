package org.graphstream.algorithm.test;

import java.util.Collection;

import org.graphstream.algorithm.BetweennessCentrality;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestBetweenessCentrality {

    protected static boolean loop = true;

    /**
     * Quick manual test of the centrality algorithm.
     */
    public static void main(String args[]) {
	// System.setProperty( "gs.ui.renderer",
	// "org.graphstream.ui.j2dviewer.J2DGraphRenderer" );

	Graph graph = new SingleGraph("Betweeness Centrality");

	Viewer viewer = graph.display(false);
	ViewerPipe pipeIn = viewer.newViewerPipe();

	pipeIn.addViewerListener(new ViewerListener() {
	    public void viewClosed(String id) {
		loop = false;
	    }

	    public void buttonPushed(String id) {
	    }

	    public void buttonReleased(String id) {
	    }
	});

	graph.addAttribute("ui.stylesheet", styleSheet);
	graph.addAttribute("ui.antialias");
	graph.addAttribute("ui.quality");

	BetweennessCentrality bcb = new BetweennessCentrality(true);

	buildGraph3(graph, bcb);

	bcb.betweennessCentrality(graph);

	for (Node node : graph) {
	    node.setAttribute("ui.label", String.format("%s C=%.2f", node
		    .getId(), bcb.centrality(node)));
	}

	for (Edge edge : graph.getEachEdge()) {
	    edge.setAttribute("ui.label", String.format("%.2f", bcb.weight(edge
		    .getNode0(), edge.getNode1())));
	}

	while (loop) {
	    pipeIn.pump();
	    sleep(4);
	}

	System.exit(0);
    }

    protected static void sleep(long ms) {
	try {
	    Thread.sleep(ms);
	} catch (Exception e) {
	}
    }
    
    @Test
    public void test1() {
	Graph graph = new SingleGraph("Betweeness Centrality Test 1");
	BetweennessCentrality bcb = new BetweennessCentrality(false /*unweighted*/);
	buildGraph1(graph, bcb);
	bcb.init(graph);
	bcb.compute();
	assertEquals(1.0, (Float)graph.getNode("A").getAttribute("Cb"), 0.0);
	assertEquals(1.0, (Float)graph.getNode("B").getAttribute("Cb"), 0.0);
	assertEquals(3.0, (Float)graph.getNode("C").getAttribute("Cb"), 0.0);
	assertEquals(3.0, (Float)graph.getNode("D").getAttribute("Cb"), 0.0);
	assertEquals(1.0, (Float)graph.getNode("E").getAttribute("Cb"), 0.0);
	assertEquals(3.0, (Float)graph.getNode("F").getAttribute("Cb"), 0.0);
    }
    
    @Test
    public void test2() {
	Graph graph = new SingleGraph("Betweeness Centrality Test 2");
	BetweennessCentrality bcb = new BetweennessCentrality(true /*weighted*/);
	buildGraph2(graph, bcb);
	bcb.init(graph);
	bcb.compute();
	assertEquals(4.0, (Float)graph.getNode("A").getAttribute("Cb"), 0.0);
	assertEquals(0.0, (Float)graph.getNode("B").getAttribute("Cb"), 0.0);
	assertEquals(0.0, (Float)graph.getNode("C").getAttribute("Cb"), 0.0);
	assertEquals(4.0, (Float)graph.getNode("D").getAttribute("Cb"), 0.0);
    }

    @Test
    public void test3() {
	Graph graph = new SingleGraph("Betweeness Centrality Test 3");
	BetweennessCentrality bcb = new BetweennessCentrality(true /*weighted*/);
	buildGraph3(graph, bcb);
	bcb.init(graph);
	bcb.compute();
	assertEquals(6.0, (Float)graph.getNode("A").getAttribute("Cb"), 0.0);
	assertEquals(0.0, (Float)graph.getNode("B").getAttribute("Cb"), 0.0);
	assertEquals(6.0, (Float)graph.getNode("C").getAttribute("Cb"), 0.0);
	assertEquals(8.0, (Float)graph.getNode("D").getAttribute("Cb"), 0.0);
	assertEquals(0.0, (Float)graph.getNode("E").getAttribute("Cb"), 0.0);
    }

    protected static void buildGraph1(Graph graph, BetweennessCentrality bcb) {
	//
	// Unweighted graph:
	//
	//      F---E        Cb(A) = 1
	//     /|    \       Cb(B) = 1
	//    / |     \      Cb(C) = 3
	//   /  |      \     Cb(D) = 3
	//  A---C-------D    Cb(E) = 1
	//   \  |     _/     Cb(F) = 3
	//    \ |  __/
	//     \|_/
	//      B
	
	Node A = graph.addNode("A");
	Node B = graph.addNode("B");
	Node C = graph.addNode("C");
	Node D = graph.addNode("D");
	Node E = graph.addNode("E");
	Node F = graph.addNode("F");

	graph.addEdge("AB", "A", "B");
	graph.addEdge("AC", "A", "C");
	graph.addEdge("AF", "A", "F");
	graph.addEdge("BC", "B", "C");
	graph.addEdge("FC", "F", "C");
	graph.addEdge("CD", "C", "D");
	graph.addEdge("FE", "F", "E");
	graph.addEdge("ED", "E", "D");
	graph.addEdge("BD", "B", "D");

	A.addAttribute("xyz", -1, 0);
	A.addAttribute("ui.label", "A");
	B.addAttribute("xyz", 0, -1);
	B.addAttribute("ui.label", "B");
	C.addAttribute("xyz", 0, 0);
	C.addAttribute("ui.label", "C");
	D.addAttribute("xyz", 2, 0);
	D.addAttribute("ui.label", "D");
	E.addAttribute("xyz", 1, .7);
	E.addAttribute("ui.label", "E");
	F.addAttribute("xyz", 0, 1);
	F.addAttribute("ui.label", "F");
    }

    protected static void buildGraph2(Graph graph, BetweennessCentrality bcb) {
	//
	// Weighted graph (edge BC=10, others=1):
	//
	//    B         Cb(A) = 4
	//   / \10      Cb(B) = 0
	//  /   \       Cb(C) = 0
	// A     C      Cb(D) = 4
	//  \   /
	//   \ /
	//    D
	
	Node A = graph.addNode("A");
	Node B = graph.addNode("B");
	Node C = graph.addNode("C");
	Node D = graph.addNode("D");

	graph.addEdge("AB", "A", "B");
	graph.addEdge("BC", "B", "C");
	graph.addEdge("CD", "C", "D");
	graph.addEdge("DA", "D", "A");

	A.addAttribute("xyz", -1, 0);
	A.addAttribute("ui.label", "A");
	B.addAttribute("xyz", 0, 1);
	B.addAttribute("ui.label", "B");
	C.addAttribute("xyz", 1, 0);
	C.addAttribute("ui.label", "C");
	D.addAttribute("xyz", 0, -1);
	D.addAttribute("ui.label", "D");

	bcb.setWeight(B, C, 10f);
    }

    protected static void buildGraph3(Graph graph, BetweennessCentrality bcb) {
	//
	// Weighted graph (edge BC=10, others=1):
	//
	//    B         Cb(A) = 6   AB=1, AE=10, AD=1
	//   /|\        Cb(B) = 0   BC=10, BE=10
	//  / | \       Cb(C) = 6   CD=1, CE=1
	// A--E--C      Cb(D) = 8   DE=10
	//  \ | /       Cb(E) = 0
	//   \|/
	//    D
	Node A = graph.addNode("A");
	Node B = graph.addNode("B");
	Node C = graph.addNode("C");
	Node D = graph.addNode("D");
	Node E = graph.addNode("E");

	graph.addEdge("AB", "A", "B");
	graph.addEdge("BC", "B", "C");
	graph.addEdge("CD", "C", "D");
	graph.addEdge("DA", "D", "A");

	graph.addEdge("AE", "A", "E");
	graph.addEdge("BE", "B", "E");
	graph.addEdge("CE", "C", "E");
	graph.addEdge("DE", "D", "E");

	A.addAttribute("xyz", -1, 0);
	A.addAttribute("ui.label", "A");
	B.addAttribute("xyz", 0, 1);
	B.addAttribute("ui.label", "B");
	C.addAttribute("xyz", 1, 0);
	C.addAttribute("ui.label", "C");
	D.addAttribute("xyz", 0, -1);
	D.addAttribute("ui.label", "D");
	E.addAttribute("xyz", 0, 0);
	E.addAttribute("ui.label", "E");

	bcb.setWeight(B, C, 10f);
	bcb.setWeight(A, E, 10f);
	bcb.setWeight(E, D, 10f);
	bcb.setWeight(B, E, 10f);
    }

    protected static String mkString(Collection<Node> set) {
	int n = set.size();
	StringBuffer buf = new StringBuffer();

	for (Node node : set) {
	    buf.append(node.getId());
	    if (n > 1)
		buf.append(", ");
	    n--;
	}

	return buf.toString();
    }

    protected static String styleSheet = "graph {" + "	padding: 60px;" + "}"
	    + "node {" + "	text-color: black;"
	    + "	text-background-mode: plain;"
	    + "	text-background-color: white;" + "}";
}
