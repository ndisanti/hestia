package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserAction;
import com.despegar.p13n.euler.commons.client.model.data.CruiseData;
import com.despegar.p13n.euler.commons.client.model.data.ProductData;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.IteratorAdapter;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;

/** 
 * ranking destination within region
 */
@Service
public class RankingCruiseRegionFunction
    extends BaseSectionFunction {

    @Autowired
    private GeoService geoService;

    private RankingItemBuilder itemBuilder = new RankingItemBuilder();

    @Override
    public RowHome buildRow(Product home, Product prToShow, ActionRecommendation action, Param param) {

        ItemUtils.checkProduct(prToShow, Product.HOTELS, Product.FLIGHTS);
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH);


        if (!action.getSearchActivity().isActivityFor(Product.CRUISES)) {
            return null;
        }

        UserActivity activity = action.getSearchActivity().getActivity(Product.CRUISES);

        UserAction userAction = activity.getAction();

        CruiseData cruiseData = ProductData.create(userAction);

        Iterator<RankingItemDTO> it = this.getRankingByRegion(cruiseData.region(), action);

        action.getTitleData().addDestination(activity.getDestination());

        return ItemUtils.buildRow(home, prToShow, action, param, it, this.itemBuilder);

    }

    private class RankingItemBuilder
        implements ItemBuilder {

        @Override
        public ItemHome buildItem(Product home, Product prOffer, ActionRecommendation action, Param param, RankingItemDTO dto) {
            return RankingCruiseRegionFunction.this.buildDestination(prOffer, null, dto.getId(), action.getOrigin(), action);
        }
    }

    private Iterator<RankingItemDTO> getRankingByRegion(String region, ActionRecommendation action) {

        final Iterator<String> it = this.geoService.getIatasByRegion(action.getCountryCode(), region).iterator();

        return new IteratorAdapter<String, RankingItemDTO>(it) {
            @Override
            public RankingItemDTO convert(String f) {
                return new RankingItemDTO(f);
            }
        }.newIterator();

    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.RANKING_CRUISE_REGION;
    }

    @Override
    public String getDescription() {
        return "Ranking de destino para la region si busco Cruceros. "//
            + "Si no busco Cruceros, defaultea.";
    }
}
