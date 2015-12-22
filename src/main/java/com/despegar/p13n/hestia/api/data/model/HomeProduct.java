package com.despegar.p13n.hestia.api.data.model;

import java.util.List;

public class HomeProduct {

	 private final List<Offer> specialOffersModule;

	    private final List<RowHome> rowModules;

	    /**
	     * @param specialOfferModule
	     * @param rowModules
	     */
	    public HomeProduct(final List<Offer> specialOffersModule, final List<RowHome> rowModules) {
	        super();
	        this.specialOffersModule = specialOffersModule;
	        this.rowModules = rowModules;
	    }

	    public List<Offer> getSpecialOffersModule() {
	        return this.specialOffersModule;
	    }

	    public List<RowHome> getRowModules() {
	        return this.rowModules;
	    }


	    @Override
	    public String toString() {
	        return "HomeProduct [ specialOffersModule=" + this.specialOffersModule + ", rowModules=" + this.rowModules + "]";
	    }
}
