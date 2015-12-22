package com.despegar.p13n.hestia.recommend.allinone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.UserContextClient;
import com.despegar.p13n.euler.commons.client.UserContextClientParameters;
import com.despegar.p13n.euler.commons.client.model.Brand;
import com.despegar.p13n.euler.commons.client.model.BrandGroup;
import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.HomeUtils;
import com.despegar.p13n.euler.commons.client.model.IntenseSearch;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.ProductDataTransformer;
import com.despegar.p13n.euler.commons.client.model.UserAction;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserHistoryFilter;
import com.despegar.p13n.euler.commons.client.model.UserHistoryFilterEnum;
import com.despegar.p13n.euler.commons.client.model.UserLocation;
import com.despegar.p13n.euler.commons.client.model.data.ClosedPackageData;
import com.despegar.p13n.euler.commons.client.model.data.CruiseData;
import com.despegar.p13n.euler.commons.client.model.data.HotelData;
import com.despegar.p13n.euler.commons.client.model.data.ProductData;
import com.despegar.p13n.euler.commons.client.model.data.VacationRentalsData;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.activity.BuyActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.matrix.SearchCount;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newrelic.api.agent.Trace;

@Service
public class ActionRecommendationBuilder {

    private static final int HISTORY_DAYS = 60;
    private static final int HISTORY_ITEMS = 5 * HISTORY_DAYS;
    private static final String CHECK_IN = "ci";
    private static final String CHECK_IN_CARS = "dt";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    protected static final Logger log = LoggerFactory.getLogger(ActionRecommendationBuilder.class);

    private static final int GEO_TIMEOUT_GEOLOCATE_MS = 80;

    private static final EnumSet<Product> SUPPORTED_PRODUCT_HISTORY = EnumSet.of(Product.FLIGHTS, Product.CARS,
        Product.CRUISES, Product.HOTELS, Product.CLOSED_PACKAGES, Product.COMBINED_PRODUCTS, Product.ACTIVITIES,
        Product.TURISMO);

    private static final EnumSet<Flow> SUPPORTED_FLOW_HISTORY = EnumSet.of(Flow.SEARCH, Flow.LANDING, Flow.DETAIL,
        Flow.LANDING_DETAIL, Flow.CHECKOUT, Flow.THANKS);

    private static final Collection<Brand> SUPPORTED_BRAND_HISTORY = BrandGroup.DESPEGAR_AND_VIAJEROS.getBrands();

    @Autowired
    private GeoService geoService;
    @Autowired
    private UserContextClient usercontextClient;

    @Trace
    public ActionRecommendation buildActionRecommendation(HomeParam homeParam) {
        formatter.setLenient(false);
        final ActionRecommendation action = new ActionRecommendation(homeParam);

        if (homeParam.getToTs() != null) {
            action.addDebug("Filtering actions by timestamp. Showing before: " + homeParam.getToTs() + ", date: "
                + new DateTime(homeParam.getToTs()));
        }
        return action;
    }

    public void populateUserContext(final ActionRecommendation action) {
        
    	UserContextClientParameters params = UserContextClientParameters.builder()//
                .withUserId(action.getUserId())//
                .toNextStep()//
                .fromTimeStamp(new DateTime().minusDays(HISTORY_DAYS).getMillis())//
                .toTimeStamp(action.getHomeParam().getToTs() == null ? Long.MAX_VALUE : action.getHomeParam().getToTs())//
                .withAmountOfActions(HISTORY_ITEMS)//
                .includeWishlist(true)//
                .withFlows(SUPPORTED_FLOW_HISTORY)//
                .withProducts(SUPPORTED_PRODUCT_HISTORY)//
                .withBrands(SUPPORTED_BRAND_HISTORY)//
                .withTimeoutForIpLocationMillis(GEO_TIMEOUT_GEOLOCATE_MS)//
                .useRequestDataForLocalization(true, action.getHomeParam().getCc(), action.getHomeParam().getIp().getHostAddress())//
                .withUserHistoryFilters(getFilters())//
                .withProductDataTransformers(getTransformers())//
                .mergeRelatedSocialHistory(true)//
                .includeHistoricalSocialTransactions(true)//
                .build();

            UserContext userContext = this.usercontextClient.getUserContext(params);

            action.setUserContext(userContext);

            // if the user bought we also get the user searches
            this.populateBuyActivity(action);

            this.populateSearchActivity(action);

            this.addOrigin(action);

            this.addLastSearchedItems(action);
        
    }

