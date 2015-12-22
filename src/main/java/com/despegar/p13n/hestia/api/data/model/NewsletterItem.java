package com.despegar.p13n.hestia.api.data.model;

public class NewsletterItem extends ItemHome {

    public NewsletterItem() {
        super(ItemType.NEWSLETTER);
    }

    @Override
    public String getId() {
        return this.getOfferType().name();
    }

    @Override
    public String toString() {
        return "NewsletterItem []";
    }
}
