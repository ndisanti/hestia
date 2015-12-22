/**
 * 
 */
package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import org.junit.Assert;
import org.junit.Test;

import ar.com.despegar.p13n.hestia.test.MockitoAnnotationBaseTest;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HotelDestinationItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;


public class SearchDestinationFunctionTest
    extends MockitoAnnotationBaseTest {


    private SearchDestinationFunction function = new SearchDestinationFunction();

    @Test
    public void test() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Search(Product.HOTELS, "MIA");

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
