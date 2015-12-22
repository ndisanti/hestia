package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.client.RecommendationsClient;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.IteratorAdapter;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function.CountryType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.recommend.filter.FilterFactory;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.newrelic.api.agent.Trace;

@Service
public class RecommendFunction
    extends BaseSectionFunction {

    private RecommendationsClient recommendation;

    @Autowired
    private FilterFactory filterFactory;

    private DestinationItemBuilder destinationBuilder = new DestinationItemBuilder();

    @Autowired
    private DetailItemBuilder detailItemBuilder;

    public static final EnumSet<Product> DETAIL_PRODUCTS = EnumSet.of(Product.HOTELS, Product.ACTIVITIES);

    @Override
    public List<Offer> buildOffers(Product home, Product prOffer, ActionRecommendation action, Param param) {

        this.preconditions(prOffer, action, param);

        boolean isDetail = this.isDetail(action, param, prOffer);
        Iterator<RankingItemDTO> it = this.getRanking(prOffer, action, param, isDetail);

        if (isDetail) {
            return ItemUtils.buildOffers(home, prOffer, action, param, it, this.detailItemBuilder);
        } else {
            return ItemUtils.buildOffers(home, prOffer, action, param, it, this.destinationBuilder);
        }
    }

    @Override
    public RowHome buildRow(Product home, Product prOffer, ActionRecommendation action, Param param) {

        this.preconditions(prOffer, action, param);

        boolean isDetail = this.isDetail(action, param, prOffer);
        Iterator<RankingItemDTO> it = this.getRanking(prOffer, action, param, isDetail);

        if (isDetail) {
            return ItemUtils.buildRow(home, prOffer, action, param, it, this.detailItemBuilder);
        } else {
            return ItemUtils.buildRow(home, prOffer, action, param, it, this.destinationBuilder);
        }
    }


    private void preconditions(Product prOffer, ActionRecommendation action, Param param) {

        Product pr1 = param.getPr1();
        Flow flow1 = param.getFlow1();
        Flow flow2 = param.getFlow2();

        Preconditions.checkNotNull(pr1);
        Preconditions.checkNotNull(flow1);
        Preconditions.checkNotNull(flow2);

        // if detail, only hotels or activities are supported
        Preconditions.checkArgument(flow2 != Flow.DETAIL || DETAIL_PRODUCTS.contains(prOffer));

        Preconditions.checkArgument(EnumSet.of(Flow.SEARCH, Flow.DETAIL, Flow.THANKS).contains(flow1));
        Preconditions.checkArgument(EnumSet.of(Flow.SEARCH, Flow.DETAIL, Flow.THANKS).contains(flow2));

        // countryType param is only valid for CARS SEARCH-SEARCH and DESTINATION type
        Preconditions
            .checkArgument(
                param.getCountryType() == null
                    || (param.getCountryType() == CountryType.DOMESTIC && pr1 == Product.CARS && flow1 == Flow.SEARCH && flow2 == Flow.SEARCH),
                "countryType param is only valid for CARS SEARCH-SEARCH");
    }

    private boolean isDetail(ActionRecommendation action, Param param, Product prOffer) {
        boolean isDetail = param.getFlow2() == Flow.DETAIL && //
            action.getSearchActivity().isActivityFor(prOffer) && //
            ItemUtils.isDetailOrCheckout(action.getSearchActivity().getActivity(prOffer).getAction().getFlow()) && //
            DETAIL_PRODUCTS.contains(prOffer); //

        if (isDetail) {
            action.addDebug("Recommend DETAIL items");

            // el recomendador devuelve items en el mismo destino
            action.getTitleData().addDestination(action.getSearchActivity().getLastActivity().getDestination());
        }
        return isDetail;

    }

    private Iterator<RankingItemDTO> getRanking(Product prOffer, ActionRecommendation action, Param param, boolean isDetail) {

        String item;

        if (isDetail) {
            item = action.getSearchActivity().getActivityOrLast(prOffer).getProductBusinessId();
        } else { // SEARCH or THANKS
            item = action.getLastDestination();
            action.getTitleData().addDestination(item);
        }

        Preconditions.checkNotNull(item);


        Iterator<RankingItemDTO> it = this.createRanking(action, param.getPr1(), param.getFlow1(), prOffer,
            param.getFlow2(), item, param, isDetail);

        return it;
    }

    @Trace
    private Iterator<RankingItemDTO> createRanking(ActionRecommendation action, Product pr1, Flow flow1, Product prOffer,
        Flow flow2, String item, Param param, boolean isDetail) {

        List<Recommendation> list;
        String destination = action.getLastDestination();
        boolean isDomesticFilter = param.getCountryType() == CountryType.DOMESTIC && destination != null;
       

        if (isDomesticFilter) {
        	String strCC =  this.getGeoService().getCountryFromIata(destination);
        	list = this.recommendation
                    .recommend(pr1, flow1, prOffer, flow2, item, BaseFunction.RANKING_SIZE, CountryCode.fromString(strCC));
        } else {
        	list = this.recommendation.recommend(pr1, flow1, prOffer, flow2, item, BaseFunction.RANKING_SIZE);
        }

        list = this.addSearch(action, prOffer, item, param, isDetail, list);
        list = this.addBuy(action, prOffer, item, param, isDetail, list);


        Iterator<Recommendation> it = list.iterator();

        return new IteratorAdapter<Recommendation, RankingItemDTO>(it) {
            @Override
            public RankingItemDTO convert(Recommendation f) {
                return new RankingItemDTO(f.getRecommendation());
            }
        }.newIterator();

    }

    private List<Recommendation> addSearch(ActionRecommendation action, Product prOffer, String item, Param param,
        boolean isDetail, List<Recommendation> list) {
        if (param.isAddSearch()) {

            list = Lists.newArrayList(list);
            int index = 0;

            // adding searched hid for the same destination to the return
            if (isDetail && prOffer == Product.HOTELS) {

                if (action.getSearchActivity() != null) {

                    String hotelsDestination = action.getSearchActivity().getLastActivity().getDestination();

                    list.add(index++, new Recommendation(item, 1.0f));

                    for (String hid : action.getSearchActivity().getLastHotels(hotelsDestination)) {
                        list.add(index++, new Recommendation(hid, 1.0f));
                    }
                }
            }
            // adding the searched destinations to the return
            if (!isDetail) {
                list.add(index++, new Recommendation(item, 1.0f));

                if (action.getSearchActivity() != null) {
                    for (String destination : action.getSearchActivity().getLastDestinations()) {
                        list.add(index++, new Recommendation(destination, 1.0f));
                    }
                }
            }
        }
        return list;
    }

    private List<Recommendation> addBuy(ActionRecommendation action, Product prOffer, String item, Param param,
        boolean isDetail, List<Recommendation> list) {
        if (param.isAddBuy()) {

            // adding the bought destination to the return
            if (!isDetail) {
                list = Lists.newArrayList(list);
                int index = 0;

                if (action.getBuyActivity() != null) {
                    String destination = action.getBuyActivity().getActivity().getDestination();
                    list.add(index++, new Recommendation(destination, 1.0f));
                }
            }
        }
        return list;
    }

   private final class DestinationItemBuilder
        implements ItemBuilder {
        @Override
        public ItemHome buildItem(Product home, Product prOffer, ActionRecommendation action, Param param, RankingItemDTO dto) {

            if (home == Product.CRUISES || prOffer == Product.CRUISES) {
                param = param.rankingType(StaticRankingTypes.CRUISE_REGIONS);
            }

            String destination = dto.getId();

            Preconditions.checkNotNull(destination);

            return RecommendFunction.this.buildDestination(prOffer, param.getRankingType(), destination, action.getOrigin(),
                action);
        }
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.RECOMMEND;
    }

    @Override
    public String getDescription() {
        return "Recomendador. Si flow2 es SEARCH o THANKS devuelve destinos. Si flow2 es DETAIL devuelve un item."
            + "Si es CARS SEARCH/SEARCH and countryType = domestic usa filtro del recomendador domestic"
            + "Si param es addSearch, agrega los ultimos destinos buscados (SEARCH) o hotel ids si era un DETAIL"
            + "Si param es addBuy agregar el ultimo destino comprado.";
    }

    public DetailItemBuilder getDetailItemBuilder() {
        return this.detailItemBuilder;
    }
}
