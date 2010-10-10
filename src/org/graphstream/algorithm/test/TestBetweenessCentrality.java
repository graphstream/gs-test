package org.graphstream.algorithm.test;

import java.util.Collection;

import org.graphstream.algorithm.BetweennessCentrality;
//import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
//import org.graphstream.ui.swingViewer.Viewer;
//import org.graphstream.ui.swingViewer.ViewerListener;
//import org.graphstream.ui.swingViewer.ViewerPipe;

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

//		Viewer viewer = graph.display(true);
//		ViewerPipe pipeIn = viewer.newViewerPipe();
//
//		pipeIn.addViewerListener(new ViewerListener() {
//			public void viewClosed(String id) {
//				loop = false;
//			}
//
//			public void buttonPushed(String id) {
//			}
//
//			public void buttonReleased(String id) {
//			}
//		});

		graph.addAttribute("ui.stylesheet", styleSheet);
		graph.addAttribute("ui.antialias");
		graph.addAttribute("ui.quality");

		BetweennessCentrality bcb = new BetweennessCentrality(true);

		buildGraph6(graph, bcb);

		bcb.betweennessCentrality(graph);

//		for (Node node : graph) {
//			node.setAttribute(
//					"ui.label",
//					String.format("%s C=%.2f", node.getId(),
//							bcb.centrality(node)));
//			System.out.printf("Cb(%s) = %f%n", node.getId(), bcb.centrality(node));
//		}
		
		System.out.printf("Cb(%s) = %f%n", graph.getNode("A").getId(), bcb.centrality(graph.getNode("A")));
		System.out.printf("Cb(%s) = %f%n", graph.getNode("B").getId(), bcb.centrality(graph.getNode("B")));
		System.out.printf("Cb(%s) = %f%n", graph.getNode("C").getId(), bcb.centrality(graph.getNode("C")));
		System.out.printf("Cb(%s) = %f%n", graph.getNode("D").getId(), bcb.centrality(graph.getNode("D")));
		System.out.printf("Cb(%s) = %f%n", graph.getNode("E").getId(), bcb.centrality(graph.getNode("E")));

//		for (Edge edge : graph.getEachEdge()) {
//			edge.setAttribute(
//					"ui.label",
//					String.format("%.2f",
//							bcb.weight(edge.getNode0(), edge.getNode1())));
//		}

