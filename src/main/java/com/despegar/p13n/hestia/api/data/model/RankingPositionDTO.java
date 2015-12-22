package com.despegar.p13n.hestia.api.data.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class RankingPositionDTO  implements Comparable<RankingPositionDTO> {

    private String destination;
    private Long frequency;
    private Map<String, String> meta;

    public RankingPositionDTO() {
    }

    public RankingPositionDTO(String destination, Long frequency) {
        super();
        this.destination = destination;
        this.frequency = frequency;
        this.meta = null;
    }

    public RankingPositionDTO(String destination, Long frequency, Map<String, String> meta) {
        super();
        this.destination = destination;
        this.frequency = frequency;
        this.meta = meta != null && !meta.isEmpty() ? meta : null;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }

    public Long getFrequency() {
        return this.frequency;
    }

    public void addMeta(String key, String value) {
        if (this.meta == null) {
            this.meta = new HashMap<String, String>();
        }
        this.meta.put(key, value);
    }

    public Map<String, String> getMeta() {
        return this.meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.destination).append(this.frequency).append(this.meta).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RankingPositionDTO) {
            RankingPositionDTO rhs = (RankingPositionDTO) obj;
            return new EqualsBuilder().append(this.destination, rhs.getDestination())
                .append(this.frequency, rhs.getFrequency()).append(this.meta, rhs.meta).isEquals();
        }
        return false;
    }

    public int compareTo(RankingPositionDTO o) {
        Long v1 = this.getFrequency();
        Long v2 = o.getFrequency();
        int valueComparison = v2.compareTo(v1);
        if (valueComparison == 0) {
            return this.getDestination().compareTo(o.getDestination());
        } else {
            return valueComparison;
        }
    }

    @Override
    public String toString() {
        return "RankingPositionDTO [destination=" + this.destination + ", frequency=" + this.frequency + ", meta="
            + this.meta + "]";
    }
}
