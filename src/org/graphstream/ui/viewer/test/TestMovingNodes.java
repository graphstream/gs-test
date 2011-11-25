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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.geom.Vector3;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;

public class TestMovingNodes {
	public static void main(String args[]) {
		(new TestMovingNodes()).test();
	}
	
	protected static final int NODES = 400;
	
	protected static final double VIEW_DISTANCE = 0.08;
	
	protected static final double MAX_SPEED = 0.01;
	
	protected static final long MAX_STEPS = 5000;
	
	protected Random random = new Random(1);
	
	protected boolean loop = true;
	
	protected long steps = 0;
	
	public void test() {
//		sleep(30000);	// Wait 30sec.
		
		Graph graph = new SingleGraph("moving");
		SpriteManager sm = new SpriteManager(graph);
		Sprite s1 = sm.addSprite("S1");
		Sprite s2 = sm.addSprite("S2");
		
		s1.setPosition(0, 0, 0);
		s2.setPosition(1, 1, 0);
		graph.addAttribute("ui.title", "GraphStream: Moving Nodes");
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
		graph.addAttribute("ui.log", "movingFPS.log");
		graph.addAttribute("ui.stylesheet", styleSheet);
		graph.addAttribute("ui.fps", 70);
		graph.display(false);
		
		for(int i=0; i<NODES; i++) {
			Node node = graph.addNode(String.format("%d", i));
			node.addAttribute("mov", new Movement(node));
		}
		
		while(loop) {
			for(Node node: graph) {
				removeInvalidEdges(node);
				addCloseEdges(node);
				Movement mov = node.getAttribute("mov");
				mov.move(node);
			}
			
			sleep(5);
			steps += 1;
			
			if(steps >= MAX_STEPS) loop = false;
		}
		
		graph.removeAttribute("ui.log");
		System.exit(0);
	}
	
	protected void removeInvalidEdges(Node node) {
		Movement mov = node.getAttribute("mov");
		Iterator<? extends Node> neighbors = node.getNeighborNodeIterator();
		ArrayList<Edge> willDieSoon = new ArrayList<Edge>();

		while(neighbors.hasNext()) {
			Node neighbor = neighbors.next();
			Movement nmov = neighbor.getAttribute("mov");
			if(mov.distance(nmov) > VIEW_DISTANCE) {
				willDieSoon.add(node.getEdgeBetween(neighbor.getIndex()));
			}
		}
		
		for(Edge edge: willDieSoon) node.getGraph().removeEdge(edge.getIndex());
	}
	
	protected void addCloseEdges(Node node) {
		Movement mov = node.getAttribute("mov");
		
		for(Node other: node.getGraph()) {
			if(other != node && (!node.hasEdgeBetween(other.getIndex()))) {
				Movement omov = other.getAttribute("mov");
				if(mov.distance(omov) < VIEW_DISTANCE) {
					node.getGraph().addEdge(String.format("%s_%s", node.getId(), other.getId()), node.getIndex(), other.getIndex());
				}
			}
		}
	}
	
	protected void sleep(long ms) {
		try { Thread.sleep(ms); } catch(InterruptedException e) {}
	}
	
	class Movement {
		public Point3 pos = new Point3();
		public Point3 target = new Point3();
		public double speed = MAX_SPEED;

		public Movement(Node node) {
			pos.set(random.nextDouble(), random.nextDouble(), 0);
			target.set(random.nextDouble(), random.nextDouble(), 0);
			speed = random.nextDouble()*MAX_SPEED;
			node.addAttribute("xy", pos.x, pos.y);
		}

		public double distance(Movement other) {
			return pos.distance(other.pos);
		}

		public void move(Node node) {
			Vector3 dir = new Vector3(pos, target);
			dir.normalize();
			dir.scalarMult(speed);
			pos.x += dir.data[0];
			pos.y += dir.data[1];
			
			node.setAttribute("xy", pos.x, pos.y);
			
			if(target.distance(pos) < speed) {
				target.set(random.nextDouble(), random.nextDouble());
				speed = random.nextDouble()*MAX_SPEED;
			}
		}
	}
	
	protected static final String styleSheet = 
		"sprite { fill-color: red; size: 3px; }" +
		"node { fill-color: #AAA; size: 5px; stroke-mode: plain; stroke-color: white; stroke-width: 1px; }" +
		"edge { fill-color: #0003; }";
}