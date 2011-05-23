package org.graphstream.ui.viewer.test;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;

import static org.graphstream.algorithm.Toolkit.*;

public class TestEvents implements ViewerListener {

	public static void main(String args[]) {
		new TestEvents();
	}
	
	public TestEvents() {
		Graph graph = new MultiGraph("main graph");
		Viewer viewer = graph.display();
		ViewerPipe fromView = viewer.newViewerPipe();
		fromView.addAttributeSink(graph);
		fromView.addViewerListener(this);

		graph.addNode("A");
		graph.addNode("B");
		graph.addNode("C");
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");
		
		//viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
		
		while(true) {
			fromView.pump();
			
			try { Thread.sleep(10); } catch (Exception e) {}
			
//			System.out.printf("Node positions:%n");
//			for(Node node:graph) {
//				double[] pos = nodePosition(node);
//				System.out.printf("    [%s] (%f, %f, %f)%n", node.getId(), pos[0], pos[1], pos[2]);
//			}
		}
	}

	
	public void viewClosed(String viewName) {
		System.err.printf("View closed%n");
	}

	public void buttonPushed(String id) {
		System.err.printf("pushed %s%n", id);
	}

	public void buttonReleased(String id) {
		System.err.printf("released %s%n", id);
	}
}