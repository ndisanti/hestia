package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.CruiseRegionItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;

/**
 * Region ranking by user ip
 */
@Service
public class CruiseRankingIpFunction
    extends BaseSectionFunction {

    private CruiseItemBuilder itemBuilder = new CruiseItemBuilder();


    @Override
    public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {
        ItemUtils.checkProduct(pr, Product.CRUISES);
        ItemUtils
            .checkActivityType(action.getActivityType(), ActivityType.NO_HISTORY, ActivityType.SEARCH, ActivityType.BUY);

        action.getTitleData().addDestination(action.getOrigin());

        Iterator<RankingItemDTO> it = this.getRankingIterator(pr, action, StaticRankingTypes.CRUISE_REGIONS);

        return ItemUtils.buildOffers(home, pr, action, param, it, this.itemBuilder);
    }

    @Override
    public RowHome buildRow(Product home, Product pr, ActionRecommendation action, Param param) {

        ItemUtils.checkProduct(pr, Product.CRUISES);
        ItemUtils
            .checkActivityType(action.getActivityType(), ActivityType.NO_HISTORY, ActivityType.SEARCH, ActivityType.BUY);

        action.getTitleData().addDestination(action.getOrigin());

        Iterator<RankingItemDTO> it = this.getRankingIterator(pr, action, StaticRankingTypes.CRUISE_REGIONS);

        return ItemUtils.buildRow(home, pr, action, param, it, this.itemBuilder);
    }


    private static class CruiseItemBuilder
        implements ItemBuilder {

        @Override
        public ItemHome buildItem(Product home, Product prOffer, ActionRecommendation action, Param param, RankingItemDTO dto) {
            return new CruiseRegionItem(dto.getId());
        }
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.CRUISE_RANKING_IP;
    }

    @Override
    public String getDescription() {
        return "Ranking de regions por ip del usuario";
    }

}
