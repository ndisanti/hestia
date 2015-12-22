package com.despegar.p13n.hestia.utils;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.collections.map.DefaultedMap;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.HomeContentRuleService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class ProductCountrySupportUtils {

	 private static DefaultedMap supportedProducts = new DefaultedMap(Lists.newArrayList(Product.FLIGHTS, Product.HOTELS,
		        Product.CARS, Product.HOME_AS_PRODUCT, Product.CLOSED_PACKAGES));
		    static {

		        supportedProducts.put(CountryCode.AR, Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS,
		            Product.CLOSED_PACKAGES, Product.COMBINED_PRODUCTS, Product.CRUISES, Product.ACTIVITIES,
		            Product.VACATIONRENTALS, Product.INSURANCE, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.BR, Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS,
		            Product.CLOSED_PACKAGES, Product.COMBINED_PRODUCTS, Product.CRUISES, Product.ACTIVITIES,
		            Product.VACATIONRENTALS, Product.INSURANCE, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.MX, Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS,
		            Product.CLOSED_PACKAGES, Product.COMBINED_PRODUCTS, Product.CRUISES, Product.ACTIVITIES,
		            Product.VACATIONRENTALS, Product.INSURANCE, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.CO, Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS,
		            Product.CLOSED_PACKAGES, Product.COMBINED_PRODUCTS, Product.CRUISES, Product.ACTIVITIES,
		            Product.VACATIONRENTALS, Product.INSURANCE, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.CL, Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS,
		            Product.CLOSED_PACKAGES, Product.COMBINED_PRODUCTS, Product.CRUISES, Product.ACTIVITIES,
		            Product.VACATIONRENTALS, Product.INSURANCE, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.BO, Lists.newArrayList(Product.ACTIVITIES, Product.INSURANCE,
		            Product.VACATIONRENTALS, Product.CARS, Product.HOTELS, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.CR, Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS,
		            Product.CLOSED_PACKAGES, Product.COMBINED_PRODUCTS, Product.ACTIVITIES, Product.VACATIONRENTALS,
		            Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.DO, Lists.newArrayList(Product.HOTELS, Product.CARS, Product.ACTIVITIES,
		            Product.VACATIONRENTALS, Product.INSURANCE, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.EC, Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS,
		            Product.CLOSED_PACKAGES, Product.COMBINED_PRODUCTS, Product.ACTIVITIES, Product.VACATIONRENTALS,
		            Product.INSURANCE, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.ES,
		            Lists.newArrayList(Product.HOTELS, Product.CARS, Product.VACATIONRENTALS, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.GT, Lists.newArrayList(Product.HOTELS, Product.CARS, Product.ACTIVITIES,
		            Product.VACATIONRENTALS, Product.INSURANCE, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.HN, Lists.newArrayList(Product.HOTELS, Product.CARS, Product.ACTIVITIES,
		            Product.VACATIONRENTALS, Product.INSURANCE, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.NI, Lists.newArrayList(Product.HOTELS, Product.CARS, Product.ACTIVITIES,
		            Product.VACATIONRENTALS, Product.INSURANCE, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.PA, Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS,
		            Product.CLOSED_PACKAGES, Product.COMBINED_PRODUCTS, Product.ACTIVITIES, Product.VACATIONRENTALS,
		            Product.INSURANCE, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.PE, Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS,
		            Product.CLOSED_PACKAGES, Product.COMBINED_PRODUCTS, Product.ACTIVITIES, Product.VACATIONRENTALS,
		            Product.INSURANCE, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.PR, Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS,
		            Product.ACTIVITIES, Product.VACATIONRENTALS, Product.INSURANCE, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.PY, Lists.newArrayList(Product.HOTELS, Product.CARS, Product.ACTIVITIES,
		            Product.VACATIONRENTALS, Product.INSURANCE, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.UY, Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS,
		            Product.CLOSED_PACKAGES, Product.COMBINED_PRODUCTS, Product.ACTIVITIES, Product.VACATIONRENTALS,
		            Product.INSURANCE, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.SV, Lists.newArrayList(Product.HOTELS, Product.CARS, Product.ACTIVITIES,
		            Product.VACATIONRENTALS, Product.INSURANCE, Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.US, Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS,
		            Product.CLOSED_PACKAGES, Product.COMBINED_PRODUCTS, Product.ACTIVITIES, Product.VACATIONRENTALS,
		            Product.HOME_AS_PRODUCT));
		        supportedProducts.put(CountryCode.VE, Lists.newArrayList(Product.FLIGHTS, Product.HOTELS, Product.CARS,
		            Product.CLOSED_PACKAGES, Product.COMBINED_PRODUCTS, Product.HOME_AS_PRODUCT));
		    }


		    /**
		     * Returns true if the product is not supported in the given country
		     */
		    @SuppressWarnings("unchecked")
		    public static boolean isMissing(CountryCode countryCode, Product pr) {
		        Preconditions.checkNotNull(countryCode);
		        Preconditions.checkNotNull(pr);
		        return !((List<Product>) supportedProducts.get(countryCode)).contains(pr);
		    }

		    /**
		     * Returns true if the product is supported in the given country
		     */
		    public static boolean isSupported(CountryCode countryCode, Product pr) {
		        return !isMissing(countryCode, pr);
		    }

		    /**
		     * Returns true if all products are supported in the given country
		     */
		    public static boolean areAllSupported(CountryCode countryCode, Collection<Product> prs) {
		        for (Product pr : prs) {
		            if (isMissing(countryCode, pr)) {
		                return false;
		            }
		        }
		        return true;
		    }


		    public static EnumSet<Product> getHomesSupported(CountryCode cc) {

		        EnumSet<Product> homesByCountry = EnumSet.noneOf(Product.class);

		        for (Product pr : HomeContentRuleService.ALL_HOMES) {
		            if (isSupported(cc, pr)) {
		                homesByCountry.add(pr);
		            }
		        }

		        return homesByCountry;
		    }
}
