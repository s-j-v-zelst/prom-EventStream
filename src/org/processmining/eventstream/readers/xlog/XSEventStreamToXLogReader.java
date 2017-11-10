package org.processmining.eventstream.readers.xlog;

import org.deckfour.xes.model.XLog;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.stream.core.interfaces.XSReader;

@Deprecated // refactored to StreamBasedEventLog
public interface XSEventStreamToXLogReader extends XSReader<XSEvent, XLog> {

	/**
	 * captures the actual memory consumption of the structure
	 * 
	 * @return
	 */
	long getNumberOfMemoryEntriesRepresentingEvents();

	/**
	 * captures the number of events actually described
	 * 
	 * @return
	 */
	long getTotalNumberOfEventsDescribedByMemory();

	/**
	 * fetches all stored metadata in the structure.
	 * 
	 * @return
	 */
	long getTotalPayloadMemoryOccupation();

}
