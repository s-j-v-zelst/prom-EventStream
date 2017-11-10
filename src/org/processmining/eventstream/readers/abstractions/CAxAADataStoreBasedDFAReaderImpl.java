package org.processmining.eventstream.readers.abstractions;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import org.deckfour.xes.classification.XEventClass;
import org.processmining.eventstream.readers.abstr.AbstractXSEventReader;
import org.processmining.framework.util.Pair;
import org.processmining.logabstractions.factories.DirectlyFollowsAbstractionFactory;
import org.processmining.logabstractions.models.DirectlyFollowsAbstraction;
import org.processmining.logabstractions.models.implementations.DirectlyFollowsAbstractionImpl;
import org.processmining.stream.model.datastructure.DSParameterMissingException;
import org.processmining.stream.model.datastructure.DSWrongParameterException;
import org.processmining.stream.model.datastructure.DataStructure.Type;
import org.processmining.stream.model.datastructure.DataStructureFactory;
import org.processmining.stream.model.datastructure.IterableDataStructure;
import org.processmining.stream.model.datastructure.PointerBasedDataStructure;

@Deprecated // -> moved to StreamAbstractRepresentationPackage 
public class CAxAADataStoreBasedDFAReaderImpl extends
		AbstractXSEventReader<DirectlyFollowsAbstraction<XEventClass>, Object, XSEventStreamToDFAReaderParameters> {

	public static Collection<Type> DEFAULT_ALLOWED_ACTIVITY_ACTIVITY_DATA_STRUCTURES = EnumSet.of(Type.LOSSY_COUNTING,
			Type.FREQUENT_ALGORITHM, Type.SPACE_SAVING);

	public static Collection<Type> DEFAULT_ALLOWED_CASE_ACTIVITY_DATA_STRUCTURES = EnumSet.of(
			Type.LOSSY_COUNTING_POINTER, Type.FREQUENT_POINTER, Type.SPACE_SAVING_POINTER,
			Type.COUNT_MIN_SKETCH_POINTER);

	private PointerBasedDataStructure<String, XEventClass> caseActivityDataStructure;
	private IterableDataStructure<Pair<XEventClass, XEventClass>> activityActivityDataStructure;

	public CAxAADataStoreBasedDFAReaderImpl(String name, XSEventStreamToDFAReaderParameters params) {
		super(name, null, params);
		try {
			caseActivityDataStructure = DataStructureFactory.createPointerDataStructure(
					params.getCaseActivityDataStructureType(), params.getCaseActivityDataStructureParameters());
		} catch (OperationNotSupportedException | DSParameterMissingException | DSWrongParameterException e) {
			e.printStackTrace();
			caseActivityDataStructure = null;
			this.interrupt();
		}
		try {
			activityActivityDataStructure = DataStructureFactory.createIterableDataStructure(
					params.getActivityActivityDataStructureType(), params.getActivityActivityDataStructureParameters());
		} catch (DSParameterMissingException | DSWrongParameterException | OperationNotSupportedException e) {
			e.printStackTrace();
			activityActivityDataStructure = null;
			this.interrupt();
		}
	}

	public PointerBasedDataStructure<String, XEventClass> getCaseActivityDataStructure() {
		return caseActivityDataStructure;
	}

	public IterableDataStructure<Pair<XEventClass, XEventClass>> getActivityActivityDataStructure() {
		return activityActivityDataStructure;
	}

	//FIXME #1: Currently, the dfa is re-computed every time we compute a result.
	// in a less academic setting we should keep the data structure live..
	//FIXME #2: We should not build the dfa with completely unused
	// activities as it will give trouble for the alpha miner... This pruning
	// is rather costly and should be improved in terms of performance
	protected DirectlyFollowsAbstraction<XEventClass> computeCurrentResult() {
		Map<XEventClass, Integer> usedClasses = new HashMap<>();
		XEventClass[] classArr = new XEventClass[0];
		for (Pair<XEventClass, XEventClass> dfr : getActivityActivityDataStructure()) {
			XEventClass first = dfr.getFirst();
			XEventClass sec = dfr.getSecond();
			XEventClass newC;
			if (!usedClasses.containsKey(first)) {
				classArr = Arrays.copyOf(classArr, classArr.length + 1);
				int index = classArr.length - 1;
				newC = new XEventClass(first.getId(), index);
				classArr[index] = newC;
				usedClasses.put(newC, index);
			}
			if (!usedClasses.containsKey(sec)) {
				classArr = Arrays.copyOf(classArr, classArr.length + 1);
				int index = classArr.length - 1;
				newC = new XEventClass(sec.getId(), index);
				classArr[index] = newC;
				usedClasses.put(newC, index);
			}
		}
		int numClasses = classArr.length;
		double[][] matrix = new double[numClasses][numClasses];
		for (Pair<XEventClass, XEventClass> dfr : getActivityActivityDataStructure()) {
			matrix[usedClasses.get(dfr.getFirst())][usedClasses
					.get(dfr.getSecond())] = getActivityActivityDataStructure().getFrequencyOf(dfr);
		}
		return new DirectlyFollowsAbstractionImpl<>(classArr, matrix,
				DirectlyFollowsAbstractionFactory.DEFAULT_THRESHOLD_BOOLEAN);
	}

	public void processNewXSEvent(String caseId, XEventClass activity) {
		if (getCaseActivityDataStructure().contains(caseId)) {
			XEventClass previousActivityForCase = getCaseActivityDataStructure().getPointedElement(caseId);
			getActivityActivityDataStructure()
					.add(new Pair<XEventClass, XEventClass>(previousActivityForCase, activity));
		}
		getCaseActivityDataStructure().add(caseId, activity);
	}

	@Override
	public long measureUsedMemory() {
		long res;
		try {
			res = caseActivityDataStructure.getUsedMemoryInBytes();
			res += activityActivityDataStructure.getUsedMemoryInBytes();
		} catch (IllegalStateException e) {
			// please try: -javaagent:<path to>/jamm.jar in JVM arguments
			res = -1;
		}
		return res;
	}

}
