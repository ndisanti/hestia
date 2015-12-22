package com.despegar.p13n.hestia.api.data.model;

public class HolidaysItem  extends ItemHome {

    private String tripType;
    private String origin;
    private String destination;

    public HolidaysItem(String origin, String destination, String tripType) {
        super(ItemType.HOLIDAYS);
        this.tripType = tripType;
        this.origin = origin;
        this.destination = destination;
    }

    @Override
    public String getId() {
        return this.getOfferType().name();
    }

    public String getTripType() {
        return this.tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "HolidaysItem [tripType=" + this.tripType + ", origin=" + this.origin + ", destination=" + this.destination
            + "]";
    }}
