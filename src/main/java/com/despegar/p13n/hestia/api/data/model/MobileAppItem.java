package com.despegar.p13n.hestia.api.data.model;

public class MobileAppItem extends ItemHome {

    public MobileAppItem() {
        super(ItemType.APP_MOBILE);
    }

    @Override
    public String getId() {
        return this.getOfferType().name();
    }

    @Override
    public String toString() {
        return "MobileItem []";
    }
}
