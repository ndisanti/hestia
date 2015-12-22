package com.despegar.p13n.hestia.recommend.allinone.rules.products.functions;

import java.util.List;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;

@RunWith(MockitoJUnitRunner.class)
public class MonoDestinationProductFunctionTest {


    private MonoDestinationProductFunction function = new MonoDestinationProductFunction();

    @Mock
    private UserContext userContextAccesor;
    @Mock
    private UserRecord userPrifileRecord;


    @Before
    public void setUp() {
        Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(this.userPrifileRecord);
        Mockito.when(this.userPrifileRecord.getLastIntenseSearch()).thenReturn(null);
        this.function.initMaps();
    }

    @Test
    public void getProductForCarsFlowSearch() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.CARS, "MIA");
        action.setCurrentHome(Product.CARS);
        List<Product> products = this.function.getProducts(ItemTypeId.DESTINATION, "MIA", action);
        Assert.assertNotNull(products);
        Assert.assertFalse(products.isEmpty());
        Assert.assertTrue(products.get(0).equals(Product.FLIGHTS));
        Assert.assertTrue(products.get(1).equals(Product.HOTELS));
        Assert.assertTrue(products.get(2).equals(Product.CARS));
        Assert.assertTrue(products.get(3).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(4).equals(Product.HOTELS));
        Assert.assertTrue(products.get(5).equals(Product.HOTELS));
        Assert.assertTrue(products.get(6).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(7).equals(Product.HOTELS));
        Assert.assertTrue(products.get(8).equals(Product.HOTELS));
        Assert.assertTrue(products.get(9).equals(Product.ACTIVITIES));
    }

    @Test
    public void getProductForCruisesFlowSearch() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.CRUISES, "MIA");
        action.setCurrentHome(Product.CRUISES);
        List<Product> products = this.function.getProducts(ItemTypeId.DESTINATION, "MIA", action);
        Assert.assertNotNull(products);
        Assert.assertFalse(products.isEmpty());
        Assert.assertTrue(products.get(0).equals(Product.FLIGHTS));
        Assert.assertTrue(products.get(1).equals(Product.HOTELS));
        Assert.assertTrue(products.get(2).equals(Product.HOTELS));
        Assert.assertTrue(products.get(3).equals(Product.HOTELS));
        Assert.assertTrue(products.get(4).equals(Product.HOTELS));
        Assert.assertTrue(products.get(5).equals(Product.CARS));
        Assert.assertTrue(products.get(6).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(7).equals(Product.HOTELS));
        Assert.assertTrue(products.get(8).equals(Product.HOTELS));
        Assert.assertTrue(products.get(9).equals(Product.HOTELS));
    }

    @Test
    public void getProductForHotelsFlowSearch() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.HOTELS, "MIA");
        action.setCurrentHome(Product.HOTELS);
        List<Product> products = this.function.getProducts(ItemTypeId.DESTINATION, "MIA", action);
        Assert.assertNotNull(products);
        Assert.assertFalse(products.isEmpty());
        Assert.assertTrue(products.get(0).equals(Product.FLIGHTS));
        Assert.assertTrue(products.get(1).equals(Product.HOTELS));
        Assert.assertTrue(products.get(2).equals(Product.HOTELS));
        Assert.assertTrue(products.get(3).equals(Product.CARS));
        Assert.assertTrue(products.get(4).equals(Product.HOTELS));
        Assert.assertTrue(products.get(5).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(6).equals(Product.HOTELS));
        Assert.assertTrue(products.get(7).equals(Product.HOTELS));
        Assert.assertTrue(products.get(8).equals(Product.HOTELS));
        Assert.assertTrue(products.get(9).equals(Product.HOTELS));
    }

    @Test
    public void getProductForFlightsFlowSearch() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.FLIGHTS, "MIA");
        action.setCurrentHome(Product.FLIGHTS);
        List<Product> products = this.function.getProducts(ItemTypeId.DESTINATION, "MIA", action);
        Assert.assertNotNull(products);
        Assert.assertFalse(products.isEmpty());
        Assert.assertTrue(products.get(0).equals(Product.FLIGHTS));
        Assert.assertTrue(products.get(1).equals(Product.HOTELS));
        Assert.assertTrue(products.get(2).equals(Product.HOTELS));
        Assert.assertTrue(products.get(3).equals(Product.CARS));
        Assert.assertTrue(products.get(4).equals(Product.HOTELS));
        Assert.assertTrue(products.get(5).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(6).equals(Product.HOTELS));
        Assert.assertTrue(products.get(7).equals(Product.HOTELS));
        Assert.assertTrue(products.get(8).equals(Product.HOTELS));
        Assert.assertTrue(products.get(9).equals(Product.HOTELS));
    }

    @Test
    public void getProductForActivitiesFlowSearch() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.ACTIVITIES, "MIA");
        action.setCurrentHome(Product.ACTIVITIES);
        List<Product> products = this.function.getProducts(ItemTypeId.DESTINATION, "MIA", action);
        Assert.assertNotNull(products);
        Assert.assertFalse(products.isEmpty());
        Assert.assertTrue(products.get(0).equals(Product.FLIGHTS));
        Assert.assertTrue(products.get(1).equals(Product.HOTELS));
        Assert.assertTrue(products.get(2).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(3).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(4).equals(Product.CARS));
        Assert.assertTrue(products.get(5).equals(Product.HOTELS));
        Assert.assertTrue(products.get(6).equals(Product.HOTELS));
        Assert.assertTrue(products.get(7).equals(Product.HOTELS));
        Assert.assertTrue(products.get(8).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(9).equals(Product.ACTIVITIES));
    }

    @Test
    public void getProductForRentalsFlowSearch() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.VACATIONRENTALS, "MIA");
        action.setCurrentHome(Product.VACATIONRENTALS);
        List<Product> products = this.function.getProducts(ItemTypeId.DESTINATION, "MIA", action);
        Assert.assertNotNull(products);
        Assert.assertFalse(products.isEmpty());
        Assert.assertTrue(products.get(0).equals(Product.FLIGHTS));
        Assert.assertTrue(products.get(1).equals(Product.VACATIONRENTALS));
        Assert.assertTrue(products.get(2).equals(Product.VACATIONRENTALS));
        Assert.assertTrue(products.get(3).equals(Product.CARS));
        Assert.assertTrue(products.get(4).equals(Product.HOTELS));
        Assert.assertTrue(products.get(5).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(6).equals(Product.CARS));
        Assert.assertTrue(products.get(7).equals(Product.HOTELS));
        Assert.assertTrue(products.get(8).equals(Product.HOTELS));
        Assert.assertTrue(products.get(9).equals(Product.HOTELS));
    }

    @Test
    public void getProductForPackagesFlowSearch() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.CLOSED_PACKAGES, "MIA");
        action.setCurrentHome(Product.CLOSED_PACKAGES);
        List<Product> products = this.function.getProducts(ItemTypeId.DESTINATION, "MIA", action);
        Assert.assertNotNull(products);
        Assert.assertFalse(products.isEmpty());
        Assert.assertTrue(products.get(0).equals(Product.FLIGHTS));
        Assert.assertTrue(products.get(1).equals(Product.HOTELS));
        Assert.assertTrue(products.get(2).equals(Product.CARS));
        Assert.assertTrue(products.get(3).equals(Product.HOTELS));
        Assert.assertTrue(products.get(4).equals(Product.HOTELS));
        Assert.assertTrue(products.get(5).equals(Product.HOTELS));
        Assert.assertTrue(products.get(6).equals(Product.HOTELS));
        Assert.assertTrue(products.get(7).equals(Product.HOTELS));
        Assert.assertTrue(products.get(8).equals(Product.HOTELS));
        Assert.assertTrue(products.get(9).equals(Product.HOTELS));
    }


    @Test
    public void getProductForCarsFlowBuy() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CARS, "MIA");
        action.setUserContext(this.userContextAccesor);
        action.setCurrentHome(Product.CARS);
        List<Product> products = this.function.getProducts(ItemTypeId.DESTINATION, "MIA", action);
        Assert.assertNotNull(products);
        Assert.assertFalse(products.isEmpty());
        Assert.assertTrue(products.get(0).equals(Product.FLIGHTS));
        Assert.assertTrue(products.get(1).equals(Product.HOTELS));
        Assert.assertTrue(products.get(2).equals(Product.CARS));
        Assert.assertTrue(products.get(3).equals(Product.HOTELS));
        Assert.assertTrue(products.get(4).equals(Product.HOTELS));
        Assert.assertTrue(products.get(5).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(6).equals(Product.HOTELS));
        Assert.assertTrue(products.get(7).equals(Product.HOTELS));
        Assert.assertTrue(products.get(8).equals(Product.HOTELS));
        Assert.assertTrue(products.get(9).equals(Product.ACTIVITIES));
    }

    @Test
    public void getProductForCruisesFlowBuy() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        action.setUserContext(this.userContextAccesor);
        action.setCurrentHome(Product.CRUISES);
        List<Product> products = this.function.getProducts(ItemTypeId.DESTINATION, "MIA", action);
        Assert.assertNotNull(products);
        Assert.assertFalse(products.isEmpty());
        Assert.assertTrue(products.get(0).equals(Product.FLIGHTS));
        Assert.assertTrue(products.get(1).equals(Product.HOTELS));
        Assert.assertTrue(products.get(2).equals(Product.HOTELS));
        Assert.assertTrue(products.get(3).equals(Product.HOTELS));
        Assert.assertTrue(products.get(4).equals(Product.HOTELS));
        Assert.assertTrue(products.get(5).equals(Product.CARS));
        Assert.assertTrue(products.get(6).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(7).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(8).equals(Product.HOTELS));
        Assert.assertTrue(products.get(9).equals(Product.HOTELS));
    }

    @Test
    public void getProductForHotelsFlowBuy() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.HOTELS, "MIA");
        action.setUserContext(this.userContextAccesor);
        action.setCurrentHome(Product.HOTELS);
        List<Product> products = this.function.getProducts(ItemTypeId.DESTINATION, "MIA", action);
        Assert.assertNotNull(products);
        Assert.assertFalse(products.isEmpty());
        Assert.assertTrue(products.get(0).equals(Product.FLIGHTS));
        Assert.assertTrue(products.get(1).equals(Product.HOTELS));
        Assert.assertTrue(products.get(2).equals(Product.HOTELS));
        Assert.assertTrue(products.get(3).equals(Product.HOTELS));
        Assert.assertTrue(products.get(4).equals(Product.CARS));
        Assert.assertTrue(products.get(5).equals(Product.HOTELS));
        Assert.assertTrue(products.get(6).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(7).equals(Product.HOTELS));
        Assert.assertTrue(products.get(8).equals(Product.HOTELS));
        Assert.assertTrue(products.get(9).equals(Product.HOTELS));
    }

    @Test
    public void getProductForFlightsFlowBuy() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.FLIGHTS, "MIA");
        action.setUserContext(this.userContextAccesor);
        action.setCurrentHome(Product.FLIGHTS);
        List<Product> products = this.function.getProducts(ItemTypeId.DESTINATION, "MIA", action);
        Assert.assertNotNull(products);
        Assert.assertFalse(products.isEmpty());
        Assert.assertTrue(products.get(0).equals(Product.HOTELS));
        Assert.assertTrue(products.get(1).equals(Product.HOTELS));
        Assert.assertTrue(products.get(2).equals(Product.HOTELS));
        Assert.assertTrue(products.get(3).equals(Product.CARS));
        Assert.assertTrue(products.get(4).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(5).equals(Product.HOTELS));
        Assert.assertTrue(products.get(6).equals(Product.HOTELS));
        Assert.assertTrue(products.get(7).equals(Product.HOTELS));
        Assert.assertTrue(products.get(8).equals(Product.HOTELS));
        Assert.assertTrue(products.get(9).equals(Product.HOTELS));
    }

    @Test
    public void getProductForActivitiesFlowBuy() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.ACTIVITIES, "MIA");
        action.setUserContext(this.userContextAccesor);
        action.setCurrentHome(Product.ACTIVITIES);
        List<Product> products = this.function.getProducts(ItemTypeId.DESTINATION, "MIA", action);
        Assert.assertNotNull(products);
        Assert.assertFalse(products.isEmpty());
        Assert.assertTrue(products.get(0).equals(Product.FLIGHTS));
        Assert.assertTrue(products.get(1).equals(Product.HOTELS));
        Assert.assertTrue(products.get(2).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(3).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(4).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(5).equals(Product.HOTELS));
        Assert.assertTrue(products.get(6).equals(Product.CARS));
        Assert.assertTrue(products.get(7).equals(Product.HOTELS));
        Assert.assertTrue(products.get(8).equals(Product.HOTELS));
        Assert.assertTrue(products.get(9).equals(Product.HOTELS));
    }

    @Test
    public void getProductForRentalsFlowBuy() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.VACATIONRENTALS, "MIA");
        action.setUserContext(this.userContextAccesor);
        action.setCurrentHome(Product.VACATIONRENTALS);
        List<Product> products = this.function.getProducts(ItemTypeId.DESTINATION, "MIA", action);
        Assert.assertNotNull(products);
        Assert.assertFalse(products.isEmpty());
        Assert.assertTrue(products.get(0).equals(Product.FLIGHTS));
        Assert.assertTrue(products.get(1).equals(Product.VACATIONRENTALS));
        Assert.assertTrue(products.get(2).equals(Product.VACATIONRENTALS));
        Assert.assertTrue(products.get(3).equals(Product.CARS));
        Assert.assertTrue(products.get(4).equals(Product.HOTELS));
        Assert.assertTrue(products.get(5).equals(Product.HOTELS));
        Assert.assertTrue(products.get(6).equals(Product.HOTELS));
        Assert.assertTrue(products.get(7).equals(Product.HOTELS));
        Assert.assertTrue(products.get(8).equals(Product.VACATIONRENTALS));
        Assert.assertTrue(products.get(9).equals(Product.VACATIONRENTALS));
    }

    @Test
    public void getProductForPackagesFlowBuy() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CLOSED_PACKAGES, "MIA");
        action.setUserContext(this.userContextAccesor);
        action.setCurrentHome(Product.CLOSED_PACKAGES);
        List<Product> products = this.function.getProducts(ItemTypeId.DESTINATION, "MIA", action);
        Assert.assertNotNull(products);
        Assert.assertFalse(products.isEmpty());
        Assert.assertTrue(products.get(0).equals(Product.FLIGHTS));
        Assert.assertTrue(products.get(1).equals(Product.HOTELS));
        Assert.assertTrue(products.get(2).equals(Product.FLIGHTS));
        Assert.assertTrue(products.get(3).equals(Product.HOTELS));
        Assert.assertTrue(products.get(4).equals(Product.CARS));
        Assert.assertTrue(products.get(5).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(6).equals(Product.HOTELS));
        Assert.assertTrue(products.get(7).equals(Product.HOTELS));
        Assert.assertTrue(products.get(8).equals(Product.ACTIVITIES));
        Assert.assertTrue(products.get(9).equals(Product.HOTELS));
    }

}
