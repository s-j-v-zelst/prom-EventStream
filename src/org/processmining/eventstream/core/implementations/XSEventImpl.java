package org.processmining.eventstream.core.implementations;

import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.processmining.eventstream.core.interfaces.XSEvent;

public class XSEventImpl extends XAttributeMapImpl implements XSEvent {

	private static final long serialVersionUID = 6140538108230456453L;
	private long size = 0;

	@Override
	public long diskSize() {
		return this.size;
	}

	@Override
	public XSEvent clone() {
		// Note: We can cast the clone method of XAttributeMapImpl
		// to XSEvent because both are a Map<String,XAttribute>
		return (XSEvent) super.clone();
	}
}
