/**
 * 
 */
package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import static org.mockito.Matchers.anyString;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import com.despegar.p13n.hestia.api.data.model.CarsLocationDto;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.service.external.PublicCarsService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.google.common.collect.Maps;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CarsCategoryDestinationFunctionTest {

	@InjectMocks
	private CarsCategoryDestinationFunction carsCategoryDestinationFunction = new CarsCategoryDestinationFunction();
	  @Mock
	    private PublicCarsService carsService;
	
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
    public void testNoData() {


    	 Mockito.when(this.carsService.getTopCategoriesByDestination(anyString(), anyString())).thenReturn(Collections.<String, CarsLocationDto> emptyMap());
        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setUserContext(this.userContextAccesor);
        final Param param = new Param();

        List<Offer> offers = carsCategoryDestinationFunction.buildOffers(Product.CARS, Product.CARS, context, param);

        Assert.assertNull(offers);
       
        RowHome buildRow = carsCategoryDestinationFunction.buildRow(Product.CARS, Product.CARS, context, param);

        Assert.assertNull(buildRow);
    }

    @Test
    public void testSomeData() {

    	Map<String, CarsLocationDto> categories = Maps.newLinkedHashMap();
        categories.put("a", new CarsLocationDto("pa", "ta"));
        categories.put("b", new CarsLocationDto("pb", "tb"));
        categories.put("c", new CarsLocationDto("pc", "tc"));
        categories.put("d", new CarsLocationDto("pd", "td"));
        categories.put("e", new CarsLocationDto("pe", "te"));
        categories.put("f", new CarsLocationDto("pf", "tf"));
        categories.put("g", new CarsLocationDto("pg", "tf"));
        categories.put("h", new CarsLocationDto("ph", "tf"));
        Mockito.when(this.carsService.getTopCategoriesByDestination(anyString(), anyString())).thenReturn(categories);
        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setUserContext(this.userContextAccesor);
        final Param param = new Param();

        Offer offer = carsCategoryDestinationFunction.buildOffers(Product.CARS, Product.CARS, context, param).get(0);

        TestFuntions.assertCarCategory(offer.getOffer(), "a", "pa", "ta");

        RowHome row = carsCategoryDestinationFunction.buildRow(Product.CARS, Product.CARS, context, param);

        Assert.assertNull(row.getHighlighted());
        Assert.assertEquals(7, row.getOffers().size());
        TestFuntions.assertCarCategory(row.getOffers().get(0), "b", "pb", "tb");
        TestFuntions.assertCarCategory(row.getOffers().get(1), "c", "pc", "tc");
        TestFuntions.assertCarCategory(row.getOffers().get(2), "d", "pd", "td");
    }
}
