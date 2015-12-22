package com.despegar.p13n.hestia.recommend.allinone.title;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.Title;
import com.google.common.base.Preconditions;

public class TitleBuilder {

    private TitleEnum title;
    private Product product;
    private String destination;
    private String origin;

    public static TitleBuilder builder(TitleEnum title) {
        return new TitleBuilder(title);
    }

    public TitleBuilder(TitleEnum title) {
        Preconditions.checkNotNull(title);
        this.title = title;
    }

    public TitleBuilder iata(String destination) {
        Preconditions.checkState(this.destination == null, "Destination is already set");
        this.destination = destination;
        return this;
    }

    public TitleBuilder pr(Product product) {
        Preconditions.checkState(this.product == null, "Product is already set");
        this.product = product;
        return this;
    }

    public TitleBuilder origin(String origin) {
        Preconditions.checkState(this.origin == null, "Origin is already set");
        this.origin = origin;
        return this;
    }

    public Title build() {

        // dont send these if are not required
        Product product = null;
        String destination = null;
        String origin = null;

        if (this.title.isProductRequired()) {
            Preconditions.checkNotNull(this.product, "Product is required for: " + this.title);
            product = this.product;
        }

        if (this.title.isDestinationRequired()) {
            Preconditions.checkNotNull(this.destination, "Destination is required for: " + this.title);
            destination = this.destination;
        }

        if (this.title.isOriginRequired()) {
            Preconditions.checkNotNull(this.origin, "Origin is required for " + this.title);
            origin = this.origin;
        }

        if (this.title.isProductRequired() && !this.title.getSupported().contains(this.product)) {
            throw new RuntimeException(this.title + " doesn't support " + this.product);
        }

        return new Title(this.title.toString(), product == null ? null : product.toString(), destination, origin);
    }
}
