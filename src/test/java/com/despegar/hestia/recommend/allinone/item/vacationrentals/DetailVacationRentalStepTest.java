package com.despegar.hestia.recommend.allinone.item.vacationrentals;

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
import com.despegar.p13n.hestia.api.data.model.VacationRentalItem;
import com.despegar.p13n.hestia.client.RecommendationsClient;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.vacactionrentals.DetailVacationRentalStep;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

@RunWith(MockitoJUnitRunner.class)
public class DetailVacationRentalStepTest {

    @InjectMocks
    private DetailVacationRentalStep steps = new DetailVacationRentalStep();
    @Mock
    private RecommendationsClient recommendation;

    @Before
    public void setUp(){
    	List<Recommendation> recommendations = new ArrayList<Recommendation>();
        recommendations.add(new Recommendation("22222", 99));
        Mockito.when(this.recommendation.recommend(Mockito.any(Product.class), Mockito.any(Flow.class),
        		Mockito.any(Product.class), Mockito.any(Flow.class), Mockito.anyString(),
                Mockito.any(Integer.class))).thenReturn(recommendations);        
     }
    
    @Test
    public void testGetItem() {

        // setp 1
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.VACATIONRENTALS, "MIA");
        SearchActivity searchActivity = Mockito.mock(SearchActivity.class);
        action.setSearchActivity(searchActivity);
        Mockito.when(searchActivity.isActivityFor(Product.VACATIONRENTALS)).thenReturn(true);
        UserActivity userActivity = Mockito.mock(UserActivity.class);
        UserAction userAction = new UserAction();
        userAction.setFlow(Flow.DETAIL);
        Mockito.when(userActivity.getAction()).thenReturn(userAction);
        Mockito.when(searchActivity.getActivity(Product.VACATIONRENTALS)).thenReturn(userActivity);
        Mockito.when(userActivity.getProductBusinessId()).thenReturn("1234567");
        Mockito.when(userActivity.getDestination()).thenReturn("MIA");
        Mockito.when(searchActivity.getActivityOrLast(Product.VACATIONRENTALS)).thenReturn(userActivity);
        Multimap<String, String> vacRentalsBydestination = LinkedHashMultimap.create();
        vacRentalsBydestination.put("MIA", "38512");
        Mockito.when(searchActivity.getLastVacationRentalsByDestination()).thenReturn(vacRentalsBydestination);
        VacationRentalItem item = (VacationRentalItem) this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getVrid().equals("38512"));
        // step 2
        item = (VacationRentalItem) this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getVrid().equals("22222"));
        Assert.assertNull(this.steps.execute("MIA", action));
    }

}
