package com.despegar.p13n.hestia.recommend.allinone.item.hotel;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;

@Component
public class DetailDestinationHotelStep
    extends BaseFunction
    implements ItemStep {

    @Autowired
    private HotelItemBuilderCommonsFunctions commons;


    @Override
    public ItemHome execute(String destination, ActionRecommendation action) {
        return this.getItemStep1(destination, action);
    }

    private ItemHome getItemStep1(String destination, ActionRecommendation action) {
        ItemHome item = this.getViewedHotel(destination, action);
        return (item == null) ? this.getItemStep2(action) : item;
    }

    private ItemHome getItemStep2(ActionRecommendation action) {
        return this.getRecommendationDetailDetail(action);
    }

    private ItemHome getRecommendationDetailDetail(ActionRecommendation action) {
        HotelItem item = null;
        String itemId = action.getSearchActivity().getActivity(Product.HOTELS).getProductBusinessId();
        Iterator<Recommendation> it = this.getRecommendation()
            .recommend(Product.HOTELS, Flow.DETAIL, Product.HOTELS, Flow.DETAIL, itemId, RANKING_SIZE).iterator();
        boolean found = false;
        while (it.hasNext() && !found) {
            Recommendation pr = it.next();
            String id = pr.getRecommendation();
            item = new HotelItem(id);
            if (this.commons.isHotelAvailable(item) && this.commons.isUnique(action, item)) {
                found = true;
            }
        }
        return found ? item : null;
    }



    private HotelItem getViewedHotel(String destination, ActionRecommendation action) {
        HotelItem item = null;
        Collection<String> dataList = action.getSearchActivity().getLastHotelsByDestination().get(destination);
        Iterator<String> it = dataList.iterator();
        boolean found = false;
        while (it.hasNext() && !found) {
            String hotel = it.next();
            item = new HotelItem(hotel);
            if (this.commons.isHotelAvailable(item) && this.commons.isUnique(action, item)) {
                found = true;
            }
        }
        return found ? item : null;
    }

    @Override
    public String getDescription() {
        return "return a HotelItem for users that reach hotels details on defined destination";
    }

}
