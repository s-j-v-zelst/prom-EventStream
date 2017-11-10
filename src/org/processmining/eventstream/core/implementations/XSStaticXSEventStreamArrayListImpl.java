package org.processmining.eventstream.core.implementations;

import java.util.ArrayList;
import java.util.Collection;

import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.eventstream.core.interfaces.XSStaticXSEventStream;

public class XSStaticXSEventStreamArrayListImpl extends ArrayList<XSEvent> implements XSStaticXSEventStream {

	private static final long serialVersionUID = 5986822370940899674L;

	public XSStaticXSEventStreamArrayListImpl() {
		super();
	}

	public XSStaticXSEventStreamArrayListImpl(Collection<XSEvent> c) {
		super(c);
	}

}
