package com.despegar.p13n.hestia.api.data.model;

public class SecretOfferItem extends ItemHome {


    public SecretOfferItem() {
        super(ItemType.SECRET_OFFER);
    }

    @Override
    public String getId() {
        return this.getOfferType().name();
    }

    @Override
    public String toString() {
        return "SecretOfferItem []";
    }
    }
