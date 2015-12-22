package com.despegar.p13n.hestia.api.data.model;

import com.despegar.p13n.euler.commons.client.model.Product;

public class AlertItem  extends ItemHome {

    private Product product;

    private String destination;

    public AlertItem(String destination, Product product) {
        super(ItemType.FARE_ALERTS);
        this.product = product;
        this.destination = destination;
    }

    public Product getProduct() {
        return this.product;
    }


    public String getDestination() {
        return this.destination;
    }

    @Override
    public String getId() {
        return this.getOfferType().name();
    }

    @Override
    public String toString() {
        return "AlertITem [product=" + this.product + " destination = " + this.destination + "]";
    }}
