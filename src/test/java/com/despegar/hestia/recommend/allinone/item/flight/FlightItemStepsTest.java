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
import com.despegar.p13n.hestia.recommend.allinone.item.flight.FlightItemSteps;
import com.despegar.p13n.hestia.recommend.allinone.item.flight.SearchFlightStep;

@RunWith(MockitoJUnitRunner.class)
public class FlightItemStepsTest {

    private FlightItemSteps steps;
    @Mock
    private SearchFlightStep searchFlightStep;
    @Mock
    private BuyFlightStep buyStep;

    @Before
    public void setUp() {
        this.steps = new FlightItemSteps(this.searchFlightStep, this.buyStep);
    }

    @Test
    public void testgetItemSearch() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.FLIGHTS, "BUE");
        FlightDestinationItem itemHome = new FlightDestinationItem("MIA", "BUE", "XXXX");
        Mockito.when(this.searchFlightStep.execute("MIA", action)).thenReturn(itemHome);
        FlightDestinationItem item = (FlightDestinationItem) this.steps.execute("MIA", action);
        Mockito.verifyNoMoreInteractions(this.buyStep);
        Assert.assertNotNull(item);
        Assert.assertSame(itemHome, item);
    }

    @Test
    public void testgetItemBuy() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.FLIGHTS, "BUE");
        FlightDestinationItem itemHome = new FlightDestinationItem("MIA", "BUE", "XXXX");
        Mockito.when(this.buyStep.execute("MIA", action)).thenReturn(itemHome);
        FlightDestinationItem item = (FlightDestinationItem) this.steps.execute("MIA", action);
        Mockito.verifyNoMoreInteractions(this.searchFlightStep);
        Assert.assertNotNull(item);
        Assert.assertSame(itemHome, item);
    }
}
