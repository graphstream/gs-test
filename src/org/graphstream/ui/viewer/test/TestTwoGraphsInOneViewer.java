package org.graphstream.ui.viewer.test;

import org.graphstream.algorithm.generator.*;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.thread.*;
import org.graphstream.ui.swingViewer.*;

public class TestTwoGraphsInOneViewer {
	public static void main(String args[]) {
		new TestTwoGraphsInOneViewer();
	}

	public TestTwoGraphsInOneViewer() {
		boolean loop = true;
		Graph graph1 = new MultiGraph("g1");
		Graph graph2 = new MultiGraph("g2");
		Viewer viewer1 = new Viewer(new ThreadProxyPipe(graph1));
		Viewer viewer2 = new Viewer(new ThreadProxyPipe(graph2));

		graph1.addAttribute("ui.stylesheet", styleSheet1);
		graph2.addAttribute("ui.stylesheet", styleSheet2);
		View view1 = viewer1.addDefaultView(true);
		View view2 = viewer2.addDefaultView(true);
		viewer1.enableAutoLayout();
		viewer2.enableAutoLayout();

		//view1.setBackLayerRenderer(view2);
		
		Generator gen = new DorogovtsevMendesGenerator();

		gen.addSink(graph1);
		gen.begin();
		for (int i = 0; i < 100; i++)
			gen.nextEvents();
		gen.end();

		gen.removeSink(graph1);
		gen.addSink(graph2);
		gen.begin();
		for(int i=0; i<100; i++)
			gen.nextEvents();
		gen.end();
	}
	
	protected String styleSheet1 =
		"graph { padding: 40px; }" +
		"node { fill-color: red; }";
	
	protected String styleSheet2 =
		"graph { padding: 40px; }" +
		"node { fill-color: blue; }";
}
