package com.despegar.p13n.hestia.recommend.decisiontree;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public abstract class ValueNode<T>
    extends Node<T> {


    @Override
    public Node<T> getNext(Query q) {
        return null;
    }

    @Override
    public boolean hasChilds() {
        return false;
    }

    @Override
    public Collection<Node<T>> getChilds() {
        return Collections.emptyList();
    }

    @Override
    public abstract Value<T> add(RuleDef ruleDef, T value);

    @Override
    public boolean containsRule(RuleDef rule) {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public Node<T> getNewChild() {
        return null;
    }

    @Override
    public Map<String, Node<T>> getChildsKeysAsString() {
        return Collections.emptyMap();
    }

    @Override
    public int leaves() {
        return 1;
    }


}
