package org.processmining.eventstream.models;

import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.stream.core.interfaces.XSReader;

public interface XSEventReader<T> extends XSReader<XSEvent, T> {

}
