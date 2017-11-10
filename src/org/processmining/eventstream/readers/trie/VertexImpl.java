package org.processmining.eventstream.readers.trie;

import java.util.HashMap;
import java.util.Map;

public class VertexImpl<T> implements Cloneable {

	private final Map<String, Object> annotation = new HashMap<>();

	private int count;

	private int depth;

	private final T obj;

	public VertexImpl(T obj) {
		this.obj = obj;
	}

	public VertexImpl(VertexImpl<T> v) {
		count = v.getCount();
		depth = v.getDepth();
		annotation.putAll(v.getAnnotation());
		obj = v.obj;
	}

	public Map<String, Object> getAnnotation() {
		return annotation;
	}

	public int getCount() {
		return count;
	}

	public int getDepth() {
		return depth;
	}

	public T getVertexObject() {
		return obj;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	@Override
	public String toString() {
		return "V(" + obj.toString() + ", " + annotation.toString() + ")_" + System.identityHashCode(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (o instanceof VertexImpl) {
			VertexImpl<T> c = (VertexImpl<T>) o;
			boolean res = c.getCount() == count;
			res &= c.getDepth() == depth;
			res &= c.getAnnotation().equals(annotation);
			res &= c.getVertexObject().equals(obj);
			return res;
		}
		return false;
	}

	public Object clone() {
		return new VertexImpl<T>(this);
	}
}
