package com.despegar.p13n.hestia.recommend.allinone.rules.function.core;

import java.util.List;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;

/**
 * <p>Based on a {@link ActionRecommendation}, {@link ItemTypeId} and item id, it builds a product list</p>
 * 
 * <p>A ProductFunction is associated with a unique {@link ProductFuncCode}</p>
 */
public interface ProductFunction {

    ProductFuncCode getFunctionCode();

    List<Product> getProducts(ItemTypeId idType, String itemId, ActionRecommendation action);

}
