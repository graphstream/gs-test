/*
 * Copyright 2006 - 2011 
 *     Stefan Balev 	<stefan.balev@graphstream-project.org>
 *     Julien Baudry	<julien.baudry@graphstream-project.org>
 *     Antoine Dutot	<antoine.dutot@graphstream-project.org>
 *     Yoann Pigné		<yoann.pigne@graphstream-project.org>
 *     Guilhelm Savin	<guilhelm.savin@graphstream-project.org>
 * 
 * This file is part of GraphStream <http://graphstream-project.org>.
 * 
 * GraphStream is a library whose purpose is to handle static or dynamic
 * graph, create them from scratch, file or any source and display them.
 * 
 * This program is free software distributed under the terms of two licenses, the
 * CeCILL-C license that fits European law, and the GNU Lesser General Public
 * License. You can  use, modify and/ or redistribute the software under the terms
 * of the CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
 * URL <http://www.cecill.info> or under the terms of the GNU LGPL as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C and LGPL licenses and that you accept their terms.
 */

package org.graphstream.ui.viewer.test;

import java.util.Random;

import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.graphicGraph.stylesheet.Values;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteFactory;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;

import static org.graphstream.algorithm.Toolkit.*;

/**
 * Test the sprites.
 */
public class TestSprites implements ViewerListener {
	public static void main(String args[]) {
//		sleep(60000);
		(new TestSprites()).test1(100, 10000);
//		(new TestSprites()).test2(200, 5000);
	}
	
	protected boolean loop = true;
	
	protected Graph graph = null;
	
	protected SpriteManager sprites = null;
	
	protected int STEPS = 1000;
	
	public void test1(int nodeCount, int spriteCount) {
		graph = new MultiGraph("sprites");
		Viewer viewer = graph.display(false);
		ViewerPipe pipeIn = viewer.newViewerPipe();
		Generator gen = new GridGenerator(false, false, true); 
		
		pipeIn.addAttributeSink(graph);
		pipeIn.addViewerListener(this);
		pipeIn.pump();
		
		graph.addAttribute("ui.log", "spritesFPS.log");
		graph.addAttribute("ui.default.title", "Sprites");
		graph.addAttribute("ui.stylesheet", styleSheet1);
		
		gen.addSink(graph);
		gen.begin();
		for(int i=0; i<nodeCount; i++) gen.nextEvents();
		gen.end();
		
		sleep(1000);
		addSprites(spriteCount);
		
		System.out.printf("%d ~nodes, %d sprites%n", nodeCount, spriteCount);
		System.out.printf("%d nodes, %d edges%n", graph.getNodeCount(), graph.getEdgeCount());

		int i = 0;
		
		while(loop && i < STEPS) {
			pipeIn.pump();
			moveSprites();
			sleep(40);
			i += 1;
		}
		
		System.exit(0);
	}

	public void test2(int nodeCount, int spriteCount) {
		graph = new MultiGraph("sprites");
		Viewer viewer = graph.display(true);
		ViewerPipe pipeIn = viewer.newViewerPipe();
		Generator gen = new DorogovtsevMendesGenerator(new Random(1));
		
		
		pipeIn.addAttributeSink(graph);
		pipeIn.addViewerListener(this);
		pipeIn.pump();
		
		System.out.printf("%d nodes, %d sprites%n", nodeCount, spriteCount);
		
		graph.addAttribute("ui.log", "spritesFPS.log");
		graph.addAttribute("ui.default.title", "Sprites");
		graph.addAttribute("ui.stylesheet", styleSheet2);
		
		gen.addSink(graph);
		gen.begin();
		for(int i=0; i<nodeCount; i++) gen.nextEvents();
		gen.end();
		
		sleep(1000);
		addSprites(spriteCount);
		
		int i = 0;
		
		while(loop && i < STEPS) {
			pipeIn.pump();
			moveSprites();
			sleep(40);
			i += 1;
		}
		
		System.exit(0);
	}
	
	protected static void sleep(long ms) {
		try { Thread.sleep(ms); } catch(Exception e) {}
	}
	
	protected void addSprites(int spriteCount) {
		sprites = new SpriteManager(graph);
		
		sprites.setSpriteFactory(new TestSpritesFactory());
		
		for(int i=0; i<spriteCount; i++) {
			sprites.addSprite(Integer.toString(i));
		}
		
		for(Sprite sprite: sprites) {
			sprite.attachToEdge(randomEdge(graph).getId());
		}
	}
	
	protected void moveSprites() {
		for(Sprite sprite: sprites) {
			((TestSprite)sprite).move();
		}
	}

	// ViewerListener interface
	
	public void viewClosed(String id) {
		loop = false;
	}
	
	public void buttonPushed(String id) {
	}
	
	public void buttonReleased(String id) {
	}
	
	// Sprites
	
	protected class TestSpritesFactory extends SpriteFactory {
		@Override
		public Sprite newSprite(String id, SpriteManager manager, Values position) {
			return new TestSprite(id, manager);
		}
	}
	
	protected class TestSprite extends Sprite {
		protected double dir = 0.01;
		
		public TestSprite(String identifier, SpriteManager manager) {
			super(identifier, manager);
		}
		
		public void move() {
			double p = getX();
			
			p += dir;
			
			if(p<0 || p>1)
				chooseNextEdge();
			else setPosition(p);
		}
		
		public void chooseNextEdge() {
			Edge edge = (Edge)getAttachment();
			Node node = dir>0 ? edge.getTargetNode() : edge.getSourceNode();
			Edge next = randomEdge(node);
			double pos = 0;
			
			if(node == next.getSourceNode()) {
				dir = 0.01f; pos = 0;
			} else {
				dir = -0.01f; pos = 1;
			}
			
			attachToEdge(next.getId());
			setPosition(pos);
		}
	}
	
	// Style
	
	protected String styleSheet1 =
		"graph {"+
		"	fill-mode: plain;"+
		"	fill-color: white, gray;"+
		"	padding: 60px;"+
		"}"+
		"node {"+
		"	shape: circle;"+
		"	size: 4px;"+
		"	fill-mode: plain;"+
		"	fill-color: grey;"+
		"	stroke-mode: none;"+
		"	text-visibility-mode: zoom-range;"+
		"	text-visibility: 0, 0.9;"+
		"}"+
		"edge {"+
		"	size: 1px;"+
		"	shape: line;"+
		"	fill-color: grey;"+
		"	fill-mode: plain;"+
		"	stroke-mode: none;"+
		"}"+
		"sprite {"+
		"	shape: circle;"+
		"	size: 3px;"+
		"	fill-mode: plain;"+
		"	fill-color: red;"+
		"	stroke-mode: none;"+
		"}";
	protected String styleSheet2 = styleSheet1 +
		"edge {"+
		"	shape: cubic-curve;"+
		"}";

}