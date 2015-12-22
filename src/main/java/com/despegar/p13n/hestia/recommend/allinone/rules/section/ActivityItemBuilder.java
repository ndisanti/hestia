package com.despegar.p13n.hestia.recommend.allinone.rules.section;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.google.common.base.Preconditions;

public class ActivityItemBuilder
    implements ItemBuilder {

    @Override
    public ItemHome buildItem(Product home, Product prOffer, ActionRecommendation action, Param param, RankingItemDTO dto) {

        Preconditions.checkArgument(prOffer == Product.ACTIVITIES);

        String hid = dto.getId();

        Preconditions.checkNotNull(hid);
        return new ActivityItem(dto.getId());
    }

}
