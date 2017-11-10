package org.processmining.eventstream.readers.trie;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class StreamTrieImpl<T, V extends VertexImpl<T>> implements Cloneable {

	private final Collection<EdgeImpl<T, V>> edges = new HashSet<>();
	private final Map<V, EdgeImpl<T, V>> in = new HashMap<>();
	private final Object lock = new Object();
	private final Map<V, Collection<EdgeImpl<T, V>>> out = new HashMap<>();
	private final V root;

	public StreamTrieImpl(V root) {
		super();
		this.root = root;
		root.setDepth(0);
		root.setCount(0);
	}

	@SuppressWarnings("unchecked")
	public StreamTrieImpl(StreamTrieImpl<T, V> trie) {
		Map<V, V> copies = new HashMap<>();
		root = (V) trie.getRoot().clone();
		copies.put(trie.getRoot(), root);
		for (EdgeImpl<T, V> e : trie.getEdges()) {
			V from = copies.get(e.getFrom());
			if (from == null) {
				from = (V) e.getFrom().clone();
				copies.put(e.getFrom(), from);
			}
			V to = copies.get(e.getTo());
			if (to == null) {
				to = (V) e.getTo().clone();
				copies.put(e.getTo(), from);
			}
			edges.add(new EdgeImpl<T, V>(from, to));
		}
		for (EdgeImpl<T, V> e : edges) {
			in.put(e.getTo(), e);
			if (!out.containsKey(e.getFrom())) {
				out.put(e.getFrom(), new HashSet<EdgeImpl<T, V>>());
			}
			out.get(e.getFrom()).add(e);
		}
	}

	/**
	 * adds an edge from "from" to "to". If the edge already exists, the
	 * annotation will be updated.
	 * 
	 * @param from
	 * @param to
	 * @param edgeAnnotation
	 * @return
	 */
	public EdgeImpl<T, V> addEdge(V from, V to, Map<String, Object> edgeAnnotation) {
		EdgeImpl<T, V> edge = null;
		if (!out.containsKey(from)) {
			out.put(from, new HashSet<EdgeImpl<T, V>>());
		}
		for (EdgeImpl<T, V> e : out.get(from)) {
			if (e.getTo().getVertexObject().equals(to.getVertexObject())) {
				edge = e;
				break;
			}
		}
		edge = edge == null ? new EdgeImpl<>(from, to) : edge;
		edge.getAnnotation().clear();
		if (edgeAnnotation != null) {
			edge.getAnnotation().putAll(edgeAnnotation);
		}
		out.get(from).add(edge);
		in.put(to, edge);
		edges.add(edge);
		return edge;
	}

	public Collection<EdgeImpl<T, V>> getEdges() {
		return edges;
	}

	public EdgeImpl<T, V> getInEdge(V v) {
		return in.containsKey(v) ? in.get(v) : null;
	}

	public Object getLock() {
		return lock;
	}

	public Collection<EdgeImpl<T, V>> getOutEdges(V v) {
		return out.containsKey(v) ? out.get(v) : new HashSet<EdgeImpl<T, V>>();
	}

	public V getRoot() {
		return root;
	}

	public Collection<V> getVertices() {
		Collection<V> vertices = new HashSet<>();
		vertices.addAll(out.keySet());
		vertices.addAll(in.keySet());
		return vertices;
	}

	public void remove(EdgeImpl<T, V> e) {
		edges.remove(e);
		if (in.containsKey(e.getTo())) {
			in.remove(e.getTo());
		}
		if (out.containsKey(e.getFrom())) {
			out.get(e.getFrom()).remove(e);
		}
	}

	public void remove(V v) {
		if (in.containsKey(v)) {
			edges.remove(in.get(v));
		}
		if (out.containsKey(v)) {
			edges.removeAll(out.get(v));
		}
		in.remove(v);
		out.remove(v);
	}

	public Object clone() {
		return new StreamTrieImpl<T, V>(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (o instanceof StreamTrieImpl) {
			StreamTrieImpl<T, V> c = (StreamTrieImpl<T, V>) o;
			boolean res = c.getEdges().equals(edges);
			res &= c.in.equals(in);
			res &= c.out.equals(out);
			res &= c.getRoot().equals(root);
			return res;
		}
		return false;
	}

}
