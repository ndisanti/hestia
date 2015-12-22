package com.despegar.p13n.hestia.recommend.allinone;

import java.util.EnumMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.data.ProductData;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.utils.ProductCountrySupportUtils;
import com.google.common.collect.Lists;

@Component
public class SearchBoxRecommender {

    private EnumMap<Product, Product> priorityProdChain = new EnumMap<Product, Product>(Product.class);

    public SearchBoxRecommender() {
        this.init();
    }

    private void init() {
        this.priorityProdChain.put(Product.FLIGHTS, Product.HOTELS);
        this.priorityProdChain.put(Product.HOTELS, Product.FLIGHTS);
        this.priorityProdChain.put(Product.CARS, Product.FLIGHTS);
        this.priorityProdChain.put(Product.ACTIVITIES, Product.FLIGHTS);
        this.priorityProdChain.put(Product.TRANSFER, Product.FLIGHTS);
        this.priorityProdChain.put(Product.BUS, Product.FLIGHTS);
        this.priorityProdChain.put(Product.INSURANCE, Product.FLIGHTS);
        this.priorityProdChain.put(Product.CRUISES, Product.FLIGHTS);
        this.priorityProdChain.put(Product.CLOSED_PACKAGES, Product.CARS);
        this.priorityProdChain.put(Product.COMBINED_PRODUCTS, Product.ACTIVITIES);
        this.priorityProdChain.put(Product.HOME_AS_PRODUCT, Product.FLIGHTS);
    }

    public void recommendHome(HomeContent homeContent, ActionRecommendation action) {

        Product recommendedHome = Product.HOTELS;

        switch (action.getActivityType()) {
        case SEARCH:
            recommendedHome = this.getRecommendedHomeForSearch(action);
            break;
        case BUY:
            recommendedHome = this.getRecommendedHomeForBuy(action);
            break;
        case NO_HISTORY:
            recommendedHome = this.getRecommendedHomeForNoHistory(action);
        default:
        }

        recommendedHome = ProductCountrySupportUtils.isSupported(action.getCountryCode(), recommendedHome) ? recommendedHome
            : Product.HOTELS;
        homeContent.setRecommendedHome(recommendedHome);
    }

    private Product getRecommendedHomeForNoHistory(ActionRecommendation action) {
        return action.getNoHistoryRecommendedHome();
    }

    private Product getRecommendedHomeForBuy(ActionRecommendation action) {
        return this.priorityProdChain.get(action.getBuyActivity().getProduct());
    }

    private Product getRecommendedHomeForSearch(ActionRecommendation action) {
        return this.executSteps(action.getUserContext());
    }

    private Product executSteps(UserContext userContext) {
        return this.executStep1(userContext);
    }

    private Product executStep1(UserContext userContext) {
        Product pr = this.getLastProductForFlow(userContext, Flow.CHECKOUT);
        return pr == null ? this.executStep2(userContext) : pr;
    }

    private Product executStep2(UserContext userContext) {
        Product pr = this.getLastProductForFlow(userContext, Flow.DETAIL);
        return pr == null ? this.executStep3(userContext) : pr;
    }

    private Product executStep3(UserContext userContext) {
        Product pr = this.getLastProductForFlow(userContext, Flow.SEARCH);
        return pr == null ? Product.HOTELS : pr;
    }

    private Product getLastProductForFlow(UserContext userContext, Flow flow) {
        List<ProductData> data = userContext.getProductDataList(Lists.newArrayList(flow));

        Product product = null;
        if (!data.isEmpty()) {
            product = data.get(0).getParent().getProduct();
            product = product.equals(Product.COMBINED_PRODUCTS) ? Product.CLOSED_PACKAGES : product;
        }
        return product;
    }
}