    private static Collection<UserHistoryFilter> getFilters() {

        List<UserHistoryFilter> filters = Lists.newArrayList();

        filters.add(UserHistoryFilterEnum.DESTINATION_IATA_NOT_BLANK);
        filters.add(new UserHistoryFilter() {

            public boolean accept(ProductData data) {
                // filtering invalid hid for DESPEGAR events
                if (BrandGroup.ALL_DESPEGAR.getBrands().contains(data.getParent().getBrand())) {
                    if (HomeUtils.isDetailOrCheckout(data.getParent().getFlow())) {
                        if (data.getParent().getProduct() == Product.HOTELS
                            && StringUtils.isBlank(data.getProductBussinessId())) {
                            return false;
                        }
                    }
                }
                return true;
            }

            public String desc() {
                return "Hotel with no id";
            }
        });
        return filters;
    }

    private static Collection<ProductDataTransformer> getTransformers() {

        List<ProductDataTransformer> transformers = Lists.newArrayList();

        // change TURISMO events to HOTELS-LANDING
        // (since TURISMO is not an actual product that we offer)
        transformers.add(new ProductDataTransformer() {
            public ProductData transform(ProductData data) {
                if (Product.TURISMO.equals(data.getParent().getProduct())) {
                    data.getParent().setProduct(Product.HOTELS);
                    data.getParent().setFlow(Flow.LANDING);
                    data = ProductData.create(data.getParent());
                }
                return data;
            }
        });

        // change VIAJEROS events to LANDING
        // (since they don't have dates or hotel ids)
        transformers.add(new ProductDataTransformer() {
            public ProductData transform(ProductData data) {
                if (Brand.VIAJEROS.equals(data.getParent().getBrand())) {
                    data.getParent().setFlow(Flow.LANDING);
                }
                return data;
            }
        });

        return transformers;

    }

    private void addOrigin(ActionRecommendation action) {
        UserLocation userLocation = action.getHomeUserLocation();
        String origin = userLocation.getCity();
        action.setOrigin(origin);
        action.addDebug("Origin: " + origin);
    }

    private void addLastSearchedItems(ActionRecommendation action) {

        if (action.getSearchActivity() == null) {
            return;
        }

        ArrayListMultimap<Flow, ProductData> byFlow = ArrayListMultimap.create();

        for (ProductData prData : action.getHistory()) {
            byFlow.put(prData.getParent().getFlow(), prData);
        }

        for (Flow flow : Arrays.asList(Flow.CHECKOUT, Flow.LANDING_DETAIL, Flow.DETAIL, Flow.LANDING, Flow.SEARCH)) {
            if (byFlow.containsKey(flow)) {
                List<ProductData> prDataList = byFlow.get(flow);

                for (ProductData productData : prDataList) {
                    if (productData.getParent().getProduct() == Product.CRUISES) {
                        continue;
                    }

                    if (productData.getDestinationIata() != null) {
                        action.getSearchActivity().addLastSearchedItem(ItemTypeId.DESTINATION, productData);
                    }

                    switch (productData.getParent().getProduct()) {
                    case HOTELS:
                        HotelData hotelData = (HotelData) productData;
                        if (hotelData.getProductBussinessId() != null) {
                            action.getSearchActivity().addLastSearchedItem(ItemTypeId.HID, productData);
                        }
                        break;
                    case CRUISES:
                        CruiseData cruiseData = (CruiseData) productData;
                        if (cruiseData.did() != null) {
                            action.getSearchActivity().addLastSearchedItem(ItemTypeId.DID, productData);
                        }
                        break;
                    case CLOSED_PACKAGES:
                        ClosedPackageData closedPackageData = (ClosedPackageData) productData;
                        if (closedPackageData.clusterId() != null) {
                            action.getSearchActivity().addLastSearchedItem(ItemTypeId.CLUID, productData);
                        }
                        break;
                    case VACATIONRENTALS:
                        VacationRentalsData vacationRentalsData = (VacationRentalsData) productData;
                        if (vacationRentalsData.vrid() != null) {
                            action.getSearchActivity().addLastSearchedItem(ItemTypeId.VRID, productData);
                        }
                        break;
                    default:
                        break;
                    }
                }
            }
        }

        action.addDebug("Last destinations: "
            + (action.getSearchActivity() == null ? "" : action.getSearchActivity().getLastDestinations()));
        action.addLastDestinationTrace();
        action.addDebug("Last hotel ids for destination: "
            + (action.getSearchActivity() == null ? "" : action.getSearchActivity().getLastHotelsByDestination()));
    }


    private void populateBuyActivity(ActionRecommendation action) {

        for (ProductData prData : action.getHistory()) {

            if (prData.getParent().getFlow() == Flow.THANKS && prData.getDaysAnticipationFromNow() != null) {

                try {
                    String destination = this.getDestination(prData, action.getCountryCode());

                    int timeRange = this.getLastActionDays(prData.getParent().getTimestamp());

                    String prBizId = prData.getProductBussinessId();

                    int anticipation = this.getDaysAnticipationFromNow(prData);

                    UserActivity activity = new UserActivity(timeRange, destination, prBizId, anticipation,
                        prData.getParent(), SearchCount.NA);
                    BuyActivity buyAct = new BuyActivity(prData.getParent().getProduct(), activity);

                    if (this.isValidBuy(action, buyAct.getActivity())) {
                        action.setBuyActivity(buyAct);
                        break; // only get last buy
                    }

                } catch (ParseException e) {

                }
            }
        }
    }

