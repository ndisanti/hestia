package com.despegar.hestia.recommend.allinone.item.hotel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.hestia.test.TestUtils;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HotelDestinationItem;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.hotel.DefaultHotelStep;
import com.despegar.p13n.hestia.recommend.allinone.item.hotel.HotelItemBuilderCommonsFunctions;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;

@RunWith(MockitoJUnitRunner.class)
public class DefaultHotelStepTest {

    @InjectMocks
    private DefaultHotelStep steps = new DefaultHotelStep();
    @Mock
    private HotelItemBuilderCommonsFunctions commons;
    @Mock
    private RankingsClient hotRankingService;
 

    @Before
    public void setUp() {
        Mockito.when(this.commons.isHotelAvailable(Mockito.any(HotelItem.class))).thenReturn(true);
        Mockito.when(this.commons.isUnique(Mockito.any(ActionRecommendation.class), Mockito.any(HotelItem.class)))
            .thenReturn(true);
    }

    @Test
    public void test() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.HOTELS, "MIA");
        HotelDestinationItem itemDestination = (HotelDestinationItem) this.steps.execute("RIO", action);
        Assert.assertNotNull(itemDestination);
        Assert.assertTrue(itemDestination.getDestination().equals("RIO"));
        List<Pair<String, Long>> recs = new ArrayList<Pair<String, Long>>();
        recs.add(Pair.of("38512", 2L));
        RankingTreeDTO ranking = TestUtils.createRankingTreeDTO(recs);
        Mockito.when(
            this.hotRankingService.getRankingFromIataNoFallback(Product.HOTELS, "RIO",
                StaticRankingTypes.HOTEL_DETAIL_DESTINATON, BaseFunction.RANKING_SIZE)).thenReturn(ranking);
        HotelItem item = (HotelItem) this.steps.execute("RIO", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getHid().equals("38512"));
    }

}
