package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.google.common.collect.Lists;

@Deprecated
@Service
public class InternationalFunction
    extends BaseSectionFunction {


    public static List<String> rowDestinations = Lists.newArrayList("CUN", "MEX", "BUE", "RIO", "MIA", "NYC", "BOG", "PDP",
        "TUY", "ORL");

    public static List<String> starDestinations = Lists.newArrayList("RIO", "CUN", "BUE", "LIM");

    @Override
    public List<Offer> buildOffers(Product home, Product prToShow, ActionRecommendation action, Param param) {

        String destination = this.getOfferRandomDestination();
        ItemHome itemHome = this.buildDestination(prToShow, param.getRankingType(), destination, action.getOrigin(), action);

        return ItemUtils.buildOffers(home, itemHome, action, prToShow);
    }

    @Override
    public RowHome buildRow(Product home, Product prToShow, ActionRecommendation action, Param param) {

        List<ItemHome> destinations = Lists.newArrayList();
        for (String destination : rowDestinations) {
            ItemHome itemHome = this.buildDestination(prToShow, param.getRankingType(), destination, action.getOrigin(),
                action);
            if (itemHome != null) {
                destinations.add(itemHome);
            }
        }
        return new RowHome(null, destinations);
    }

    private String getOfferRandomDestination() {
        Random rand = new Random();
        int index = rand.nextInt(starDestinations.size());
        return starDestinations.get(index);
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.INTERNATIONAL;
    }

    @Override
    public String getDescription() {
        return "return random IATA for offer + harcoded IATA's for rows ";
    }

}
