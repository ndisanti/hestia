package com.despegar.p13n.hestia.external.geo;

import java.util.Map;

public interface GeoSpot {
    public String getId();

    public long getLongId();

    public Map<String, String> getDescriptions();

    public String getCode();

    public LocationDto getLocation();


    public enum Spot {
        CITY, AIRPORT, PORT;
    }
}
