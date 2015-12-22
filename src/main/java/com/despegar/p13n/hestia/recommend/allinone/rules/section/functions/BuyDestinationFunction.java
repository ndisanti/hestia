package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.List;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.BuyActivity;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Service
public class BuyDestinationFunction
    extends BaseSectionFunction {

    @Override
    public List<Offer> buildOffers(Product home, Product prToOffer, ActionRecommendation action, Param param) {

        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.BUY);

        BuyActivity buy = action.getBuyActivity();

        String destination = buy.getActivity().getDestination();

        action.getTitleData().addDestination(destination);

        ItemHome itemHome = BuyDestinationFunction.this.buildDestination(prToOffer, param.getRankingType(), destination,
            action.getOrigin(), action);

        return ItemUtils.buildOffers(home, itemHome, action, prToOffer);
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.BUY_DESTINATION;
    }

    @Override
    public String getDescription() {
        return "Destino comprado.";
    }
}