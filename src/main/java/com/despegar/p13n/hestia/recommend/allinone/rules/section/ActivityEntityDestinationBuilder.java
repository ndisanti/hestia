package com.despegar.p13n.hestia.recommend.allinone.rules.section;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ActivityDestinationItem;
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;

public class ActivityEntityDestinationBuilder
    implements ItemBuilder {

    private static final String INSURANCE = "INSURANCE";
    private static final String ORLANDO = "UN_ORL";
    private static final String DISNEY = "DY_ORL";

    @Override
    public ItemHome buildItem(Product home, Product prOffer, ActionRecommendation action, Param param, RankingItemDTO dto) {

        String id = dto.getId();

        if (id.equals(INSURANCE) || id.equals(ORLANDO) || id.equals(DISNEY)) {
            return new ActivityItem(id);
        } else {
            return new ActivityDestinationItem(id);
        }
    }
}
