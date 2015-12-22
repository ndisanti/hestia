package com.despegar.hestia.recommend.allinone.item.hotel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserAction;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.client.RecommendationsClient;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.hotel.DetailDestinationHotelStep;
import com.despegar.p13n.hestia.recommend.allinone.item.hotel.HotelItemBuilderCommonsFunctions;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

@RunWith(MockitoJUnitRunner.class)
public class DetailDestinationHotelStepTest {

    @InjectMocks
    private DetailDestinationHotelStep steps = new DetailDestinationHotelStep();
    @Mock
    private RecommendationsClient recommendation;
    @Mock
    private HotelItemBuilderCommonsFunctions commons;

    @Before
    public void setUp() {
        Mockito.when(this.commons.isHotelAvailable(Mockito.any(HotelItem.class))).thenReturn(true);
        Mockito.when(this.commons.isUnique(Mockito.any(ActionRecommendation.class), Mockito.any(HotelItem.class)))
            .thenReturn(true, false, true);
    }

    @Test
    public void test() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.HOTELS, "MIA");
        SearchActivity searchActivity = Mockito.mock(SearchActivity.class);
        action.setSearchActivity(searchActivity);
        Mockito.when(searchActivity.isActivityFor(Product.HOTELS)).thenReturn(true);
        UserActivity userActivity = Mockito.mock(UserActivity.class);
        UserAction userAction = new UserAction();
        userAction.setFlow(Flow.DETAIL);
        Mockito.when(userActivity.getAction()).thenReturn(userAction);
        Mockito.when(searchActivity.getActivity(Product.HOTELS)).thenReturn(userActivity);
        Mockito.when(userActivity.getProductBusinessId()).thenReturn("1234567");
        Mockito.when(userActivity.getDestination()).thenReturn("MIA");
        Mockito.when(searchActivity.getActivityOrLast(Product.HOTELS)).thenReturn(userActivity);
        Multimap<String, String> hotelsBydestination = LinkedHashMultimap.create();
        hotelsBydestination.put("MIA", "38512");
        Mockito.when(searchActivity.getLastHotelsByDestination()).thenReturn(hotelsBydestination);
        HotelItem item = (HotelItem) this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getHid().equals("38512"));
        List<Recommendation> recommendations = new ArrayList<Recommendation>();
        recommendations.add(new Recommendation("22222", 99));
        Mockito.when(
            this.recommendation.recommend(Mockito.eq(Product.HOTELS), Mockito.eq(Flow.DETAIL), Mockito.eq(Product.HOTELS),
                Mockito.eq(Flow.DETAIL), Mockito.anyString(), Mockito.eq(BaseFunction.RANKING_SIZE))).thenReturn(
            recommendations);
        item = (HotelItem) this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getHid().equals("22222"));
    }

}
