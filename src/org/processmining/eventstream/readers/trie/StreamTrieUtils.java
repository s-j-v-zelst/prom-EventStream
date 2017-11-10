package org.processmining.eventstream.readers.trie;

import java.util.ArrayList;
import java.util.List;

public class StreamTrieUtils {
	
	@Deprecated // name is confusing, use: constructSequenceEndingInVertex	
	public static <T> List<T> constructSequenceFromRootToGivenVertex(StreamTrieImpl<T, VertexImpl<T>> trie,
			VertexImpl<T> v, int maxLength, boolean includeroot) {
		List<T> list = new ArrayList<T>();
		list.add(v.getVertexObject());
		EdgeImpl<T, VertexImpl<T>> e = trie.getInEdge(v);
		while (!e.getFrom().equals(trie.getRoot()) && list.size() < maxLength) {
			list.add(0, e.getFrom().getVertexObject());
			e = trie.getInEdge(e.getFrom());
		}
		if (includeroot && list.size() < maxLength) {
			list.add(0, e.getFrom().getVertexObject());
		}
		return list;
	}
	
	public static <T> List<T> constructSequenceEndingInVertex(StreamTrieImpl<T, VertexImpl<T>> trie,
			VertexImpl<T> v, int maxLength, boolean includeroot) {
		List<T> list = new ArrayList<T>();
		list.add(v.getVertexObject());
		EdgeImpl<T, VertexImpl<T>> e = trie.getInEdge(v);
		while (!e.getFrom().equals(trie.getRoot()) && list.size() < maxLength) {
			list.add(0, e.getFrom().getVertexObject());
			e = trie.getInEdge(e.getFrom());
		}
		if (includeroot && list.size() < maxLength) {
			list.add(0, e.getFrom().getVertexObject());
		}
		return list;
	}

	public static <T> List<T> constructTraceFromListOfEdges(List<EdgeImpl<T, VertexImpl<T>>> edges, int maxSize,
			boolean useTo, boolean invert) {
		List<T> result = new ArrayList<>();
		for (EdgeImpl<T, VertexImpl<T>> e : edges) {
			if (result.size() >= maxSize)
				break;
			if (useTo) {
				if (invert) {
					result.add(0, e.getTo().getVertexObject());
				} else {
					result.add(e.getTo().getVertexObject());
				}
			} else {
				if (invert) {
					result.add(0, e.getFrom().getVertexObject());
				} else {
					result.add(e.getFrom().getVertexObject());
				}
			}
		}
		return result;
	}
	
	
	public static <T> int countCummulativeNodeCount(StreamTrieImpl<T, VertexImpl<T>> trie, boolean includeRoot) {
		int count = 0;
		if (trie.getRoot() != null) {
			return countCummulativeNodeCountRecursive(trie, trie.getRoot(), count, includeRoot);
		}
		return count;
	}
	
	private static <T> int countCummulativeNodeCountRecursive(final StreamTrieImpl<T, VertexImpl<T>> trie,
			final VertexImpl<T> node, int count, boolean includeRoot) {
		if (!node.equals(trie.getRoot()) || includeRoot) {
			count += node.getCount();
		}
		for (EdgeImpl<T, VertexImpl<T>> e : trie.getOutEdges(node)) {
			count = countCummulativeNodeCountRecursive(trie, e.getTo(), count, includeRoot);
		}
		return count;
	}
	
	@Deprecated
	// use variant with include root.
	public static <T> int countCummulativeNodeCount(StreamTrieImpl<T, VertexImpl<T>> trie) {
		int count = 0;
		if (trie.getRoot() != null) {
			return countCummulativeNodeCountRecursive(trie, trie.getRoot(), count);
		}
		return count;
	}

	@Deprecated
	// use variant with include root
	private static <T> int countCummulativeNodeCountRecursive(final StreamTrieImpl<T, VertexImpl<T>> trie,
			final VertexImpl<T> node, int count) {
		count += node.getCount();
		for (EdgeImpl<T, VertexImpl<T>> e : trie.getOutEdges(node)) {
			count = countCummulativeNodeCountRecursive(trie, e.getTo(), count);
		}
		return count;
	}

	public static <T> int countNodesInTrie(StreamTrieImpl<T, VertexImpl<T>> trie) {
		int count = 0;
		if (trie.getRoot() != null) {
			return countNodesRecursive(trie, trie.getRoot(), count);
		}
		return count;
	}

	private static <T> int countNodesRecursive(final StreamTrieImpl<T, VertexImpl<T>> trie, final VertexImpl<T> node,
			int count) {
		count++;
		for (EdgeImpl<T, VertexImpl<T>> e : trie.getOutEdges(node)) {
			count = countNodesRecursive(trie, e.getTo(), count);
		}
		return count;
	}

	public static <T> int numberOfPointersToVertex(StreamTrieImpl<T, VertexImpl<T>> caseTrie, VertexImpl<T> vertex) {
		int newVertexCount = vertex.getCount();
		int newVertexOutEdgeCount = 0;
		for (EdgeImpl<T, VertexImpl<T>> newOut : caseTrie.getOutEdges(vertex)) {
			newVertexOutEdgeCount += newOut.getCount();
		}
		return newVertexCount - newVertexOutEdgeCount;
	}

}
