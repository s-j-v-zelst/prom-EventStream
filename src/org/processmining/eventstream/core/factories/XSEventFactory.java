package org.processmining.eventstream.core.factories;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;
import org.processmining.eventstream.core.implementations.XSEventImpl;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.eventstream.core.interfaces.XSEventSignature;

public class XSEventFactory {

	public static XSEvent createXSEvent() {
		return new XSEventImpl();
	}

	public static XSEvent createXSEvent(XAttribute correlationValue) {
		XSEvent xsEvent = createXSEvent();
		xsEvent.put(XSEventSignature.TRACE, correlationValue);
		return xsEvent;
	}

	public static XSEvent createXSEvent(XAttribute correlationValue, XAttributeMap attributes) {
		XSEvent xsEvent = createXSEvent(correlationValue);
		xsEvent.putAll((XAttributeMap) attributes.clone());
		return xsEvent;
	}

	public static List<XSEvent> createXSEventList(XTrace trace, String correlationKey) {
		List<XSEvent> list = new ArrayList<XSEvent>(trace.size());
		XAttribute correlationValue = trace.getAttributes().get(correlationKey);

		for (XEvent event : trace) {
			XSEvent xsEvent = createXSEvent();
			xsEvent.put(XSEventSignature.TRACE, correlationValue);
			xsEvent.putAll((XAttributeMap) event.getAttributes().clone());
			list.add(xsEvent);
		}

		return list;
	}
}
