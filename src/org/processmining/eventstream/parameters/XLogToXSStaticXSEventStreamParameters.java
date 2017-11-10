package org.processmining.eventstream.parameters;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XAttribute;
import org.processmining.basicutils.parameters.impl.PluginParametersImpl;

public class XLogToXSStaticXSEventStreamParameters extends PluginParametersImpl {

	public enum EmissionOrdering {
		LOG, TIME_STAMP;
	}
	
	private boolean additionalDecoration = false;
	private EmissionOrdering emissionOrdering = EmissionOrdering.LOG;
	private XEventClassifier eventClassifier = null;
	private boolean tagEventOrderNumber = false;
	private boolean tagFinalEvent = false;
	private XAttribute traceClassifier = null;

	public EmissionOrdering getEmissionOrdering() {
		return emissionOrdering;
	}


	public XEventClassifier getEventClassifier() {
		return eventClassifier;
	}

	public XAttribute getTraceClassifier() {
		return traceClassifier;
	}

	public boolean isAdditionalDecoration() {
		return additionalDecoration;
	}

	public boolean isTagEventOrderNumber() {
		return tagEventOrderNumber;
	}

	public boolean isTagFinalEvent() {
		return tagFinalEvent;
	}

	public void setAdditionalDecoration(boolean additionalDecoration) {
		this.additionalDecoration = additionalDecoration;
	}

	public void setEmissionOrdering(EmissionOrdering emissionOrdering) {
		this.emissionOrdering = emissionOrdering;
	}

	public void setEventClassifier(XEventClassifier eventClassifier) {
		this.eventClassifier = eventClassifier;
	}

	public void setTagEventOrderNumber(boolean tagEventOrderNumber) {
		this.tagEventOrderNumber = tagEventOrderNumber;
	}

	public void setTagFinalEvent(boolean tagFinalEvent) {
		this.tagFinalEvent = tagFinalEvent;
	}

	public void setTraceClassifier(XAttribute traceClassifier) {
		this.traceClassifier = traceClassifier;
	}

}
