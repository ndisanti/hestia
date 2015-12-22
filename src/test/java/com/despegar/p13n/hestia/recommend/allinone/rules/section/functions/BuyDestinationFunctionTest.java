/**
 * 
 */
package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.hestia.api.data.model.HotelDestinationItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;

@RunWith(MockitoJUnitRunner.class)
public class BuyDestinationFunctionTest {

    @InjectMocks
	private BuyDestinationFunction function = new BuyDestinationFunction();
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
    public void test() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.HOTELS, "MIA");
        context.setUserContext(this.userContextAccesor);
        Offer offer = this.function.buildOffers(null, Product.HOTELS, context, new Param()).get(0);

        Assert.assertNotNull(offer);
        Assert.assertNotNull(offer.getOffer());
        Assert.assertTrue(offer.getOffer() instanceof HotelDestinationItem);
    }


    @Test(expected = UnsupportedOperationException.class)
    public void testRowHomeNotSupported() {
        this.function.buildRow(null, Product.HOTELS, null, null);
    }

}
