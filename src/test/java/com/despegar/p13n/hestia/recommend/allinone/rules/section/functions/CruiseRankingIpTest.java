package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;

@RunWith(MockitoJUnitRunner.class)
public class CruiseRankingIpTest {

	@InjectMocks
    private CruiseRankingIpFunction cruiseRankingIp = new CruiseRankingIpFunction();
	@Mock
	private RankingsClient rankingsClient;
	
    @Test
    public void test() {

        List<RankingPositionDTO> asList = Arrays.asList(new RankingPositionDTO("a", 3l), new RankingPositionDTO("b", 2l),
            new RankingPositionDTO("c", 1l), new RankingPositionDTO("d", 5l), new RankingPositionDTO("e", 6l),
            new RankingPositionDTO("f", 6l), new RankingPositionDTO("g", 6l));

        RankingTreeDTO ranking = new RankingTreeDTO();
        ranking.setPodium(asList);
		Mockito.when(this.rankingsClient.getRankingFromIp(Mockito.any(Product.class), Mockito.anyString(), Mockito.any(StaticRankingTypes.class), Mockito.anyInt())).thenReturn(ranking );
        final ActionRecommendation context = TestFuntions.buildActionRecommendation4CruiseDetail("MIA", "SOA", "123");
        context.setOrigin("BUE");
        final Param param = new Param();

        RowHome buildRow = this.cruiseRankingIp.buildRow(Product.CRUISES, Product.CRUISES, context, param);

        Assert.assertEquals(7, buildRow.getOffers().size());
    }

    @Test
    public void testAssertFunctionCode() {
        Assert.assertEquals(SectionFunctionCode.CRUISE_RANKING_IP, this.cruiseRankingIp.getFunctionCode());
    }

}
