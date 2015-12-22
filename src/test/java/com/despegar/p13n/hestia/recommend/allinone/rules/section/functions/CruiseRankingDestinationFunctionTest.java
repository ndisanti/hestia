package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.CruiseItem;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.data.hbase.hot.types.RankingType;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;

@RunWith(MockitoJUnitRunner.class)
public class CruiseRankingDestinationFunctionTest {

	@InjectMocks
	private CruiseRankingDestinationFunction function = new CruiseRankingDestinationFunction();
	@Mock
	private GeoService geoService;
	@Mock
	private RankingsClient rankingService;

	@Test(expected = UnsupportedOperationException.class)
	public void testUnsupportedOperationException() {
		this.function.buildOffers(null, null, null, null);
	}

	@Test
	public void testCruiseDetail() {

		List<RankingPositionDTO> asList = Arrays.asList(new RankingPositionDTO(
				"a", 3l), new RankingPositionDTO("b", 2l),
				new RankingPositionDTO("c", 1l),
				new RankingPositionDTO("d", 4l),
				new RankingPositionDTO("e", 4l),
				new RankingPositionDTO("f", 4l),
				new RankingPositionDTO("g", 4l));
		RankingTreeDTO ranking = new RankingTreeDTO();
		ranking.setPodium(asList);
		when(
				this.rankingService.getRankingFromIataNoFallback(
						Mockito.any(Product.class), Mockito.anyString(),
						Mockito.any(RankingType.class),
						Mockito.any(Integer.class))).thenReturn(ranking);
		when(geoService.getIatasByRegion(CountryCode.AR, "SOA")).thenReturn(
				Arrays.asList("BUE"));

		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4CruiseDetail("MIA", "SOA", "123");

		final Param param = new Param();

		RowHome buildRow = this.function.buildRow(Product.HOTELS,
				Product.CRUISES, context, param);

		Assert.assertEquals(7, buildRow.getOffers().size());
		Assert.assertTrue(buildRow.getOffers().get(0) instanceof CruiseItem);
		Assert.assertEquals("a",
				((CruiseItem) buildRow.getOffers().get(0)).getDid());
	}

	@Test
	public void testCruiseSearch() {

		List<RankingPositionDTO> asList = Arrays.asList(new RankingPositionDTO(
				"a", 3l), new RankingPositionDTO("b", 2l),
				new RankingPositionDTO("c", 1l),
				new RankingPositionDTO("d", 4l),
				new RankingPositionDTO("e", 4l),
				new RankingPositionDTO("f", 4l),
				new RankingPositionDTO("g", 4l));

		RankingTreeDTO ranking = new RankingTreeDTO();
		ranking.setPodium(asList);
		when(
				this.rankingService.getRankingFromIataNoFallback(
						Mockito.any(Product.class), Mockito.anyString(),
						Mockito.any(RankingType.class),
						Mockito.any(Integer.class))).thenReturn(ranking);

		when(geoService.getIatasByRegion(CountryCode.AR, "SOA")).thenReturn(
				Arrays.asList("BUE"));

		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4CruiseSearch("SOA");

		final Param param = new Param();

		RowHome buildRow = this.function.buildRow(Product.HOTELS,
				Product.CRUISES, context, param);

		Assert.assertEquals(7, buildRow.getOffers().size());
		Assert.assertTrue(buildRow.getOffers().get(0) instanceof CruiseItem);
		Assert.assertEquals("a",
				((CruiseItem) buildRow.getOffers().get(0)).getDid());
	}

	@Test
	public void testOthers() {

		List<RankingPositionDTO> asList = Arrays.asList(new RankingPositionDTO(
				"a", 3l), new RankingPositionDTO("b", 2l),
				new RankingPositionDTO("c", 1l),
				new RankingPositionDTO("d", 4l),
				new RankingPositionDTO("e", 4l),
				new RankingPositionDTO("f", 4l),
				new RankingPositionDTO("g", 4l));
		RankingTreeDTO ranking = new RankingTreeDTO();
		ranking.setPodium(asList);
		when(
				this.rankingService.getRankingFromIp(
						Mockito.any(Product.class), Mockito.anyString(),
						Mockito.any(StaticRankingTypes.class), Mockito.anyInt()))
				.thenReturn(ranking);
		when(geoService.getIatasByRegion(CountryCode.AR, "SOA")).thenReturn(
				Arrays.asList("BUE"));

		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4Search(Product.CRUISES,
						Product.HOTELS, "MIA");
		context.setOrigin("BUE");

		final Param param = new Param();

		RowHome buildRow = this.function.buildRow(Product.HOTELS,
				Product.CRUISES, context, param);

		Assert.assertEquals(7, buildRow.getOffers().size());
		Assert.assertTrue(buildRow.getOffers().get(0) instanceof CruiseItem);
		Assert.assertEquals("a",
				((CruiseItem) buildRow.getOffers().get(0)).getDid());
	}
}
