package com.despegar.p13n.hestia.api.data.model;

public class TrustPilotItem  extends ItemHome {

    public TrustPilotItem() {
        super(ItemType.TRUST_PILOT);
    }

    @Override
    public String getId() {
        return this.getOfferType().name();
    }

    @Override
    public String toString() {
        return "TrustPilotItem []";
    }
}
