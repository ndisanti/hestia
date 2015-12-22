package com.despegar.p13n.hestia.api.data.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class PultypeCounterDTO {

    private String pultype;

    private Long counter;

    public PultypeCounterDTO() {
    }

    public PultypeCounterDTO(String var, Long value) {
        this.pultype = var;
        this.counter = value;
    }

    public String getPultype() {
        return this.pultype;
    }

    public void setPultype(String pultype) {
        this.pultype = pultype;
    }

    public Long getCounter() {
        return this.counter;
    }

    public void setCounter(Long counter) {
        this.counter = counter;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.pultype).append(this.counter).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PultypeCounterDTO) {
            PultypeCounterDTO pc = (PultypeCounterDTO) obj;
            return new EqualsBuilder().append(this.pultype, pc.getPultype()).append(this.counter, pc.getCounter())
                .isEquals();
        }
        return false;
    }

    @Override
    public String toString() {
        return "PultypeCounterDTO [pultype=" + this.pultype + ", counter=" + this.counter + "]";
    }

}
