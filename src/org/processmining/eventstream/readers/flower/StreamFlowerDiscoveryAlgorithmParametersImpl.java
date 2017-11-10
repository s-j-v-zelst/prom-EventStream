package org.processmining.eventstream.readers.flower;

import java.util.Map;

import org.processmining.eventstream.readers.abstr.XSEventReaderParameters;
import org.processmining.stream.model.datastructure.DSParameter;
import org.processmining.stream.model.datastructure.DSParameterDefinition;
import org.processmining.stream.model.datastructure.DataStructure.Type;

public class StreamFlowerDiscoveryAlgorithmParametersImpl extends XSEventReaderParameters {

	private Type ds = null;
	private Map<DSParameterDefinition, DSParameter<?>> dsParams = null;

	public StreamFlowerDiscoveryAlgorithmParametersImpl() {
		super();
	}

	public StreamFlowerDiscoveryAlgorithmParametersImpl(final StreamFlowerDiscoveryAlgorithmParametersImpl parameters) {
		super(parameters);

	}

	public Map<DSParameterDefinition, DSParameter<?>> getDataStructureParameters() {
		return dsParams;
	}

	public Type getDataStructureType() {
		return ds;
	}

	public void setDataStructureParameters(Map<DSParameterDefinition, DSParameter<?>> dsParams) {
		this.dsParams = dsParams;
	}

	public void setDataStructureType(Type ds) {
		this.ds = ds;
	}

}
