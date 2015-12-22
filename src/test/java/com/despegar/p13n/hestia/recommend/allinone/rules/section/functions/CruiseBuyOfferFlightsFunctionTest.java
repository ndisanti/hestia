package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

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
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserLocation;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.hestia.api.data.model.FlightDestinationItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.data.hbase.hot.RankingQuery;
import com.despegar.p13n.hestia.data.hbase.hot.types.RankingType;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;

@RunWith(MockitoJUnitRunner.class)
public class CruiseBuyOfferFlightsFunctionTest {

	@InjectMocks
    private CruiseBuyOfferFlightsFunction function = new CruiseBuyOfferFlightsFunction();

	@Mock
	private RankingsClient rankingService;
    @Mock
    private UserContext userContextAccesor;
    @Mock
    private UserRecord userPrifileRecord;
    @Mock
    private UserLocation homeUserLocation;

   

    @Before
    public void before() {
    	Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(this.userPrifileRecord);
        Mockito.when(this.userPrifileRecord.getLastIntenseSearch()).thenReturn(null);
        Mockito.when(this.userContextAccesor.getUserLocation()).thenReturn(this.homeUserLocation);
    	List<RankingPositionDTO> asList = Arrays.asList(new RankingPositionDTO("a", 3l),//
                new RankingPositionDTO("b", 2l),//
                new RankingPositionDTO("c", 1l));
        RankingTreeDTO ranking = new RankingTreeDTO();
        ranking.addPosition(new RankingPositionDTO("ROUNDTRIP", 5L));
        Mockito.when(this.rankingService.getRankingNoLocation(Matchers.any(RankingQuery.class), Matchers.anyString(), Matchers.anyString(), Matchers.any(CountryCode.class),Matchers.anyInt())).thenReturn(ranking); 
        RankingTreeDTO otherRanking = new RankingTreeDTO();
        otherRanking.setPodium(asList);
		Mockito.when(this.rankingService.getRanking(Mockito.any(Product.class), Mockito.any(RankingType.class), Mockito.any(CountryCode.class), Mockito.any(Integer.class))).thenReturn(otherRanking );
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() {
        this.function.buildRow(null, null, null, null);
    }


    /**
     * user: MIA, origin: MIA, destination: MIA
     */
    @Test
    public void testAAA() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setUserContext(this.userContextAccesor);
        context.setOrigin("MIA");
        context.getBuyActivity().getActivity().getAction().getActionData().put("it", "MIA_RIO_MIA");

        final Param param = new Param();

        Offer buildRow = this.function.buildOffers(Product.HOTELS, Product.FLIGHTS, context, param).get(0);

        Assert.assertTrue(buildRow.getOffer() instanceof FlightDestinationItem);
        Assert.assertEquals("MIA", ((FlightDestinationItem) buildRow.getOffer()).getOrigin());
        Assert.assertEquals("a", ((FlightDestinationItem) buildRow.getOffer()).getDestination());
    }

    /**
     * user: MIA, origin: MIA, destination: NYC
     */
    @Test
    public void testAAB() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setUserContext(this.userContextAccesor);
        context.setOrigin("MIA");
        context.getBuyActivity().getActivity().getAction().getActionData().put("it", "MIA_RIO_NYC");

        final Param param = new Param();

        Offer buildRow = this.function.buildOffers(Product.HOTELS, Product.FLIGHTS, context, param).get(0);

        Assert.assertTrue(buildRow.getOffer() instanceof FlightDestinationItem);
        Assert.assertEquals("NYC", ((FlightDestinationItem) buildRow.getOffer()).getOrigin());
        Assert.assertEquals("MIA", ((FlightDestinationItem) buildRow.getOffer()).getDestination());
    }

    /**
     * user: MIA, origin: NYC, destination: NYC
     */
    @Test
    public void testABB() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setUserContext(this.userContextAccesor);
        context.setOrigin("MIA");
        context.getBuyActivity().getActivity().getAction().getActionData().put("it", "NYC_RIO_NYC");

        final Param param = new Param();

        Offer buildRow = this.function.buildOffers(Product.HOTELS, Product.FLIGHTS, context, param).get(0);

        Assert.assertTrue(buildRow.getOffer() instanceof FlightDestinationItem);
        Assert.assertEquals("MIA", ((FlightDestinationItem) buildRow.getOffer()).getOrigin());
        Assert.assertEquals("NYC", ((FlightDestinationItem) buildRow.getOffer()).getDestination());
    }

    /**
     * user: MIA, origin: NYC, destination: MAD
     */
    @Test
    public void testABC() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setUserContext(this.userContextAccesor);
        context.setOrigin("MIA");
        context.getBuyActivity().getActivity().getAction().getActionData().put("it", "NYC_RIO_MAD");

        final Param param = new Param();

        Offer buildRow = this.function.buildOffers(Product.HOTELS, Product.FLIGHTS, context, param).get(0);

        Assert.assertTrue(buildRow.getOffer() instanceof FlightDestinationItem);
        Assert.assertEquals("MIA", ((FlightDestinationItem) buildRow.getOffer()).getOrigin());
        Assert.assertEquals("NYC", ((FlightDestinationItem) buildRow.getOffer()).getDestination());
    }

    /**
     * user: MIA, origin: NYC, destination: MIA
     */
    @Test
    public void testABA() {

        mock(GeoService.class);

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setUserContext(this.userContextAccesor);
        context.setOrigin("MIA");
        context.getBuyActivity().getActivity().getAction().getActionData().put("it", "NYC_RIO_MIA");

        final Param param = new Param();

        Offer buildRow = this.function.buildOffers(Product.HOTELS, Product.FLIGHTS, context, param).get(0);

        Assert.assertTrue(buildRow.getOffer() instanceof FlightDestinationItem);
        Assert.assertEquals("MIA", ((FlightDestinationItem) buildRow.getOffer()).getOrigin());
        Assert.assertEquals("NYC", ((FlightDestinationItem) buildRow.getOffer()).getDestination());
    }

}
