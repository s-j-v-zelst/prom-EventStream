package org.processmining.eventstream.models;

import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.stream.core.interfaces.XSHub;
import org.processmining.stream.core.interfaces.XSReader;

public interface XSEventHub extends XSEventAuthor, XSHub<XSEvent, XSEvent>, XSReader<XSEvent, XSEvent> {

}
