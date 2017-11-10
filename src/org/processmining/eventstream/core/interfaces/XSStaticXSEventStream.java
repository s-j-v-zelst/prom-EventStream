package org.processmining.eventstream.core.interfaces;

import java.util.Collection;

import org.processmining.eventstream.core.implementations.XSStaticXSEventStreamArrayListImpl;
import org.processmining.eventstream.core.implementations.XSStaticXSEventStreamLinkedListImpl;
import org.processmining.stream.core.interfaces.XSStaticStream;

public interface XSStaticXSEventStream extends XSStaticStream<XSEvent> {

	public class Factory {
		public static XSStaticXSEventStream createArrayListBasedXSStaticXSEventStream() {
			return new XSStaticXSEventStreamArrayListImpl();
		}

		public static XSStaticXSEventStream createArrayListBasedXSStaticXSEventStream(Collection<XSEvent> coll) {
			return new XSStaticXSEventStreamArrayListImpl(coll);
		}

		public static XSStaticXSEventStream createLinkedListBasedXSStaticXSEventStream() {
			return new XSStaticXSEventStreamLinkedListImpl();
		}
	}

}
