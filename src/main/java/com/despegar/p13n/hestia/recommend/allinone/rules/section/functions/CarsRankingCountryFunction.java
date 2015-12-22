package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.CarRankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.CarsCategoryRankingDto;
import com.despegar.p13n.hestia.api.data.model.CarsLocationDto;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.PriceCarCategoryItem;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.service.external.PublicCarsService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.IteratorAdapter;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.CarsItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.newrelic.api.agent.Trace;

/**
 * Ranking for country based on SEARCH (param): CarRankingPositionDTO
 * 
 * Ranking for country based on CHECKOUT (param): DestinationPriceDTO
 */
@Service
public class CarsRankingCountryFunction
    extends BaseSectionFunction {

    @Autowired
    private PublicCarsService carsService;

    private RankingItemBuilder itemBuilder = new RankingItemBuilder();

    @Override
    public List<Offer> buildOffers(Product home, Product prOffer, ActionRecommendation action, Param param) {

        ItemUtils.checkProduct(prOffer, Product.CARS);
        Preconditions.checkNotNull(param.getCarRankingType());

        List<CarRankingPositionDTO> list = this.getNormalizedCarRankingPositionDTOList(this.getRankingIterator(prOffer,
            action, param));
        return ItemUtils.buildOffersCars(home, prOffer, action, param, list, this.itemBuilder);
    }


    @Override
    public RowHome buildRow(Product home, Product prOffer, ActionRecommendation action, Param param) {

        ItemUtils.checkProduct(prOffer, Product.CARS);
        Preconditions.checkNotNull(param.getCarRankingType());

        ArrayList<CarRankingPositionDTO> list = this.getNormalizedCarRankingPositionDTOList(this.getRankingIterator(prOffer,
            action, param));
        return ItemUtils.buildRowCars(home, prOffer, action, param, list, this.itemBuilder);
    }

    private class RankingItemBuilder
        implements CarsItemBuilder {

        @Override
        public ItemHome buildItem(Product home, Product prOffer, ActionRecommendation action, Param param,
            CarRankingPositionDTO dto) {

            if (param.getCarRankingType() == Function.CarRankingType.SEARCH) {
                return CarsRankingCountryFunction.this.buildDestination(Product.CARS, null, dto.getPul(), null, action);
            } else {
                return new PriceCarCategoryItem(dto.getPul(), dto.getPultype(), dto.getCarcat(), dto.getPrice(),
                    dto.getCurrency());
            }
        }
    }

    private Iterator<CarRankingPositionDTO> getRankingIterator(Product prToShow, final ActionRecommendation action,
        Param param) {

        Iterator<CarRankingPositionDTO> it;


        if (param.getCarRankingType() == Function.CarRankingType.SEARCH) {
            it = this.createSearchIterator(action);
        } else {
            it = this.createCheckoutIterator(action);
        }

        return it;
    }


    @Trace
    protected Iterator<CarRankingPositionDTO> createSearchIterator(final ActionRecommendation action) {

        String cc = action.getCountryCode().toString();

        Map<String, List<CarsLocationDto>> allCountries = this.carsService.getTopSearchesRanking(cc,
            BaseFunction.RANKING_SIZE);

        if (!allCountries.containsKey(action.getCountryCode().toString())) {
            return Collections.<CarRankingPositionDTO> emptyList().iterator();
        }

        final Iterator<CarsLocationDto> byCountryIt = allCountries.get(cc).iterator();


        return new IteratorAdapter<CarsLocationDto, CarRankingPositionDTO>(byCountryIt) {

            @Override
            public CarRankingPositionDTO convert(CarsLocationDto f) {
                return new CarRankingPositionDTO(f.getPul(), f.getPultype(), null, 0, "");
            }

        }.newIterator();

    }

    /**
     * Returns a unique iterator based on the top of each car category iterator.
     */
    @Trace
    protected Iterator<CarRankingPositionDTO> createCheckoutIterator(final ActionRecommendation action) {

        String cc = action.getCountryCode().toString();

        Map<String, CarsCategoryRankingDto> allCountries = this.carsService.getTopCheckoutsRanking(cc, RANKING_SIZE);


        if (!allCountries.containsKey(cc)) {
            return Collections.<CarRankingPositionDTO> emptyList().iterator();
        }

        CarsCategoryRankingDto byCountry = allCountries.get(cc);

        Map<String, List<CarsLocationDto>> dtos = byCountry.getCategoriesRanking();

        final Map<String, Iterator<CarsLocationDto>> map = Maps.newLinkedHashMap();

        int size = 0;

        for (java.util.Map.Entry<String, List<CarsLocationDto>> entry : dtos.entrySet()) {
            map.put(entry.getKey(), entry.getValue().iterator());
            size += entry.getValue().size();
        }

        final int s = size;

        return new Iterator<CarRankingPositionDTO>() {

            private Iterator<java.util.Map.Entry<String, Iterator<CarsLocationDto>>> m = map.entrySet().iterator();
            private int size = s;

            @Override
            public boolean hasNext() {
                return this.size > 0;
            }

            @Override
            public CarRankingPositionDTO next() {

                java.util.Map.Entry<String, Iterator<CarsLocationDto>> entry;

                do {
                    if (this.m.hasNext()) {
                        entry = this.m.next();
                    } else {
                        this.m = map.entrySet().iterator();
                        entry = this.m.next();
                    }
                } while (!entry.getValue().hasNext());

                CarsLocationDto d = entry.getValue().next();

                this.size--;

                return new CarRankingPositionDTO(d.getPul(), d.getPultype(), entry.getKey(), 0, "");
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.CARS_RANKING_COUNTRY;
    }

     @Override
    public String getDescription() {
        return "Si el param es SEARCH, ranking de destinos en el pais. "
            + "Si el param es CHECKOUT ranking de destinos unicos por categoria para el pais.";
    }

}
