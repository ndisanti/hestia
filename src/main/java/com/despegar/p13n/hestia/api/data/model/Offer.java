package com.despegar.p13n.hestia.api.data.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Offer {

	 private Title titleOffer;
	    private ItemHome offer;

	    @Deprecated
	    public Offer() {
	        // for serialization
	    }

	    public Offer(ItemHome offer) {
	        this.offer = offer;
	    }

	    public Offer(ItemHome offer, Title titleOffer) {
	        this.offer = offer;
	        this.titleOffer = titleOffer;
	    }

	    public ItemHome getOffer() {
	        return this.offer;
	    }

	    @JsonIgnore
	    public boolean isTitleRequired() {
	        return this.offer.getOfferType().isTitleRequired();
	    }

	    public void setOffer(ItemHome offer) {
	        this.offer = offer;
	    }

	    public Title getTitleOffer() {
	        return this.titleOffer;
	    }

	    public void setTitleOffer(Title titleOffer) {
	        this.titleOffer = titleOffer;
	    }

	    @Override
	    public String toString() {
	        return "Offer [offer=" + this.offer + ", titleOffer=" + this.titleOffer + "]";
	    }
}
