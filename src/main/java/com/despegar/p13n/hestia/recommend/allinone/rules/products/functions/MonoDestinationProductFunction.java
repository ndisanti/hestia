package com.despegar.p13n.hestia.recommend.allinone.rules.products.functions;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFunction;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.despegar.p13n.hestia.utils.ProductCountrySupportUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class MonoDestinationProductFunction
    implements ProductFunction {

    private Map<ProdFunctionKey, List<Product>> products = Maps.newHashMap();

    @PostConstruct
    public void initMaps() {

        this.products.put(new ProdFunctionKey(ActivityType.BUY, Product.HOME_AS_PRODUCT, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.CARS,
                Product.HOTELS, Product.ACTIVITIES, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.BUY, Product.FLIGHTS, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.HOTELS, Product.CARS, Product.ACTIVITIES,
                Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.BUY, Product.HOTELS, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.CARS,
                Product.HOTELS, Product.ACTIVITIES, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.BUY, Product.INSURANCE, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.CARS,
                Product.HOTELS, Product.ACTIVITIES, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.BUY, Product.CARS, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS, Product.HOTELS, Product.HOTELS,
                Product.ACTIVITIES, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.ACTIVITIES));

        this.products.put(new ProdFunctionKey(ActivityType.BUY, Product.CRUISES, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS,
                Product.CARS, Product.ACTIVITIES, Product.ACTIVITIES, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.BUY, Product.CLOSED_PACKAGES, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.FLIGHTS, Product.HOTELS, Product.CARS,
                Product.ACTIVITIES, Product.HOTELS, Product.HOTELS, Product.ACTIVITIES, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.BUY, Product.ACTIVITIES, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.ACTIVITIES, Product.ACTIVITIES, Product.ACTIVITIES,
                Product.HOTELS, Product.CARS, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.BUY, Product.VACATIONRENTALS, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.VACATIONRENTALS, Product.VACATIONRENTALS, Product.CARS,
                Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.VACATIONRENTALS,
                Product.VACATIONRENTALS));

        this.products.put(new ProdFunctionKey(ActivityType.SEARCH, Product.HOME_AS_PRODUCT, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.HOTELS, Product.CARS, Product.HOTELS,
                Product.ACTIVITIES, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.SEARCH, Product.FLIGHTS, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.HOTELS, Product.CARS, Product.HOTELS,
                Product.ACTIVITIES, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.SEARCH, Product.HOTELS, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.HOTELS, Product.CARS, Product.HOTELS,
                Product.ACTIVITIES, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.SEARCH, Product.INSURANCE, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.HOTELS, Product.CARS, Product.HOTELS,
                Product.ACTIVITIES, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.SEARCH, Product.CARS, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS, Product.ACTIVITIES, Product.HOTELS,
                Product.HOTELS, Product.ACTIVITIES, Product.HOTELS, Product.HOTELS, Product.ACTIVITIES));

        this.products.put(new ProdFunctionKey(ActivityType.SEARCH, Product.CRUISES, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS,
                Product.CARS, Product.ACTIVITIES, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.SEARCH, Product.CLOSED_PACKAGES, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS, Product.HOTELS, Product.HOTELS,
                Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.SEARCH, Product.ACTIVITIES, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.ACTIVITIES, Product.ACTIVITIES, Product.CARS,
                Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.ACTIVITIES, Product.ACTIVITIES));

        this.products.put(new ProdFunctionKey(ActivityType.SEARCH, Product.VACATIONRENTALS, false), //
            Lists.newArrayList(Product.FLIGHTS, Product.VACATIONRENTALS, Product.VACATIONRENTALS, Product.CARS,
                Product.HOTELS, Product.ACTIVITIES, Product.CARS, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.BUY, Product.CLOSED_PACKAGES, true), //
            Lists.newArrayList(Product.HOTELS, Product.FLIGHTS, Product.CARS, Product.HOTELS, Product.HOTELS,
                Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.BUY, Product.FLIGHTS, true), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS, Product.HOTELS, Product.HOTELS,
                Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.BUY, Product.CARS, true), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS, Product.HOTELS, Product.HOTELS,
                Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.SEARCH, Product.HOTELS, true), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.HOTELS, Product.CARS, Product.HOTELS,
                Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.SEARCH, Product.FLIGHTS, true), //
            Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.HOTELS, Product.CARS, Product.HOTELS,
                Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS));

        this.products.put(new ProdFunctionKey(ActivityType.SEARCH, Product.CARS, true), //
            Lists.newArrayList(Product.FLIGHTS, Product.CARS, Product.HOTELS, Product.HOTELS, Product.HOTELS,
                Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.HOTELS));


    };

    @Override
    public ProductFuncCode getFunctionCode() {
        return ProductFuncCode.PRODUCTS;
    }

    @Override
    public List<Product> getProducts(ItemTypeId idType, String itemId, ActionRecommendation action) {

        ItemUtils.checkItemTypeId(ItemTypeId.DESTINATION, idType);


        List<Product> products = Lists.newArrayList();

        boolean isException = this.isProductFunctionException(action);
        switch (action.getActivityType()) {
        case SEARCH:

            products = this.products.get(new ProdFunctionKey(ActivityType.SEARCH, action.getCurrentHome(), isException));
            break;
        case BUY:

            products = this.products.get(new ProdFunctionKey(ActivityType.BUY, action.getCurrentHome(), isException));
            if (this.isBuyFlightForDestiny(action, itemId)) {
                products.remove(0);
                products.add(0, Product.HOTELS);
            }
            break;
        default:
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < products.size(); i++) {
            Product pr = products.get(i);
            if (ProductCountrySupportUtils.isMissing(action.getCountryCode(), pr)) {
                products.remove(i);
                products.add(i, Product.HOTELS);
            }
        }
        return products;
    }

    private boolean isProductFunctionException(ActionRecommendation action) {

        boolean countryException = action.getCountryCode().equals(CountryCode.VE)
            || CountryCode.isInternational(action.getCountryCode());

        if (!countryException) {
            return false;
        }

        switch (action.getActivityType()) {
        case SEARCH:

            return action.getCurrentHome().equals(Product.FLIGHTS) || action.getCurrentHome().equals(Product.HOTELS)
                || action.getCurrentHome().equals(Product.CARS);

        case BUY:
            return action.getCurrentHome().equals(Product.FLIGHTS)
                || action.getCurrentHome().equals(Product.CLOSED_PACKAGES) || action.getCurrentHome().equals(Product.CARS);
        default:
            throw new IllegalArgumentException();
        }
    }

    public boolean isBuyFlightForDestiny(ActionRecommendation action, String itemId) {
        return action.getBuyActivity().getProduct().equals(Product.FLIGHTS)
            && action.getBuyActivity().getActivity().getDestination().equalsIgnoreCase(itemId);
    }

    private class ProdFunctionKey {

        private ActivityType activityType;
        private Product home;
        private boolean isInternational;

        public ProdFunctionKey(ActivityType activityType, Product home, boolean isInternational) {
            super();
            this.activityType = activityType;
            this.home = home;
            this.isInternational = isInternational;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + this.getOuterType().hashCode();
            result = prime * result + ((this.activityType == null) ? 0 : this.activityType.hashCode());
            result = prime * result + ((this.home == null) ? 0 : this.home.hashCode());
            result = prime * result + (this.isInternational ? 1231 : 1237);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            ProdFunctionKey other = (ProdFunctionKey) obj;
            if (!this.getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (this.activityType != other.activityType) {
                return false;
            }
            if (this.home != other.home) {
                return false;
            }
            if (this.isInternational != other.isInternational) {
                return false;
            }
            return true;
        }

        private MonoDestinationProductFunction getOuterType() {
            return MonoDestinationProductFunction.this;
        }
    }
}
