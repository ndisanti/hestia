package com.despegar.p13n.hestia.recommend.allinone.item.closedpackage;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.WishList;
import com.despegar.p13n.euler.commons.client.model.WishListClosedPackageEntry;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Component
public class SearchClosedPackageItemStep
    extends BaseFunction
    implements ItemStep {

    @Override
    public ItemHome execute(String destination, ActionRecommendation action) {
        ItemHome item = this.getLikedClosedPackage(action);
        if (item == null) {
            UserActivity cpActivity = action.getSearchActivity().getActivity(Product.CLOSED_PACKAGES);
            if (cpActivity != null && cpActivity.getAction().getFlow().equals(Flow.DETAIL)
                && cpActivity.getDestination().equals(destination)) {
                item = this.getItemSearchDetailStep1(action, destination);
            } else {
                item = this.getItemSearchDefaultStep1(destination, action);
            }
        }
        return item;
    }

    private ItemHome getItemSearchDefaultStep1(String destination, ActionRecommendation action) {
        ItemHome item = this.getItemFromRanking(action, destination);
        return (item == null) ? this.getItemSearchDefaultStep2(destination, action) : item;
    }

    private ItemHome getItemSearchDefaultStep2(String destination, ActionRecommendation action) {
        ItemHome item = this.buildDestination(Product.CLOSED_PACKAGES, null, destination, action.getOrigin(), action);
        return ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.CLOSED_PACKAGES);
    }

    private ItemHome getItemFromRanking(ActionRecommendation action, String destination) {
        ClosedPackagesItem item = null;
        RankingTreeDTO ranking = this.getHotRankingService().getRankingFromIataNoFallback(Product.CLOSED_PACKAGES, destination,
            StaticRankingTypes.PACKAGE_DETAIL_DESTINATION, RANKING_SIZE);
        Iterator<RankingPositionDTO> it = ranking.getPodium().iterator();

        boolean found = false;
        while (it.hasNext() && !found) {
            RankingPositionDTO rankingItem = it.next();
            String id = rankingItem.getDestination();
            item = new ClosedPackagesItem(id);
            if (ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.CLOSED_PACKAGES) != null) {
                found = true;
            }
        }
        return found ? item : null;
    }

    private ItemHome getItemSearchDetailStep1(ActionRecommendation action, String destination) {
        ItemHome item = this.getViewedClosedPackage(destination, action);
        return (item == null) ? this.getItemSearchDetailStep2(action) : item;
    }

    private ItemHome getItemSearchDetailStep2(ActionRecommendation action) {
        return this.getRecommendationDetailDetail(action);
    }

    private ItemHome getRecommendationDetailDetail(ActionRecommendation action) {
        String itemId = action.getSearchActivity().getActivity(Product.CLOSED_PACKAGES).getProductBusinessId();

        Iterator<Recommendation> it = this.getRecommendation()
            .recommend(Product.CLOSED_PACKAGES, Flow.DETAIL, Product.CLOSED_PACKAGES, Flow.DETAIL, itemId, RANKING_SIZE)
            .iterator();
        boolean found = false;
        ClosedPackagesItem item = null;
        while (it.hasNext() && !found) {
            Recommendation pr = it.next();
            String id = pr.getRecommendation();
            item = new ClosedPackagesItem(id);
            if (ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.CLOSED_PACKAGES) != null) {
                found = true;
            }
        }
        return found ? item : null;
    }

    private ItemHome getViewedClosedPackage(String destination, ActionRecommendation action) {
        ClosedPackagesItem item = null;
        Collection<String> dataList = action.getSearchActivity().getLastClosedPackagesByDestination().get(destination);
        Iterator<String> it = dataList.iterator();
        boolean found = false;
        while (it.hasNext() && !found) {
            String cp = it.next();
            item = new ClosedPackagesItem(cp);
            if (ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.CLOSED_PACKAGES) != null) {
                found = true;
            }
        }
        return found ? item : null;
    }

    private ItemHome getLikedClosedPackage(ActionRecommendation action) {
        WishList wishList = action.getWishList().filterByProduct(Product.CLOSED_PACKAGES);
        ClosedPackagesItem item = null;
        Iterator<WishListClosedPackageEntry> it = wishList.getEntriesAs(WishListClosedPackageEntry.class).iterator();
        boolean found = false;
        while (it.hasNext() && !found) {
            WishListClosedPackageEntry cp = it.next();
            item = new ClosedPackagesItem(cp.getCluid());
            if (ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.CLOSED_PACKAGES) != null) {
                found = true;
            }
        }
        return found ? item : null;
    }

    @Override
    public String getDescription() {
        return "return a ClosedPackagesItem for users that didn't buy";
    }



}
