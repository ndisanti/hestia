/**
 * 
 */
package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.despegar.hestia.api.data.model.DetailAction;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.data.hbase.hot.types.RankingType;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.matrix.SearchCount;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.google.common.collect.Lists;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class RankingHotelsDestinationFunctionTest {

    @InjectMocks
    private RankingHotelsDestinationFunction function = new RankingHotelsDestinationFunction();
    @Mock
    private HotelItemBuilder itemBuilder;
	@Mock
	private RankingsClient rankingsClient;
    @Mock
    private UserContext userContextAccesor;
    @Mock
    private UserRecord userPrifileRecord;

    @Before
    public void setUp() {
        Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(this.userPrifileRecord);
        Mockito.when(this.userPrifileRecord.getLastIntenseSearch()).thenReturn(null);
        Mockito.when(
            this.itemBuilder.buildItem(Mockito.any(Product.class), Mockito.any(Product.class),
                Mockito.any(ActionRecommendation.class), Mockito.any(Param.class), Mockito.any(RankingItemDTO.class)))
            .thenReturn(new HotelItem("15"), new HotelItem("2"), new HotelItem("3"), new HotelItem("4"), new HotelItem("5"),
                new HotelItem("6"), new HotelItem("7"), new HotelItem("8"));
    }

    @Test
    public void testNoData() {

        List<RankingPositionDTO> podium = Lists.newArrayList();
        RankingTreeDTO ranking = new RankingTreeDTO();
        ranking.setPodium(podium);
		Mockito.when(this.rankingsClient.getRankingFromIataNoFallback(Mockito.any(Product.class), Mockito.anyString(), Mockito.any(RankingType.class), Mockito.any(Integer.class))).thenReturn(ranking);
        
        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setUserContext(this.userContextAccesor);
        final Param param = new Param();

        List<Offer> offers = this.function.buildOffers(null, Product.HOTELS, context, param);

        Assert.assertNull(offers);

        RowHome buildRow = this.function.buildRow(null, Product.HOTELS, context, param);

        Assert.assertNull(buildRow);
    }

    @Test
    public void testSomeData4Buy() {

        List<RankingPositionDTO> asList = Arrays.asList(//
            new RankingPositionDTO("1", 3l), //
            new RankingPositionDTO("2", 2l),//
            new RankingPositionDTO("3", 1l),//
            new RankingPositionDTO("4", 4l),//
            new RankingPositionDTO("5", 5l), //
            new RankingPositionDTO("7", 5l), //
            new RankingPositionDTO("8", 5l), //
            new RankingPositionDTO("6", 5l));

        RankingTreeDTO ranking = new RankingTreeDTO();
        ranking.setPodium(asList);
		Mockito.when(this.rankingsClient.getRankingFromIataNoFallback(Mockito.any(Product.class), Mockito.anyString(), Mockito.any(RankingType.class), Mockito.any(Integer.class))).thenReturn(ranking);


        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setUserContext(this.userContextAccesor);
        final Param param = new Param();

        Offer offer = this.function.buildOffers(Product.HOTELS, Product.HOTELS, context, param).get(0);

        TestFuntions.assertHotelItem(offer.getOffer(), "15");

        RowHome buildRow = this.function.buildRow(Product.HOTELS, Product.HOTELS, context, param);

        Assert.assertNull(buildRow.getHighlighted());
        Assert.assertEquals(7, buildRow.getOffers().size());
        TestFuntions.assertHotelItem(buildRow.getOffers().get(0), "2");
        TestFuntions.assertHotelItem(buildRow.getOffers().get(1), "3");

    }

    @Test
    public void testSomeData4Search() {

        List<RankingPositionDTO> asList = Arrays.asList(//
            new RankingPositionDTO("1", 3l),//
            new RankingPositionDTO("2", 2l),//
            new RankingPositionDTO("3", 1l),//
            new RankingPositionDTO("4", 4l),//
            new RankingPositionDTO("5", 5l), //
            new RankingPositionDTO("7", 5l), //
            new RankingPositionDTO("8", 5l), //
            new RankingPositionDTO("6", 5l));

        RankingTreeDTO ranking = new RankingTreeDTO();
        ranking.setPodium(asList);
		Mockito.when(this.rankingsClient.getRankingFromIataNoFallback(Mockito.any(Product.class), Mockito.anyString(), Mockito.any(RankingType.class), Mockito.any(Integer.class))).thenReturn(ranking);


        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Search(Product.HOTELS, "MIA");

        final Param param = new Param();

        Offer offer = this.function.buildOffers(Product.HOTELS, Product.HOTELS, context, param).get(0);

        TestFuntions.assertHotelItem(offer.getOffer(), "15");

        RowHome buildRow = this.function.buildRow(Product.HOTELS, Product.HOTELS, context, param);

        Assert.assertNull(buildRow.getHighlighted());
        Assert.assertEquals(7, buildRow.getOffers().size());
        TestFuntions.assertHotelItem(buildRow.getOffers().get(0), "2");
        TestFuntions.assertHotelItem(buildRow.getOffers().get(1), "3");

    }

    @Test
    public void testSomeData4SearchSeen() {

        List<RankingPositionDTO> asList = Arrays.asList(//
            new RankingPositionDTO("1", 3l),//
            new RankingPositionDTO("2", 2l),//
            new RankingPositionDTO("3", 1l),//
            new RankingPositionDTO("4", 4l),//
            new RankingPositionDTO("5", 5l), //
            new RankingPositionDTO("7", 5l), //
            new RankingPositionDTO("8", 5l), //
            new RankingPositionDTO("6", 5l));

        RankingTreeDTO ranking = new RankingTreeDTO();
        ranking.setPodium(asList);
		Mockito.when(this.rankingsClient.getRankingFromIataNoFallback(Mockito.any(Product.class), Mockito.anyString(), Mockito.any(RankingType.class), Mockito.any(Integer.class))).thenReturn(ranking);

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Search(Product.HOTELS, "MIA");

        Param param = Param.builder().seen(true).build();

        Offer offer = this.function.buildOffers(Product.HOTELS, Product.HOTELS, context, param).get(0);

        TestFuntions.assertHotelItem(offer.getOffer(), "15");

        RowHome buildRow = this.function.buildRow(Product.HOTELS, Product.HOTELS, context, param);

        TestFuntions.assertHotelDestination(buildRow.getHighlighted(), "MIA");
        Assert.assertEquals(7, buildRow.getOffers().size());
        TestFuntions.assertHotelItem(buildRow.getOffers().get(0), "2");
        TestFuntions.assertHotelItem(buildRow.getOffers().get(1), "3");

    }

    @Test
    public void testSomeData4SearchSeenDetail() {

        List<RankingPositionDTO> asList = Arrays.asList(//
            new RankingPositionDTO("2", 3l),//
            new RankingPositionDTO("3", 2l),//
            new RankingPositionDTO("4", 1l),//
            new RankingPositionDTO("5", 4l),//
            new RankingPositionDTO("6", 5l),//
            new RankingPositionDTO("7", 5l), //
            new RankingPositionDTO("8", 5l), //
            new RankingPositionDTO("9", 6l));

        RankingTreeDTO ranking = new RankingTreeDTO();
        ranking.setPodium(asList);
		Mockito.when(this.rankingsClient.getRankingFromIataNoFallback(Mockito.any(Product.class), Mockito.anyString(), Mockito.any(RankingType.class), Mockito.any(Integer.class))).thenReturn(ranking);


        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Search(Product.HOTELS, "MIA");

        DetailAction detailAction = new DetailAction();
        detailAction.setUserId("12345");
        detailAction.setProduct(Product.HOTELS);
        detailAction.setFlow(Flow.DETAIL);

        UserActivity lastActivity = new UserActivity(2, "MIA", "1", 2, detailAction, SearchCount.ONE_TO_THREE);

        context.getSearchActivity().addSearchActivity(Product.HOTELS, lastActivity);

        Param param = Param.builder().seen(true).build();

        Offer offer = this.function.buildOffers(Product.HOTELS, Product.HOTELS, context, param).get(0);

        TestFuntions.assertHotelItem(offer.getOffer(), "15");

        context.setCurrentSection(SectionsEnum.ROW1);

        RowHome buildRow = this.function.buildRow(Product.HOTELS, Product.HOTELS, context, param);

        TestFuntions.assertHotelItem(buildRow.getHighlighted(), "1");
        Assert.assertEquals(7, buildRow.getOffers().size());
        TestFuntions.assertHotelItem(buildRow.getOffers().get(0), "2");
        TestFuntions.assertHotelItem(buildRow.getOffers().get(1), "3");

    }

}
