package com.despegar.p13n.hestia.api.data.model;

public class VacationRentalDestinationItem extends DestinationItem {

    @Deprecated
    public VacationRentalDestinationItem() {
        super(ItemType.VACATION_RENTAL_DESTINATION);
    }

    public VacationRentalDestinationItem(String destination) {
        super(ItemType.VACATION_RENTAL_DESTINATION, destination);
    }

    @Override
    public String toString() {
        return "VacationRentalDestinationItem [destination=" + this.destination + "]";
    }
}
