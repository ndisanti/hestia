package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.client.RecommendationsClient;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
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
public class ActivitiesViewedFunction
    extends BaseSectionFunction {

    @Autowired
    private RecommendationsClient recommendation;

    private ActivityItemBuilder builder = new ActivityItemBuilder();


    @Override
    public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {

        String actid = this.getActid(home, pr, action);

        if (actid == null) {
            return null;
        }

        return ItemUtils.buildOffers(home, new ActivityItem(actid), action, pr);

    }

    private String getActid(Product home, Product pr, ActionRecommendation action) {
        ItemUtils.checkProduct(pr, Product.ACTIVITIES);
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH);

        SearchActivity search = action.getSearchActivity();

        if (search.isActivityFor(Product.ACTIVITIES) && search.getActivity(Product.ACTIVITIES).isDetailOrCheckout()) {
            String actid = search.getActivity(Product.ACTIVITIES).getProductBusinessId();
            Preconditions.checkArgument(StringUtils.isNotBlank(actid), "Empty actid");
            return actid;
        } else {
            return null;
        }
    }

    @Override
    public RowHome buildRow(Product home, Product pr, ActionRecommendation action, Param param) {

        String actid = this.getActid(home, pr, action);

        if (actid == null) {
            return null;
        }

        Iterator<RankingItemDTO> it = this.createRanking(pr, Flow.DETAIL, pr, Flow.DETAIL, actid);

        return ItemUtils.buildRow(home, pr, action, param, it, this.builder);
    }

    @Trace
    private Iterator<RankingItemDTO> createRanking(Product pr1, Flow flow1, Product prOffer, Flow flow2, String item) {

        List<Recommendation> list = this.recommendation.recommend(pr1, flow1, prOffer, flow2, item,
            BaseFunction.RANKING_SIZE);

        float value = 0F;
        Recommendation r = new Recommendation(item, value);

        if (!list.isEmpty()) {
            value = 1F;
        }
        list = new ArrayList<>(list);

        list.add(0, r);

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
        return SectionFunctionCode.ACTIVITIES_VIEWED;
    }

    @Override
    public String getDescription() {
        return "STAR: Last viewed actid." + "ROW: Last viewed actid and recommendation ACTIVITIES/ACTIVITIES DETAIL/DETAIL";
    }

}
