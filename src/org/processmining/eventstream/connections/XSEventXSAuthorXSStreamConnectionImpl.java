package org.processmining.eventstream.connections;

import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.eventstream.core.interfaces.XSEventStream;
import org.processmining.eventstream.models.XSEventAuthor;
import org.processmining.stream.connections.XSAuthorXSStreamConnectionImpl;

public class XSEventXSAuthorXSStreamConnectionImpl extends XSAuthorXSStreamConnectionImpl<XSEventStream, XSEvent> {

	public XSEventXSAuthorXSStreamConnectionImpl(XSEventStream stream, XSEventAuthor author) {
		super(stream, author);
	}

}
