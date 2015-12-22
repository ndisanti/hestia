package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.client.DestinationsClient;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.IteratorAdapter;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Service
public class NearbyBuyDestinationFunction
    extends BaseSectionFunction {

    @Autowired
    private DestinationsClient destinationService;

    private NearbyItemBuilder itemBuilder = new NearbyItemBuilder();

    @Override
    public List<Offer> buildOffers(Product home, Product prOffer, ActionRecommendation action, Param param) {

        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.BUY);

        String dest = action.getBuyActivity().getActivity().getDestination();

        action.getTitleData().addDestination(dest);

        Iterator<RankingItemDTO> it = this.getRanking(prOffer, dest, action, action.getBuyActivity().getProduct());

        return ItemUtils.buildOffers(home, prOffer, action, param, it, this.itemBuilder);
    }

    @Override
    public RowHome buildRow(Product home, Product prOffer, ActionRecommendation action, Param param) {

        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.BUY);

        String dest = action.getBuyActivity().getActivity().getDestination();

        action.getTitleData().addDestination(dest);

        Iterator<RankingItemDTO> it = this.getRanking(prOffer, dest, action, action.getBuyActivity().getProduct());

        return ItemUtils.buildRow(home, prOffer, action, param, it, this.itemBuilder);
    }

    private class NearbyItemBuilder
        implements ItemBuilder {

        @Override
        public ItemHome buildItem(Product home, Product prOffer, ActionRecommendation action, Param param, RankingItemDTO dto) {

            String destination = dto.getId();
            return NearbyBuyDestinationFunction.this.buildDestination(prOffer, param.getRankingType(), destination, null,
                action);
        }
    }

    private Iterator<RankingItemDTO> getRanking(Product prOffer, String dest, ActionRecommendation action, Product prBought) {
        final Iterator<String> it = this.destinationService.searchesNearByCities(prOffer, dest, BaseFunction.RANKING_SIZE)
            .getDestinations().iterator();

        return new IteratorAdapter<String, RankingItemDTO>(it) {
            @Override
            public RankingItemDTO convert(String f) {
                return new RankingItemDTO(f);
            }
        }.newIterator();
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.NEARBY_BUY_DESTINATION;
    }

    @Override
    public String getDescription() {
        return "Destinos cercanos al destino que compro";
    }

}
