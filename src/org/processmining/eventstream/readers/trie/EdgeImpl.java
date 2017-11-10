package org.processmining.eventstream.readers.trie;

import java.util.HashMap;
import java.util.Map;

public class EdgeImpl<T, V extends VertexImpl<T>> implements Cloneable {

	private final Map<String, Object> annotation = new HashMap<>();

	private int count;

	private final V from;

	private final V to;

	public EdgeImpl(V from, V to) {
		this.from = from;
		this.to = to;
	}

	@SuppressWarnings("unchecked")
	public EdgeImpl(EdgeImpl<T, V> e) {
		count = e.getCount();
		annotation.putAll(e.getAnnotation());
		from = (V) e.getFrom().clone();
		to = (V) e.getTo().clone();
	}

	public Map<String, Object> getAnnotation() {
		return annotation;
	}

	public int getCount() {
		return count;
	}

	public V getFrom() {
		return from;
	}

	public V getTo() {
		return to;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "(" + from.toString() + "," + to.toString() + ", " + annotation.toString() + ")";
	}

	public Object clone() {
		return new EdgeImpl<T, V>(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (o instanceof EdgeImpl) {
			EdgeImpl<T, V> c = (EdgeImpl<T, V>) o;
			boolean res = c.getAnnotation().equals(annotation);
			res &= c.getCount() == count;
			res &= c.getFrom().equals(from);
			res &= c.getTo().equals(to);
			return res;
		}
		return false;
	}
}
