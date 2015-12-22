package com.despegar.p13n.hestia.recommend.allinone;

import java.io.Serializable;
import java.net.InetAddress;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.common.net.InetAddresses;

public class HomeUserLocation
    implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6974518378496670105L;

    private String ip;
    private String countryCode;
    private String city;
    private boolean userLocationByGeo;

    public HomeUserLocation() {
    }

    public HomeUserLocation(String ip, String countryCode, String city, boolean userLocationByGeo) {
        super();
        this.ip = ip;
        this.countryCode = countryCode;
        this.city = city;
        this.userLocationByGeo = userLocationByGeo;
    }

    public String getIp() {
        return this.ip;
    }

    @JsonIgnore
    public InetAddress getInetAddress() {

        if (this.ip == null) {
            return null;
        }

        try {
            return InetAddresses.forString(this.ip);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isUserLocationByGeo() {
        return this.userLocationByGeo;
    }

    public void setUserLocationByGeo(boolean userLocationByGeo) {
        this.userLocationByGeo = userLocationByGeo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.city == null) ? 0 : this.city.hashCode());
        result = prime * result + ((this.countryCode == null) ? 0 : this.countryCode.hashCode());
        result = prime * result + ((this.ip == null) ? 0 : this.ip.hashCode());
        result = prime * result + (this.userLocationByGeo ? 1231 : 1237);
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
        HomeUserLocation other = (HomeUserLocation) obj;
        if (this.city == null) {
            if (other.city != null) {
                return false;
            }
        } else if (!this.city.equals(other.city)) {
            return false;
        }
        if (this.countryCode == null) {
            if (other.countryCode != null) {
                return false;
            }
        } else if (!this.countryCode.equals(other.countryCode)) {
            return false;
        }
        if (this.ip == null) {
            if (other.ip != null) {
                return false;
            }
        } else if (!this.ip.equals(other.ip)) {
            return false;
        }
        if (this.userLocationByGeo != other.userLocationByGeo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HomeUserLocation [ip=").append(this.ip).append(", countryCode=").append(this.countryCode)
            .append(", city=").append(this.city).append(", userLocationByGeo=").append(this.userLocationByGeo).append("]");
        return builder.toString();
    }


}
