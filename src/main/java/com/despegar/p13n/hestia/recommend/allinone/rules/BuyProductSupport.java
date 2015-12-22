package com.despegar.p13n.hestia.recommend.allinone.rules;

import java.util.EnumSet;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.google.common.base.Preconditions;

/**
 * Buy product support
 */
public enum BuyProductSupport {

    CARS(Product.CARS), //
    CRUISES(Product.CRUISES), //
    HOTELS(Product.HOTELS), //
    FLIGHTS(Product.FLIGHTS), //
    CLOSED_PACKAGES(Product.CLOSED_PACKAGES), //
    COMBINED_PRODUCTS(Product.COMBINED_PRODUCTS), //
    ACTIVITIES(Product.ACTIVITIES), //
    ANY(Product.CARS, Product.CRUISES, Product.HOTELS, Product.FLIGHTS, Product.CLOSED_PACKAGES, Product.HOME_AS_PRODUCT,
                    Product.ACTIVITIES, Product.VACATIONRENTALS), //
    NONE(),
    VACATION_RENTALS(Product.VACATIONRENTALS);

    private EnumSet<Product> products;

    private BuyProductSupport(Product... products) {
        EnumSet<Product> set = EnumSet.noneOf(Product.class);

        for (Product pr : products) {
            set.add(pr);
        }
        this.products = set;
    }

    public EnumSet<Product> getProducts() {
        return this.products;
    }

    public boolean contains(Product pr) {

        Preconditions.checkNotNull(pr);

        if (this == ANY) {
            return true;
        }

        if (this.products.contains(pr)) {
            return true;
        } else {
            return false;
        }
    }

    public static BuyProductSupport getHomeSupport(Product pr) {

        for (BuyProductSupport range : BuyProductSupport.values()) {
            if (range.contains(pr)) {
                return range;
            }
        }
        return ANY;
    }

}
