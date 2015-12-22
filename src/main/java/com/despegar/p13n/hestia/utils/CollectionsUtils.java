package com.despegar.p13n.hestia.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class CollectionsUtils {

    private CollectionsUtils() {
    }

    public interface Indexer<K, T> {
        T getIndex(K object);
    }

    public static <T, K> Map<T, List<K>> groupBy(List<K> list, Indexer<K, T> index) {
        Map<T, List<K>> result = new HashMap<T, List<K>>();
        for (K element : list) {
            T key = index.getIndex(element);
            List<K> values = result.get(key);
            if (values == null) {
                values = new ArrayList<K>();
                result.put(key, values);
            }
            values.add(element);
        }
        return Collections.unmodifiableMap(result);
    }

    public static <T, K> Map<T, K> indexBy(List<K> list, Indexer<K, T> indexer) {
        Map<T, K> result = new HashMap<T, K>();
        for (K element : list) {
            result.put(indexer.getIndex(element), element);
        }
        return Collections.unmodifiableMap(result);
    }

    public static <T, K> Map<T, K> indexBy(Collection<K> collection, Indexer<K, T> indexer) {
        Map<T, K> result = new HashMap<T, K>();
        for (K element : collection) {
            result.put(indexer.getIndex(element), element);
        }
        return Collections.unmodifiableMap(result);
    }

    public static <T> T getRandomEntryFrom(List<T> list) {
        if (list.size() == 0) {
            return null;
        }
        Random r = new Random();
        return list.get(r.nextInt(list.size()));
    }

}
