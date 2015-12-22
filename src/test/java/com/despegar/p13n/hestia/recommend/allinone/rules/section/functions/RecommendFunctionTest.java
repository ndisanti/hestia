/**
 * 
 */
package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.client.RecommendationsClient;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function.CountryType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.filter.FilterFactory;

/**
 * @author tulio
 *
 */

@RunWith(MockitoJUnitRunner.class)
public class RecommendFunctionTest {

	@InjectMocks
	private RecommendFunction function = new RecommendFunction();
	@Mock
	private DetailItemBuilder itemBuilder;
	@Mock
	private RecommendationsClient rc;
	@Mock
	private UserContext userContextAccesor;
	@Mock
	private UserRecord userPrifileRecord;
	@Mock
	private FilterFactory filterFactory;
	@Mock
	private GeoService geoService;
	
	@Before
	public void setUp() {
		Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(
				this.userPrifileRecord);
		Mockito.when(this.userPrifileRecord.getLastIntenseSearch()).thenReturn(
				null);
		Mockito.when(
				this.itemBuilder.buildItem(Mockito.any(Product.class),
						Mockito.any(Product.class),
						Mockito.any(ActionRecommendation.class),
						Mockito.any(Param.class),
						Mockito.any(RankingItemDTO.class))).thenReturn(
				new HotelItem("1"), new HotelItem("2"), new HotelItem("3"),
				new HotelItem("4"), new HotelItem("5"), new HotelItem("6"),
				new HotelItem("7"));
	}

	@Test
	public void testNoData() {

		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
		context.setUserContext(this.userContextAccesor);
		Param param = Param.builder().pr1(Product.FLIGHTS).flow1(Flow.THANKS)
				.flow2(Flow.THANKS).build();

		List<Offer> offers = this.function.buildOffers(null, Product.CARS,
				context, param);

		Assert.assertNull(offers);

		RowHome buildRow = this.function.buildRow(null, Product.CARS, context,
				param);

		Assert.assertNull(buildRow);

	}

	@Test
	public void test3Recomendations() {

		Mockito.when(
				this.rc.recommend(Mockito.any(Product.class),
						Mockito.any(Flow.class), Mockito.any(Product.class),
						Mockito.any(Flow.class), Mockito.anyString(),
						Mockito.any(Integer.class))).thenReturn(
				Arrays.asList(new Recommendation("a", 10),//
						new Recommendation("b", 9), //
						new Recommendation("m", 8), //
						new Recommendation("c", 7), //
						new Recommendation("d", 9),//
						new Recommendation("e", 11), //
						new Recommendation("f", 11), //
						new Recommendation("g", 11)));
		
		Mockito.when(geoService.normalizeIata(anyString())).thenReturn("a", "b","m","c","d","e","f","g");
		Param param = Param.builder().pr1(Product.FLIGHTS).flow1(Flow.THANKS)
				.flow2(Flow.THANKS).build();

		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
		context.setUserContext(this.userContextAccesor);
		Offer offer4Hotels = this.function.buildOffers(Product.HOTELS,
				Product.HOTELS, context, param).get(0);
		TestFuntions.assertHotelDestination(offer4Hotels.getOffer(), "a");

		RowHome row4Hotels = this.function.buildRow(Product.HOTELS,
				Product.HOTELS, context, param);

		Assert.assertNull(row4Hotels.getHighlighted());
		Assert.assertEquals(7, row4Hotels.getOffers().size());
		TestFuntions.assertHotelDestination(row4Hotels.getOffers().get(0), "b");
		TestFuntions.assertHotelDestination(row4Hotels.getOffers().get(1), "m");
	}

