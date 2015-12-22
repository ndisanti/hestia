/**
 * 
 */
package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.hestia.api.data.model.CarDestinationItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;

@RunWith(MockitoJUnitRunner.class)
public class BuyOffersCarsTest {

    private BuyOffersCars function = new BuyOffersCars();
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
    public void testCity() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.FLIGHTS, "MIA");
        context.setUserContext(this.userContextAccesor);
        Offer offer = this.function.buildOffers(null, Product.CARS, context, new Param()).get(0);

        Assert.assertNotNull(offer);
        Assert.assertNotNull(offer.getOffer());
        Assert.assertTrue(offer.getOffer() instanceof CarDestinationItem);
        Assert.assertEquals("MIA", ((CarDestinationItem) offer.getOffer()).getDestination());
        Assert.assertEquals("city", ((CarDestinationItem) offer.getOffer()).getPulType());
    }


    @Test(expected = UnsupportedOperationException.class)
    public void testRowHomeNotSupported() {
        this.function.buildRow(null, Product.HOTELS, null, null);
    }

}
