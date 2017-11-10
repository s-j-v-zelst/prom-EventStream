package org.processmining.eventstream.authors.xlog.implementations;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeBooleanImpl;
import org.deckfour.xes.model.impl.XAttributeDiscreteImpl;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XAttributeTimestampImpl;
import org.processmining.eventstream.authors.xlog.parameters.XLogToXSEventStreamParameters;
import org.processmining.eventstream.core.factories.XSEventFactory;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.eventstream.core.interfaces.XSEventSignature;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.stream.core.abstracts.AbstractXSAuthor;
import org.processmining.stream.visualization.XSDynamicBarChart;

/**
 * The XLogToXSEventAuthor takes an XLog as an input and generates an
 * XSEventStream as an output. It will always create a linked list after which
 * it will start emitting events according to the list's order. How the list is
 * ordered and whether there is some intermediate break within emission is
 * specified by the user.
 */
public class XLogToXSEventAuthor extends AbstractXSAuthor<XSEvent, String> {

	private XLog log;
	private List<XSEvent> events = new LinkedList<XSEvent>();
	private List<Long> emissionDelay = new LinkedList<Long>();
	private Long generalizedDelay = 1L;
	private PluginContext context;
	private int eventIndex = 0;
	private XLogToXSEventStreamParameters parameters;
	
	private final static int DELTA = 25;

	public XLogToXSEventAuthor(final PluginContext context, XLog log, XLogToXSEventStreamParameters parameters) {
		// somehow using BPI Challenge 2012 without the "delta" gave the wrong number of event classes...
		super("XSEvent Author (input: XLog)", new XSDynamicBarChart("XLog Author Visualizer",
				XLogInfoFactory.createLogInfo(log, new XEventNameClassifier()).getEventClasses().size()+ DELTA));
		this.context = context;
		this.parameters = parameters;
		this.log = log;
		this.prepareEventEmissionList();
	}

	private void prepareEventEmissionList() {
		convertLogToLinkedList();
		sortEvents();
		calculateEmissionDelays();
	}

	private void convertLogToLinkedList() {
		XLogInfo logInfo = XLogInfoFactory.createLogInfo(log);
		context.log("Converting input XLog (" + log.getAttributes().get("concept:name").toString()
				+ ") to a chain of events...");
		context.getProgress().setMinimum(0);
		context.getProgress().setMaximum(logInfo.getNumberOfEvents());
		List<XSEvent> events = new LinkedList<XSEvent>();
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
								.equals(XLogToXSEventStreamParameters.EmissionOrdering.TIME_STAMP)) {
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
		this.events = events;
	}

	private void sortEvents() {
		if (parameters.getEmissionOrdering().equals(XLogToXSEventStreamParameters.EmissionOrdering.TIME_STAMP)) {
			context.log("Reordering chain of events based on time stamps");
			sortEventListByTimeStamp();
		}
	}

	private void sortEventListByTimeStamp() {
		Collections.sort(events, new Comparator<XSEvent>() {
			public int compare(XSEvent event1, XSEvent event2) {
				XAttributeTimestampImpl date1 = (XAttributeTimestampImpl) event1.get("time:timestamp");
				XAttributeTimestampImpl date2 = (XAttributeTimestampImpl) event2.get("time:timestamp");
				return date1.compareTo(date2);
			}
		});
	}

	private void calculateEmissionDelays() {
		switch (parameters.getEmissionSpeedType()) {
			case REAL_LOG :
				calculateLogBasedEmissionDelays();
				break;
			case RATE :
			default :
				calculateRateBasedEmissionDelay();
				break;
		}
	}

	private void calculateLogBasedEmissionDelays() {
		int index = 0;
		XAttributeTimestamp datePreviousEvent = null;
		XAttributeTimestamp dateEvent;
		for (XSEvent event : events) {
			if (index == 0) {
				datePreviousEvent = (XAttributeTimestamp) event.get("time:timestamp");
				index++;
			} else {
				dateEvent = (XAttributeTimestamp) event.get("time:timestamp");
				Long emissionDelay = dateEvent.getValueMillis() - datePreviousEvent.getValueMillis();
				this.emissionDelay.add(emissionDelay);
				index++;
				datePreviousEvent = dateEvent;
			}
		}
	}

	private void calculateRateBasedEmissionDelay() {
		generalizedDelay = 1000L / parameters.getEmissionRate();
	}

	@Override
	public void workPackage() {
		if (eventIndex < events.size()) {
			XSEvent event = events.get(eventIndex);
			write(event);
			try {
				Thread.sleep(getEmissionDelay(eventIndex));
				eventIndex++;
				getVisualization().updateVisualization(event.get(XConceptExtension.KEY_NAME).toString());
			} catch (InterruptedException e) {
				if (!isStopped()) {
					e.printStackTrace();
				}
				parameters.displayMessage("Log based stream generator was stopped!");
			}
		} else {
			stopXSRunnable();
		}
	}

	private Long getEmissionDelay(int eventIndex) {
		Long delay;
		switch (parameters.getEmissionSpeedType()) {
			case REAL_LOG :
				if (eventIndex < emissionDelay.size()) {
					delay = emissionDelay.get(eventIndex);
				} else {
					delay = 0L;
				}
				break;
			case RATE :
			default :
				delay = generalizedDelay;
				break;
		}
		return delay;
	}

	public Class<XSEvent> getTopic() {
		return XSEvent.class;
	}
}