	@Test
	public void testRecomendationsCars() {

		List<Recommendation> recomendations = Arrays.asList(new Recommendation(
				"a", 10),//
				new Recommendation("b", 9), //
				new Recommendation("b", 8), //
				new Recommendation("c", 7), //
				new Recommendation("d", 9),//
				new Recommendation("e", 11),//
				new Recommendation("f", 11), //
				new Recommendation("g", 11));//
		Mockito.when(geoService.normalizeIata(anyString())).thenReturn("a", "b","b","c","d","e","f","g");

		Mockito.when(
				this.rc.recommend(Mockito.any(Product.class),
						Mockito.any(Flow.class), Mockito.any(Product.class),
						Mockito.any(Flow.class), Mockito.anyString(),
						Mockito.any(Integer.class),Mockito.any(CountryCode.class))).thenReturn(recomendations);
		Mockito.when(geoService.getCountryFromIata("MIA")).thenReturn(
				CountryCode.US.toString());
		Mockito.when(geoService.normalizeIata(anyString())).thenAnswer(
				new Answer<String>() {
					@Override
					public String answer(InvocationOnMock invocation)
							throws Throwable {
						Object[] args = invocation.getArguments();
						return (String) args[0];
					}
				});

		Param param = Param.builder().pr1(Product.CARS).flow1(Flow.SEARCH)
				.flow2(Flow.SEARCH).countryType(CountryType.DOMESTIC).build();

		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4Buy(Product.HOTELS, "MIA");
		context.setUserContext(this.userContextAccesor);
		RowHome row4Hotels = this.function.buildRow(Product.HOTELS,
				Product.CARS, context, param);

		Assert.assertNull(row4Hotels.getHighlighted());
		Assert.assertEquals(7, row4Hotels.getOffers().size());
		TestFuntions.assertCarDestinationItem(row4Hotels.getOffers().get(0),
				"", "a", "city");
		TestFuntions.assertCarDestinationItem(row4Hotels.getOffers().get(1),
				"", "b", "city");
	}

	@Test
	public void testRecomendationsCruises() {

		List<Recommendation> recomendations = Arrays.asList(new Recommendation(
				"a", 10),//
				new Recommendation("b", 9), //
				new Recommendation("b", 8), //
				new Recommendation("c", 7), //
				new Recommendation("d", 9),//
				new Recommendation("e", 11), //
				new Recommendation("f", 11), //
				new Recommendation("g", 11));//

		when(
				this.rc.recommend(Mockito.eq(Product.CRUISES),
						Mockito.eq(Flow.SEARCH), Mockito.eq(Product.CRUISES),
						Mockito.eq(Flow.SEARCH), anyString(), anyInt()))
				.thenReturn(recomendations);

		GeoService geoService = Mockito.mock(GeoService.class);
		Mockito.when(geoService.getCountryFromIata("MIA")).thenReturn(
				CountryCode.US.toString());
		Mockito.when(geoService.normalizeIata(anyString())).thenAnswer(
				new Answer<String>() {
					@Override
					public String answer(InvocationOnMock invocation)
							throws Throwable {
						Object[] args = invocation.getArguments();
						return (String) args[0];
					}
				});
		Param param = Param.builder().pr1(Product.CRUISES).flow1(Flow.SEARCH)
				.flow2(Flow.SEARCH).build();

		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
		context.setUserContext(this.userContextAccesor);
		RowHome row = this.function.buildRow(Product.CRUISES, Product.CRUISES,
				context, param);

		Assert.assertNull(row.getHighlighted());
		Assert.assertEquals(7, row.getOffers().size());
		TestFuntions.assertCruiseRegionItem(row.getOffers().get(0), "a");
		TestFuntions.assertCruiseRegionItem(row.getOffers().get(1), "b");
	}

