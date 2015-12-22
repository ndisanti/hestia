package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.List;

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
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;

@RunWith(MockitoJUnitRunner.class)
public class RankingRentalsDestinationFunctionTest {

    @Mock
    private RankingsClient rankingService;
    @InjectMocks
    private RankingRentalsDestinationFunction function = new RankingRentalsDestinationFunction();
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


        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setUserContext(this.userContextAccesor);
        final Param param = new Param();
        RankingTreeDTO emptyPodium = new RankingTreeDTO();
        Mockito.when(
            this.rankingService.getRankingFromIataNoFallback(Product.VACATIONRENTALS, "MIA",
                StaticRankingTypes.VACATIONS_RENTALS_VRID_BY_DESTINATION, 10)).thenReturn(emptyPodium);
        Mockito.when(
            this.rankingService.getRankingFromIataNoFallback(Product.VACATIONRENTALS, "MIA",
                StaticRankingTypes.VACATIONS_RENTALS_CONTACT_VRID, 10)).thenReturn(emptyPodium);
        List<Offer> offers = this.function.buildOffers(null, Product.VACATIONRENTALS, context, param);
        Assert.assertNull(offers);
        RowHome buildRow = this.function.buildRow(null, Product.VACATIONRENTALS, context, param);
        Assert.assertNull(buildRow);
    }

    @Test
    public void testSomeData4Buy() {

        RankingTreeDTO podium = new RankingTreeDTO();
        podium.addPosition("1", 3l);
        podium.addPosition("2", 2l);
        podium.addPosition("3", 1l);
        podium.addPosition("4", 4l);
        podium.addPosition("5", 5l);
        podium.addPosition("6", 6l);
        podium.addPosition("7", 7l);
        podium.addPosition("8", 8l);
        RankingTreeDTO emptyPodium = new RankingTreeDTO();
        Mockito.when(
            this.rankingService.getRankingFromIataNoFallback(Product.VACATIONRENTALS, "MIA",
                StaticRankingTypes.VACATIONS_RENTALS_VRID_BY_DESTINATION, 10)).thenReturn(podium);

        Mockito.when(
            this.rankingService.getRankingFromIataNoFallback(Product.VACATIONRENTALS, "MIA",
                StaticRankingTypes.VACATIONS_RENTALS_CONTACT_VRID, 10)).thenReturn(emptyPodium);

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setUserContext(this.userContextAccesor);
        final Param param = new Param();

        Offer offer = this.function.buildOffers(Product.VACATIONRENTALS, Product.VACATIONRENTALS, context, param).get(0);

        TestFuntions.assertRentalsItem(offer.getOffer(), "1");

        RowHome buildRow = this.function.buildRow(Product.VACATIONRENTALS, Product.VACATIONRENTALS, context, param);

        Assert.assertNull(buildRow.getHighlighted());
        Assert.assertEquals(7, buildRow.getOffers().size());
        TestFuntions.assertRentalsItem(buildRow.getOffers().get(0), "2");
        TestFuntions.assertRentalsItem(buildRow.getOffers().get(1), "3");
        TestFuntions.assertRentalsItem(buildRow.getOffers().get(2), "4");
        TestFuntions.assertRentalsItem(buildRow.getOffers().get(3), "5");
        TestFuntions.assertRentalsItem(buildRow.getOffers().get(4), "6");
        TestFuntions.assertRentalsItem(buildRow.getOffers().get(5), "7");
        TestFuntions.assertRentalsItem(buildRow.getOffers().get(6), "8");

    }

    @Test
    public void testSomeData4Search() {

        RankingTreeDTO podium = new RankingTreeDTO();
        podium.addPosition("1", 3l);
        podium.addPosition("2", 2l);
        podium.addPosition("3", 1l);
        podium.addPosition("4", 4l);
        podium.addPosition("5", 5l);
        podium.addPosition("6", 6l);
        podium.addPosition("7", 7l);
        podium.addPosition("8", 8l);
        RankingTreeDTO emptyPodium = new RankingTreeDTO();
        Mockito.when(
            this.rankingService.getRankingFromIataNoFallback(Product.VACATIONRENTALS, "MIA",
                StaticRankingTypes.VACATIONS_RENTALS_VRID_BY_DESTINATION, 10)).thenReturn(podium);

        Mockito.when(
            this.rankingService.getRankingFromIataNoFallback(Product.VACATIONRENTALS, "MIA",
                StaticRankingTypes.VACATIONS_RENTALS_CONTACT_VRID, 10)).thenReturn(emptyPodium);

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Search(Product.VACATIONRENTALS, "MIA");

        final Param param = new Param();

        Offer offer = this.function.buildOffers(Product.VACATIONRENTALS, Product.VACATIONRENTALS, context, param).get(0);

        TestFuntions.assertRentalsItem(offer.getOffer(), "1");

        RowHome buildRow = this.function.buildRow(Product.VACATIONRENTALS, Product.VACATIONRENTALS, context, param);

        Assert.assertNull(buildRow.getHighlighted());
        Assert.assertEquals(7, buildRow.getOffers().size());
        TestFuntions.assertRentalsItem(buildRow.getOffers().get(0), "2");
        TestFuntions.assertRentalsItem(buildRow.getOffers().get(1), "3");

    }

    @Test
    public void testSomeData4Search2() {

        RankingTreeDTO podium = new RankingTreeDTO();
        podium.addPosition("1", 3l);
        podium.addPosition("2", 2l);
        podium.addPosition("3", 1l);
        podium.addPosition("4", 4l);
        podium.addPosition("5", 5l);
        RankingTreeDTO otherPodium = new RankingTreeDTO();
        podium.addPosition("6", 3l);
        podium.addPosition("7", 2l);
        podium.addPosition("8", 1l);
        podium.addPosition("9", 4l);
        Mockito.when(
            this.rankingService.getRankingFromIataNoFallback(Product.VACATIONRENTALS, "MIA",
                StaticRankingTypes.VACATIONS_RENTALS_VRID_BY_DESTINATION, 10)).thenReturn(podium);

        Mockito.when(
            this.rankingService.getRankingFromIataNoFallback(Product.VACATIONRENTALS, "MIA",
                StaticRankingTypes.VACATIONS_RENTALS_CONTACT_VRID, 10)).thenReturn(otherPodium);


        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Search(Product.VACATIONRENTALS, "MIA");

        Param param = Param.builder().seen(true).build();

        Offer offer = this.function.buildOffers(Product.VACATIONRENTALS, Product.VACATIONRENTALS, context, param).get(0);

        TestFuntions.assertRentalsItem(offer.getOffer(), "1");

        RowHome buildRow = this.function.buildRow(Product.VACATIONRENTALS, Product.VACATIONRENTALS, context, param);

        Assert.assertEquals(8, buildRow.getOffers().size());
        TestFuntions.assertRentalsItem(buildRow.getOffers().get(0), "2");
        TestFuntions.assertRentalsItem(buildRow.getOffers().get(1), "3");
        TestFuntions.assertRentalsItem(buildRow.getOffers().get(6), "8");
    }
}
