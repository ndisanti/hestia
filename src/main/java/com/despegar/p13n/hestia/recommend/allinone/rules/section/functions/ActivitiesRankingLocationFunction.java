package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ActivityItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.newrelic.api.agent.Trace;

@Service
public class ActivitiesRankingLocationFunction
    extends BaseSectionFunction {

    private ActivityItemBuilder builder = new ActivityItemBuilder();


    @Override
    public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {

        ItemUtils.checkProduct(pr, Product.ACTIVITIES);
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.BUY);

        // use the first item of the ranking
        Iterator<RankingItemDTO> it = this.createStarRanking(pr, action);

        return ItemUtils.buildOffers(home, pr, action, param, it, this.builder);
    }

    @Override
    public RowHome buildRow(Product home, Product pr, ActionRecommendation action, Param param) {
        ItemUtils.checkProduct(pr, Product.ACTIVITIES);
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.BUY, ActivityType.SEARCH);

        Iterator<RankingItemDTO> it = this.createRankingRow(pr, action);

        return ItemUtils.buildRow(home, pr, action, param, it, this.builder);
    }

    @Trace
    private Iterator<RankingItemDTO> createStarRanking(Product pr, ActionRecommendation action) {
        List<RankingItemDTO> list = this.getRankingList(pr, action, StaticRankingTypes.ACTIVITIES_DETAIL_ACTID_DEST_HB);
        return list.iterator();
    }

    @Trace
    private Iterator<RankingItemDTO> createRankingRow(Product pr, ActionRecommendation action) {
        List<RankingItemDTO> list = this.getRankingList(pr, action, StaticRankingTypes.ACTIVITIES_DETAIL_ACTID_DEST_HB);
        return list.iterator();
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.ACTIVITIES_RANKING_LOCATION;
    }

    @Override
    public String getDescription() {
        return "STAR: First of Activities 'HB_' Ranking for user destination "
            + "ROW: Insurance + Activities 'HB_' Ranking for user destination";
    }

}
