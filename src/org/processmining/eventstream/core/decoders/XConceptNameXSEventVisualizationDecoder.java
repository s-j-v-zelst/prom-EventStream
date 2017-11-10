package org.processmining.eventstream.core.decoders;

import java.util.Date;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.framework.util.Pair;
import org.processmining.stream.core.interfaces.XSDataPacketVisualizer;

public class XConceptNameXSEventVisualizationDecoder
		implements XSDataPacketVisualizer<XSEvent, Pair<Date, String>> {

	private static XConceptNameXSEventVisualizationDecoder singleton = null;

	private XConceptNameXSEventVisualizationDecoder() {
	}

	public static XConceptNameXSEventVisualizationDecoder instance() {
		if (singleton == null) {
			singleton = new XConceptNameXSEventVisualizationDecoder();
		}
		return singleton;
	}

	public Pair<Date, String> decode(XSEvent packet) {
		return new Pair<Date, String>(new Date(), packet.get(XConceptExtension.KEY_NAME).toString());
	}

}
