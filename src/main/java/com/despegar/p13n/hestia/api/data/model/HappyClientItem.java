package com.despegar.p13n.hestia.api.data.model;

import com.despegar.p13n.euler.commons.client.model.Product;

public class HappyClientItem  extends ItemHome {

    private Product product;

    // for serialization
    @Deprecated
    public HappyClientItem() {
        super(ItemType.HAPPY_CLIENT);
    }

    public HappyClientItem(Product product) {
        super(ItemType.HAPPY_CLIENT);
        this.product = product;
    }

    public Product getProduct() {
        return this.product;
    }

    @Override
    public String getId() {
        return this.getOfferType().name();
    }

    @Override
    public String toString() {
        return "HappyClientItem [product=" + this.product + " ]";
    }
}
