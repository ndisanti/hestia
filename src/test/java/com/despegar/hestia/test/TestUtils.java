package com.despegar.hestia.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.curator.test.TestingServer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.mockito.Mockito;

import com.despegar.hestia.api.data.model.CheckoutAction;
import com.despegar.hestia.api.data.model.DetailAction;
import com.despegar.hestia.api.data.model.EnterHomeAction;
import com.despegar.hestia.api.data.model.ThanksAction;
import com.despegar.hestia.api.data.model.TravelDate;
import com.despegar.p13n.commons.dto.UserActionKeys;
import com.despegar.p13n.euler.commons.client.model.Brand;
import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.EventAction;
import com.despegar.p13n.euler.commons.client.model.EventName;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.TransactionIdentifier;
import com.despegar.p13n.euler.commons.client.model.UserAction;
import com.despegar.p13n.euler.commons.client.model.data.CarData;
import com.despegar.p13n.euler.commons.client.model.data.CombinedProductData;
import com.despegar.p13n.euler.commons.client.model.data.FlightData;
import com.despegar.p13n.euler.commons.client.model.data.HotelData;
import com.despegar.p13n.euler.commons.client.model.data.ProductData;
import com.despegar.p13n.hbasecommons.hbase.core.HBaseTemplate;
import com.despegar.p13n.hbasecommons.hbase.core.HBaseTemplateMock;
import com.despegar.p13n.hbasecommons.hbase.core.MockHbaseConfiguration;
import com.despegar.p13n.hbasecommons.hbase.core.UnderMaintenanceChecker;
import com.despegar.p13n.hbasecommons.hbase.mock.MockHTable;
import com.despegar.p13n.hbasecommons.hbase.mock.MockHTableFactory;
import com.despegar.p13n.hbasecommons.hbase.tools.ObjectMapperPersistenceSerializer;
import com.despegar.p13n.hbasecommons.hbase.tools.ObjectPersistenceService;
import com.despegar.p13n.hbasecommons.hbase.tools.ObjectPersistenceServiceImpl;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.api.data.model.SearchAction;
import com.despegar.p13n.hestia.utils.DateUtils;
import com.google.common.collect.Lists;

/**
 * The Class TestUtils.
 *
 * @author jcastro
 * @since Nov 1, 2012
 */
public abstract class TestUtils {

    public static final String DEFAULT_USER_ID = "1234";
    public static final Product DEFAULT_PRODUCT = Product.FLIGHTS;
    public static final CountryCode DEFAULT_COUNTRY = CountryCode.AR;
    public static final String DEFAULT_EMAIL = "prueba@despegar.com";

    private static TestingServer zkServerTest;

    public static DetailAction createDetailAction() {
        return createDetailAction(DEFAULT_USER_ID);
    }

    public static EnterHomeAction createEnterHomeAction() {
        return createEnterHomeAction(DEFAULT_USER_ID);
    }

    public static EventAction createEventAction(EventName type) {
        return createEventAction(DEFAULT_USER_ID, type);
    }

    public static EventAction createEventAction(EventName type, Flow flow) {
        return createEventAction(DEFAULT_USER_ID, type, flow);
    }

    public static SearchAction createSearchAction() {
        return createSearchAction(DEFAULT_USER_ID);
    }

    public static CheckoutAction createCheckoutAction() {
        return createCheckoutAction(DEFAULT_USER_ID);
    }

    public static UserAction createFlowAction(Flow flow) {
        return createFlowAction(DEFAULT_USER_ID, flow);
    }

    public static ThanksAction createThanksAction() {
        return createThanksAction(DEFAULT_USER_ID);
    }


    public static EnterHomeAction createEnterHomeAction(final String userId, final Product p) {
        final EnterHomeAction result = new EnterHomeAction();
        fillUserAction(result, Flow.HOME, p, userId);
        return result;
    }

    public static SearchAction createSearchAction(final String userId, final Product p) {
        final SearchAction result = new SearchAction();
        fillUserAction(result, Flow.SEARCH, p, userId);
        result.getActionData().put(UserActionKeys.ORIGIN_IP.getCode(), "200.69.128.1");
        return result;
    }


