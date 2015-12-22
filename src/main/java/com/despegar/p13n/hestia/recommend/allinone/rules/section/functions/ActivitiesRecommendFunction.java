package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.client.RecommendationsClient;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.IteratorAdapter;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ActivityItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.google.common.base.Preconditions;
import com.newrelic.api.agent.Trace;

@Service
public class ActivitiesRecommendFunction
    extends BaseSectionFunction {

    @Autowired
    private RecommendationsClient recommendation;

    private ActivityItemBuilder builder = new ActivityItemBuilder();

    @Override
    public RowHome buildRow(Product home, Product prOffer, ActionRecommendation action, Param param) {

        ItemUtils.checkProduct(prOffer, Product.ACTIVITIES);
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.BUY);

        Preconditions.checkNotNull(action.getBuyActivity());
        Preconditions.checkArgument(action.getBuyActivity().getActivity().getProduct() == Product.ACTIVITIES);

        String activityid = action.getBuyActivity().getActivity().getProductBusinessId();

        String destination = action.getBuyActivity().getActivity().getDestination();
        action.getTitleData().addDestination(destination);

        Iterator<RankingItemDTO> it = this.createRanking(prOffer, activityid, param);

        return ItemUtils.buildRow(home, prOffer, action, param, it, this.builder);

    }


    @Trace
    private Iterator<RankingItemDTO> createRanking(Product prOffer, String activityId, Param param) {

        List<Recommendation> list = this.recommendation.recommend(Product.ACTIVITIES, Flow.DETAIL, prOffer, Flow.DETAIL,
            activityId, BaseFunction.RANKING_SIZE);

        Iterator<Recommendation> it = list.iterator();

        return new IteratorAdapter<Recommendation, RankingItemDTO>(it) {
            @Override
            public RankingItemDTO convert(Recommendation f) {
                return new RankingItemDTO(f.getRecommendation());
            }
        }.newIterator();

    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.ACTIVITIES_RECOMMENDER;
    }

      @Override
    public String getDescription() {
        return "Insurance + Recomendador de actividades DETAIL/DETAIL.";
    }

}
