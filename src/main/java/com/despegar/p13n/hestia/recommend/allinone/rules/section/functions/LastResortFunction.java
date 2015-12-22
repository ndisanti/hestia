package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.external.geo.CityDto;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function.CountryType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@Service
public class LastResortFunction
    extends BaseSectionFunction {

    private static final String DEF_RANKING_KEY = "def";

    private static final String CRUISE_RANKING_KEY = "cruises";

    private static final String ORLANDO = "UN_ORL";

    private LastResortBuilder itemBuilder = new LastResortBuilder();

    @Autowired
    private GeoService geoService;

    @Resource(name = "rankings")
    private Properties rankings;

    public List<Offer> buildOffers(Product home, Product prToShow, ActionRecommendation action, Param param) {

        param = this.getParamWithRankingType(param, prToShow);

        Iterator<RankingItemDTO> it = this.getRankingIterator(prToShow, action, param);

        return ItemUtils.buildOffers(home, prToShow, action, param, it, this.itemBuilder);
    }

    public RowHome buildRow(Product home, Product prToShow, ActionRecommendation action, Param param) {

        param = this.getParamWithRankingType(param, prToShow);

        Iterator<RankingItemDTO> it = this.getRankingIterator(prToShow, action, param);

        return ItemUtils.buildRow(home, prToShow, action, param, it, this.itemBuilder);
    }


    private Iterator<RankingItemDTO> getRankingIterator(Product prToShow, ActionRecommendation action, Param param) {
        StaticRankingTypes rankingType = param.getRankingType();

        if (CountryType.DOMESTIC == param.getCountryType()) {
            rankingType = StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC;
        }

        List<RankingItemDTO> list = this.getRankingCountryList(prToShow, rankingType, action);
        if (action.getCurrentHome().equals(Product.ACTIVITIES) && action.getCountryCode().equals(CountryCode.AR)) {
            list.add(0, new RankingItemDTO(ORLANDO));
        }

        Iterator<RankingItemDTO> it = list.iterator();

        if (rankingType == StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY
            || rankingType == StaticRankingTypes.CRUISE_REGIONS) {

            String key = null;

            switch (rankingType) {
            case HOT_SEARCHES_DESTINATIONS_ANY:
                key = this.rankings.containsKey(action.getCountryCode()) ? action.getCountryCode().toString()
                    : DEF_RANKING_KEY;
                break;
            default:
                key = CRUISE_RANKING_KEY;
                break;
            }
            Iterator<String> destIt = Arrays.asList(this.rankings.get(key).toString().split(",")).iterator();
            List<RankingItemDTO> dtoList = Lists.newArrayList();

            while (destIt.hasNext()) {
                dtoList.add(new RankingItemDTO(destIt.next()));
            }
            return Iterators.concat(it, dtoList.iterator());
        } else {
            return it;
        }
    }

    private Param getParamWithRankingType(Param param, Product prToShow) {

        if (prToShow == Product.CRUISES) {
            return param.rankingType(StaticRankingTypes.CRUISE_REGIONS);
        } else {
            return param.rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY);
        }
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.LAST_RESORT;
    }

    private class LastResortBuilder
        implements ItemBuilder {

        @Override
        public ItemHome buildItem(Product home, Product prOffer, ActionRecommendation action, Param param, RankingItemDTO dto) {

            // this is mainly used for last resort cache
            // we need to set the origin because we have no user to geolocate
            // so origin is main city for country

            CityDto originCityDTO = LastResortFunction.this.geoService.getMainCityFromCountry(action.getCountryCode());
            // For CountryCodes without main city, this is a temporal solution
            originCityDTO = originCityDTO == null ? this.getDefaultSimplCityDTO() : originCityDTO;
            Preconditions.checkNotNull(originCityDTO, "No main city for " + action.getCountryCode());
            Preconditions.checkNotNull(originCityDTO.getCode());
            action.setOrigin(originCityDTO.getCode());

            return LastResortFunction.this.buildDestination(prOffer, param.getRankingType(), dto.getId(),
                action.getOrigin(), action);
        }

        private CityDto getDefaultSimplCityDTO() {
            CityDto defCityDTO = new CityDto();
            defCityDTO.setAdministrative_division_id(12591);
            defCityDTO.setCountry_id(20208);
            defCityDTO.setCode("NYC");
            defCityDTO.setTimezone("America/New_York");
            return defCityDTO;
        }
    }

    @Override
    public String getDescription() {
        return "Esta funcion solo es usada para armar el contenido de homes cuando Euler se inicia. "
            + "El contenido que genera esta funcion se usa como ultimo recurso "
            + "cuando alguna de las otras funciones no devuelve datos suficientes, hay un timeout o un error."
            + "Devuelve ranking de HOT_SEARCHES_DESTINATIONS_ANY en el pais";
    }

}
