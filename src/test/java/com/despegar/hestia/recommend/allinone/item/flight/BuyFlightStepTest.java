package com.despegar.hestia.recommend.allinone.item.flight;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.FlightDestinationItem;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.flight.BuyFlightStep;
import com.despegar.p13n.hestia.recommend.allinone.item.flight.FlightCommonsFunctions;

@RunWith(MockitoJUnitRunner.class)
public class BuyFlightStepTest {

    private BuyFlightStep steps;
    @Mock
    private FlightCommonsFunctions commons;

    @Before
    public void setUp() {
        this.steps = new BuyFlightStep(this.commons);
    }

    @Test
    public void testFlight() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.FLIGHTS, "MDP");
        FlightDestinationItem destination = new FlightDestinationItem("MIA", "MDP", "XXX");
        Mockito.when(this.commons.builDestination(action, "MDP", "MIA")).thenReturn(destination);
        FlightDestinationItem item = (FlightDestinationItem) this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertEquals(item, destination);
    }

    @Test
    public void testClosed() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CLOSED_PACKAGES, "MDP");
        FlightDestinationItem destination = new FlightDestinationItem("MIA", "MDP", "XXX");
        Mockito.when(this.commons.builDestination(action, "MDP", "MIA")).thenReturn(destination);
        FlightDestinationItem item = (FlightDestinationItem) this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertEquals(item, destination);
    }

    @Test
    public void testCombined() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.COMBINED_PRODUCTS, "MDP");
        FlightDestinationItem destination = new FlightDestinationItem("MIA", "MDP", "XXX");
        Mockito.when(this.commons.builDestination(action, "MDP", "MIA")).thenReturn(destination);
        FlightDestinationItem item = (FlightDestinationItem) this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertEquals(item, destination);
    }

    @Test
    public void testDefault() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CARS, "BUE");
        action.setOrigin("BUE");
        FlightDestinationItem destination = new FlightDestinationItem("MIA", "BUE", "XXX");
        Mockito.when(this.commons.builDestination(action, "BUE", "MIA")).thenReturn(destination);
        FlightDestinationItem item = (FlightDestinationItem) this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertEquals(item, destination);
    }

}
