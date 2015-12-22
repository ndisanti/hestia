/**
 * 
 */
package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.test.MockitoAnnotationBaseTest;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.data.CarData;
import com.despegar.p13n.hestia.api.data.model.CarDestinationItem;
import com.despegar.p13n.hestia.api.data.model.CarsLocationDto;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.api.data.model.SearchAction;
import com.despegar.p13n.hestia.api.service.external.PublicCarsService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.matrix.SearchCount;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleData;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

@RunWith(MockitoJUnitRunner.class)
public class CarsSearchFunctionTest extends MockitoAnnotationBaseTest {

	@Mock
	private PublicCarsService carsService;
	@InjectMocks
	private CarsSearchFunction function = new CarsSearchFunction();

	@Test
	public void testCarsSearch() {

		Map<String, CarsLocationDto> value = ImmutableMap
				.<String, CarsLocationDto> builder()
				.put("Economy", new CarsLocationDto("MIA", "AEP")).build();

		Mockito.when(
				this.carsService.getTopCategoriesByDestination("AR", "MIA"))
				.thenReturn(Maps.newLinkedHashMap(value));

		final ActionRecommendation context = this.buildActionRecommendation(
				"MIA", "Economy");
		context.setCurrentSection(SectionsEnum.OFFER);

		Offer offer = this.function.buildOffers(Product.CARS, Product.CARS,
				context, null).get(0);

		Assert.assertNotNull(offer.getOffer());
		Assert.assertTrue(offer.getOffer() instanceof CarDestinationItem);
		Assert.assertEquals("MIA",
				((CarDestinationItem) offer.getOffer()).getDestination());
	}

	@Test
	public void testSearchOtherProduct() {

		Map<String, CarsLocationDto> value = ImmutableMap
				.<String, CarsLocationDto> builder()
				.put("Economy", new CarsLocationDto("MIA", "AEP"))
				.put("Grandes", new CarsLocationDto("MIA", "CIT")).build();

		Mockito.when(
				this.carsService.getTopCategoriesByDestination("AR", "MIA"))
				.thenReturn(Maps.newLinkedHashMap(value));

		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4Search(Product.HOTELS, "MIA");

		Offer offer = this.function.buildOffers(null, Product.CARS, context,
				null).get(0);

		Assert.assertNotNull(offer);
		Assert.assertNotNull(offer.getOffer());
		Assert.assertTrue(offer.getOffer() instanceof CarDestinationItem);
		Assert.assertEquals("MIA",
				((CarDestinationItem) offer.getOffer()).getDestination());
	}

	@Test
	public void testSearchOtherProductNoDate() {
		Map<String, CarsLocationDto> value = Collections.emptyMap();

		Mockito.when(
				this.carsService.getTopCategoriesByDestination("AR", "MIA"))
				.thenReturn(value);

		final ActionRecommendation context = TestFuntions
				.buildActionRecommendation4Search(Product.HOTELS, "MIA");

		List<Offer> offers = this.function.buildOffers(null, Product.CARS,
				context, null);

		Assert.assertNull(offers);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRowNotSupported() {
		this.function.buildRow(null, null, null, null);
	}

	public ActionRecommendation buildActionRecommendation(String destination,
			String category) {
		final ActionRecommendation context = new ActionRecommendation("",
				Product.CARS, CountryCode.AR, Language.ES, null,
				RulesVersion.getDefault(), null, null);

		SearchAction searchAction = new SearchAction();
		searchAction.setUserId("12345");
		searchAction.setProduct(Product.CARS);
		Map<String, Object> data = ImmutableMap.<String, Object> builder()
				.put(CarData.PICK_UP_LOCATION, destination)
				.put(CarData.CATEGORY, category).build();
		searchAction.setActionData(data);
		searchAction.setFlow(Flow.SEARCH);

		SearchActivity searchActivity = new SearchActivity();
		searchActivity.addSearchActivity(Product.CARS, new UserActivity(2,
				destination, null, 2, searchAction, SearchCount.ONE_TO_THREE));

		context.setSearchActivity(searchActivity);
		context.setTitleData(new TitleData());
		return context;
	}
}
