package com.despegar.p13n.hestia.recommend.allinone;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;

public class ItemHomeCoordinate {

	    private Product product;
	    private SectionsEnum row;
	    private int col;

	    public ItemHomeCoordinate(Product item, SectionsEnum row, int col) {
	        this.product = item;
	        this.row = row;
	        this.col = col;
	    }

	    public SectionsEnum getRow() {
	        return this.row;
	    }

	    public int getCol() {
	        return this.col;
	    }

	    public Product getProduct() {
	        return this.product;
	    }
}
