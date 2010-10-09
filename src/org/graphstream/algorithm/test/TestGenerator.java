package org.graphstream.algorithm.test;

import javax.swing.JOptionPane;

import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.FullGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.algorithm.generator.IncompleteGridGenerator;
import org.graphstream.algorithm.generator.PreferentialAttachmentGenerator;
import org.graphstream.algorithm.generator.RandomEuclideanGenerator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.Pipe;
import org.graphstream.ui.swingViewer.Viewer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestGenerator {
	@Test
	public void testFullGenerator() {
		testGenerator(new FullGenerator(), "FullGenerator", 10);
	}

	@Test
	public void testRandomGenerator() {
		testGenerator(new RandomGenerator(), "RandomGenerator", 100);
	}

	@Test
	public void testRandomEuclideanGenerator() {
		testGenerator(new RandomEuclideanGenerator(),
				"RandomEuclideanGenerator", 100);
	}

	@Test
	public void testGridGenerator() {
		testGenerator(new GridGenerator(), "GridGenerator", 10);
	}

	@Test
	public void testIncompleteGridGenerator() {
		testGenerator(new IncompleteGridGenerator(), "IncompleteGridGenerator",
				10);
	}

	@Test
	public void testPreferentialAttachmentGenerator() {
		testGenerator(new PreferentialAttachmentGenerator(),
				"PreferentialAttachmentGenerator", 100);
	}

	@Test
	public void testDorogovtsevMendesGenerator() {
		testGenerator(new DorogovtsevMendesGenerator(),
				"DorogovtsevMendesGenerator", 100);
	}

	protected void testGenerator(Generator gen, String name, int size) {
		DefaultGraph g = new DefaultGraph("test-" + name);

		int i = size;

		gen.addSink(g);

		if (gen instanceof Pipe)
			g.addAttributeSink((Pipe) gen);

		Viewer gvr = g.display();

		gen.begin();

		while (i-- > 0)
			gen.nextEvents();

		gen.end();

		assertEquals(JOptionPane.showConfirmDialog(null, String.format(
				"%s with %d iterations. Is it correct ?", name, size), name,
				JOptionPane.YES_NO_OPTION), JOptionPane.YES_OPTION);

		gvr.close();
	}
}
