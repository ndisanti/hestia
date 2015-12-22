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
import com.despegar.p13n.hestia.recommend.allinone.item.flight.FlightCommonsFunctions;
import com.despegar.p13n.hestia.recommend.allinone.item.flight.SearchFlightStep;

@RunWith(MockitoJUnitRunner.class)
public class SearchFlightStepTest {

    private SearchFlightStep steps;
    @Mock
    private FlightCommonsFunctions commons;

    @Before
    public void setUp() {
        this.steps = new SearchFlightStep(this.commons);
    }

    @Test
    public void test() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.FLIGHTS, "BUE");
        action.setOrigin("BUE");
        FlightDestinationItem destination = new FlightDestinationItem("MIA", "BUE", "XXXX");
        Mockito.when(this.commons.builDestination(action, action.getOrigin(), "MIA")).thenReturn(destination);
        FlightDestinationItem item = (FlightDestinationItem) this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertEquals(destination, item);
    }
}
