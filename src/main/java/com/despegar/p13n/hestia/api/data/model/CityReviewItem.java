package com.despegar.p13n.hestia.api.data.model;

public class CityReviewItem extends ItemHome {

    private String destination;

    public CityReviewItem(String destination) {
        super(ItemType.CITY_REVIEW);
        this.destination = destination;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String getId() {
        return this.getOfferType().name();
    }

    @Override
    public String toString() {
        return "CityReviewItem [destination = " + this.destination + "]";
    }
 }
