package com.despegar.p13n.hestia.api.data.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.common.base.Preconditions;

public abstract class DestinationItem   extends ItemHome {

    protected String destination;

    protected DestinationItem(ItemType offerType) {
        super(offerType);
        // for serialization
    }

    protected DestinationItem(ItemType offerType, String destination) {
        super(offerType);
        Preconditions.checkArgument(destination != null, "destination is mandatory for DestinationItem");

        this.destination = destination;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    @JsonIgnore
    public String getId() {
        return this.destination;
    }
}
