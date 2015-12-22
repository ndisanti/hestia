package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.IteratorAdapter;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;

@Service
public class RankingHotelsDestinationFunction
    extends BaseSectionFunction {

    @Autowired
    private HotelItemBuilder itemBuilder;

    @Override
    public List<Offer> buildOffers(Product home, Product prToShow, ActionRecommendation action, Param param) {
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH, ActivityType.BUY);

        String dest = action.getLastDestination();

        action.getTitleData().addDestination(dest);

        Iterator<RankingItemDTO> it = this.getRanking(dest, action);

        return ItemUtils.buildOffers(home, prToShow, action, param, it, this.itemBuilder);
    }

    @Override
    public RowHome buildRow(Product home, Product prToShow, ActionRecommendation action, Param param) {
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH, ActivityType.BUY);

        ItemHome youSaw = null;

        if (param.isSeen()) {
            UserActivity activity = action.getSearchActivity().getLastActivity();

            if (activity.isDetailOrCheckout()) {
                youSaw = new HotelItem(activity.getProductBusinessId());
            } else {
                youSaw = RankingHotelsDestinationFunction.this.buildDestination(prToShow, null, activity.getDestination(),
                    null, action);
            }
        }

        youSaw = ItemUtils.checkUnique(home, youSaw, action, prToShow);

        String dest = action.getLastDestination();

        action.getTitleData().addDestination(dest);

        Iterator<RankingItemDTO> it = this.getRanking(dest, action);

        RowHome rowHome = ItemUtils.buildRow(home, prToShow, action, param, it, this.itemBuilder);

        if (rowHome != null) {
            rowHome.setHighlighted(youSaw);
            ItemUtils.addSubtitles(action);
        }

        return rowHome;
    }

    private Iterator<RankingItemDTO> getRanking(String dest, ActionRecommendation action) {

        Preconditions.checkNotNull(dest);

        final Iterator<RankingPositionDTO> it = this
            .getRankingIata(Product.HOTELS, dest, StaticRankingTypes.HOTEL_DETAIL_DESTINATON).getPodium().iterator();

        // add searched hotel ids for the destination
        Iterator<String> searchedHidsIt = action.getSearchActivity() == null ? Iterators.<String> emptyIterator() : action
            .getSearchActivity().getLastHotels(dest).iterator();

        Iterator<RankingItemDTO> hidRankingItemIt = new IteratorAdapter<String, RankingItemDTO>(searchedHidsIt) {
            @Override
            public RankingItemDTO convert(String f) {
                return new RankingItemDTO(f);
            }
        }.newIterator();

        Iterator<RankingItemDTO> hotelDetailIt = new IteratorAdapter<RankingPositionDTO, RankingItemDTO>(it) {
            @Override
            public RankingItemDTO convert(RankingPositionDTO f) {
                return new RankingItemDTO(f.getDestination());
            }
        }.newIterator();


        return Iterators.concat(hidRankingItemIt, hotelDetailIt);
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.RANKING_HOTELS_DESTINATION;
    }

    @Override
    public String getDescription() {
        return "Hotel Rankings en el destino que compro o busco";
    }

}
