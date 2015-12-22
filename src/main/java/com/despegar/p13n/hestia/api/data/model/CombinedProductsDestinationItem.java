package com.despegar.p13n.hestia.api.data.model;

public class CombinedProductsDestinationItem  extends DestinationItem {

    @Deprecated
    public CombinedProductsDestinationItem() {
        super(ItemType.COMBINED_PRODUCTS);
        // constructor for serializacion
    }

    /**
     * @param destination
     * @param carcat
     * @param price
     */
    public CombinedProductsDestinationItem(String destination) {
        super(ItemType.COMBINED_PRODUCTS, destination);
    }

    @Override
    public String toString() {
        return "CombinedProductsDestinationItem [destination=" + this.destination + "]";
    }}
