/*
 * This file is part of GraphStream.
 * 
 * GraphStream is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GraphStream is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GraphStream.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2006 - 2009
 * 	Julien Baudry
 * 	Antoine Dutot
 * 	Yoann Pigné
 * 	Guilhelm Savin
 */
package org.graphstream.stream.sync;

import org.graphstream.stream.sync.SinkTime;
import org.graphstream.stream.sync.SourceTime;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestSync
{
	/**
	 * Used to access to disableSync.
	 */
	class TestSinkTime
		extends SinkTime
	{
		public boolean isSynchEnable()
		{
			return ! disableSync;
		}
	}
	
	@Test
	public void testSync()
	{
		TestSinkTime 	tst = new TestSinkTime();
		SourceTime		st  = new SourceTime("test");
		
		if( tst.isSynchEnable() )
		{
			System.err.printf("sync is enable%n" );
			
			assertTrue( tst.isNewEvent( st.getSourceId(), st.newEvent() ) );
			assertTrue( tst.isNewEvent( st.getSourceId(), st.newEvent() ) );
			assertTrue( tst.isNewEvent( st.getSourceId(), st.newEvent() ) );
			
			long timeId = st.newEvent();
			
			assertTrue( tst.isNewEvent( st.getSourceId(), timeId ) );
			assertFalse( tst.isNewEvent( st.getSourceId(), timeId ) );
		}
		else
		{
			System.err.printf("sync is disable%n" );
			
			assertTrue( tst.isNewEvent( st.getSourceId(), st.newEvent() ) );
			assertTrue( tst.isNewEvent( st.getSourceId(), st.newEvent() ) );
			assertTrue( tst.isNewEvent( st.getSourceId(), st.newEvent() ) );
			
			long timeId = st.newEvent();
			
			assertTrue( tst.isNewEvent( st.getSourceId(), timeId ) );
			assertTrue( tst.isNewEvent( st.getSourceId(), timeId ) );
		}
	}
}
