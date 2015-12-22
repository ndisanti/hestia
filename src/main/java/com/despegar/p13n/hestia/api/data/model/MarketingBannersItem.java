package com.despegar.p13n.hestia.api.data.model;

public class MarketingBannersItem
    extends ItemHome {

    public MarketingBannersItem() {
        super(ItemType.MARKETING);
    }


    @Override
    public String getId() {
        return this.getOfferType().name();
    }


    @Override
    public String toString() {
        return "MarketingBannersItem []";
    }

}
