/**
 * 
 */
package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.test.MockitoAnnotationBaseTest;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.hestia.api.data.model.DestinationRanking;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.client.DestinationsClient;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.google.common.collect.Lists;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class NearbyBuyDestinationFunctionTest
    extends MockitoAnnotationBaseTest {

	@InjectMocks
	private NearbyBuyDestinationFunction function = new NearbyBuyDestinationFunction();
    @Mock
    private UserContext userContextAccesor;
    @Mock
    private UserRecord userPrifileRecord;
    @Mock
    private DestinationsClient destinationClients;
    
    @Before
    public void setUp() {
        Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(this.userPrifileRecord);
        Mockito.when(this.userPrifileRecord.getLastIntenseSearch()).thenReturn(null);
    }

    @Test
    public void testNoData() {

    	Mockito.when(this.destinationClients.searchesNearByCities(Mockito.any(Product.class),Mockito.anyString(), Mockito.anyInt())).thenReturn(new DestinationRanking<String>(Lists.newArrayList()));
        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.FLIGHTS, "MIA");
        context.setUserContext(this.userContextAccesor);
        final Param param = new Param();

        List<Offer> offers = function.buildOffers(Product.HOTELS, Product.HOTELS, context, param);

        Assert.assertNull(offers);

        RowHome buildRow = function.buildRow(Product.HOTELS, Product.HOTELS, context, param);

        Assert.assertNull(buildRow);
    }

    @Test
    public void testSomeData() {


        Mockito.when(this.destinationClients.searchesNearByCities(Mockito.any(Product.class),Mockito.anyString(), Mockito.anyInt())).thenReturn(new DestinationRanking<String>(Arrays.asList("a", "b", "c", "d", "e", "f",
                "g", "h")));
        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.HOTELS, "MIA");
        context.setUserContext(this.userContextAccesor);
        final Param param = new Param();

        Offer offer = function.buildOffers(Product.HOTELS, Product.HOTELS, context, param).get(0);

        TestFuntions.assertHotelDestination(offer.getOffer(), "a");

        RowHome row = function.buildRow(Product.HOTELS, Product.HOTELS, context, param);

        Assert.assertNull(row.getHighlighted());
        Assert.assertEquals(7, row.getOffers().size());
        TestFuntions.assertHotelDestination(row.getOffers().get(0), "b");
        TestFuntions.assertHotelDestination(row.getOffers().get(1), "c");
    }

}
