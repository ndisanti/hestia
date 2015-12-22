package com.despegar.p13n.hestia.api.data.model;

public class GenericInspirationItem  extends ItemHome {

    public GenericInspirationItem() {
        super(ItemType.GENERIC_INSPIRATION);
    }

    @Override
    public String getId() {
        return this.getOfferType().name();
    }

    @Override
    public String toString() {
        return "GenericInspirationItem []";
    }}
