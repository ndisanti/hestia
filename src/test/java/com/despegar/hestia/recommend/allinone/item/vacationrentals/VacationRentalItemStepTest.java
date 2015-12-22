package com.despegar.hestia.recommend.allinone.item.vacationrentals;

import org.junit.Assert;

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
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.VacationRentalItem;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.vacactionrentals.DefaultVacationRentalStep;
import com.despegar.p13n.hestia.recommend.allinone.item.vacactionrentals.DetailVacationRentalStep;
import com.despegar.p13n.hestia.recommend.allinone.item.vacactionrentals.VacationRentalsItemSteps;

@RunWith(MockitoJUnitRunner.class)
public class VacationRentalItemStepTest {

    @InjectMocks
    private VacationRentalsItemSteps steps = new VacationRentalsItemSteps();
    @Mock
    private DetailVacationRentalStep detailSteps;
    @Mock
    private DefaultVacationRentalStep defSteps;


    @Test
    public void testGetItemForDefSteps() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.VACATIONRENTALS, "MIA");
        ItemHome vrItem = new VacationRentalItem("12345676");
        Mockito.when(this.defSteps.execute("MIA", action)).thenReturn(vrItem);
        VacationRentalItem item = (VacationRentalItem) this.steps.execute("MIA", action);
        Mockito.verify(this.defSteps).execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.equals(vrItem));
    }

    @Test
    public void testGetItemForDefSteps2() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.VACATIONRENTALS, "MIA");
        SearchActivity searchActivity = new SearchActivity();
        UserAction userAction = new UserAction();
        userAction.setFlow(Flow.SEARCH);
        UserActivity lastActivity = new UserActivity(0, "MIA", "12345678789", 0, userAction, null);
        searchActivity.addSearchActivity(Product.VACATIONRENTALS, lastActivity);
        action.setSearchActivity(searchActivity);
        ItemHome vrItem = new VacationRentalItem("12345676");
        Mockito.when(this.defSteps.execute("MIA", action)).thenReturn(vrItem);
        VacationRentalItem item = (VacationRentalItem) this.steps.execute("MIA", action);
        Mockito.verify(this.defSteps).execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.equals(vrItem));
    }

    @Test
    public void testGetItemForDetailSteps() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.VACATIONRENTALS, "MIA");
        SearchActivity searchActivity = new SearchActivity();
        UserAction userAction = new UserAction();
        userAction.setFlow(Flow.DETAIL);
        UserActivity lastActivity = new UserActivity(0, "MIA", "12345678789", 0, userAction, null);
        searchActivity.addSearchActivity(Product.VACATIONRENTALS, lastActivity);
        action.setSearchActivity(searchActivity);
        ItemHome vrItem = new VacationRentalItem("12345676");
        Mockito.when(this.detailSteps.execute("MIA", action)).thenReturn(vrItem);
        VacationRentalItem item = (VacationRentalItem) this.steps.execute("MIA", action);
        Mockito.verify(this.detailSteps).execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.equals(vrItem));
    }
}