//		while (loop) {
//			pipeIn.pump();
//			sleep(4);
//		}
//
//		System.exit(0);
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
		BetweennessCentrality bcb = new BetweennessCentrality(false /* unweighted */);
		buildGraph1(graph, bcb);
		bcb.init(graph);
		bcb.compute();
		assertEquals(1.0, (Double) graph.getNode("A").getAttribute("Cb"), 0.0);
		assertEquals(1.0, (Double) graph.getNode("B").getAttribute("Cb"), 0.0);
		assertEquals(3.0, (Double) graph.getNode("C").getAttribute("Cb"), 0.0);
		assertEquals(3.0, (Double) graph.getNode("D").getAttribute("Cb"), 0.0);
		assertEquals(1.0, (Double) graph.getNode("E").getAttribute("Cb"), 0.0);
		assertEquals(3.0, (Double) graph.getNode("F").getAttribute("Cb"), 0.0);
	}

	@Test
	public void test1b() {
		Graph graph = new SingleGraph("Betweeness Centrality Test 1 (b)");
		BetweennessCentrality bcb = new BetweennessCentrality(true /* weighted */);
		buildGraph1b(graph, bcb);
		bcb.init(graph);
		bcb.compute();
		assertEquals(1.0, (Double) graph.getNode("A").getAttribute("Cb"), 0.0);
		assertEquals(1.0, (Double) graph.getNode("B").getAttribute("Cb"), 0.0);
		assertEquals(3.0, (Double) graph.getNode("C").getAttribute("Cb"), 0.0);
		assertEquals(3.0, (Double) graph.getNode("D").getAttribute("Cb"), 0.0);
		assertEquals(1.0, (Double) graph.getNode("E").getAttribute("Cb"), 0.0);
		assertEquals(3.0, (Double) graph.getNode("F").getAttribute("Cb"), 0.0);
	}

	@Test
	public void test2() {
		Graph graph = new SingleGraph("Betweeness Centrality Test 2");
		BetweennessCentrality bcb = new BetweennessCentrality(true /* weighted */);
		buildGraph2(graph, bcb);
		bcb.init(graph);
		bcb.compute();
		assertEquals(4.0, (Double) graph.getNode("A").getAttribute("Cb"), 0.0);
		assertEquals(0.0, (Double) graph.getNode("B").getAttribute("Cb"), 0.0);
		assertEquals(0.0, (Double) graph.getNode("C").getAttribute("Cb"), 0.0);
		assertEquals(4.0, (Double) graph.getNode("D").getAttribute("Cb"), 0.0);
	}

	@Test
	public void test3() {
		Graph graph = new SingleGraph("Betweeness Centrality Test 3");
		BetweennessCentrality bcb = new BetweennessCentrality(true /* weighted */);
		buildGraph3(graph, bcb);
		bcb.init(graph);
		bcb.compute();
		assertEquals(6.0, (Double) graph.getNode("A").getAttribute("Cb"), 0.0);
		assertEquals(0.0, (Double) graph.getNode("B").getAttribute("Cb"), 0.0);
		assertEquals(6.0, (Double) graph.getNode("C").getAttribute("Cb"), 0.0);
		assertEquals(8.0, (Double) graph.getNode("D").getAttribute("Cb"), 0.0);
		assertEquals(0.0, (Double) graph.getNode("E").getAttribute("Cb"), 0.0);
	}

	@Test
	public void test4() {
		Graph graph = new SingleGraph("Betweeness Centrality Test 4");
		BetweennessCentrality bcb = new BetweennessCentrality(true /* weighted */);
		buildGraph4(graph, bcb);
		bcb.init(graph);
		bcb.compute();
		assertEquals(0.0, (Double) graph.getNode("0").getAttribute("Cb"), 0.0);
		assertEquals(4.0, (Double) graph.getNode("1").getAttribute("Cb"), 0.0);
		assertEquals(3.0, (Double) graph.getNode("2").getAttribute("Cb"), 0.0);
		assertEquals(1.0, (Double) graph.getNode("3").getAttribute("Cb"), 0.0);
		assertEquals(1.0, (Double) graph.getNode("4").getAttribute("Cb"), 0.0);
	}
	
	@Test
	public void test5() {
		Graph graph = new SingleGraph("Betweeness Centrality Test 5");
		BetweennessCentrality bcb = new BetweennessCentrality(true /* weighted */);
		buildGraph5(graph, bcb);
		bcb.init(graph);
		bcb.compute();
		assertEquals(0.0,    (Double) graph.getNode("A").getAttribute("Cb"), 0.0);
		assertEquals(0.0,    (Double) graph.getNode("B").getAttribute("Cb"), 0.0);
		assertEquals(0.0,    (Double) graph.getNode("C").getAttribute("Cb"), 0.0);
		assertEquals(8.3333, (Double) graph.getNode("D").getAttribute("Cb"), 0.01);
		assertEquals(2.6666, (Double) graph.getNode("E").getAttribute("Cb"), 0.01);
	}
	
	@Test
	public void test6() {
		Graph graph = new SingleGraph("Betweeness Centrality Test 6");
		BetweennessCentrality bcb = new BetweennessCentrality(true /* weighted */);
		buildGraph6(graph, bcb);
		bcb.init(graph);
		bcb.compute();
		assertEquals(4, (Double) graph.getNode("A").getAttribute("Cb"), 0);
		assertEquals(2, (Double) graph.getNode("B").getAttribute("Cb"), 0);
		assertEquals(0, (Double) graph.getNode("C").getAttribute("Cb"), 0);
		assertEquals(2, (Double) graph.getNode("D").getAttribute("Cb"), 0);
		assertEquals(4, (Double) graph.getNode("E").getAttribute("Cb"), 0);
	}	

	protected static void buildGraph1(Graph graph, BetweennessCentrality bcb) {
		//
		// Unweighted graph:
		//
		//     F---E     Cb(A) = 1
		//    /|    \    Cb(B) = 1
		//   / |     \   Cb(C) = 3
		//  /  |      \  Cb(D) = 3
		// A---C-------D Cb(E) = 1
		//  \  |     _/  Cb(F) = 3
		//   \ |  __/
		//    \|_/
		//     B

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

	protected static void buildGraph1b(Graph graph, BetweennessCentrality bcb) {
		//
		// Weighted graph with all edges at weight 1, should give teh same
		// result as test1.
		//
		//     F---E     Cb(A) = 1
		//    /|    \    Cb(B) = 1
		//   / |     \   Cb(C) = 3
		//  /  |      \  Cb(D) = 3
		// A---C-------D Cb(E) = 1
		//  \  |     _/  Cb(F) = 3
		//   \ |  __/
		//    \|_/
		//     B

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

		bcb.setWeight(A, B, 1.0);
		bcb.setWeight(A, C, 1.0);
		bcb.setWeight(A, F, 1.0);
		bcb.setWeight(B, C, 1.0);
		bcb.setWeight(F, C, 1.0);
		bcb.setWeight(C, D, 1.0);
		bcb.setWeight(F, E, 1.0);
		bcb.setWeight(E, D, 1.0);
		bcb.setWeight(B, D, 1.0);
		
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
		//    B     Cb(A) = 4
		//   / \10  Cb(B) = 0
		//  /   \   Cb(C) = 0
		// A     C  Cb(D) = 4
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
		//    B     Cb(A) = 6   AB=1,  AE=10, AD=1
		//   /|\    Cb(B) = 0   BC=10, BE=10
		//  / | \   Cb(C) = 6   CD=1,  CE=1
		// A--E--C  Cb(D) = 8   DE=10
		//  \ | /   Cb(E) = 0
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
    
    protected static void buildGraph4(Graph graph, BetweennessCentrality bcb) {
    	//    1    Cb(0) = 0    0-1 = 1, 0-3 = 1
    	//   /|\   Cb(1) = 4    1-2 = 0.5, 1-3 = 1
    	//  / | \  Cb(2) = 3    2-4 = 0.5
    	// 0  |  2 Cb(3) = 1    3-4 = 1
    	//  \ |  | Cb(4) = 1
    	//   \|  | 
    	//    3--4
    	
    	Node N0= graph.addNode("0");
    	Node N1= graph.addNode("1");
    	Node N2= graph.addNode("2");
    	Node N3= graph.addNode("3");
    	Node N4= graph.addNode("4");

    	graph.addEdge("0_1","0","1");
    	graph.addEdge("0_3","0","3");
    	graph.addEdge("1_3","1","3");
    	graph.addEdge("1_2","1","2");
    	graph.addEdge("3_4","3","4");
    	graph.addEdge("4_2","4","2");
    	
    	bcb.setWeight(N0,N1,1);
    	bcb.setWeight(N0,N3,1);
    	bcb.setWeight(N1,N3,1);
    	bcb.setWeight(N1,N2,0.5);
    	bcb.setWeight(N3,N4,1);
    	bcb.setWeight(N4,N2,0.5);
    //	try { graph.write( "test.gml" ); } catch(Exception e){}
   }

	protected static void buildGraph5(Graph graph, BetweennessCentrality bcb) {
		//    B---     A-B = 10, A-C = 3, A-D = 1
		//   /|\  \    B-C = 6,  B-D = 4, B-E = 3
		//  / | \  \   C-D = 2,  C-E = 10
		// A--+--\--D  D-E = 1
		//  \ | __\/|  This graph allows mutliple shortest paths between several nodes. 
		//   \|/   \|  Cb(A) = Cb(B) = Cb(C) = 0
		//    C-----E  Cb(D) = 8.3333, Cb(E) = 2.6666
		
		Node A = graph.addNode("A");
		Node B = graph.addNode("B");
		Node C = graph.addNode("C");
		Node D = graph.addNode("D");
		Node E = graph.addNode("E");

		graph.addEdge("AB", "A", "B");
		graph.addEdge("AC", "A", "C");
		graph.addEdge("AD", "A", "D");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("BD", "B", "D");
		graph.addEdge("CD", "C", "D");
		graph.addEdge("EC", "E", "C");
		graph.addEdge("EB", "E", "B");
		graph.addEdge("ED", "E", "D");

		bcb.setWeight(A, B, 10);
		bcb.setWeight(A, C, 3);
		bcb.setWeight(A, D, 1);
		bcb.setWeight(B, C, 6);
		bcb.setWeight(B, D, 4);
		bcb.setWeight(C, D, 2);
		bcb.setWeight(E, C, 10);
		bcb.setWeight(E, B, 3);
		bcb.setWeight(E, D, 1);
	}
	
	protected static void buildGraph6(Graph graph, BetweennessCentrality bcb) {

		//    E----D  AB=1, BC=5, CD=3, DE=2, BE=6, EA=4  
		//   /|    |  Cb(A)=4 (NetworkX finds 3.5, by hand I find 4).
		//  / |    |  Cb(B)=2
		// A  |    |  Cb(C)=0
		//  \ |    |  Cb(D)=2
		//   \|    |  Cb(E)=4 (NetworkX finds 3.5, by hand I find 4).
		//    B----C
		
		Node A = graph.addNode("A");
		Node B = graph.addNode("B");
		Node E = graph.addNode("E");
		Node C = graph.addNode("C");
		Node D = graph.addNode("D");

		graph.addEdge("AB", "A", "B");
		graph.addEdge("BE", "B", "E");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("ED", "E", "D");
		graph.addEdge("CD", "C", "D");
		graph.addEdge("AE", "A", "E");
		
		bcb.setWeight(A, B, 1);
		bcb.setWeight(B, E, 6);
		bcb.setWeight(B, C, 5);
		bcb.setWeight(E, D, 2);
		bcb.setWeight(C, D, 3);
		bcb.setWeight(A, E, 4);
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