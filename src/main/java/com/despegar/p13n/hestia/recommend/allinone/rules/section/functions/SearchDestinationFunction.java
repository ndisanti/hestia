package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.List;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Service
public class SearchDestinationFunction
    extends BaseSectionFunction {

    @Override
    public List<Offer> buildOffers(Product home, Product prToOffer, ActionRecommendation action, Param param) {

        home = param.getPr1() != null ? param.getPr1() : home;

        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH, ActivityType.BUY);

        SearchActivity search = action.getSearchActivity();

        if (search == null) {
            return null;
        }
        String destination = search.getActivityOrLast(prToOffer).getDestination();

        action.getTitleData().addDestination(destination);

        // origin and destination are equals for flights, so other default rule will handle this
        if (!ItemUtils.isOriginOk(prToOffer, action.getOrigin(), destination)) {
            return null;
        }

        ItemHome itemHome = this
            .buildDestination(prToOffer, param.getRankingType(), destination, action.getOrigin(), action);

        return ItemUtils.buildOffers(home, itemHome, action, prToOffer);
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.SEARCH_DESTINATION;
    }

    @Override
    public String getDescription() {
        return "Destino buscado";
    }
}