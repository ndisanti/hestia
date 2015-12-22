package com.despegar.p13n.hestia.recommend.allinone.rules.products.functions;

import java.util.List;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFunction;
import com.despegar.p13n.hestia.utils.ProductCountrySupportUtils;
import com.google.common.collect.Lists;

@Service
public class MultiDestinationSearchProduct
    implements ProductFunction {

    @Override
    public ProductFuncCode getFunctionCode() {
        return ProductFuncCode.MULTIDESTINATION_SEARCH_PRODUCTS;
    }

    @Override
    public List<Product> getProducts(ItemTypeId idType, String itemId, ActionRecommendation action) {

        List<Product> products = Lists.newArrayList(Product.FLIGHTS, //
            Product.FLIGHTS, //
            Product.FLIGHTS, //
            Product.HOTELS, //
            Product.HOTELS, //
            Product.CARS, //
            Product.FLIGHTS, //
            Product.HOTELS, //
            Product.HOTELS, //
            Product.HOTELS);
        CountryCode cc = action.getCountryCode();
        for (int i = 0; i < products.size(); i++) {
            if (!ProductCountrySupportUtils.isSupported(cc, products.get(i))) {
                products.set(i, Product.HOTELS);
            }
        }
        return products;
    }

}
