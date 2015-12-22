package com.despegar.p13n.hestia.recommend.allinone.activity;

import com.despegar.p13n.euler.commons.client.model.Product;

public class BuyActivity {
    private Product product;
    private UserActivity activity;

    public BuyActivity(Product product, UserActivity activity) {
        super();
        this.product = product;
        this.activity = activity;
    }

    public Product getProduct() {
        return this.product;
    }

    public UserActivity getActivity() {
        return this.activity;
    }

    @Override
    public String toString() {
        return "BuyActivity [product=" + this.product + ", activity=" + this.activity + "]";
    }

}
