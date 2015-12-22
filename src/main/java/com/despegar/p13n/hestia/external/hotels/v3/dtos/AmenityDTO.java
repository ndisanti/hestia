package com.despegar.p13n.hestia.external.hotels.v3.dtos;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class AmenityDTO
    implements Serializable {


    private static final long serialVersionUID = 1L;
    private String id;
    private DescriptionsDTO descriptions;

    public AmenityDTO() {
    }

    @JsonProperty("amenity_category")
    private AmenityCategoryDTO amenityCategory;

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

    public AmenityCategoryDTO getAmenityCategory() {
        return this.amenityCategory;
    }

    public void setAmenityCategory(AmenityCategoryDTO amenityCategory) {
        this.amenityCategory = amenityCategory;
    }

    @Override
    public String toString() {
        return "AmenityDTO [id=" + this.id + ", descriptions=" + this.descriptions + ", amenityCategory="
            + this.amenityCategory + "]";
    }
}
