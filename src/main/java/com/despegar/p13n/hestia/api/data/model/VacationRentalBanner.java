package com.despegar.p13n.hestia.api.data.model;

public class VacationRentalBanner extends ItemHome {

    public VacationRentalBanner() {
        super(ItemType.VACATION_RENTALS_BANNER);
    }

    @Override
    public String getId() {
        return this.getOfferType().name();
    }

    @Override
    public String toString() {
        return "VacationRentalBanner []";
    }
}
