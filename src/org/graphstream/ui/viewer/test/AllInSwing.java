package org.graphstream.ui.viewer.test;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.graph.implementations.MultiGraph;

public class AllInSwing {
	public static void main(String args[]) {
		System.setProperty( "gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer" );
		// Avec invokeLater, on est sûr que tout le code tourne toujours dans
		// le thread Swing.
		SwingUtilities.invokeLater(new ApplicationExample());
	}
}

class ApplicationExample extends JFrame implements Runnable {
	protected Graph graph;
	protected Viewer viewer;
	
	public ApplicationExample() {
		this.graph = new MultiGraph("mg");
		this.viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_SWING_THREAD);
	}
	
	public void run() {
		graph.addNode("A");
		graph.addNode("B");
		graph.addNode("C");
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");
		graph.addAttribute( "ui.antialias" );
		graph.addAttribute( "ui.quality" );
		graph.addAttribute( "ui.stylesheet", styleSheet );
   
		graph.getNode("A").setAttribute("xyz", -1, 0, 0 );
		graph.getNode("B").setAttribute("xyz",  1, 0, 0 );
  		graph.getNode("C").setAttribute("xyz",  0, 1, 0 );
   
  		// On insère la vue principale du viewer dans la JFrame.
  		
  		View view = viewer.addDefaultView(false);
  		
		add(view, BorderLayout.CENTER);
		
		// On centre la vue en (1,0)
		
		view.setViewCenter(1,0,0);
		
		// On zoome à 200%.
		
		view.setViewPercent(0.5);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		setVisible(true);
	}
  
	protected static String styleSheet =
			"graph {"+
			"	padding: 60px;"+
			"}";
}