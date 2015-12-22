package com.despegar.p13n.hestia.api.data.model;

import java.io.Serializable;
import java.util.List;

public class DestinationRanking<T extends Serializable> {

    private List<T> destinations;

    public DestinationRanking(List<T> destinations) {
        super();
        this.destinations = destinations;
    }

    @Deprecated
    public DestinationRanking() {
    }

    public List<T> getDestinations() {
        return this.destinations;
    }

    public boolean empty() {
        return this.destinations != null && this.destinations.isEmpty();
    }

    public int size() {
        if (this.destinations != null) {
            return this.destinations.size();
        }
        return 0;
    }
}