package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.City;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ActivityItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.google.common.collect.Lists;
import com.newrelic.api.agent.Trace;

@Service
public class ActivitiesUniversalFunction
    extends BaseSectionFunction {

    public static final String UNIVERSAL = "UN_ORL";
    private static final ItemHome UNIVERSAL_ITEM = new ActivityItem(UNIVERSAL);
    public static final String INSURANCE = "INSURANCE";

    private ActivityItemBuilder builder = new ActivityItemBuilder();

    @Override
    public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {
        ItemUtils.checkProduct(pr, Product.ACTIVITIES);
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH);

        return ItemUtils.buildOffers(home, UNIVERSAL_ITEM, action, pr);
    }

    @Override
    public RowHome buildRow(Product home, Product pr, ActionRecommendation action, Param param) {
        ItemUtils.checkProduct(pr, Product.ACTIVITIES);
        ItemUtils
            .checkActivityType(action.getActivityType(), ActivityType.NO_HISTORY, ActivityType.SEARCH, ActivityType.BUY);

        Iterator<RankingItemDTO> it = this.createRanking(pr, action, param);

        return ItemUtils.buildRow(home, pr, action, param, it, this.builder);
    }

    @Trace
    private Iterator<RankingItemDTO> createRanking(Product pr, ActionRecommendation action, Param param) {

        List<RankingItemDTO> list = Lists.newLinkedList();

        if (param.getCity() != null && param.getCity().equals(City.ORLANDO)) {
            list = this.getRankingList(pr, action, StaticRankingTypes.ACTIVITIES_DETAIL_ACTID_HB_ORL);
        }

        list = list.isEmpty() ? this.getRankingList(pr, action, StaticRankingTypes.ACTIVITIES_DETAIL_ACTID_HB) : list;
        list.add(0, new RankingItemDTO(ActivitiesUniversalFunction.UNIVERSAL));
        return list.iterator();
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.ACTIVITIES_UNIVERSAL;
    }

    @Override
    public String getDescription() {
        return "STAR: Universal (UN_ORL). "
            + "ROW: Universal (UN_ORL) + Hot Ranking of Activities 'HB_' for user location based on IP";
    }

}
