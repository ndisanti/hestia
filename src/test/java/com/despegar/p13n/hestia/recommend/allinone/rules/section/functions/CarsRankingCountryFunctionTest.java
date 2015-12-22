/**
 * 
 */
package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.CarsCategoryRankingDto;
import com.despegar.p13n.hestia.api.data.model.CarsLocationDto;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.service.external.PublicCarsService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function.CarRankingType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.google.common.collect.Maps;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CarsRankingCountryFunctionTest {

	@InjectMocks
	private CarsRankingCountryFunction carsRankingFunction = new CarsRankingCountryFunction();

	@Mock
	private PublicCarsService carsService;

	@Test
	public void testNoData() {

		Mockito.when(
				this.carsService.getTopSearchesRanking(anyString(), anyInt()))
				.thenReturn(
						Collections.<String, List<CarsLocationDto>> emptyMap());
		Mockito.when(
				this.carsService.getTopCheckoutsRanking(anyString(), anyInt()))
				.thenReturn(
						Collections.<String, CarsCategoryRankingDto> emptyMap());

		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4Buy(Product.CRUISES, "MIA");

		Param param = Param.builder().carRankingType(CarRankingType.SEARCH)
				.build();

		List<Offer> offers = carsRankingFunction.buildOffers(Product.CARS,
				Product.CARS, context, param);

		Assert.assertNull(offers);

		RowHome buildRow = carsRankingFunction.buildRow(Product.CARS,
				Product.CARS, context, param);

		Assert.assertNull(buildRow);
	}

	@Test
	public void testSomeDataRankingTypeCheckout() {
		Map<String, CarsCategoryRankingDto> categories = Maps
				.newLinkedHashMap();

		CarsCategoryRankingDto dto = new CarsCategoryRankingDto();
		dto.addCategory("catA", Arrays.asList(//
				new CarsLocationDto("pulA1", "typeA1"),//
				new CarsLocationDto("pulA2", "typeA2")));
		dto.addCategory("catB", Arrays.asList(//
				new CarsLocationDto("pulB1", "typeB1"),//
				new CarsLocationDto("pulB2", "typeB2"),//
				new CarsLocationDto("pulB3", "typeB3")));//
		dto.addCategory("catC", Arrays.asList(//
				new CarsLocationDto("pulC1", "typeC1"),//
				new CarsLocationDto("pulC2", "typeC2"),//
				new CarsLocationDto("pulC3", "typeC3")));//
		categories.put("AR", dto);

		Mockito.when(
				this.carsService.getTopSearchesRanking(anyString(), anyInt()))
				.thenReturn(
						Collections.<String, List<CarsLocationDto>> emptyMap());
		Mockito.when(
				this.carsService.getTopCheckoutsRanking(anyString(), anyInt()))
				.thenReturn(categories);

		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4Buy(Product.CRUISES, "MIA");

		Param param = Param.builder().carRankingType(CarRankingType.CHECKOUT)
				.build();

		Offer offer = carsRankingFunction.buildOffers(Product.CARS,
				Product.CARS, context, param).get(0);

		TestFuntions.assertCarCategory(offer.getOffer(), "catA", "pulA1",
				"typeA1");

		RowHome row = carsRankingFunction.buildRow(Product.CARS, Product.CARS,
				context, param);

		Assert.assertNull(row.getHighlighted());
		Assert.assertEquals(7, row.getOffers().size());
		TestFuntions.assertCarCategory(row.getOffers().get(0), "catB", "pulB1",
				"typeB1");
		TestFuntions.assertCarCategory(row.getOffers().get(1), "catC", "pulC1",
				"typeC1");
		TestFuntions.assertCarCategory(row.getOffers().get(2), "catA", "pulA2",
				"typeA2");
	}

	@Test
	public void testSomeDataRankingTypeSearch() {
		List<CarsLocationDto> destinations = Arrays.asList(//
				new CarsLocationDto("pa1", "ta1"),//
				new CarsLocationDto("pb1", "tb1"), //
				new CarsLocationDto("pb2", "tb2"), //
				new CarsLocationDto("pb3", "tb3"), //
				new CarsLocationDto("pb5", "tb5"), //
				new CarsLocationDto("pb7", "tb5"), //
				new CarsLocationDto("pb8", "tb5"), //
				new CarsLocationDto("pb4", "tb4"));

		Map<String, List<CarsLocationDto>> m = new HashMap<String, List<CarsLocationDto>>();
		m.put("AR", destinations);

		Mockito.when(
				this.carsService.getTopSearchesRanking(anyString(), anyInt()))
				.thenReturn(m);
		Mockito.when(
				this.carsService.getTopCheckoutsRanking(anyString(), anyInt()))
				.thenReturn(
						Collections.<String, CarsCategoryRankingDto> emptyMap());

		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4Buy(Product.CRUISES, "MIA");

		Param param = Param.builder().carRankingType(CarRankingType.SEARCH)
				.build();
		Offer offer = carsRankingFunction.buildOffers(Product.CARS,
				Product.CARS, context, param).get(0);
		TestFuntions.assertCarDestinationItem(offer.getOffer(), "a1", "pa1",
				"city");

		RowHome row = carsRankingFunction.buildRow(Product.CARS, Product.CARS,
				context, param);

		Assert.assertNull(row.getHighlighted());
		Assert.assertEquals(7, row.getOffers().size());
		TestFuntions.assertCarDestinationItem(row.getOffers().get(0), "b",
				"pb1", "city");
		TestFuntions.assertCarDestinationItem(row.getOffers().get(1), "b",
				"pb2", "city");
		TestFuntions.assertCarDestinationItem(row.getOffers().get(2), "b",
				"pb3", "city");
	}

}