    public static CheckoutAction createCheckoutAction(final String userId, final Product p) {
        final CheckoutAction result = new CheckoutAction();
        fillUserAction(result, Flow.CHECKOUT, p, userId);
        result.setActionData(new HashMap<String, Object>());
        return result;
    }

    public static ThanksAction createThanksAction(final String userId, final Product p) {
        final ThanksAction action = new ThanksAction();
        fillUserAction(action, Flow.THANKS, p, userId);
        action.getActionData().put(UserActionKeys.EMAIL.getCode(), DEFAULT_EMAIL);
        return action;
    }

    public static EventAction createEventAction(final String userId, final Product p, EventName type) {
        return createEventAction(userId, p, Flow.EVENT_SELECTION, type);
    }

    public static EventAction createEventAction(final String userId, final Product p, Flow flow, EventName type) {
        final EventAction action = new EventAction();
        fillUserAction(action, flow, p, userId);
        action.setEventName(type);
        action.getActionData().put(UserActionKeys.EVENT.getCode(), type.getEvent());
        action.getActionData().put(UserActionKeys.EMAIL.getCode(), DEFAULT_EMAIL);
        return action;
    }

    public static DetailAction createDetailAction(final String userId, final Product p) {
        final DetailAction action = new DetailAction();
        fillUserAction(action, Flow.DETAIL, p, userId);
        return action;
    }

    public static UserAction createLandingDetailAction(final String userId, final Product p) {
        final UserAction action = new UserAction();
        fillUserAction(action, Flow.LANDING_DETAIL, p, userId);
        action.getActionData().put(UserActionKeys.EMAIL.getCode(), DEFAULT_EMAIL);
        return action;
    }

    public static UserAction createFlowAction(final String userId, final Product p, final Flow f) {
        final UserAction action = new UserAction();
        fillUserAction(action, f, p, userId);
        action.getActionData().put(UserActionKeys.EMAIL.getCode(), DEFAULT_EMAIL);
        return action;
    }

    public static DetailAction createDetailAction(final String userId) {
        return createDetailAction(userId, DEFAULT_PRODUCT);
    }

    public static EnterHomeAction createEnterHomeAction(final String userId) {
        return createEnterHomeAction(userId, DEFAULT_PRODUCT);
    }

    public static SearchAction createSearchAction(final String userId) {
        return createSearchAction(userId, DEFAULT_PRODUCT);
    }


    public static CheckoutAction createCheckoutAction(final String userId) {
        return createCheckoutAction(userId, DEFAULT_PRODUCT);
    }

    public static ThanksAction createThanksAction(final String userId) {
        return createThanksAction(userId, DEFAULT_PRODUCT);
    }

    public static UserAction createFlowAction(final String userId, Flow flow) {
        return createFlowAction(userId, DEFAULT_PRODUCT, flow);
    }

    public static EventAction createEventAction(final String userId, EventName type) {
        return createEventAction(userId, DEFAULT_PRODUCT, type);
    }

    public static EventAction createEventAction(final String userId, EventName type, Flow flow) {
        return createEventAction(userId, DEFAULT_PRODUCT, flow, type);
    }

    public static void fillUserAction(final UserAction result, final Flow flow, final Product product, final String userId) {
        result.setProduct(product);
        result.setBrand(Brand.DESPEGAR);
        result.setCountryCode(DEFAULT_COUNTRY);
        result.setFlow(flow);

        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        result.setTimestamp(System.currentTimeMillis());
        result.setUserId(userId);
        result.setTransactionIdentifier(new TransactionIdentifier(userId, product, DEFAULT_COUNTRY, null, null));

        result.setActionData(fillActionMap(result));
    }



