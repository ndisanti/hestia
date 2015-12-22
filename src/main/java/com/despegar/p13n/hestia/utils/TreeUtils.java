package com.despegar.p13n.hestia.utils;

import java.util.Map;
import java.util.Map.Entry;

import com.despegar.p13n.hestia.recommend.decisiontree.Node;
import com.google.common.collect.Maps;

public class TreeUtils {

    public static <T> Map<String, Node<T>> toStringList(Map<?, Node<T>> childs) {

        Map<String, Node<T>> m = Maps.newLinkedHashMap();

        for (Entry<?, ?> entry : childs.entrySet()) {

            @SuppressWarnings("unchecked")
            Node<T> node = (Node<T>) entry.getValue();

            m.put(entry.getKey().toString(), node);
        }

        return m;
    }

}
