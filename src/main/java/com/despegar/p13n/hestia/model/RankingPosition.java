package com.despegar.p13n.hestia.model;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class RankingPosition
implements Comparable<RankingPosition>  {

    private Long value;
    private String name;
    private Map<String, String> meta;

    public RankingPosition() {
    }

    public RankingPosition(Long value, String name) {
        this.value = value;
        this.name = name;
        this.meta = Collections.emptyMap();
    }

    public String getName() {
        return this.name;
    }

    public Long getValue() {
        return this.value;
    }

    public void plusValue(Long plus) {
        this.value = this.value + plus;
    }

    public Map<String, String> getMeta() {
        return this.meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    public int compareTo(RankingPosition o) {
        Long v1 = this.getValue();
        Long v2 = o.getValue();
        int valueComparison = v2.compareTo(v1);
        if (valueComparison == 0) {
            return this.getName().compareTo(o.getName());
        } else {
            return valueComparison;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.name).append(this.value).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RankingPosition) {
            RankingPosition other = (RankingPosition) obj;
            return new EqualsBuilder().append(other.name, this.name).append(other.value, this.value).isEquals();
        }
        return false;
    }

    @Override
    public String toString() {
        return "RankingPosition [value=" + this.value + ", name=" + this.name + "]";
    }

}