    public static Map<String, Object> fillActionMap(UserAction action) {
        Map<String, Object> actionMap = new HashMap<>();
        actionMap.put(UserActionKeys.USERID.getCode(), action.getUserId());
        actionMap.put(UserActionKeys.PRODUCT.getCode(), action.getProduct().name());
        actionMap.put(UserActionKeys.BRAND.getCode(), action.getBrand().name());
        actionMap.put(UserActionKeys.COUNTRY.getCode(), action.getCountryCode().name());
        actionMap.put(UserActionKeys.FLOW.getCode(), action.getFlow().name());
        actionMap.put(UserActionKeys.TRANSACTION_ID.getCode(), action.getTransactionId());
        actionMap.put(UserActionKeys.DATETIME.getCode(), action.getTimestamp());
        return actionMap;
    }


    public static HBaseTemplate createHbaseTemplateMock(final MockHTable... hTables) {
        final HBaseTemplate template = new HBaseTemplateMock("eu");
        template.setConnection(new MockHbaseConfiguration.MockConnection(new MockHTableFactory().add(hTables)));
        UnderMaintenanceChecker underMaintenceMocked = Mockito.mock(UnderMaintenanceChecker.class);
        Mockito.when(underMaintenceMocked.isAutoMaintenance(Mockito.anyString())).thenReturn(false);
        template.setMaintenanceChecker(underMaintenceMocked);
        return template;
    }

    public static ObjectPersistenceService createPersistenceObjectServiceMock() {
        final String tableName = "eu:objectService";
        final String columnFamily = "d";
        HBaseTemplate hbase = TestUtils.createHbaseTemplateMock(MockHTable.create("eu", tableName, columnFamily));
        final ObjectPersistenceServiceImpl persistenceObjectServiceImpl = new ObjectPersistenceServiceImpl(tableName,
            columnFamily, hbase, new ObjectMapperPersistenceSerializer());
        persistenceObjectServiceImpl.setColumnFamily(columnFamily);
        persistenceObjectServiceImpl.setTableName(tableName);
        return persistenceObjectServiceImpl;
    }

    /**
     * Start test zookeeper server.
     *
     * @return the connection string to configure on services.
     * @throws Exception if could not start test server
     */
    public static String startTestZookeeperServer() throws Exception {
        zkServerTest = new TestingServer();
        return zkServerTest.getConnectString();
    }

    public static void sleep() {
        try {
            Thread.sleep(1);
        } catch (final InterruptedException e) {

        }
    }

    public static RankingTreeDTO createRankingTreeDTO(List<Pair<String, Long>> recs) {
        RankingTreeDTO ranking = new RankingTreeDTO();
        for (Pair<String, Long> pair : recs) {
            ranking.addPosition(pair.getLeft(), pair.getRight());
        }
        return ranking;
    }

    public static DateTime createDateTime(int year, int month, int day, boolean randomTimeOfDay) {
        Random rn = new Random();
        int hour = randomTimeOfDay ? rn.nextInt(24) : 0;
        int minutes = randomTimeOfDay ? rn.nextInt(60) : 0;
        int seconds = randomTimeOfDay ? rn.nextInt(60) : 0;
        return new DateTime(year, month, day, hour, minutes, seconds);
    }

    private static String dateAsStr(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DateUtils.FORMAT_yyyy_MM_dd.print(date);
    }

    public static ProductData createProductData(Product pr, Flow fl, DateTime dateTime) {
        UserAction action = TestUtils.createFlowAction(DEFAULT_USER_ID, pr, fl);
        action.setTimestamp(dateTime.getMillis());
        return ProductData.create(action);
    }

    public static ProductData createHotelData(String iataCode, Flow flow, DateTime dateTime, TravelDate travelDate) {
        ProductData data = createProductData(Product.HOTELS, flow, dateTime);
        data.getParent().getActionData().put(HotelData.DESTINATION_CODE, iataCode);
        data.getParent().getActionData().put(HotelData.CHECKIN, dateAsStr(travelDate.getStartDate()));
        data.getParent().getActionData().put(HotelData.CHECKOUT, dateAsStr(travelDate.getEndDate()));
        return data;
    }

