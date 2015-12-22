package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HotelDestinationItem;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;

@RunWith(MockitoJUnitRunner.class)
public class RankingCruiseRegionFunctionTest {

	@InjectMocks
	private RankingCruiseRegionFunction function = new RankingCruiseRegionFunction();
	@Mock
	private GeoService geoService;

	@Test(expected = UnsupportedOperationException.class)
	public void testUnsupported() {
		this.function.buildOffers(null, null, null, null);
	}

	@Test
	public void test() throws Exception {
		when(geoService.getIatasByRegion(CountryCode.AR, "SOA")).thenReturn(
				Arrays.asList("BUE", "MDP", "RIO", "BUZ", "CCL", "ABC", "DEF"));
		when(geoService.normalizeIata(Mockito.anyString())).thenReturn("BUE", "MDP", "RIO", "BUZ", "CCL", "ABC", "DEF");

		
		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4CruiseDetail("MIA", "SOA", "123");
		context.setOrigin("MIA");
		final Param param = new Param();
		RowHome buildRow = this.function.buildRow(Product.HOTELS,
				Product.HOTELS, context, param);
		Assert.assertEquals(7, buildRow.getOffers().size());
		Assert.assertTrue(buildRow.getOffers().get(0) instanceof HotelDestinationItem);
		Assert.assertEquals("BUE", ((HotelDestinationItem) buildRow.getOffers()
				.get(0)).getDestination());
	}
}
