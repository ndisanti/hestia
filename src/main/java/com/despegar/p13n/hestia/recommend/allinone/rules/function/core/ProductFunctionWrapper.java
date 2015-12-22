package com.despegar.p13n.hestia.recommend.allinone.rules.function.core;

import java.util.List;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;

public class ProductFunctionWrapper
    implements ProductFunction {

    private ProductFunction productFunction;

    public ProductFunctionWrapper(ProductFunction productFunction) {
        this.productFunction = productFunction;
    }

    public List<Product> getProducts(ItemTypeId idType, String itemId, ActionRecommendation action) {
        return this.productFunction.getProducts(idType, itemId, action);
    }
    
    public ProductFuncCode getFunctionCode() {
        return this.productFunction.getFunctionCode();
    }

}
