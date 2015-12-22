package com.despegar.p13n.hestia.api.data.model;

public class BankOfferItem extends ItemHome {

    public BankOfferItem() {
        super(ItemType.BANK_OFFER);
    }

    @Override
    public String getId() {
        return this.getOfferType().name();
    }

    @Override
    public String toString() {
        return "BankOfferItem []";
    }
}
