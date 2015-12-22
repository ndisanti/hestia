package com.despegar.p13n.hestia.external.hotels.v3.dtos;

import java.io.Serializable;

public class AmenityCategoryDTO
    implements Serializable {


    private static final long serialVersionUID = 1L;
    private String id;
    private DescriptionsDTO descriptions;

    public AmenityCategoryDTO() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DescriptionsDTO getDescriptions() {
        return this.descriptions;
    }

    public void setDescriptions(DescriptionsDTO descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public String toString() {
        return "AmenityCategoryDTO [id=" + this.id + ", descriptions=" + this.descriptions + "]";
    }
}
