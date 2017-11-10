package org.processmining.eventstream.algorithms;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeBooleanImpl;
import org.deckfour.xes.model.impl.XAttributeDiscreteImpl;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XAttributeTimestampImpl;
import org.processmining.eventstream.core.factories.XSEventFactory;
import org.processmining.eventstream.core.implementations.XSStaticXSEventStreamLinkedListImpl;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.eventstream.core.interfaces.XSEventSignature;
import org.processmining.eventstream.core.interfaces.XSStaticXSEventStream;
import org.processmining.eventstream.parameters.XLogToXSStaticXSEventStreamParameters;
import org.processmining.framework.plugin.PluginContext;

public class XLogToXSStaticXSEventStreamAlgorithm {

	private XLog log;
	private XSStaticXSEventStream stream = new XSStaticXSEventStreamLinkedListImpl();
	private PluginContext context;
	private XLogToXSStaticXSEventStreamParameters parameters;

	public XLogToXSStaticXSEventStreamAlgorithm(final PluginContext context, XLog log,
			XLogToXSStaticXSEventStreamParameters parameters) {
		this.context = context;
		this.parameters = parameters;
		this.log = log;
		this.constructStaticEventStream();
	}

	public XSStaticXSEventStream get() {
		return stream;
	}

	private void constructStaticEventStream() {
		convertLogToLinkedList();
		sortEvents();
	}

	private void convertLogToLinkedList() {
		XLogInfo logInfo = XLogInfoFactory.createLogInfo(log);
		context.log("Converting input XLog (" + log.getAttributes().get("concept:name").toString()
				+ ") to a static event stream...");
		context.getProgress().setMinimum(0);
		context.getProgress().setMaximum(logInfo.getNumberOfEvents());
		XSStaticXSEventStream events = new XSStaticXSEventStreamLinkedListImpl();
		int traceIndex = 1;
		for (XTrace trace : log) {
			int eventIndex = 0;
			int traceSize = trace.size();
			XAttributeMap traceAttributes = trace.getAttributes();
			XAttribute streamTrace;
			if (parameters.getTraceClassifier() != null) {
				XAttribute traceIdentifier = traceAttributes.get(parameters.getTraceClassifier().getKey());
				streamTrace = XFactoryRegistry.instance().currentDefault()
						.createAttributeLiteral(XSEventSignature.TRACE, traceIdentifier.toString(), null);
			} else {
				streamTrace = XFactoryRegistry.instance().currentDefault()
						.createAttributeLiteral(XSEventSignature.TRACE, Integer.toString(traceIndex), null);
				traceIndex++;
			}
			for (XEvent event : trace) {
				XAttributeMap logEventAttributes = event.getAttributes();
				XAttributeMap streamEventAttributes = new XAttributeMapImpl();

				streamEventAttributes.put(XSEventSignature.TRACE, streamTrace);
				streamEventAttributes.put(XConceptExtension.KEY_NAME,
						XFactoryRegistry.instance().currentDefault().createAttributeLiteral(XConceptExtension.KEY_NAME,
								parameters.getEventClassifier().getClassIdentity(event), XConceptExtension.instance()));

				if (parameters.isAdditionalDecoration()) {
					for (String s : logEventAttributes.keySet()) {
						XAttribute attr = logEventAttributes.get(s);
						streamEventAttributes.put("xsevent:data:" + attr.getKey(),
								XFactoryRegistry.instance().currentDefault().createAttributeLiteral(
										"xsevent:data:" + attr.getKey(), attr.toString(), null));
					}
				}

				for (Map.Entry<String, XAttribute> attribute : event.getAttributes().entrySet()) {
					if (attribute.getKey().equals(XTimeExtension.KEY_TIMESTAMP)) {
						if (parameters.getEmissionOrdering()
								.equals(XLogToXSStaticXSEventStreamParameters.EmissionOrdering.TIME_STAMP)) {
							streamEventAttributes.put(attribute.getKey(), attribute.getValue());
						}
					}
				}

				if (parameters.isTagEventOrderNumber()) {
					XAttribute eventOrderNrAttr = new XAttributeDiscreteImpl("xsevent:index", eventIndex);
					streamEventAttributes.put("xsevent:index", eventOrderNrAttr);
				}

				if (parameters.isTagFinalEvent()) {
					boolean isFinal = (eventIndex == traceSize - 1 ? true : false);
					XAttribute eventIsFinalAttr = new XAttributeBooleanImpl("xsevent:final", isFinal);
					streamEventAttributes.put("xsevent:final", eventIsFinalAttr);
				}
				XSEvent streamAbleEvent = XSEventFactory.createXSEvent(streamTrace, streamEventAttributes);
				events.add(streamAbleEvent);
				eventIndex++;
				context.getProgress().inc();
			}
		}
		this.stream = events;
	}

	private void sortEvents() {
		if (parameters.getEmissionOrdering()
				.equals(XLogToXSStaticXSEventStreamParameters.EmissionOrdering.TIME_STAMP)) {
			context.log("Reordering chain of events based on time stamps");
			sortEventListByTimeStamp();
		}
	}

	private void sortEventListByTimeStamp() {
		Collections.sort(stream, new Comparator<XSEvent>() {
			public int compare(XSEvent event1, XSEvent event2) {
				XAttributeTimestampImpl date1 = (XAttributeTimestampImpl) event1.get("time:timestamp");
				XAttributeTimestampImpl date2 = (XAttributeTimestampImpl) event2.get("time:timestamp");
				return date1.compareTo(date2);
			}
		});
	}
}
