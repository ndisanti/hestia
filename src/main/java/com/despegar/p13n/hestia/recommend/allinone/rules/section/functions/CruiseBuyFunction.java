package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.client.RecommendationsClient;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.IteratorAdapter;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.google.common.base.Preconditions;
import com.newrelic.api.agent.Trace;

/**
 * <pre>If the user is located in the same destination of the cruise departure,
 *          we offer the first result from the recommendation thanks/thanks for flights or hotels
 *      else
 *          we offer a flight or hotel to the destination of the cruise departure.
 *</pre>
 */
@Service
public class CruiseBuyFunction
    extends BaseSectionFunction {

    private RecommendationsClient recommendation;

    @Override
    public List<Offer> buildOffers(Product home, Product prToShow, ActionRecommendation action, Param param) {

        ItemUtils.checkProduct(prToShow, Product.FLIGHTS, Product.HOTELS);
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.BUY);

        String userIata = action.getOrigin();

        // cruise destination
        String departureIata = action.getBuyActivity().getActivity().getDestination();

        // if origin and destination match, we get another destination from the ranking
        if (StringUtils.equals(userIata, departureIata)) {
            Iterator<RankingItemDTO> it = this.getRanking(prToShow, departureIata, action, action.getBuyActivity()
                .getProduct(), param);

            if (it.hasNext()) {
                String destination = it.next().getId();
                action.getTitleData().addDestination(destination);
                ItemHome itemHome = CruiseBuyFunction.this.buildDestination(prToShow, null, destination, userIata, action);
                return ItemUtils.buildOffers(home, itemHome, action, prToShow);

            } else {
                return null;
            }

        } else {
            ItemHome itemHome = CruiseBuyFunction.this.buildDestination(prToShow, null, departureIata, userIata, action);
            action.getTitleData().addDestination(departureIata);
            return ItemUtils.buildOffers(home, itemHome, action, prToShow);
        }

    }

    private Iterator<RankingItemDTO> getRanking(Product prOffer, String dest, ActionRecommendation action, Product prBought,
        Param param) {

        Flow flow2 = param.getFlow2();

        Preconditions.checkNotNull(flow2);

        Iterator<RankingItemDTO> it = this.createRanking(prBought, dest, prOffer, flow2);
        return it;
    }

    @Trace
    private Iterator<RankingItemDTO> createRanking(Product prBought, String dest, Product prOffer, Flow flow2) {

        final Iterator<Recommendation> it = this.recommendation.recommend(prBought, Flow.THANKS, prOffer, flow2, dest,
            BaseFunction.RANKING_SIZE).iterator();

        return new IteratorAdapter<Recommendation, RankingItemDTO>(it) {
            @Override
            public RankingItemDTO convert(Recommendation f) {
                return new RankingItemDTO(f.getRecommendation());
            }
        }.newIterator();

    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.CRUISE_BUY;
    }

    @Override
    public String getDescription() {
        return "Se usa cuando un usuario compro un Crucero. Se le ofrece hoteles o vuelos solo para STAR."
            + "Si el usuario es localizado en el mismo destino que el cruise departure, ofrecemos el"
            + " primer resultado de la recomendacion thanks/thanks para flights o hotels. "
            + "Si no, ofrecemos un vuelos o hotel para el destino del cruise departure.";
    }

}
