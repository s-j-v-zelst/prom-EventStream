package org.processmining.eventstream.authors.xlog.parameters;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XAttribute;
import org.processmining.basicutils.parameters.impl.PluginParametersImpl;

public class XLogToXSEventStreamParameters extends PluginParametersImpl {

	public enum EmissionOrdering {
		LOG, TIME_STAMP;
	}
	
	// "REAL_LOG" emission speed is currently disabled.
	public enum EmissionSpeedType {
		RATE, REAL_LOG;
	}

	private boolean additionalDecoration = false;
	private EmissionOrdering emissionOrdering = EmissionOrdering.LOG;
	private int emissionRate = 100;
	private EmissionSpeedType emissionSpeedType = EmissionSpeedType.RATE;
	private XEventClassifier eventClassifier = null;
	private boolean tagEventOrderNumber = false;
	private boolean tagFinalEvent = false;
	private XAttribute traceClassifier = null;

	public EmissionOrdering getEmissionOrdering() {
		return emissionOrdering;
	}

	public int getEmissionRate() {
		return emissionRate;
	}

	public EmissionSpeedType getEmissionSpeedType() {
		return emissionSpeedType;
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

	public void setEmissionRate(int emissionRate) {
		this.emissionRate = emissionRate;
	}

	public void setEmissionSpeedType(EmissionSpeedType emissionSpeedType) {
		this.emissionSpeedType = emissionSpeedType;
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
