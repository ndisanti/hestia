package com.despegar.p13n.hestia.recommend.allinone.rules;

import java.util.EnumSet;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.ItemType;
import com.google.common.base.Preconditions;

/**
 * Home support
 */
public enum HomeSupport {

    VACATION_RENTALS(Product.VACATIONRENTALS), //
    CARS(Product.CARS), //
    CRUISES(Product.CRUISES), //
    HOTELS(Product.HOTELS), //
    FLIGHTS(Product.FLIGHTS), //
    CLOSED_PACKAGES(Product.CLOSED_PACKAGES), //
    COMBINED_PRODUCTS(Product.COMBINED_PRODUCTS), //
    HOME_AS_PRODUCT(Product.HOME_AS_PRODUCT), //
    ACTIVITIES(Product.ACTIVITIES), //
    INSURANCE(Product.INSURANCE),
    ALLBUTHOME(Product.CARS, Product.CRUISES, Product.HOTELS, Product.FLIGHTS, Product.CLOSED_PACKAGES,
                    Product.COMBINED_PRODUCTS, Product.ACTIVITIES, Product.INSURANCE), //
    ANY(Product.CARS, Product.CRUISES, Product.HOTELS, Product.FLIGHTS, Product.CLOSED_PACKAGES, Product.COMBINED_PRODUCTS,
                    Product.HOME_AS_PRODUCT, Product.ACTIVITIES, Product.INSURANCE),
    MAIN(Product.CARS, Product.CRUISES, Product.HOTELS, Product.FLIGHTS, Product.CLOSED_PACKAGES, Product.ACTIVITIES);

    private EnumSet<Product> products;

    private HomeSupport(Product... products) {
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

    public static HomeSupport getHomeSupport(Product pr) {

        for (HomeSupport range : HomeSupport.values()) {
            if (range.contains(pr)) {
                return range;
            }
        }
        return ANY;
    }

    public static ItemType getItemType(Class<? extends ItemHome> class1) {
        // TODO Auto-generated method stub
        return null;
    }

}
