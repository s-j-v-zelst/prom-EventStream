package org.processmining.eventstream.core.factories;

import org.processmining.eventstream.core.implementations.XSEventStreamImpl;
import org.processmining.eventstream.core.interfaces.XSEventStream;
import org.processmining.stream.core.enums.CommunicationType;

public class XSEventStreamFactory {

	public static XSEventStream createXSEventStream(CommunicationType communicationType) {
		return new XSEventStreamImpl(communicationType);
	}

}
