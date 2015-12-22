package com.despegar.p13n.hestia.external.hotels.v3.dtos;

import java.io.Serializable;

public class LocationDTO
    implements Serializable {

    private static final long serialVersionUID = 1L;
    private CityDTO city;
    private String address;
    private String zipcode;
    private Double latitude;
    private Double longitude;

    public LocationDTO() {
    }

    public CityDTO getCity() {
        return this.city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return this.zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LocationDTO [city=" + this.city + ", address=" + this.address + ", zipcode=" + this.zipcode + ", latitude="
            + this.latitude + ", longitude=" + this.longitude + "]";
    }
}
