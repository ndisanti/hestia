package com.despegar.hestia.recommend.allinone.item.vacationrentals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.hestia.test.TestUtils;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.api.data.model.VacationRentalDestinationItem;
import com.despegar.p13n.hestia.api.data.model.VacationRentalItem;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.vacactionrentals.DefaultVacationRentalStep;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;

@RunWith(MockitoJUnitRunner.class)
public class DefaultVacationRentalStepTest {

    @InjectMocks
    private DefaultVacationRentalStep steps = new DefaultVacationRentalStep();
    @Mock
    private RankingsClient hotRankingService;

    @Test
    public void testGetItem() {
        // Step1
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.VACATIONRENTALS, "MIA");
        VacationRentalDestinationItem destinationItem = (VacationRentalDestinationItem) this.steps.execute("MIA", action);
        Assert.assertNotNull(destinationItem);
        Assert.assertTrue(destinationItem.getDestination().equals("MIA"));
        // Step2
        List<Pair<String, Long>> recs = new ArrayList<Pair<String, Long>>();
        recs.add(Pair.of("38512", 2L));
        RankingTreeDTO ranking = TestUtils.createRankingTreeDTO(recs);
        Mockito.when(
            this.hotRankingService.getRankingFromIataNoFallback(Product.VACATIONRENTALS, "MIA",
                StaticRankingTypes.VACATIONS_RENTALS_VRID_BY_DESTINATION, BaseFunction.RANKING_SIZE)).thenReturn(ranking);
        VacationRentalItem item = (VacationRentalItem) this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getVrid().equals("38512"));

        Assert.assertNull(this.steps.execute("MIA", action));
    }

}
