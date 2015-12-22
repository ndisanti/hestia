package com.despegar.p13n.hestia.external.hotels.v3.dtos;

import java.io.Serializable;

public class HotelTypeDTO
    implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;

    public HotelTypeDTO() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "HotelTypeDTO [id=" + this.id + "]";
    }
}
