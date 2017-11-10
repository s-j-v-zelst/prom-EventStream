package org.processmining.eventstream.readers.staticeventstream.parameters;

import org.processmining.eventstream.readers.abstr.XSEventReaderParameters;

public class XSEventStreamToXSStaticEventStreamParameters extends XSEventReaderParameters {

	private int totalNumberOfEvents = 0;

	public XSEventStreamToXSStaticEventStreamParameters() {
	}

	public XSEventStreamToXSStaticEventStreamParameters(int totalNumberOfEvents) {
		this.setTotalNumberOfEvents(totalNumberOfEvents);
	}

	public int getTotalNumberOfEvents() {
		return totalNumberOfEvents;
	}

	public void setTotalNumberOfEvents(int totalNumberOfEvents) {
		this.totalNumberOfEvents = totalNumberOfEvents;
	}

}
