package com.despegar.p13n.hestia.external.geo;


public class GeoCountry {

    private Long oid;
    private String iataCode;

    public GeoCountry() {

    }


    public GeoCountry(Long oid, String iataCode) {
        this.oid = oid;
        this.iataCode = iataCode;
    }


    public GeoCountry(CountryDto dto) {
        this.oid = dto.getLongId();
        this.iataCode = dto.getCode();
    }

    public Long getOid() {
        return this.oid;
    }

    public String getIataCode() {
        return this.iataCode;
    }
}
