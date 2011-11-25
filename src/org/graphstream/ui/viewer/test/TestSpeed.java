package org.graphstream.ui.viewer.test;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.algorithm.generator.GridGenerator;

/**
 * A speed test.
 *
 * <p>
 * After a warm-up phase of drawing a 10x10 grid graph, pausing 100ms between 10 steps of adding
 * lines and rows one by one, we add 90 lines and 90 raws, by 90 steps separated by 10ms pauses.
 * We log the FPS in a "fps.log" file.
 * </p>
 */
public class TestSpeed {
	public static void main(String args[]) throws InterruptedException {
        //System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		(new TestSpeed()).test1();
	}

	protected void wait1mn() {
		for(int i=1; i<60; i++) {
			System.err.println(String.format("%d", i));
			sleep(1000);
		}
	}
	
	protected void sleep(long ms) {
		try { Thread.sleep(ms); } catch(InterruptedException e) {}
	}

    public void test1() {
//    	wait1mn();
        
        Graph graph = new SingleGraph("simple");
        GridGenerator gridg = new GridGenerator(false, false, true);
        
        graph.addAttribute("ui.log", "fps.log");
        graph.display(false);
        gridg.addSink(graph);
        gridg.begin();
        gridg.nextEvents();
        sleep(1000);
        for(int i=1; i<10; i++) {
            gridg.nextEvents();
            sleep(100);
        }
        for(int i=1; i<100; i++) {
            gridg.nextEvents();
            sleep(10);
        }
        gridg.end();
        gridg.removeSink(graph);
    }

    public void test2() {
//		wait1mn();
        
        Graph graph = new SingleGraph("simple");
        GridGenerator gridg = new GridGenerator(false, false, true);
        
        graph.addAttribute("ui.log", "fps.log");
        graph.display(false);
        gridg.addSink(graph);
        gridg.begin();
        gridg.nextEvents();
        sleep(1000);
        for(int i=1; i<10; i++) {
            gridg.nextEvents();
            sleep(100);
        }
        for(int i=1; i<100; i++) {
            gridg.nextEvents();
            sleep(10);
        }
        gridg.end();
        gridg.removeSink(graph);
        for(Node node: graph) {
        	node.addAttribute("ui.label", node.getId());
        }
    }
    
    public void test3() {
//		wait1mn();
    }
}