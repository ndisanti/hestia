package com.despegar.p13n.hestia.api.data.model;

import com.google.common.base.Preconditions;

public class ClosedPackagesDestinationItem  extends DestinationItem {

    private String origin;

    @Deprecated
    public ClosedPackagesDestinationItem() {
        super(ItemType.CLOSED_PACKAGES_DESTINATION);
        // constructor for serializacion
    }

    /**
     * @param destination
     * @param carcat
     * @param price
     */
    public ClosedPackagesDestinationItem(String destination, String origin) {
        super(ItemType.CLOSED_PACKAGES_DESTINATION, destination);

        Preconditions.checkArgument(destination != null, "destination is mandatory for ClosedPackagesDestinationItem");
        Preconditions.checkArgument(origin != null, "origin is mandatory for ClosedPackagesDestinationItem");
        Preconditions.checkArgument(!origin.equals(destination), "Origin and Destination are equals: " + origin);

        this.origin = origin;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "ClosedPackagesDestinationItem [origin=" + this.origin + ", destination=" + this.destination + "]";
    }
}