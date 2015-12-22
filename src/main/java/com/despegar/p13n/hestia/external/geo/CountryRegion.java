package com.despegar.p13n.hestia.external.geo;

public class CountryRegion {

    private String country;
    private String region;

    public CountryRegion(String country, String region) {
        super();
        this.country = country;
        this.region = region;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.country == null) ? 0 : this.country.hashCode());
        result = prime * result + ((this.region == null) ? 0 : this.region.hashCode());
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
        CountryRegion other = (CountryRegion) obj;
        if (this.country == null) {
            if (other.country != null) {
                return false;
            }
        } else if (!this.country.equals(other.country)) {
            return false;
        }
        if (this.region == null) {
            if (other.region != null) {
                return false;
            }
        } else if (!this.region.equals(other.region)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CountryRegion [country=" + this.country + ", region=" + this.region + "]";
    }

}
