package com.despegar.p13n.hestia.recommend.allinone.item.activity;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Component
public class ActivityStepsCommonsFunctions
    extends BaseFunction {

    public ActivityItem getItemFromRanking(ActionRecommendation action, String destination) {
        ActivityItem item = null;
        RankingTreeDTO ranking = this.getHotRankingService().getRankingFromIataNoFallback(Product.ACTIVITIES, destination,
            StaticRankingTypes.ACTIVITIES_DETAIL_ACTID_DEST, RANKING_SIZE);
        Iterator<RankingPositionDTO> it = ranking.getPodium().iterator();

        boolean found = false;
        while (it.hasNext() && !found) {
            RankingPositionDTO rankingItem = it.next();
            String id = rankingItem.getDestination();
            item = new ActivityItem(id);
            if (ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.ACTIVITIES) != null) {
                found = true;
            }
        }
        return found ? item : null;
    }

    public List<RankingItemDTO> getDestinationRanking(ActionRecommendation action) {
        return this.getRankingList(Product.ACTIVITIES, action, StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC);
    }

    @Override
    public String getDescription() {
        return "commons functions for ActivityItem Steps";
    }
}
