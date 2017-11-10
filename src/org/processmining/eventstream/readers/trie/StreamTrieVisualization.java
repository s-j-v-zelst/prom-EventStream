package org.processmining.eventstream.readers.trie;

import java.util.Date;

import javax.swing.JComponent;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.processmining.eventstream.models.GraphStreamJPanel;
import org.processmining.framework.util.Pair;
import org.processmining.stream.core.abstracts.AbstractXSRunnable;
import org.processmining.stream.core.interfaces.XSVisualization;

public class StreamTrieVisualization<T, V extends VertexImpl<T>> extends AbstractXSRunnable
		implements XSVisualization<StreamTrieImpl<T, V>> {

	static {
		// use a more advanced renderer for the graph
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
	}

	private final GraphStreamJPanel graphPanel;
	private Graph graph;

	private static long elemCounter;

	private BidiMap vertices = new DualHashBidiMap();
	private BidiMap edges = new DualHashBidiMap();

	public StreamTrieVisualization(String name) {
		super(name);
		graph = new SingleGraph("Social Network");
		graphPanel = new GraphStreamJPanel(graph);
		graph.setStrict(false);
	}

	public JComponent asComponent() {
		return graphPanel;
	}

	@Deprecated
	public void update(Pair<Date, String> message) {
	}

	@Deprecated
	public void update(String object) {
	}

	protected void workPackage() {
	}

	public void updateVisualization(Pair<Date, StreamTrieImpl<T, V>> newArtifact) {
		updateVisualization(newArtifact);

	}

	public void updateVisualization(StreamTrieImpl<T, V> newArtifact) {
		// vertices
		for (V v : newArtifact.getVertices()) {
			if (!vertices.containsKey(v)) {
				String id = v.toString() + "_" + elemCounter;
				elemCounter++;
				vertices.put(v, id);
				Node n = graph.addNode(id);
				n.addAttribute("ui.label", v.getVertexObject().toString());
			}
		}
		// edges
		for (EdgeImpl<T, V> e : newArtifact.getEdges()) {
			if (!edges.containsKey(e)) {
				String id = e.toString() + "_" + elemCounter;
				elemCounter++;
				edges.put(e, id);
				graph.addEdge(id, vertices.get(e.getFrom()).toString(), vertices.get(e.getTo()).toString(), true);
			}
		}

		//TODO: REMOVAL OF NODES

	}

}
