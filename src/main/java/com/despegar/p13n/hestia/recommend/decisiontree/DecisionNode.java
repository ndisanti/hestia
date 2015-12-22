package com.despegar.p13n.hestia.recommend.decisiontree;


public abstract class DecisionNode<T>
    extends Node<T> {


    @Override
    public void setValue(Value<T> value) {
        throw new UnsupportedOperationException("Cant' set value in decision node");
    }

    @Override
    public Value<T> getValue() {
        throw new UnsupportedOperationException("Cant' get value from a decision node");
    }

    @Override
    public abstract Node<T> getNext(Query query);

    @Override
    public abstract boolean hasChilds();

    @Override
    public abstract Value<T> add(RuleDef ruleDef, T t);

    @Override
    public abstract boolean containsRule(RuleDef rule);

    @Override
    public abstract Node<T> getNewChild();

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public int leaves() {

        int count = 0;

        for (Node<?> child : this.getChilds()) {
            count += child.leaves();
        }

        return count;
    }

}
