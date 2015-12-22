package com.despegar.p13n.hestia.api.data.model;

import com.google.common.base.Preconditions;

public class CarDestinationItem extends DestinationItem {

    private int price;
    private String pulType;
    private String currency;

    @Deprecated
    public CarDestinationItem() {
        super(ItemType.CAR_DESTINATION);
        // constructor for serializacion
    }

    /**
     * @param destination
     * @param pultype
     */
    public CarDestinationItem(String destination, String pulType) {
        super(ItemType.CAR_DESTINATION, destination);
        Preconditions.checkArgument(pulType != null, "pulType is mandatory for CarDestinationItem");
        this.pulType = pulType;

        // TODO: should be removed, exposed in the api and home is using it
        this.price = 0;
        this.currency = "";
    }

    /**
     * @param destination
     * @param pultype
     * @param price
     * @param currency
     */
    @Deprecated
    public CarDestinationItem(String destination, String pulType, int price, String currency) {
        super(ItemType.CAR_DESTINATION, destination);
        this.price = price;
        this.pulType = pulType;
        this.currency = currency;
    }

    public int getPrice() {
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
    public String toString() {
        return "CarDestinationItem [price=" + this.price + ", pulType=" + this.pulType + ", currency=" + this.currency
            + ", destination=" + this.destination + "]";
    }

}
