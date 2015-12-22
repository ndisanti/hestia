package com.despegar.p13n.hestia.recommend.allinone.item.vacactionrentals;

import java.util.Iterator;

import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.api.data.model.VacationRentalItem;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Component
public class DefaultVacationRentalStep
    extends BaseFunction
    implements ItemStep {

    @Override
    public ItemHome execute(String destination, ActionRecommendation action) {
        return this.getItemStep1(action, destination);
    }

    private ItemHome getItemStep1(ActionRecommendation action, String destination) {
        ItemHome item = this.buildDestination(Product.VACATIONRENTALS, null, destination, action.getOrigin(), action);
        item = ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.VACATIONRENTALS);
        return (item == null) ? this.getItemStep2(destination, action) : item;
    }

    private ItemHome getItemStep2(String destination, ActionRecommendation action) {
        return this.getItemFromRanking(action, destination);
    }

    private ItemHome getItemFromRanking(ActionRecommendation action, String destination) {
        VacationRentalItem item = null;
        RankingTreeDTO ranking = this.getHotRankingService().getRankingFromIataNoFallback(Product.VACATIONRENTALS, destination,
            StaticRankingTypes.VACATIONS_RENTALS_VRID_BY_DESTINATION, RANKING_SIZE);
        Iterator<RankingPositionDTO> it = ranking.getPodium().iterator();

        boolean found = false;
        while (it.hasNext() && !found) {
            RankingPositionDTO rankingItem = it.next();
            String id = rankingItem.getDestination();
            item = new VacationRentalItem(id);
            if (ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.VACATIONRENTALS) != null) {
                found = true;
            }
        }
        return found ? item : null;
    }

    @Override
    public String getDescription() {
        return "return a VacationRentalItem for users that hasn't reach vacation rentals details on defined destination";
    }

}
