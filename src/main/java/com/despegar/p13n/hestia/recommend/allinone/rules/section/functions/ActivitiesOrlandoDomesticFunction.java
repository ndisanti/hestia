package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ActivityDestinationItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ActivityEntityDestinationBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.newrelic.api.agent.Trace;

@Service
public class ActivitiesOrlandoDomesticFunction
    extends BaseSectionFunction {

    private static final String UNIVERSAL = "UN_ORL";
    public static final String DISNEY = "DY_ORL";
    private static final String ORL = "ORL";
    private static final ItemHome ORLANDO = new ActivityDestinationItem(ORL);

    private ActivityEntityDestinationBuilder builder = new ActivityEntityDestinationBuilder();



    @Override
    public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {
        ItemUtils.checkProduct(pr, Product.ACTIVITIES);
        return ItemUtils.buildOffers(home, ORLANDO, action, pr);
    }

    @Override
    public RowHome buildRow(Product home, Product pr, ActionRecommendation action, Param param) {
        ItemUtils.checkProduct(pr, Product.ACTIVITIES);
        Iterator<RankingItemDTO> it = this.createRanking(pr, action);
        return ItemUtils.buildRow(home, pr, action, param, it, this.builder);
    }


    @Trace
    private Iterator<RankingItemDTO> createRanking(Product pr, ActionRecommendation action) {
        List<RankingItemDTO> list = this.getRankingList(pr, action, StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC);
        list.add(0, new RankingItemDTO(DISNEY));
        list.add(1, new RankingItemDTO(UNIVERSAL));
        return list.iterator();
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.ACTIVITIES_ORL_DOMESTIC;
    }

    @Override
    public String getDescription() {
        return "OFFER: Orlando destination, ROW: Disney + Orlando + Domestic Activities Ranking ";
    }

}
