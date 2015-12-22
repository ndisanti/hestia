package com.despegar.p13n.hestia.api.data.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class PriceCarCategoryItem  extends ItemHome {

    private String destination;
    private String carcat;
    private int price;
    private String pulType;
    private String currency;

    @Deprecated
    public PriceCarCategoryItem() {
        super(ItemType.CAR_CATEGORY);
        // constructor for serialization
    }

    /**
     * @param destination
     * @param carcat
     * @param price
     */
    public PriceCarCategoryItem(String destination, String pulType, String carcat, int price, String currency) {
        super(ItemType.CAR_CATEGORY);
        this.destination = destination;
        this.pulType = pulType;
        this.carcat = carcat;
        this.price = price;
        this.currency = currency;
    }


    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCarcat() {
        return this.carcat;
    }

    public void setCarcat(String carcat) {
        this.carcat = carcat;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPulType() {
        return this.pulType;
    }

    public void setPulType(String pulType) {
        this.pulType = pulType;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    @JsonIgnore
    public String getId() {
        return this.carcat;
    }

    @Override
    public String toString() {
        return "PriceCarCategoryItem [destination=" + this.destination + ", carcat=" + this.carcat + ", price=" + this.price
            + ", pulType=" + this.pulType + ", currency=" + this.currency + "]";
    }
}
