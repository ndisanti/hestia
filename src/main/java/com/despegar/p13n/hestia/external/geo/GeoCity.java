package com.despegar.p13n.hestia.external.geo;

import com.despegar.p13n.euler.commons.client.model.CountryCode;

public class GeoCity {

    private long countryOid;
    private long administrativeDivisionOid;
    private GeoLocation location;
    private String iataCode;
    private CountryCode cc;
    private Long oid;

    public GeoCity(Long oid, String iataCode, long countryOid, long administrativeDivisionOid, GeoLocation location,
        CountryCode cc) {
        super();
        this.oid = oid;
        this.iataCode = iataCode;
        this.countryOid = countryOid;
        this.administrativeDivisionOid = administrativeDivisionOid;
        this.location = location;
        this.cc = cc;
    }

    public String getIataCode() {
        return this.iataCode;
    }

    public long getAdministrativeDivisionOid() {
        return this.administrativeDivisionOid;
    }

    public long getCountryOid() {
        return this.countryOid;
    }

    public GeoLocation getLocation() {
        return this.location;
    }

    public CountryCode getCc() {
        return this.cc;
    }

    public Long getOid() {
        return this.oid;
    }

    @Override
    public String toString() {
        return "GeoCity [countryOid=" + this.countryOid + ", administrativeDivisionOid=" + this.administrativeDivisionOid
            + ", location=" + this.location + ", iataCode=" + this.iataCode + ", cc=" + this.cc + ", oid=" + this.oid + "]";
    }



}
