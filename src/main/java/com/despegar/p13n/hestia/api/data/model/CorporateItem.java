package com.despegar.p13n.hestia.api.data.model;

public class CorporateItem extends ItemHome {

	public CorporateItem() {
	    super(ItemType.CORPORATE);
	}
	
	@Override
	public String getId() {
	    return this.getOfferType().name();
	}
	
	@Override
	public String toString() {
	    return "CorporateItem []";
	}
}
