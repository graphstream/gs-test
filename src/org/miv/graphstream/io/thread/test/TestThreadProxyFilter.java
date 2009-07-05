package org.miv.graphstream.io.thread.test;

import org.junit.Test;
import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.implementations.MultiGraph;
import org.miv.graphstream.io2.thread.ThreadProxyFilter;

/**
 * Test the thread proxy filter.
 * 
 * <p>
 * Sadly it is quite difficult to test thread things using junit.
 * </p>
 */
public class TestThreadProxyFilter
{
	public static void main( String args[] )
	{
		new TestThreadProxyFilter();
	}
	
	public TestThreadProxyFilter()
	{
		
	}
	
	@Test
	public void Test1_GraphToWardGraph()
	{
		Graph source = new MultiGraph();
		Graph target = new MultiGraph();
		
		// Start to populate the graph to test the "replay" feature of the proxy.
		
		source.addNode( "A" );
		source.addNode( "B" );
		source.addNode( "C" );
		source.addEdge( "AB", "A", "B" );
		source.addEdge( "BC", "B", "C" );
		source.addEdge( "CA", "C", "A " );
		
		source.getNode( "A" ).addAttribute( "A1", "foo" );
		source.getNode( "A" ).addAttribute( "A2", "foo" );
		
		ThreadProxyFilter proxy = new ThreadProxyFilter( source );
		
		proxy.addGraphListener( target );

		Thread other = new Thread( new AnotherThread( proxy, target ) {
			public void run() {
				// The second part of the test starts
				// in this target thread.

				boolean loop = true;
				
				do
				{
					proxy.checkEvents();
					
					if( target.hasAttribute( "STOP!" ) )
						loop = false;
				}
				while( loop );
			}
			
		} );
		
		other.start();
		
		// The first part of the test begins in this
		// source thread.
		
		source.addNode( "X" );
		source.addNode( "Y" );
		source.addNode( "Z" );
		source.addEdge( "XY", "X", "Y" );
		source.addEdge( "YZ", "Y", "Z" );
		source.addEdge( "ZX", "Z", "X" );
		source.addEdge( "XA", "X", "A" );
		source.removeEdge( "AB" );
		source.removeNode( "B" );
		source.getNode( "X" ).addAttribute( "X1", "foo" );
		source.getNode( "X" ).setAttribute( "X1", "bar" );
		source.getNode( "A" ).removeAttribute( "A1" );
		
		source.addAttribute( "STOP!" );
		
		// End of the test, wait for the other thread to terminate
		
		try
        {
	        other.join();
        }
        catch( InterruptedException e )
        {
	        e.printStackTrace();
        }
        
        // Now test the results in the target thread.
	}

/**
 * Separate runnable that knows about the proxy. 
 */
public abstract class AnotherThread implements Runnable
{
	protected ThreadProxyFilter proxy;
	
	protected Graph target;
		
	public AnotherThread( ThreadProxyFilter proxy, Graph target )
	{
		this.proxy  = proxy;
		this.target = target;
	}
}
}