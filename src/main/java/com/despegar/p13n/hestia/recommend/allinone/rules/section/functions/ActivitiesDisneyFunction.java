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
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ActivityItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.newrelic.api.agent.Trace;

@Service
public class ActivitiesDisneyFunction
    extends BaseSectionFunction {

    public static final String DISNEY = "DY_ORL";
    public static final String UNIVERSAL = "UN_ORL";
    public static final String INSURANCE = "INSURANCE";

    private static final ItemHome DISNEY_ITEM = new ActivityItem(DISNEY);
    private ActivityItemBuilder builder = new ActivityItemBuilder();

    @Override
    public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {
        ItemUtils.checkProduct(pr, Product.ACTIVITIES);
        ItemUtils
            .checkActivityType(action.getActivityType(), ActivityType.NO_HISTORY, ActivityType.SEARCH, ActivityType.BUY);

        return ItemUtils.buildOffers(home, DISNEY_ITEM, action, pr);
    }

    @Override
    public RowHome buildRow(Product home, Product pr, ActionRecommendation action, Param param) {
        ItemUtils.checkProduct(pr, Product.ACTIVITIES);
        ItemUtils
            .checkActivityType(action.getActivityType(), ActivityType.BUY, ActivityType.SEARCH, ActivityType.NO_HISTORY);

        Iterator<RankingItemDTO> it = this.createRanking(pr, action);

        return ItemUtils.buildRow(home, pr, action, param, it, this.builder);
    }

    @Trace
    private Iterator<RankingItemDTO> createRanking(Product pr, ActionRecommendation action) {
        List<RankingItemDTO> list = this.getRankingList(pr, action, StaticRankingTypes.ACTIVITIES_DETAIL_ACTID_DEST_HB);

        list.add(0, new RankingItemDTO(ActivitiesDisneyFunction.DISNEY));
        list.add(1, new RankingItemDTO(ActivitiesDisneyFunction.UNIVERSAL));

        return list.iterator();
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.ACTIVITIES_DISNEY;
    }

    @Override
    public String getDescription() {
        return "STAR: Disney (DY_ORL). "
            + "ROW: Disney (DY_ORL) + Universal (UN_ORL) + Ranking of Activities 'HB_' for user destination.";
    }
}
