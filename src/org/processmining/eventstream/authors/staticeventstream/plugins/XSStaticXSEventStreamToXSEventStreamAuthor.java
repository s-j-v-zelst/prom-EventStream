package org.processmining.eventstream.authors.staticeventstream.plugins;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.eventstream.models.XSEventAuthor;
import org.processmining.stream.author.staticstream.AbstractStaticStreamXSAuthor;
import org.processmining.stream.core.interfaces.XSStaticStream;
import org.processmining.stream.visualization.XSNumberOfEventsSentVisualizationImpl;

public class XSStaticXSEventStreamToXSEventStreamAuthor extends AbstractStaticStreamXSAuthor<XSEvent, String>
		implements XSEventAuthor {

	public XSStaticXSEventStreamToXSEventStreamAuthor(String name, XSStaticStream<XSEvent> staticStream) {
		super(name, new XSNumberOfEventsSentVisualizationImpl("num_packets"), staticStream);
	}

	public Class<XSEvent> getTopic() {
		return XSEvent.class;
	}

	protected void updateVisualization(XSEvent data) {
		getVisualization().updateVisualization(data.get(XConceptExtension.KEY_NAME).toString());
	}

}
