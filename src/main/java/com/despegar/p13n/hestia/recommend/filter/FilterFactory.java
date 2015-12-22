package com.despegar.p13n.hestia.recommend.filter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.external.hrm.HRMService;
import com.despegar.p13n.hestia.recommend.filter.ItemIdFilter.FilterType;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.newrelic.api.agent.Trace;

/**
 * A factory for creating Filter objects.
 *
 * @author jcastro
 * @since May 28, 2013
 */
@Component
public class FilterFactory {

    public static final String COUNTRY_DOMESTIC_FILTER_NAME = "COUNTRYDOMESTIC";
    public static final String COUNTRY_INTERNATIONAL_FILTER = "COUNTRYINTERNATIONAL";
    public static final String HOTEL_COUNTRY_FILTER_NAME = "HOTELCOUNTRY";
    public static final String HOTEL_CITY_FILTER_NAME = "HOTELCITY";

    private static final Logger logger = LoggerFactory.getLogger(FilterFactory.class);

    @Autowired
    private GeoService geoService;
    @Autowired
    private HRMService hrmService;

    @Value("${recommendations.filter.cache.minutes:5}")
    private int memoizeExpiration = 5;

    /**
     * Supplier that memoizes unpublished hotels filter in memory to avoid heavy calculation every time.
     * 
     * After the memoize expires, unpublished hotel lists is refreshed.
     * 
     */
    private Supplier<Filter<String>> unpublishedHotelsSupplier = Suppliers.memoizeWithExpiration(
        new Supplier<Filter<String>>() {
            @Override
            public Filter<String> get() {
                return FilterFactory.this.unpublishedHotelFilter();
            }
        }, this.memoizeExpiration, TimeUnit.MINUTES);

    @Trace
    public Filter<String> createFilterForItem(Product p1, Flow f1, Product p2, Flow f2, Map<String, String[]> parametersMap,
        String item) {
        List<Filter<String>> filterList = this.createParametersFilters(parametersMap);

        if (filterList.isEmpty()) {
            // Default filter for hotel/detail, only in case no other filter were requested.
            if (p1 == Product.HOTELS && f1 == Flow.DETAIL && p2 == Product.HOTELS && f2 == Flow.DETAIL) {
                String iata = this.hrmService.getHotelCity(item);
                if (iata != null) {
                    filterList.add(this.hotelCityFilter(iata));
                }
                filterList.add(this.unpublishedHotelsSupplier.get());
            }
        }
        return this.composeFilters(filterList);
    }

    @Trace
    public Filter<String> createFilterForLastSearches(Product p1, Flow f1, Product p2, Flow f2,
        Map<String, String[]> parametersMap, List<String> iatas) {
        List<Filter<String>> filterList = this.createParametersFilters(parametersMap);
        // filter last searched iatas
        if (iatas != null && !iatas.isEmpty()) {
            filterList.add(new ItemIdFilter(FilterType.EXCLUDE, Sets.newHashSet(iatas)));
        }
        return this.composeFilters(filterList);
    }

    private Filter<String> composeFilters(List<Filter<String>> filterList) {
        if (!filterList.isEmpty()) {
            return new CompositeFilter(filterList);
        } else {
            return null;
        }
    }

    private List<Filter<String>> createParametersFilters(Map<String, String[]> parametersMap) {
        List<Filter<String>> filterList = Lists.newArrayList();
        if (parametersMap != null) {
            for (Entry<String, String[]> entry : parametersMap.entrySet()) {
                String filterName = entry.getKey();
                String filterValue = entry.getValue()[0];
                Filter<String> filter = this.createSimpleFilter(filterName, filterValue);
                if (filter != null) {
                    filterList.add(filter);
                }
            }
        }
        return filterList;
    }

    private Filter<String> createSimpleFilter(String filterName, String filterValue) {
        String name = filterName.toUpperCase();
        if (COUNTRY_DOMESTIC_FILTER_NAME.equalsIgnoreCase(name)) {
            return this.domesticDestinationFilter(filterValue);
        } else if (COUNTRY_INTERNATIONAL_FILTER.equalsIgnoreCase(name)) {
            return this.internationalDestinationFilter(filterValue);
        } else if (HOTEL_COUNTRY_FILTER_NAME.equalsIgnoreCase(name)) {
            return this.hotelCountryFilter(filterValue);
        } else if (HOTEL_CITY_FILTER_NAME.equalsIgnoreCase(name)) {
            return this.hotelCityFilter(filterValue);
        } else {
            // if it's not some of the other parameters we expect in the query string, log an error
            if (!Arrays.asList("FILTERSENABLED", "COUNTRY", "LIMIT", "SEARCHES").contains(name)) {
                logger.error("No filter for name [{}]", filterName);
            }
            return null;
        }
    }

    private Filter<String> domesticDestinationFilter(String country) {
        return this.destinationFilter(country, FilterType.INCLUDE);
    }

    private Filter<String> internationalDestinationFilter(String country) {
        return this.destinationFilter(country, FilterType.EXCLUDE);
    }

    private Filter<String> destinationFilter(String country, FilterType type) {
        List<String> cities = this.geoService.getCitiesForCountry(country);
        if (cities != null) {
            return new ItemIdFilter(type, Sets.newHashSet(cities));
        } else {
            logger.error("Invalid country code [{}]", country);
            return null;
        }
    }

    private Filter<String> hotelCountryFilter(String countryIso) {
        String country = countryIso;
        List<String> hotelIds = this.hrmService.getHotelIdsForCountry(country);
        return new ItemIdFilter(FilterType.INCLUDE, Sets.newHashSet(hotelIds));
    }

    private Filter<String> hotelCityFilter(String cityIata) {
        String city = cityIata;
        List<String> hotelIds = this.hrmService.getHotelIdsForCity(city);
        return new ItemIdFilter(FilterType.INCLUDE, Sets.newHashSet(hotelIds));
    }


    /**
     * This method is quite expensive because unpublished hotels list has a size greather than 13K.
     * That's the reason why we use {@link #unpublishedHotelsSupplier}
     * 
     * than 
     * @return
     */
    private Filter<String> unpublishedHotelFilter() {
        return new ItemIdFilter(FilterType.EXCLUDE, this.hrmService.getUnpublishedHotels());
    }



}
