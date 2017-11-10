package org.processmining.eventstream.readers.abstractions;

import java.util.HashMap;
import java.util.Map;

import org.processmining.eventstream.readers.abstr.XSEventReaderParameters;
import org.processmining.stream.model.datastructure.DSParameter;
import org.processmining.stream.model.datastructure.DSParameterDefinition;
import org.processmining.stream.model.datastructure.DataStructure.Type;

@Deprecated // -> moved to StreamAbstractRepresentation package
public class XSEventStreamToDFAReaderParameters extends XSEventReaderParameters {

	private Type activityActivityDataStructure = null;
	private Map<DSParameterDefinition, DSParameter<?>> activityActivityParams = null;
	private Type caseActivityDataStructure = null;
	private Map<DSParameterDefinition, DSParameter<?>> caseActivityParams = null;

	public XSEventStreamToDFAReaderParameters() {
		super();
	}

	public XSEventStreamToDFAReaderParameters(final XSEventStreamToDFAReaderParameters params) {
		super(params);
		this.activityActivityDataStructure = params.getActivityActivityDataStructureType();
		this.activityActivityParams = new HashMap<>(params.getActivityActivityDataStructureParameters());
		this.caseActivityDataStructure = params.getCaseActivityDataStructureType();
		this.caseActivityParams = new HashMap<>(params.getCaseActivityDataStructureParameters());
	}

	public Type getActivityActivityDataStructureType() {
		return activityActivityDataStructure;
	}

	public Map<DSParameterDefinition, DSParameter<?>> getActivityActivityDataStructureParameters() {
		return activityActivityParams;
	}

	public Type getCaseActivityDataStructureType() {
		return caseActivityDataStructure;
	}

	public Map<DSParameterDefinition, DSParameter<?>> getCaseActivityDataStructureParameters() {
		return caseActivityParams;
	}

	public void setActivityActivityDataStructureType(Type activityActivityDataStructure) {
		this.activityActivityDataStructure = activityActivityDataStructure;
	}

	public void setActivityActivityDataStructureParameters(
			Map<DSParameterDefinition, DSParameter<?>> activityActivityParams) {
		this.activityActivityParams = activityActivityParams;
	}

	public void setCaseActivityDataStructureType(Type caseActivityDataStructure) {
		this.caseActivityDataStructure = caseActivityDataStructure;
	}

	public void setCaseActivityDataStructureParameters(Map<DSParameterDefinition, DSParameter<?>> caseActivityParams) {
		this.caseActivityParams = caseActivityParams;
	}

	@Override
	public boolean equals(Object o) {
		if (!super.equals(o))
			return false;
		if (o instanceof XSEventStreamToDFAReaderParameters) {
			XSEventStreamToDFAReaderParameters c = (XSEventStreamToDFAReaderParameters) o;
			if (!c.getActivityActivityDataStructureParameters().equals(getActivityActivityDataStructureParameters()))
				return false;
			if (!c.getActivityActivityDataStructureType().equals(getActivityActivityDataStructureType()))
				return false;
			if (!c.getCaseActivityDataStructureParameters().equals(getCaseActivityDataStructureParameters()))
				return false;
			if (!c.getCaseActivityDataStructureType().equals(c.getCaseActivityDataStructureType()))
				return false;
			return true;
		}
		return false;
	}
}
