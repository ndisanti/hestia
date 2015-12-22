package com.despegar.p13n.hestia.api.data.model;

public class InspirationItem extends ItemHome {

    private String theme;
    private String travelerType;

    public InspirationItem(String theme, String travelerType) {
        super(ItemType.INSPIRATION);
        this.theme = theme;
        this.travelerType = travelerType;
    }

    public String getTheme() {
        return this.theme;
    }

    public String getTravelerType() {
        return this.travelerType;
    }

    @Override
    public String getId() {
        return this.getOfferType().name();
    }

    @Override
    public String toString() {
        return "InspirationItem [theme= " + this.theme + " travelerType= " + this.travelerType + "]";
    }

}