    private boolean isValidBuy(ActionRecommendation action, UserActivity buyAct) throws ParseException {
        IntenseSearch intenseSearch = action.getUserContext().getUserRecord().getLastIntenseSearch();

        if (this.isValidInteseSearch(intenseSearch, buyAct.getDestination())) {

            int daysOfIntensSearch = Days.daysBetween(new DateTime(intenseSearch.getCreatedOn()), new DateTime()).getDays();

            String checkIn = buyAct.getProduct().equals(Product.CARS) ? CHECK_IN_CARS : CHECK_IN;

            Object ci = buyAct.getAction().getActionData().get(checkIn);

            if (ci == null) {
                return true;
            }
            int daysToCI = Days.daysBetween(new DateTime(), new DateTime(formatter.parse(ci.toString()).getTime()))
                .getDays();
            return !this.isSearchFlow(daysOfIntensSearch, daysToCI);
        }
        return true;
    }

    private boolean isSearchFlow(int daysOfIntensSearch, int daysToCI) {
        return (daysOfIntensSearch <= 3 && daysToCI > 3) || (daysOfIntensSearch <= 10 && daysToCI > 10);
    }

    private boolean isValidInteseSearch(IntenseSearch intenseSearch, String destination) {
        return intenseSearch != null && intenseSearch.getDestination() != null
            && !intenseSearch.getDestination().equals(destination);
    }


    private Integer getDaysAnticipationFromNow(ProductData prData) {
        Integer anticipation = prData.getDaysAnticipationFromNow();
        return (anticipation == null) ? 1 : anticipation;
    }

    private String getDestination(ProductData data, CountryCode countryCode) {
        String destination = null;

        // for cruise we get the destination from the itinerary
        // if not present, check itatas from region
        if (data instanceof CruiseData) {
            final CruiseData cruiseData = (CruiseData) data;
            destination = cruiseData.destination();

            if (destination == null) {
                String region = cruiseData.region();
                final List<String> iatasByRegion = this.geoService.getIatasByRegion(countryCode, region);
                if (iatasByRegion != null && iatasByRegion.size() > 0) {
                    destination = iatasByRegion.get(0);
                }
            }

        } else {
            destination = data.getDestinationIata();
        }
        return destination;
    }

    private int getLastActionDays(long ts) {
        return (int) TimeUnit.MILLISECONDS.toDays(new DateTime().getMillis() - ts);
    }


    /**
     * Looks for search activity (SEARCH and DETAIL, LANDING_DETAIL flows) for each product in history.
     * 
     * A DETAIL or LANDING_DETAIL overrides a SEARCH for a given product.
     */
    @Trace
    private void populateSearchActivity(ActionRecommendation actionRecom) {

        SearchActivity search = new SearchActivity();

        Map<Product, Integer> mostSearched = this.getProductSearchCount(actionRecom.getHistory());

        for (ProductData prData : actionRecom.getHistory()) {

            UserAction action = prData.getParent();

            if (HomeUtils.isSearchActivity(action.getFlow())) {

                String destination = this.getDestination(prData, actionRecom.getCountryCode());

                if (destination == null) {
                    continue;
                }

                if (search.canSetAction(action.getProduct(), destination, action.getFlow())) {

                    int lastActionDays = this.getLastActionDays(action.getTimestamp());

                    int anticipation = this.getDaysAnticipationFromNow(prData);
                    SearchCount searchCount = SearchCount.getRange(mostSearched.get(action.getProduct()));

                    UserActivity activity = new UserActivity(lastActionDays, destination, prData.getProductBussinessId(),
                        anticipation, action, searchCount);
                    search.addSearchActivity(action.getProduct(), activity);
                }

            }
        }

        if (!search.isEmpty()) {
            actionRecom.setSearchActivity(search);
        }
    }

    private Map<Product, Integer> getProductSearchCount(List<ProductData> productDataList) {

        Map<Product, Integer> counter = Maps.newHashMap();

        for (ProductData prData : productDataList) {

            UserAction action = prData.getParent();

            if (HomeUtils.isSearchActivity(action.getFlow())) {

                if (!counter.containsKey(action.getProduct())) {
                    counter.put(action.getProduct(), 1);
                }

                Integer count = counter.get(action.getProduct());
                counter.put(action.getProduct(), count + 1);
            }
        }

        return counter;
    }
}
