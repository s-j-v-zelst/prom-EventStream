package org.processmining.eventstream.readers.trie;

import java.util.Collection;
import java.util.List;

import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.framework.util.Pair;
import org.processmining.stream.core.abstracts.AbstractXSReader;
import org.processmining.stream.core.interfaces.XSVisualization;

/**
 * constructs a prefix tree based on a sequence of objects within the stream.
 * these objects could be activities, resources etc.
 * 
 * @author svzelst
 *
 * @param <V>
 * @param <G>
 * @param <P>
 */
public abstract class AbstractStreamCaseTrieAlgorithmImpl<T, V extends VertexImpl<T>, G extends StreamTrieImpl<T, V>, R, P extends StreamCaseTrieAlgorithmParameters>
		extends AbstractXSReader<XSEvent, R, R> {

	private final G trie;
	private final P parameters;

	public AbstractStreamCaseTrieAlgorithmImpl(String name, XSVisualization<R> visualization, P parameters, G graph) {
		super(name, visualization);
		this.trie = graph;
		this.parameters = parameters;
	}

	protected abstract void annotateEdge(EdgeImpl<T, V> edge);

	protected abstract void annotateVertex(V vertex);

	protected Pair<V, EdgeImpl<T, V>> checkIfEdgeExists(V source, T targetObject) {
		if (source != null && targetObject != null) {
			for (EdgeImpl<T, V> e : trie.getOutEdges(source)) {
				if (e.getFrom() == source && e.getTo().getVertexObject().equals(targetObject)) {
					return new Pair<V, EdgeImpl<T, V>>(e.getTo(), e);
				}
			}
		}
		return null;
	}

	protected abstract V constructNewAnnotatedVertex(T object);

	protected abstract V getCurrentVertex(String caseId);

	/**
	 * Update any administration that is backing the trie, e.g., a data
	 * structure in which cases point to nodes in the trie. Optionally a list of
	 * removed edges is returned.
	 * 
	 * @param vertex
	 * @param edge
	 * @param caseId
	 * @return
	 */
	protected abstract Collection<List<EdgeImpl<T, V>>> updateAdministrationAfterGraphUpdate(V vertex,
			EdgeImpl<T, V> edge, String caseId);

	protected abstract void handleNextTrie(G trie, EdgeImpl<T, V> newEdge,
			Collection<List<EdgeImpl<T, V>>> removedEdges);

	protected abstract T createTargetObjectFromEvent(XSEvent event);

	public G getTrie() {
		return trie;
	}

	public P getParameters() {
		return parameters;
	}

	public Class<XSEvent> getTopic() {
		return XSEvent.class;
	}

	protected void handleNextPacket(XSEvent packet) {
		if (packet.keySet().containsAll(parameters.getMandatoryKeys())) {
			// we need the lock because a user may trigger a swap of network build on top of the lock.
			// we don't want to build the network on a trie that is being changed.
			synchronized (trie.getLock()) {
				String caseId = packet.get(parameters.getCaseIdentifier()).toString();
				T object = createTargetObjectFromEvent(packet);
				V currentVertex = getCurrentVertex(caseId);
				Pair<V, EdgeImpl<T, V>> vertexEdge = checkIfEdgeExists(currentVertex, object);
				V vertex = vertexEdge == null ? null : vertexEdge.getFirst();
				EdgeImpl<T, V> edge = vertexEdge == null ? null : vertexEdge.getSecond();
				if (vertex == null) {
					vertex = constructNewAnnotatedVertex(object);
					vertex.setCount(0);
					vertex.setDepth(currentVertex.getDepth() + 1);
					edge = trie.addEdge(currentVertex, vertex, null);
					edge.setCount(0);
				}
				annotateVertex(vertex);
				annotateEdge(edge);
				vertex.setCount(vertex.getCount() + 1);
				edge.setCount(edge.getCount() + 1);
				Collection<List<EdgeImpl<T, V>>> removedEdges = updateAdministrationAfterGraphUpdate(vertex, edge,
						caseId);
				handleNextTrie(trie, edge, removedEdges);
			}
		}
	}

}
