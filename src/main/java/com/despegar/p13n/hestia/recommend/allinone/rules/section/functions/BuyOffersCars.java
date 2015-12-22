package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.EnumSet;
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
import com.google.common.base.Preconditions;

@Service
public class BuyOffersCars
    extends BaseSectionFunction {

    @Override
    public List<Offer> buildOffers(Product home, Product prToOffer, ActionRecommendation action, Param param) {

        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.BUY);

        BuyActivity buy = action.getBuyActivity();

        Preconditions.checkArgument(EnumSet.of(Product.FLIGHTS, Product.COMBINED_PRODUCTS).contains(buy.getProduct()));

        String destination = buy.getActivity().getDestination();

        action.getTitleData().addDestination(destination);

        ItemHome itemHome = this.buildDestination(Product.CARS, null, destination, null, action);

        return ItemUtils.buildOffers(home, itemHome, action, prToOffer);
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.BUY_OFFER_CARS;
    }

    @Override
    public String getDescription() {
        return "Compró Vuelo o Combined Products. Se ofrece autos en el destino que compró";
    }
}