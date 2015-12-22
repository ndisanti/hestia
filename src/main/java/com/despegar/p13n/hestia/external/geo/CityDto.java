package com.despegar.p13n.hestia.external.geo;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CityDto
    implements GeoSpot {

    private static final Logger logger = LoggerFactory.getLogger(CityDto.class);

    private String id;

    private long longId;

    Map<String, String> descriptions;

    private String code;

    private LocationDto location;

    private long country_id;

    private String timezone;
    private Double timezone_offset;
    private long administrative_division_id;

    private Collection<PictureDto> pictures;

    public CityDto() {
        super();
    }

    public Collection<PictureDto> getPictures() {
        return this.pictures;
    }

    public void setPictures(Collection<PictureDto> pictures) {
        this.pictures = pictures;
    }

    public String getId() {
        return this.id;
    }

    public long getLongId() {
        return this.longId;
    }

    public void setId(String id) {
        this.id = id;
        try {
            this.longId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            logger.error("City ID is not a number, ignoring city={}", id);
        }
    }

    public Map<String, String> getDescriptions() {
        return this.descriptions;
    }

    public void setDescriptions(Map<String, String> descriptions) {
        this.descriptions = descriptions;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocationDto getLocation() {
        return this.location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public long getCountry_id() {
        return this.country_id;
    }

    public void setCountry_id(long country_id) {
        this.country_id = country_id;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Double getTimezone_offset() {
        return this.timezone_offset;
    }

    public void setTimezone_offset(Double timezone_offset) {
        this.timezone_offset = timezone_offset;
    }

    public long getAdministrative_division_id() {
        return this.administrative_division_id;
    }

    public void setAdministrative_division_id(long administrative_division_id) {
        this.administrative_division_id = administrative_division_id;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId()).append(this.getCode()).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        CityDto otherObject = (CityDto) obj;
        if ((this.getId() == null) || (otherObject.getId() == null)) {
            return false;
        }

        if ((this.getCode() == null) || (otherObject.getCode() == null)) {
            return false;
        }

        return new EqualsBuilder().append(this.getId(), otherObject.getId()).append(this.getCode(), otherObject.getCode())
            .isEquals();
    }



}
