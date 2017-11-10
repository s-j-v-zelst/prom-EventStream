package org.processmining.eventstream.utils;

import java.util.Set;

import org.processmining.framework.util.Pair;

import gnu.trove.set.hash.THashSet;

public class PairUtils {

	public static <K, V> Pair<K, V> getPairByKey(Iterable<Pair<K, V>> setOfPairs, K key) {
		Pair<K, V> result = null;
		for (Pair<K, V> pair : setOfPairs) {
			if (pair.getFirst().equals(key)) {
				result = pair;
				break;
			}
		}
		return result;
	}

	public static <K, V> Set<K> keys(Set<Pair<K, V>> setOfPairs) {
		Set<K> keys = new THashSet<>();
		for (Pair<K, V> pair : setOfPairs) {
			keys.add(pair.getFirst());
		}
		return keys;
	}

	public static <T, S> Pair<S, T> reverse(Pair<T, S> pair) {
		return new Pair<S, T>(pair.getSecond(), pair.getFirst());
	}

}
