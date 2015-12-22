package com.despegar.p13n.hestia.recommend.allinone.item.closedpackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;

@Component
public class ClosedPackagesItemSteps
    implements ItemStep {

	@Autowired
    private BuyClosedPackageItemStep buyStep;
	@Autowired
	private SearchClosedPackageItemStep searchStep;

    
    @Override
    public ItemHome execute(String destination, ActionRecommendation action) {
        if (action.getBuyActivity() != null) {
            return this.buyStep.execute(destination, action);
        } else {
            return this.searchStep.execute(destination, action);
        }
    }
}
