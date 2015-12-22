package com.despegar.p13n.hestia.api.data.model;

public class ActivityDestinationItem    extends DestinationItem {

    @Deprecated
    public ActivityDestinationItem() {
        super(ItemType.ACTIVITY_DESTINATION);
    }

    /**
     * @param destination
     */
    public ActivityDestinationItem(String destination) {
        super(ItemType.ACTIVITY_DESTINATION, destination);
    }

    @Override
    public String toString() {
        return "ActivityDestinationItem [destination=" + this.destination + "]";
    }
}
