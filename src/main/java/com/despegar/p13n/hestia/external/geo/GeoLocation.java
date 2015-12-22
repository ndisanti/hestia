package com.despegar.p13n.hestia.external.geo;

import org.apache.commons.lang.builder.HashCodeBuilder;

public class GeoLocation {

    private Double latitude;
    private Double longitude;
    private String destCode;

    public GeoLocation(Double latitude, Double longitude, String destCode) {
        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("GeoLocation cannot be built with null arguments");
        }

        this.latitude = latitude;
        this.longitude = longitude;
        this.setDestCode(destCode);
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.latitude).append(this.longitude).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof GeoLocation)) {
            return false;
        }

        GeoLocation other = (GeoLocation) obj;
        return this.latitude.equals(other.getLatitude()) && this.longitude.equals(this.getLongitude());
    }

    @Override
    public String toString() {
        return "(" + this.latitude + ", " + this.longitude + ")";
    }

    public String getDestCode() {
        return this.destCode;
    }

    public void setDestCode(String destCode) {
        this.destCode = destCode;
    }

    public LocationDto toGeolocationDTO() {
        final LocationDto location = new LocationDto();
        location.setLatitude(this.latitude);
        location.setLongitude(this.longitude);
        return location;
    }
}
