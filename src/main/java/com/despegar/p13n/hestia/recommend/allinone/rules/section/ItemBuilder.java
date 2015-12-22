package com.despegar.p13n.hestia.recommend.allinone.rules.section;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;

public interface ItemBuilder {

    ItemHome buildItem(Product home, Product prOffer, ActionRecommendation action, Param param, RankingItemDTO dto);

}
