package com.despegar.p13.hestia.recommend.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.test.MockitoAnnotationBaseTest;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.external.hrm.HRMService;
import com.despegar.p13n.hestia.recommend.filter.Filter;
import com.despegar.p13n.hestia.recommend.filter.FilterFactory;
@RunWith(MockitoJUnitRunner.class)
public class FilterFactoryTest
    extends MockitoAnnotationBaseTest {

    private static final String COUNTRY = "AR";
    private static final String CITY = "CA6";

    private static final String HOTEL_ID = "1";
    private static final List<String> CITY_HOTELS = Arrays.asList(HOTEL_ID, "2", "3");
    private static final List<String> CITY2_HOTELS = Arrays.asList("4", "5");

    private static final List<String> COUNTRY_HOTELS = Arrays.asList(HOTEL_ID, "2", "3", "4", "5", "6");
    private static final List<String> COUNTRY2_HOTELS = Arrays.asList("10", "11", "12");


    private static final List<String> DOMESTIC_CITIES = Arrays.asList("BUE", "MDQ", "BRC");
    private static final List<String> INTERNATIONAL_CITIES = Arrays.asList("BCN", "MIA", "NYC");
    @SuppressWarnings("unchecked")
    private static final Collection<String> ALL_CITIES = CollectionUtils.union(INTERNATIONAL_CITIES, DOMESTIC_CITIES);

    @InjectMocks
    private FilterFactory factory = new FilterFactory();
    @Mock
    private GeoService geoService;
    @Mock
    private HRMService hrmService;


    @Before
    public void setUp() {
        Mockito.when(this.geoService.getCitiesForCountry(COUNTRY)).thenReturn(DOMESTIC_CITIES);
        Mockito.when(this.hrmService.getHotelIdsForCity(CITY)).thenReturn(CITY_HOTELS);
        Mockito.when(this.hrmService.getHotelIdsForCountry(COUNTRY)).thenReturn(COUNTRY_HOTELS);
        Mockito.when(this.hrmService.getHotelCity(HOTEL_ID)).thenReturn(CITY);
    }

    @Test
    public void testDomestic() {
        Map<String, String[]> map = Collections.singletonMap("countryDomestic", new String[] {COUNTRY});
        Filter<String> filter = this.factory.createFilterForItem(Product.FLIGHTS, Flow.SEARCH, Product.FLIGHTS, Flow.SEARCH,
            map, null);
        for (String city : DOMESTIC_CITIES) {
            Assert.assertFalse(filter.filter(city));
        }
        for (String city : INTERNATIONAL_CITIES) {
            Assert.assertTrue(filter.filter(city));
        }
    }

    @Test
    public void testInternational() {
        Map<String, String[]> map = Collections.singletonMap("countryInternational", new String[] {COUNTRY});
        Filter<String> filter = this.factory.createFilterForItem(Product.FLIGHTS, Flow.SEARCH, Product.FLIGHTS, Flow.SEARCH,
            map, null);
        for (String city : DOMESTIC_CITIES) {
            Assert.assertTrue(filter.filter(city));
        }
        for (String city : INTERNATIONAL_CITIES) {
            Assert.assertFalse(filter.filter(city));
        }
    }

    @Test
    public void testLastSearchedCitiesFilter() {
        List<String> excluded = new ArrayList<String>();
        excluded.add("MIA");
        @SuppressWarnings("unchecked")
        Collection<String> included = CollectionUtils.disjunction(ALL_CITIES, excluded);
        Filter<String> filter = this.factory.createFilterForLastSearches(Product.FLIGHTS, Flow.SEARCH, Product.FLIGHTS,
            Flow.SEARCH, null, excluded);
        for (String city : excluded) {
            Assert.assertTrue(filter.filter(city));
        }

        for (String city : included) {
            Assert.assertFalse(filter.filter(city));
        }
    }

    @Test
    public void testHotelsCountryFilter() {
        Map<String, String[]> map = Collections.singletonMap("hotelCountry", new String[] {COUNTRY});
        Filter<String> filter = this.factory.createFilterForItem(Product.HOTELS, Flow.DETAIL, Product.HOTELS, Flow.DETAIL,
            map, null);

        for (String hotel : COUNTRY_HOTELS) {
            Assert.assertFalse(filter.filter(hotel));
        }
        for (String hotel : COUNTRY2_HOTELS) {
            Assert.assertTrue(filter.filter(hotel));
        }
    }

    @Test
    public void testHotelsCityFilter() {
        Map<String, String[]> map = Collections.singletonMap("hotelCity", new String[] {CITY});
        Filter<String> filter = this.factory.createFilterForItem(Product.HOTELS, Flow.DETAIL, Product.HOTELS, Flow.DETAIL,
            map, null);
        for (String hotel : CITY_HOTELS) {
            Assert.assertFalse(filter.filter(hotel));
        }
        for (String hotel : CITY2_HOTELS) {
            Assert.assertTrue(filter.filter(hotel));
        }
    }

    @Test
    public void testDefaultCityFilter() {
        Map<String, String[]> empty = Collections.emptyMap();
        Filter<String> filter = this.factory.createFilterForItem(Product.HOTELS, Flow.DETAIL, Product.HOTELS, Flow.DETAIL,
            empty, HOTEL_ID);
        for (String hotel : CITY_HOTELS) {
            Assert.assertFalse(filter.filter(hotel));
        }
        for (String hotel : CITY2_HOTELS) {
            Assert.assertTrue(filter.filter(hotel));
        }
    }
}
