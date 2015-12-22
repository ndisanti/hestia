package com.despegar.p13n.hestia.recommend.allinone.item.cruise;

import java.util.Iterator;

import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.CruiseItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Component
public class CruiseCommonsFunctions
    extends BaseFunction {

    private static final Integer RANKING_LIMIT = 9;

    @Override
    public String getDescription() {
        return "Cruises Steps commons functions";
    }

    public ItemHome getItemFromRanking(String destination, ActionRecommendation action) {
        CruiseItem item = null;
        RankingTreeDTO ranking = this.getHotRankingService().getRankingFromIataNoFallback(Product.CRUISES, destination,
            StaticRankingTypes.CRUISE_DETAIL_DESTINATION, RANKING_LIMIT);
        Iterator<RankingPositionDTO> it = ranking.getPodium().iterator();

        boolean found = false;
        while (it.hasNext() && !found) {
            RankingPositionDTO rankingItem = it.next();
            String id = rankingItem.getDestination();
            item = new CruiseItem(id);
            if (ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.CRUISES) != null) {
                found = true;
            }
        }
        return found ? item : null;
    }
}
