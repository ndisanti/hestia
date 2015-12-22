package com.despegar.hestia.recommend.allinone.rules;

import javax.activation.UnsupportedDataTypeException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.api.data.model.CarDestinationItem;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesDestinationItem;
import com.despegar.p13n.hestia.api.data.model.CruiseItem;
import com.despegar.p13n.hestia.api.data.model.FlightDestinationItem;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.VacationRentalItem;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemHomeService;
import com.despegar.p13n.hestia.recommend.allinone.item.activity.ActivityItemSteps;
import com.despegar.p13n.hestia.recommend.allinone.item.car.CarItemSteps;
import com.despegar.p13n.hestia.recommend.allinone.item.closedpackage.TemporaryClosedPackagesSteps;
import com.despegar.p13n.hestia.recommend.allinone.item.cruise.CruiseItemSteps;
import com.despegar.p13n.hestia.recommend.allinone.item.flight.FlightItemSteps;
import com.despegar.p13n.hestia.recommend.allinone.item.hotel.HotelItemSteps;
import com.despegar.p13n.hestia.recommend.allinone.item.vacactionrentals.VacationRentalsItemSteps;

@RunWith(MockitoJUnitRunner.class)
public class ItemHomeServiceTest {

    @InjectMocks
    private ItemHomeService itemHomeService = new ItemHomeService();
    @Mock
    private ActivityItemSteps activitySteps;
    @Mock
    private CarItemSteps carSteps;
    @Mock
    private TemporaryClosedPackagesSteps closedPackageSteps;
    @Mock
    private CruiseItemSteps cruiseSteps;
    @Mock
    private FlightItemSteps flightSteps;
    @Mock
    private HotelItemSteps hotelSteps;
    @Mock
    private VacationRentalsItemSteps vacationRentalsItemSteps;
    @Mock
    private UserContext userContextAccesor;
    @Mock
    private UserRecord userPrifileRecord;

    @Before
    public void setUp() {
        Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(this.userPrifileRecord);
        Mockito.when(this.userPrifileRecord.getLastIntenseSearch()).thenReturn(null);
    }


    @Test
    public void testGetCarItem() throws UnsupportedDataTypeException {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CARS, "MIA");
        action.setUserContext(this.userContextAccesor);
        CarDestinationItem item = new CarDestinationItem("MIA", "city");
        Mockito.when(this.carSteps.execute("MIA", action)).thenReturn(item);
        ItemHome carItem = this.itemHomeService.buildItemHome(ItemTypeId.DESTINATION, "MIA", Product.CARS, action);
        Mockito.verify(this.carSteps).execute("MIA", action);
        Assert.assertNotNull(carItem);
        Assert.assertEquals(carItem, item);
    }

    @Test
    public void testGetVacationRentalItem() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.VACATIONRENTALS, "MIA");
        action.setUserContext(this.userContextAccesor);
        VacationRentalItem item = new VacationRentalItem("12345677890");
        Mockito.when(this.vacationRentalsItemSteps.execute("MIA", action)).thenReturn(item);
        ItemHome vrItem = this.itemHomeService.buildItemHome(ItemTypeId.DESTINATION, "MIA", Product.VACATIONRENTALS, action);
        Mockito.verify(this.vacationRentalsItemSteps).execute("MIA", action);
        Assert.assertNotNull(vrItem);
        Assert.assertEquals(vrItem, item);
    }

    @Test
    public void testGetActivityItem() throws UnsupportedDataTypeException {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.ACTIVITIES, "MIA");
        action.setUserContext(this.userContextAccesor);
        ActivityItem item = new ActivityItem("ACT_ID");
        Mockito.when(this.activitySteps.execute("MIA", action)).thenReturn(item);
        ItemHome activityItem = this.itemHomeService
            .buildItemHome(ItemTypeId.DESTINATION, "MIA", Product.ACTIVITIES, action);
        Mockito.verify(this.activitySteps).execute("MIA", action);
        Assert.assertNotNull(activityItem);
        Assert.assertEquals(activityItem, item);
    }

    @Test
    public void testGetClosedPackageItem() throws UnsupportedDataTypeException {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CLOSED_PACKAGES, "MIA");
        action.setUserContext(this.userContextAccesor);
        ClosedPackagesDestinationItem item = new ClosedPackagesDestinationItem("MIA", "BUE");
        Mockito.when(this.closedPackageSteps.execute("MIA", action)).thenReturn(item);
        ItemHome closedPackageItem = this.itemHomeService.buildItemHome(ItemTypeId.DESTINATION, "MIA",
            Product.CLOSED_PACKAGES, action);
        Mockito.verify(this.closedPackageSteps).execute("MIA", action);
        Assert.assertNotNull(closedPackageItem);
        Assert.assertEquals(closedPackageItem, item);
    }

    @Test
    public void getCruiseItem() throws UnsupportedDataTypeException {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        action.setUserContext(this.userContextAccesor);
        CruiseItem item = new CruiseItem("1");
        Mockito.when(this.cruiseSteps.execute("MIA", action)).thenReturn(item);
        ItemHome cruiseItem = this.itemHomeService.buildItemHome(ItemTypeId.DESTINATION, "MIA", Product.CRUISES, action);
        Mockito.verify(this.cruiseSteps).execute("MIA", action);
        Assert.assertNotNull(cruiseItem);
        Assert.assertEquals(cruiseItem, item);
    }

    @Test
    public void getHotelItem() throws UnsupportedDataTypeException {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.HOTELS, "MIA");
        action.setUserContext(this.userContextAccesor);
        HotelItem item = new HotelItem("123456");
        Mockito.when(this.hotelSteps.execute("MIA", action)).thenReturn(item);
        ItemHome hotelItem = this.itemHomeService.buildItemHome(ItemTypeId.DESTINATION, "MIA", Product.HOTELS, action);
        Mockito.verify(this.hotelSteps).execute("MIA", action);
        Assert.assertNotNull(hotelItem);
        Assert.assertEquals(hotelItem, item);
    }

    @Test
    public void getFlightItem() throws UnsupportedDataTypeException {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.FLIGHTS, "MIA");
        action.setUserContext(this.userContextAccesor);
        FlightDestinationItem item = new FlightDestinationItem("MIA", "BUE", "XXXX");
        Mockito.when(this.flightSteps.execute("MIA", action)).thenReturn(item);
        ItemHome hotelItem = this.itemHomeService.buildItemHome(ItemTypeId.DESTINATION, "MIA", Product.FLIGHTS, action);
        Mockito.verify(this.flightSteps).execute("MIA", action);
        Assert.assertNotNull(hotelItem);
        Assert.assertEquals(hotelItem, item);
    }
}
