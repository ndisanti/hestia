package com.despegar.p13n.hestia.api.data.model;

import com.google.common.base.Preconditions;

public class FlightDestinationItem  extends DestinationItem {

    private String origin;
    private String tripType;

    @Deprecated
    public FlightDestinationItem() {
        super(ItemType.FLIGHT_DESTINATION);
    }

    /**
     * @param destination
     * @param origin
     * 
     * Destination and origin should be city iatas (no airport)
     */
    public FlightDestinationItem(String destination, String origin, String tripType) {
        super(ItemType.FLIGHT_DESTINATION, destination);

        Preconditions.checkArgument(destination != null, "destination is mandatory for FlightDestinationItem");
        Preconditions.checkArgument(origin != null, "origin is mandatory for FlightDestinationItem");
        Preconditions.checkArgument(!origin.equals(destination), "Origin and Destination are equals: " + origin);
        Preconditions.checkArgument(tripType != null, "tripType is mandatory for FlightDestinationItem");

        this.origin = origin;
        this.tripType = tripType;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTripType() {
        return this.tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    @Override
    public String toString() {
        return "FlightDestinationItem [origin=" + this.origin + ", tripType=" + this.tripType + ", destination="
            + this.destination + "]";
    }
}