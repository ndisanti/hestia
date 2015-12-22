package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.google.common.base.Preconditions;

@Component
public class DetailItemBuilder
    implements ItemBuilder {

    @Autowired
    HotelItemBuilder hotelBuilder;

    @Override
    public ItemHome buildItem(Product home, Product prOffer, ActionRecommendation action, Param param, RankingItemDTO dto) {

        Preconditions.checkArgument(RecommendFunction.DETAIL_PRODUCTS.contains(prOffer));

        String id = dto.getId();
        Preconditions.checkNotNull(id);

        switch (prOffer) {
        case HOTELS:
            return this.hotelBuilder.buildItem(home, prOffer, action, param, dto);
        case ACTIVITIES:
            return new ActivityItem(id);
        default:
            throw new UnsupportedOperationException("Cant handle " + prOffer);
        }
    }

}
