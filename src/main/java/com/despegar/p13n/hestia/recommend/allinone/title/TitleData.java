package com.despegar.p13n.hestia.recommend.allinone.title;

import java.util.Set;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.google.common.collect.Sets;

public class TitleData {

    private TitleEnum title;
    private Product offer;
    private Set<String> destinations = Sets.newHashSet();
    private String origin;

    public TitleData() {
    }

    public TitleData(TitleEnum title) {
        this.title = title;
    }

    public TitleData(TitleEnum title, Product offer, String origin, String destination) {
        this.title = title;
        this.offer = offer;
        this.destinations.add(destination);
        this.origin = origin;
    }

    public TitleData(TitleEnum title, Product offer, String origin, String... destination) {
        this.title = title;
        this.offer = offer;
        this.destinations.addAll(this.destinations);
        this.origin = origin;
    }

    public TitleEnum getTitle() {
        return this.title;
    }

    public void setTitle(TitleEnum title) {
        this.title = title;
    }

    public Product getOffer() {
        return this.offer;
    }

    public void setOffer(Product offer) {
        this.offer = offer;
    }

    public String getSingleDestination() {
        if (this.isSingleDestination()) {
            return this.destinations.iterator().next();
        } else {
            return null;
        }
    }

    public boolean isSingleDestination() {
        return this.destinations.size() == 1;
    }

    public void addDestination(String destination) {
        this.destinations.add(destination);
    }

    public Set<String> getDestinations() {
        return this.destinations;
    }

    public void setDestinations(Set<String> destinations) {
        this.destinations = destinations;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }


}
