package com.despegar.p13n.hestia.recommend.decisiontree;

import java.util.Collection;
import java.util.Map;

public abstract class Node<T> {

    private Value<T> value;

    public void setValue(Value<T> value) {
        this.value = value;
    }

    public Value<T> getValue() {
        return this.value;
    }

    public abstract Node<T> getNext(Query query);

    public abstract Collection<Node<T>> getChilds();

    public abstract Map<String, Node<T>> getChildsKeysAsString();

    public abstract boolean hasChilds();

    public abstract Value<T> add(RuleDef ruleDef, T t);

    public abstract boolean containsRule(RuleDef rule);

    public abstract Node<T> getNewChild();

    public boolean isLeaf() {
        return false;
    }

    public abstract int leaves();


    public static class Value<T> {
        private T value;
        private long id;

        public Value(T t) {
            this.value = t;
        }

        public Value(T t, long id) {
            this.value = t;
            this.id = id;
        }

        public T getValue() {
            return this.value;
        }

        public void setValue(T t) {
            this.value = t;
        }

        public long getId() {
            return this.id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }

}
