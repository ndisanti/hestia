package com.despegar.p13n.hestia.external.geo;

public class LocationDto {

    private String type;
    private Double latitude;
    private Double longitude;



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.latitude == null) ? 0 : this.latitude.hashCode());
        result = prime * result + ((this.longitude == null) ? 0 : this.longitude.hashCode());
        return result;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        LocationDto other = (LocationDto) obj;
        if (this.latitude == null) {
            if (other.latitude != null) {
                return false;
            }
        } else if (!this.latitude.equals(other.latitude)) {
            return false;
        }
        if (this.longitude == null) {
            if (other.longitude != null) {
                return false;
            }
        } else if (!this.longitude.equals(other.longitude)) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!this.type.equals(other.type)) {
            return false;
        }
        return true;
    }



    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }



}
