package com.despegar.p13n.hestia.external.geo;

public final class GeoPoint {
    private final Double latitude;
    private final Double longitude;

    public GeoPoint(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Double getLatitude() {
        return this.latitude;
    }
}
