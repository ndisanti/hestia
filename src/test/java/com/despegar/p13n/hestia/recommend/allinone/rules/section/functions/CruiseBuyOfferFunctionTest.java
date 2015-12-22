/**
 * 
 */
package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.client.RecommendationsClient;
import com.despegar.p13n.hestia.data.hbase.hot.RankingQuery;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CruiseBuyOfferFunctionTest {

	@InjectMocks
	CruiseBuyFunction cruiseLocationRecommenderFunction = new CruiseBuyFunction();
	@Mock
	private RankingsClient rankingService;
	@Mock
	private RecommendationsClient rs;
	@Mock
	private UserContext userContextAccesor;
	@Mock
	private UserRecord userPrifileRecord;

	@Before
	public void before() {

		Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(
				this.userPrifileRecord);
		Mockito.when(this.userPrifileRecord.getLastIntenseSearch()).thenReturn(
				null);
		RankingTreeDTO ranking = new RankingTreeDTO();
		ranking.addPosition(new RankingPositionDTO("ROUNDTRIP", 5L));

		Mockito.when(
				rankingService.getRankingNoLocation(
						Matchers.any(RankingQuery.class), Matchers.anyString(),
						Matchers.anyString(), Matchers.any(CountryCode.class),
						Matchers.anyInt())).thenReturn(ranking);

	}

	@Test(expected = UnsupportedOperationException.class)
	public void testBuildRowUnsupported() {
		new CruiseBuyFunction().buildRow(null, null, null, null);
	}

	@Test
	public void testNoDataIp() {
	
		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
		context.setOrigin("GPA");
		context.setUserContext(this.userContextAccesor);
		final Param param = new Param();

		Offer offer = this.cruiseLocationRecommenderFunction.buildOffers(null,
				Product.FLIGHTS, context, param).get(0);

		TestFuntions.assertFlightlDestination(offer.getOffer(), "GPA", "MIA");

		try {
			this.cruiseLocationRecommenderFunction.buildRow(null,
					Product.FLIGHTS, context, param);
			Assert.fail("buildRow not supported");
		} catch (Exception e) {
			Assert.assertEquals(UnsupportedOperationException.class,
					e.getClass());
		}

	}

	@Test
	public void testSomeDataIpSameCity() {

		
		Mockito.when(this.rs.recommend(Mockito.any(Product.class), Mockito.any(Flow.class), Mockito.any(Product.class), Mockito.any(Flow.class), Mockito.anyString(), Mockito.anyInt())).thenReturn(Arrays.asList(new Recommendation(
				"a", 10), new Recommendation("b", 9),
				new Recommendation("b", 8), new Recommendation("c", 7)));
		
		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4Buy(Product.CRUISES, "GPA");
		context.setOrigin("GPA");
		context.setUserContext(this.userContextAccesor);
		Param param = Param.builder().flow2(Flow.THANKS).build();

		Offer offer = this.cruiseLocationRecommenderFunction.buildOffers(null,
				Product.FLIGHTS, context, param).get(0);

		TestFuntions.assertFlightlDestination(offer.getOffer(), "GPA", "a");

		try {
			this.cruiseLocationRecommenderFunction.buildRow(null,
					Product.FLIGHTS, context, param);
			Assert.fail("buildRow not supported");
		} catch (Exception e) {
			Assert.assertEquals(UnsupportedOperationException.class,
					e.getClass());
		}

	}
}
