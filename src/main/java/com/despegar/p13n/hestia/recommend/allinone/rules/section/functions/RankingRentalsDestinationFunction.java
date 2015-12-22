package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.data.model.VacationRentalItem;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@Service
public class RankingRentalsDestinationFunction
    extends BaseSectionFunction {


   // private static final String HSORT = "http://10.2.7.6/v3/vr/home/city/BUE?currency=USD&site=AR&language=es";
    private VacationRentaltemBuilder itemBuilder = new VacationRentaltemBuilder();

    @Override
    public List<Offer> buildOffers(Product home, Product prToShow, ActionRecommendation action, Param param) {
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH, ActivityType.BUY);

        String dest = this.determineDestination(action, param);

        action.getTitleData().addDestination(dest);

        Iterator<RankingItemDTO> it = this.getRanking(dest, action);

        return ItemUtils.buildOffers(home, prToShow, action, param, it, this.itemBuilder);
    }

    private String determineDestination(ActionRecommendation action, Param param) {
        String dest = null;

        if (param.isCheckDetail()) {
            UserActivity activity = action.getSearchActivity().getActivity(Product.VACATIONRENTALS);
            if (activity != null && activity.getFlow().equals(Flow.DETAIL)) {
                dest = activity.getDestination();
            }
        }
        if (dest == null) {
            dest = param.isSearchedDestination() ? action.getSearchActivity().getLastActivity().getDestination() : action
                .getLastDestination();
        }
        return dest;
    }

    @Override
    public RowHome buildRow(Product home, Product prToShow, ActionRecommendation action, Param param) {

        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH, ActivityType.BUY);

        ItemHome youSaw = null;

        if (param.isSeen()) {
            UserActivity activity = action.getSearchActivity().getLastActivity();

            if (activity.isDetailOrCheckout()) {
                youSaw = new VacationRentalItem(activity.getProductBusinessId());
            } else {
                youSaw = RankingRentalsDestinationFunction.this.buildDestination(prToShow, null, activity.getDestination(),
                    null, action);
            }
        }

        youSaw = ItemUtils.checkUnique(home, youSaw, action, prToShow);

        String dest = this.determineDestination(action, param);

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
            .getRankingIata(Product.VACATIONRENTALS, dest, StaticRankingTypes.VACATIONS_RENTALS_VRID_BY_DESTINATION)
            .getPodium().iterator();

        // add searched vacation rentals ids for the destination
        List<RankingItemDTO> searchedVridsList = this.stringToRankingItemDTO(dest, action);
        List<RankingItemDTO> rentalDetailList = this.toRankingItemDtoList(it);
        searchedVridsList.addAll(rentalDetailList);
        if (searchedVridsList.size() < ItemUtils.MIN_ROW_SIZE) {
            this.completeWithTopContactRentals(searchedVridsList, dest);
            if (searchedVridsList.size() < ItemUtils.MIN_ROW_SIZE) {
                this.completeWithHsort(searchedVridsList);
            }
        }
        return searchedVridsList.iterator();
    }

    private void completeWithHsort(List<RankingItemDTO> searchedVridsList) {
     
    }

    private List<RankingItemDTO> stringToRankingItemDTO(String dest, ActionRecommendation action) {

        Iterator<String> it = action.getSearchActivity() == null ? Iterators.<String> emptyIterator() : action
            .getSearchActivity().getLastVacationRentalsByDestination().get(dest).iterator();
        List<RankingItemDTO> list = Lists.newArrayList();
        while (it.hasNext()) {
            String s = it.next();
            list.add(new RankingItemDTO(s));
        }
        return list;
    }

    private void completeWithTopContactRentals(List<RankingItemDTO> ranking, String dest) {

        Iterator<RankingPositionDTO> rankingIt = this
            .getRankingIata(Product.VACATIONRENTALS, dest, StaticRankingTypes.VACATIONS_RENTALS_CONTACT_VRID).getPodium()
            .iterator();
        List<RankingItemDTO> list = this.toRankingItemDtoList(rankingIt);
        ranking.addAll(list);
    }

    private List<RankingItemDTO> toRankingItemDtoList(final Iterator<RankingPositionDTO> it) {

        List<RankingItemDTO> list = Lists.newArrayList();
        while (it.hasNext()) {
            RankingPositionDTO rankingPositionDTO = it.next();
            list.add(new RankingItemDTO(rankingPositionDTO.getDestination()));
        }
        return list;
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.RANKING_RENTALS_DESTINATION;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    private class VacationRentaltemBuilder
        implements ItemBuilder {

        @Override
        public ItemHome buildItem(Product home, Product prOffer, ActionRecommendation action, Param param, RankingItemDTO dto) {

            return new VacationRentalItem(dto.getId());

        }
    }

}
