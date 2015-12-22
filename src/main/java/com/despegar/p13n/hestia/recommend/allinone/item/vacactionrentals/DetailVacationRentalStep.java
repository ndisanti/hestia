package com.despegar.p13n.hestia.recommend.allinone.item.vacactionrentals;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.VacationRentalItem;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Component
public class DetailVacationRentalStep
    extends BaseFunction
    implements ItemStep {

    @Override
    public ItemHome execute(String destination, ActionRecommendation action) {
        return this.getItemStep1(destination, action);
    }

    private ItemHome getItemStep1(String destination, ActionRecommendation action) {
        ItemHome item = this.getViewedVacationRentals(destination, action);
        return (item == null) ? this.getItemStep2(action) : item;
    }

    private ItemHome getItemStep2(ActionRecommendation action) {
        return this.getRecommendationDetailDetail(action);
    }

    private ItemHome getRecommendationDetailDetail(ActionRecommendation action) {
        VacationRentalItem item = null;
        String itemId = action.getSearchActivity().getActivity(Product.VACATIONRENTALS).getProductBusinessId();
        Iterator<Recommendation> it = this.getRecommendation()
            .recommend(Product.VACATIONRENTALS, Flow.DETAIL, Product.VACATIONRENTALS, Flow.DETAIL, itemId, RANKING_SIZE)
            .iterator();
        boolean found = false;
        while (it.hasNext() && !found) {
            Recommendation pr = it.next();
            String id = pr.getRecommendation();
            item = new VacationRentalItem(id);
            if (ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.VACATIONRENTALS) != null) {
                found = true;
            }
        }
        return found ? item : null;
    }

    private ItemHome getViewedVacationRentals(String destination, ActionRecommendation action) {
        VacationRentalItem item = null;
        Collection<String> dataList = action.getSearchActivity().getLastVacationRentalsByDestination().get(destination);
        Iterator<String> it = dataList.iterator();
        boolean found = false;
        while (it.hasNext() && !found) {
            String vacRental = it.next();
            item = new VacationRentalItem(vacRental);
            if (ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.VACATIONRENTALS) != null) {
                found = true;
            }
        }
        return found ? item : null;
    }

    @Override
    public String getDescription() {
        return "return a VacationRentalItems for users that reach VacationRental details on defined destination";
    }

}
