package com.despegar.p13n.hestia.api.data.model;

public class HotelDestinationItem  extends DestinationItem {

    @Deprecated
    public HotelDestinationItem() {
        super(ItemType.HOTEL_DESTINATION);
    }

    /**
     * @param destination
     */
    public HotelDestinationItem(String destination) {
        super(ItemType.HOTEL_DESTINATION, destination);
    }

    @Override
    public String toString() {
        return "HotelDestinationItem [destination=" + this.destination + "]";
    }
}
