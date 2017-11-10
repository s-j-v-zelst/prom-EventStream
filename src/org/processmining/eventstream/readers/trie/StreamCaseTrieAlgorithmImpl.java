package org.processmining.eventstream.readers.trie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.processmining.stream.core.interfaces.XSVisualization;
import org.processmining.stream.model.datastructure.DSParameterMissingException;
import org.processmining.stream.model.datastructure.DSWrongParameterException;
import org.processmining.stream.model.datastructure.DataStructure;
import org.processmining.stream.model.datastructure.DataStructure.Type;
import org.processmining.stream.model.datastructure.DataStructureFactory;
import org.processmining.stream.model.datastructure.ItemPointerPair;
import org.processmining.stream.model.datastructure.PointerBasedDataStructure;

import gnu.trove.set.hash.THashSet;

public abstract class StreamCaseTrieAlgorithmImpl<T, R, P extends StreamCaseTrieAlgorithmParameters>
		extends AbstractStreamCaseTrieAlgorithmImpl<T, VertexImpl<T>, StreamTrieImpl<T, VertexImpl<T>>, R, P> {

	public static Collection<Type> DEFAULT_ALLOWED_CASE_DATA_STRUCTURES = DataStructure.POINTER_DATA_STRUCTURES;
	private PointerBasedDataStructure<String, VertexImpl<T>> caseDS;

	public StreamCaseTrieAlgorithmImpl(String name, XSVisualization<R> visualization, P parameters,
			StreamTrieImpl<T, VertexImpl<T>> graph) {
		super(name, visualization, parameters, graph);
		try {
			caseDS = DataStructureFactory.createPointerDataStructure(parameters.getCaseDataStructureType(),
					parameters.getCaseDataStructureParameters());
		} catch (OperationNotSupportedException | DSParameterMissingException | DSWrongParameterException e) {
			e.printStackTrace();
			caseDS = null;
			this.interrupt();
		}
	}

	protected void annotateEdge(EdgeImpl<T, VertexImpl<T>> edge) {
	}

	protected void annotateVertex(VertexImpl<T> vertex) {
	}

	protected VertexImpl<T> constructNewAnnotatedVertex(T object) {
		return new VertexImpl<T>(object);
	}

	protected VertexImpl<T> getCurrentVertex(String caseId) {
		if (caseDS.contains(caseId)) {
			return caseDS.getPointedElement(caseId);
		} else {
			VertexImpl<T> root = getTrie().getRoot();
			root.setCount(root.getCount() + 1);
			return root;
		}
	}

	protected Collection<List<EdgeImpl<T, VertexImpl<T>>>> updateAdministrationAfterGraphUpdate(VertexImpl<T> vertex,
			EdgeImpl<T, VertexImpl<T>> edge, String caseId) {
		Collection<List<EdgeImpl<T, VertexImpl<T>>>> removedPathsFromTrie = new THashSet<>();
		Collection<ItemPointerPair<String, VertexImpl<T>>> removedCases = caseDS.add(caseId, vertex);
		for (ItemPointerPair<String, VertexImpl<T>> pointer : removedCases) {
			// the case might exist multiple times in for example a sliding window!
			if (!caseDS.contains(pointer.getItem())) {
				List<EdgeImpl<T, VertexImpl<T>>> removedPath = new ArrayList<>();
				VertexImpl<T> v = pointer.getPointer();
				while (!v.equals(getTrie().getRoot())) {
					EdgeImpl<T, VertexImpl<T>> e = getTrie().getInEdge(v);
					v.setCount(v.getCount() - 1);
					if (v.getCount() == 0) {
						getTrie().remove(v);
					}
					e.setCount(e.getCount() - 1);
					if (e.getCount() == 0) {
						getTrie().remove(e);
					}
					v = e.getFrom();
					removedPath.add(e);
				}
				getTrie().getRoot().setCount(getTrie().getRoot().getCount() - 1);
				removedPathsFromTrie.add(removedPath);
			}
		}
		return removedPathsFromTrie;
	}
}
