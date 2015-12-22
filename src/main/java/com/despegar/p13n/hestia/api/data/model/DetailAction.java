package com.despegar.p13n.hestia.api.data.model;

import com.despegar.p13n.euler.commons.client.model.UserAction;


/**
 * Represents the event of a user entering the detail page of a product.
 * 
 * @author lbernardi
 * 
 */
public class DetailAction
    extends UserAction {

    /**
     * 
     */
    private static final String PRODUCT_REF_KEY = "hn";

    public String productRef() {
        return this.getStringFromActionMap(PRODUCT_REF_KEY);
    }


}
