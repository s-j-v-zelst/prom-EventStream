package org.processmining.eventstream.readers.petrinet;

import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.stream.core.interfaces.XSReader;

public interface XSEventStreamToPetriNetReader extends XSReader<XSEvent, Petrinet> {
	
}
