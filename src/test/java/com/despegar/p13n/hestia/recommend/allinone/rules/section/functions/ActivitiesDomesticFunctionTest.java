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
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;

@RunWith(MockitoJUnitRunner.class)
public class ActivitiesDomesticFunctionTest {

	@InjectMocks
    private ActivitiesOrlandoDomesticFunction function = new ActivitiesOrlandoDomesticFunction();
	@Mock
	private RankingsClient rankinsgClient;
	
    @Before
    public void setUpt() {

        RankingTreeDTO ranking;
        ranking = new RankingTreeDTO();
        ranking.setCity("BUE");
        ranking.addPosition(new RankingPositionDTO("BUE", 2l));
        ranking.addPosition(new RankingPositionDTO("MDQ", 2l));
        ranking.addPosition(new RankingPositionDTO("CRD", 2l));
        ranking.addPosition(new RankingPositionDTO("XYZ", 2l));
        ranking.addPosition(new RankingPositionDTO("MNO", 2l));
        Mockito.when(this.rankinsgClient.getRankingFromIp(Mockito.any(Product.class), Mockito.anyString(), Mockito.any(StaticRankingTypes.class), Mockito.anyInt())).thenReturn(ranking);
    }

    @Test
    public void testGetFunctionCode() {
        Assert.assertTrue(this.function.getFunctionCode().equals(SectionFunctionCode.ACTIVITIES_ORL_DOMESTIC));
    }

    @Test
    public void testBuildRow() {
        ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.ACTIVITIES, "BUE");
        RowHome obtainedRowHome = this.function.buildRow(Product.ACTIVITIES, Product.ACTIVITIES, context, new Param());
        Assert.assertNotNull(obtainedRowHome);
        TestFuntions.assertActivityItem(obtainedRowHome.getOffers().get(0), "DY_ORL");
        TestFuntions.assertActivityItem(obtainedRowHome.getOffers().get(1), "UN_ORL");
        TestFuntions.assertActivityDestinationItem(obtainedRowHome.getOffers().get(2), "BUE");
        TestFuntions.assertActivityDestinationItem(obtainedRowHome.getOffers().get(3), "MDQ");
        TestFuntions.assertActivityDestinationItem(obtainedRowHome.getOffers().get(4), "CRD");
        TestFuntions.assertActivityDestinationItem(obtainedRowHome.getOffers().get(5), "XYZ");
        TestFuntions.assertActivityDestinationItem(obtainedRowHome.getOffers().get(6), "MNO");
    }

    @Test
    public void testBuildOffer() {
        ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.ACTIVITIES, "BUE");
        List<Offer> obtainedOffer = this.function.buildOffers(Product.ACTIVITIES, Product.ACTIVITIES, context, new Param());
        Assert.assertNotNull(obtainedOffer);
        TestFuntions.assertActivityDestinationItem(obtainedOffer.get(0).getOffer(), "ORL");
    }
}