    public static ProductData createCarData(String iataCode, Flow flow, DateTime dateTime, TravelDate travelDate) {
        ProductData data = createProductData(Product.CARS, flow, dateTime);
        data.getParent().getActionData().put(CarData.PICK_UP_LOCATION, iataCode);
        data.getParent().getActionData().put(CarData.DROP_OFF_LOCATION, iataCode);
        data.getParent().getActionData()
            .put(CarData.DT, DateUtils.FORMAT_yyyy_MM_dd_hh_mm.print(travelDate.getStartDate().toDateTimeAtStartOfDay()));
        data.getParent().getActionData()
            .put(CarData.RETT, DateUtils.FORMAT_yyyy_MM_dd_hh_mm.print(travelDate.getEndDate().toDateTimeAtStartOfDay()));
        return data;
    }

    public static ProductData createHotelDataCheckout(String iataCode, DateTime dateTime, TravelDate travelDate) {
        ProductData data = createHotelData(iataCode, Flow.CHECKOUT, dateTime, travelDate);
        return data;
    }

    public static ProductData createHotelDataSearch(String iataCode, DateTime dateTime, TravelDate travelDate) {
        ProductData data = createHotelData(iataCode, Flow.SEARCH, dateTime, travelDate);
        return data;
    }

    public static ProductData createHotelDataDetail(String iataCode, DateTime dateTime, TravelDate travelDate) {
        ProductData data = createHotelData(iataCode, Flow.DETAIL, dateTime, travelDate);
        return data;
    }

    public static ProductData createHotelDataLanding(String iataCode, DateTime dateTime, TravelDate travelDate) {
        ProductData data = createHotelData(iataCode, Flow.LANDING, dateTime, travelDate);
        return data;
    }

    public static ProductData createHotelDataThanks(String iataCode, DateTime dateTime, TravelDate travelDate) {
        ProductData data = createHotelData(iataCode, Flow.THANKS, dateTime, travelDate);
        return data;
    }

    public static ProductData createCarDataSearch(String iataCode, DateTime dateTime, TravelDate travelDate) {
        ProductData data = createCarData(iataCode, Flow.SEARCH, dateTime, travelDate);
        return data;
    }

    public static ProductData createCarDataThanks(String iataCode, DateTime dateTime, TravelDate travelDate) {
        ProductData data = createCarData(iataCode, Flow.THANKS, dateTime, travelDate);
        return data;
    }

    public static ProductData createCombinedProductsData(Flow flow, DateTime actionDate, String iataCode,
        TravelDate travelDate, CountryCode cc, String packPrs, String hotelId, String tripType, DateTime rett) {
        ProductData data = TestUtils.createProductData(Product.COMBINED_PRODUCTS, flow, actionDate);
        data.getParent().setCountryCode(cc);
        data.getParent().getActionData().put("packprs", packPrs);
        data.getParent().getActionData()
            .put(CombinedProductData.CI, DateUtils.FORMAT_yyyy_MM_dd.print(travelDate.getStartDate()));
        data.getParent().getActionData()
            .put(CombinedProductData.CO, DateUtils.FORMAT_yyyy_MM_dd.print(travelDate.getEndDate()));
        data.getParent().getActionData().put(HotelData.HOTEL_ID, hotelId);
        data.getParent().getActionData().put(FlightData.TRIP_TYPE, tripType);
        data.getParent().getActionData().put(CarData.RETT, DateUtils.FORMAT_yyyy_MM_dd_hh_mm.print(rett));
        return data;
    }

    public static List<ProductData> createHotelDataSearches(int amount, DateTime actionDate, String iataCode,
        TravelDate travelDate, CountryCode cc) {
        List<ProductData> result = Lists.newArrayList();
        for (int i = 0; i < amount; i++) {
            ProductData data = TestUtils.createHotelDataSearch(iataCode, actionDate, travelDate);
            data.getParent().setCountryCode(cc);
            result.add(data);
        }
        return result;
    }

    public static List<ProductData> createCarDataSearches(int amount, DateTime actionDate, String iataCode,
        TravelDate travelDate, CountryCode cc) {
        List<ProductData> result = Lists.newArrayList();
        for (int i = 0; i < amount; i++) {
            ProductData data = TestUtils.createCarDataSearch(iataCode, actionDate, travelDate);
            data.getParent().setCountryCode(cc);
            result.add(data);
        }
        return result;
    }

}
