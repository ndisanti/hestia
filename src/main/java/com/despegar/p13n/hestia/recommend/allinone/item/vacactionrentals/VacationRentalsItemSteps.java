package com.despegar.p13n.hestia.recommend.allinone.item.vacactionrentals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;

@Component
public class VacationRentalsItemSteps
    implements ItemStep {

    @Autowired
    private DetailVacationRentalStep detailVacationRentalStep;
    @Autowired
    private DefaultVacationRentalStep defVacationRentalStep;

    @Override
    public ItemHome execute(String destination, ActionRecommendation action) {
        SearchActivity activity = action.getSearchActivity();
        if (activity != null) {
            if (activity.getActivity(Product.VACATIONRENTALS) != null
                && activity.getActivity(Product.VACATIONRENTALS).getFlow().equals(Flow.DETAIL)) {
                return this.detailVacationRentalStep.execute(destination, action);
            }
        }
        return this.defVacationRentalStep.execute(destination, action);
    }
}
