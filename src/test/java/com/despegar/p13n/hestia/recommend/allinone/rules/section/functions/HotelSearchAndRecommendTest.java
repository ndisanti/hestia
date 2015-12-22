/**
 * 
 */
package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HotelDestinationItem;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class HotelSearchAndRecommendTest {

	@InjectMocks
    private HotelSearchAndRecommendFunction function = new HotelSearchAndRecommendFunction();
    @Mock
	private RecommendFunction recommendFunction = new RecommendFunction();

    @Test
    public void testSearch() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Search(Product.HOTELS, "MIA");

        List<Offer> of = Lists.newArrayList();
        of.add(new Offer(new HotelDestinationItem("MIA")));
		Mockito.when(this.recommendFunction.buildOffers(Mockito.any(Product.class), Mockito.any(Product.class), Mockito.any(ActionRecommendation.class), Mockito.any(Param.class))).thenReturn(of);
        Offer offer = this.function.buildOffers(Product.HOTELS, Product.HOTELS, context, new Param()).get(0);

        Assert.assertNotNull(offer);
        Assert.assertNotNull(offer.getOffer());
        Assert.assertTrue(offer.getOffer() instanceof HotelDestinationItem);
        Assert.assertEquals("MIA", ((HotelDestinationItem) offer.getOffer()).getDestination());

        Param param = Param.builder().flow1(Flow.SEARCH).build();
		List<HotelDestinationItem> offers = Lists.newArrayList() ;
		offers.add(new HotelDestinationItem("1"));
		offers.add(new HotelDestinationItem("2"));
		RowHome ro = new RowHome(null, offers  );
		Mockito.when(this.recommendFunction.buildRow(Mockito.any(Product.class), Mockito.any(Product.class), Mockito.any(ActionRecommendation.class), Mockito.any(Param.class))).thenReturn(ro );

        RowHome row = this.function.buildRow(Product.HOTELS, Product.HOTELS, context, param);
        TestFuntions.assertHotelDestination(row.getOffers().get(0), "1");
        TestFuntions.assertHotelDestination(row.getOffers().get(1), "2");
    }


    @Test
    public void testDetail() {
        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Detail(Product.HOTELS, "MIA", "1");
        context.setOrigin("BUE");
        List<Offer> of = Lists.newArrayList();
        of.add(new Offer(new HotelItem("1")));
		Mockito.when(this.recommendFunction.buildOffers(Mockito.any(Product.class), Mockito.any(Product.class), Mockito.any(ActionRecommendation.class), Mockito.any(Param.class))).thenReturn(of);
        Offer offer = this.function.buildOffers(Product.HOTELS, Product.HOTELS, context, new Param()).get(0);

        Assert.assertNotNull(offer);
        Assert.assertNotNull(offer.getOffer());
        Assert.assertTrue(offer.getOffer() instanceof HotelItem);
        Assert.assertEquals("1", ((HotelItem) offer.getOffer()).getHid());

        Param param = Param.builder().flow1(Flow.DETAIL).build();
    	List<HotelItem> offers = Lists.newArrayList() ;
		offers.add(new HotelItem("2"));
		offers.add(new HotelItem("3"));
		RowHome ro = new RowHome(null, offers  );
		Mockito.when(this.recommendFunction.buildRow(Mockito.any(Product.class), Mockito.any(Product.class), Mockito.any(ActionRecommendation.class), Mockito.any(Param.class))).thenReturn(ro );
        RowHome row = this.function.buildRow(Product.HOTELS, Product.HOTELS, context, param);
        TestFuntions.assertHotelItem(row.getOffers().get(0), "2");
        TestFuntions.assertHotelItem(row.getOffers().get(1), "3");
    }
}
