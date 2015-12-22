package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.hotel.HotelItemBuilderCommonsFunctions;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;

@Component
public class HotelItemBuilder
    implements ItemBuilder {

    @Autowired
    private HotelItemBuilderCommonsFunctions commons;

    @Override
    public ItemHome buildItem(Product home, Product prOffer, ActionRecommendation action, Param param, RankingItemDTO dto) {

        HotelItem item = new HotelItem(dto.getId());;
        return this.commons.isHotelAvailable(item) ? item : null;
    }
}
