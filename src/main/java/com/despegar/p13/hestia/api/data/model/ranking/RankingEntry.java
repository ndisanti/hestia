package com.despegar.p13.hestia.api.data.model.ranking;

import java.io.Serializable;

public class RankingEntry<T extends Comparable<T>>
    implements Serializable, Comparable<RankingEntry<T>> {

    private static final long serialVersionUID = 1L;
    private String name;
    private T value;

    public RankingEntry() {

    }

    public RankingEntry(String name, T value) {
        super();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public int compareTo(RankingEntry<T> o) {
        T v1 = this.getValue();
        T v2 = o.getValue();
        int valueComparison = v2.compareTo(v1);
        if (valueComparison == 0) {
            return this.getName().compareTo(o.getName());
        } else {
            return valueComparison;
        }
    }

}
