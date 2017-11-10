package org.processmining.eventstream.readers.trie;

import java.util.List;
import java.util.Map;

import org.processmining.eventstream.readers.abstr.XSEventReaderParameters;
import org.processmining.stream.model.datastructure.DSParameter;
import org.processmining.stream.model.datastructure.DSParameterDefinition;
import org.processmining.stream.model.datastructure.DSParameterFactory;
import org.processmining.stream.model.datastructure.DataStructure.Type;

public class StreamCaseTrieAlgorithmParameters extends XSEventReaderParameters {

	private Type caseDataStructure = Type.FORWARD_EXPONENTIAL_DECAY_POINTER;
	private Map<DSParameterDefinition, DSParameter<?>> caseDataStructureParameters = DSParameterFactory
			.createDefaultParameters(Type.FORWARD_EXPONENTIAL_DECAY_POINTER);

	/**
	 * keys that together define tuples within the case trie
	 */
	private List<String> mandatoryKeys;

	public Type getCaseDataStructure() {
		return caseDataStructure;
	}

	public Map<DSParameterDefinition, DSParameter<?>> getCaseDataStructureParameters() {
		return caseDataStructureParameters;
	}

	public Type getCaseDataStructureType() {
		return caseDataStructure;
	}

	public void setCaseDataStructure(Type caseDataStructure) {
		this.caseDataStructure = caseDataStructure;
	}

	public void setCaseDataStructureParameters(Map<DSParameterDefinition, DSParameter<?>> caseDataStructureParameters) {
		this.caseDataStructureParameters = caseDataStructureParameters;
	}

	/**
	 * @return the artifactKeys
	 */
	public List<String> getMandatoryKeys() {
		return mandatoryKeys;
	}

	/**
	 * @param artifactKeys
	 *            the artifactKeys to set
	 */
	public void setMandatoryKeys(List<String> artifactKeys) {
		this.mandatoryKeys = artifactKeys;
	}

}
