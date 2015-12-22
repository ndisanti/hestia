package com.despegar.p13n.hestia.external.hrm;

import java.math.BigDecimal;
import java.util.List;

import com.despegar.p13n.hestia.external.hotels.v3.dtos.IdDTO;

public class HotelItem {

    private Long id;
    private BigDecimal starRating;
    private List<IdDTO> amenities;

    public HotelItem(Long oid, BigDecimal starRating, List<IdDTO> amenities) {
        this.id = oid;
        this.starRating = starRating;
        this.amenities = amenities;
    }

    public Long getId() {
        return this.id;
    }

    public BigDecimal getStarRating() {
        return this.starRating;
    }

    public List<IdDTO> getAmenities() {
        return this.amenities;
    }
}
