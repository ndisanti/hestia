package com.despegar.p13n.hestia.external.hotels.v3.dtos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class HotelDTO
    implements Serializable {


    private static final long serialVersionUID = 1L;

    private String id;
    private boolean published;
    private boolean test;
    private String name;

    @JsonProperty("hotel_type")
    private HotelTypeDTO hotelType;

    private Integer stars;

    @JsonProperty("hotel_classification")
    private IdDTO hotelClassification;

    private LocationDTO location;

    private TimeRangeDTO checkin;
    private TimeRangeDTO checkout;

    private List<IdDTO> amenities;

    @JsonProperty("creation_date")
    private Date creationDate;

    @JsonProperty("last_modified_date")
    private Date lastModifiedDate;

    public HotelDTO() {
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPublished() {
        return this.published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isTest() {
        return this.test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HotelTypeDTO getHotelType() {
        return this.hotelType;
    }

    public void setHotelType(HotelTypeDTO hotelType) {
        this.hotelType = hotelType;
    }

    public Integer getStars() {
        return this.stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public IdDTO getHotelClassification() {
        return this.hotelClassification;
    }

    public void setHotelClassification(IdDTO hotelClassification) {
        this.hotelClassification = hotelClassification;
    }

    public LocationDTO getLocation() {
        return this.location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public TimeRangeDTO getCheckin() {
        return this.checkin;
    }

    public void setCheckin(TimeRangeDTO checkin) {
        this.checkin = checkin;
    }

    public TimeRangeDTO getCheckout() {
        return this.checkout;
    }

    public void setCheckout(TimeRangeDTO checkout) {
        this.checkout = checkout;
    }

    public List<IdDTO> getAmenities() {
        return this.amenities;
    }

    public void setAmenities(List<IdDTO> amenities) {
        this.amenities = amenities;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String toString() {
        return "HotelDTO [id=" + this.id + ", published=" + this.published + ", test=" + this.test + ", name=" + this.name
            + ", hotelType=" + this.hotelType + ", stars=" + this.stars + ", hotelClassification="
            + this.hotelClassification + ", location=" + this.location + ", checkin=" + this.checkin + ", checkout="
            + this.checkout + ", amenities=" + this.amenities + ", creationDate=" + this.creationDate
            + ", lastModifiedDate=" + this.lastModifiedDate + "]";
    }
}