	@Test
	public void testSearchAnd3Recomendations() {

		Mockito.when(
				this.rc.recommend(Mockito.any(Product.class),
						Mockito.any(Flow.class), Mockito.any(Product.class),
						Mockito.any(Flow.class), Mockito.anyString(),
						Mockito.any(Integer.class))).thenReturn(
				Arrays.asList(new Recommendation("MIA", 10),//
						new Recommendation("a", 9), //
						new Recommendation("b", 8), //
						new Recommendation("c", 7), //
						new Recommendation("d", 9),//
						new Recommendation("e", 11), //
						new Recommendation("f", 11), //
						new Recommendation("g", 11)));
		Mockito.when(geoService.normalizeIata(anyString())).thenReturn("MIA", "a","b","c","d","e","f","g");

		Param param = Param.builder().addSearch(true).pr1(Product.FLIGHTS)
				.flow1(Flow.THANKS).flow2(Flow.THANKS).build();

		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
		context.setUserContext(this.userContextAccesor);
		Offer offer4Hotels = this.function.buildOffers(Product.HOTELS,
				Product.HOTELS, context, param).get(0);
		TestFuntions.assertHotelDestination(offer4Hotels.getOffer(), "MIA");

		RowHome row4Hotels = this.function.buildRow(Product.HOTELS,
				Product.HOTELS, context, param);

		Assert.assertNull(row4Hotels.getHighlighted());
		Assert.assertEquals(7, row4Hotels.getOffers().size());
		TestFuntions.assertHotelDestination(row4Hotels.getOffers().get(0), "a");
		TestFuntions.assertHotelDestination(row4Hotels.getOffers().get(1), "b");
	}

	@Test
	public void testDetailRecomendationsWithDetailHistory() {

		Mockito.when(
				this.rc.recommend(Mockito.any(Product.class),
						Mockito.any(Flow.class), Mockito.any(Product.class),
						Mockito.any(Flow.class), Mockito.anyString(),
						Mockito.any(Integer.class))).thenReturn(
				Arrays.asList(new Recommendation("1", 10),//
						new Recommendation("2", 9), //
						new Recommendation("2", 8), //
						new Recommendation("3", 7), //
						new Recommendation("4", 9),//
						new Recommendation("5", 11),//
						new Recommendation("6", 11),//
						new Recommendation("7", 11),//
						new Recommendation("8", 11))//
				);
		Param param = Param.builder().pr1(Product.HOTELS).flow1(Flow.DETAIL)
				.flow2(Flow.DETAIL).build();

		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4Detail(Product.HOTELS, "MIA", "1");

		Offer offer4Hotels = this.function.buildOffers(Product.HOTELS,
				Product.HOTELS, context, param).get(0);
		TestFuntions.assertHotelItem(offer4Hotels.getOffer(), "1");

		RowHome row4Hotels = this.function.buildRow(Product.HOTELS,
				Product.HOTELS, context, param);

		Assert.assertNull(row4Hotels.getHighlighted());
		Assert.assertEquals(6, row4Hotels.getOffers().size());
		TestFuntions.assertHotelItem(row4Hotels.getOffers().get(0), "2");
		TestFuntions.assertHotelItem(row4Hotels.getOffers().get(1), "3");
	}

	@Test
	public void testDetailRecomendationsWithNoDetailHistory() {

		Mockito.when(
				this.rc.recommend(Mockito.any(Product.class),
						Mockito.any(Flow.class), Mockito.any(Product.class),
						Mockito.any(Flow.class), Mockito.anyString(),
						Mockito.any(Integer.class))).thenReturn(
				Arrays.asList(new Recommendation("a", 10),//
						new Recommendation("b", 9), //
						new Recommendation("m", 8), //
						new Recommendation("c", 7), //
						new Recommendation("d", 9),//
						new Recommendation("e", 11), //
						new Recommendation("f", 11),//
						new Recommendation("g", 11)));
		Mockito.when(geoService.normalizeIata(anyString())).thenReturn("a", "b","m","c","d","e","f","g");

		Param param = Param.builder().pr1(Product.FLIGHTS).flow1(Flow.DETAIL)
				.flow2(Flow.DETAIL).build();

		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4Search(Product.FLIGHTS, "MIA");

		Offer offer4Hotels = this.function.buildOffers(Product.HOTELS,
				Product.HOTELS, context, param).get(0);
		TestFuntions.assertHotelDestination(offer4Hotels.getOffer(), "a");

		RowHome row4Hotels = this.function.buildRow(Product.HOTELS,
				Product.HOTELS, context, param);

		Assert.assertNull(row4Hotels.getHighlighted());
		Assert.assertEquals(7, row4Hotels.getOffers().size());
		TestFuntions.assertHotelDestination(row4Hotels.getOffers().get(0), "b");
		TestFuntions.assertHotelDestination(row4Hotels.getOffers().get(1), "m");
	}
}
