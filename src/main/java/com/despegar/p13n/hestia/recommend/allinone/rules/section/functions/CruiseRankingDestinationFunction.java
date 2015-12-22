package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.data.CruiseData;
import com.despegar.p13n.euler.commons.client.model.data.ProductData;
import com.despegar.p13n.hestia.api.data.model.CruiseItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.IteratorAdapter;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.google.common.base.Preconditions;

/** 
 * If cruise detail: ranking did in destination
 * If cruise search: ranking did in top iata from region (region-> best iata-> ranking did)
 * else, ranking did hot by ip for cruises
 */
@Service
public class CruiseRankingDestinationFunction
    extends BaseSectionFunction {

    @Autowired
    private GeoService geoService;

    @Override
    public RowHome buildRow(Product home, Product pr, ActionRecommendation action, Param param) {

        ItemUtils.checkProduct(pr, Product.CRUISES);
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH);

        Iterator<RankingItemDTO> it = this.buildRanking(pr, action);

        return ItemUtils.buildRow(home, pr, action, param, it, new ItemBuilder() {

            @Override
            public ItemHome buildItem(Product home, Product prOffer, ActionRecommendation action, Param param,
                RankingItemDTO dto) {

                return new CruiseItem(dto.getId());
            }
        });
    }


    private Iterator<RankingItemDTO> buildRanking(Product prOffer, ActionRecommendation action) {

        StaticRankingTypes rankingType = null;
        String destination = null;

        Product product = action.getActivityProduct(prOffer);
        Flow flow = action.getActivityFlow(prOffer);
        Iterator<RankingItemDTO> it = null;

        boolean cruiseActivity = action.getSearchActivity().isActivityFor(Product.CRUISES);

        if (ItemUtils.isDetailOrCheckout(flow) && product == Product.CRUISES && cruiseActivity) {
            rankingType = StaticRankingTypes.CRUISE_DETAIL_DESTINATION;
            destination = action.getSearchActivity().getActivity(Product.CRUISES).getDestination();
            action.getTitleData().addDestination(destination);
            Preconditions.checkNotNull(destination);
            it = this.getRankingByDestination(action, destination, prOffer, rankingType);
        } else if (ItemUtils.isSearch(flow) && product == Product.CRUISES && cruiseActivity) {
            CruiseData cruiseData = ProductData.create(action.getSearchActivity().getActivity(Product.CRUISES).getAction());
            String region = cruiseData.region();
            destination = this.geoService.getIatasByRegion(action.getCountryCode(), region).get(0);
            action.getTitleData().addDestination(destination);;
            rankingType = StaticRankingTypes.CRUISE_DETAIL_DESTINATION;
            it = this.getRankingByDestination(action, destination, prOffer, rankingType);
        } else {
            rankingType = StaticRankingTypes.CRUISE_DETAIL_DESTINATION;
            it = this.getRankingIterator(prOffer, action, rankingType);

            action.getTitleData().addDestination(action.getOrigin());
        }

        return it;
    }



    private Iterator<RankingItemDTO> getRankingByDestination(ActionRecommendation action, String dest, Product prToShow,
        StaticRankingTypes type) {

        action.getTitleData().addDestination(dest);

        final Iterator<RankingPositionDTO> it = this.getRankingIata(prToShow, dest, type).getPodium().iterator();

        return new IteratorAdapter<RankingPositionDTO, RankingItemDTO>(it) {
            @Override
            public RankingItemDTO convert(RankingPositionDTO f) {
                return new RankingItemDTO(f.getDestination());
            }
        }.newIterator();
    }


    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.CRUISE_RANKING_DESTINATION;
    }

    @Override
    public String getDescription() {
        return "Si llego a detail cruceros, ranking did en el destino. "
            + "Si llego a search cruceros, ranking did en el top iata de la region (region-> best iata-> ranking did). "
            + "Si no, ranking hot de did by ip de cruceros";
    }

}
