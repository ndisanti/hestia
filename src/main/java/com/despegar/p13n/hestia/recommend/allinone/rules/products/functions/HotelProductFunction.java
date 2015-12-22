package com.despegar.p13n.hestia.recommend.allinone.rules.products.functions;

import java.util.List;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFunction;
import com.google.common.collect.Lists;

@Service
public class HotelProductFunction
    implements ProductFunction {

    @Override
    public ProductFuncCode getFunctionCode() {
        return ProductFuncCode.HOTELS;
    }

    @Override
    public List<Product> getProducts(ItemTypeId idType, String itemId, ActionRecommendation action) {
        return Lists.newArrayList(Product.HOTELS);
    }

}
