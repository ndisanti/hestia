/**
 * 
 */
package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.CruiseItem;
import com.despegar.p13n.hestia.api.data.model.CruiseRegionItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;

@RunWith(MockitoJUnitRunner.class)
public class CruiseSearchFunctionTest {

    private CruiseSearchFunction function = new CruiseSearchFunction();

    @Test
    public void testCruiseSearch() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4CruiseSearch("SOA");

        Offer offer = this.function.buildOffers(null, Product.CRUISES, context, null).get(0);

        Assert.assertNotNull(offer.getOffer());
        Assert.assertTrue(offer.getOffer() instanceof CruiseRegionItem);
        Assert.assertEquals("SOA", ((CruiseRegionItem) offer.getOffer()).getReg());
    }

    @Test
    public void testCruiseDetail() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4CruiseDetail("MIA", "SOA", "id1");
        context.setOrigin("BUE");

        Offer offer = this.function.buildOffers(Product.CRUISES, Product.CRUISES, context, null).get(0);

        Assert.assertNotNull(offer);
        Assert.assertNotNull(offer.getOffer());
        Assert.assertTrue(offer.getOffer() instanceof CruiseItem);
        Assert.assertEquals("id1", ((CruiseItem) offer.getOffer()).getDid());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRowHomeNotSupported() {
        this.function.buildRow(null, Product.CRUISES, null, null);
    }
}
