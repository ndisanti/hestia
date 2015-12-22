package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ActivityDestinationItem;
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.external.geo.CityDto;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.google.common.base.Preconditions;

@Service
public class LastResortActivitiesArg
    extends BaseSectionFunction {

    private static final String UNIVERSAL = "UN_ORL";
    public static final String DISNEY = "DY_ORL";
    private static final String ORL = "ORL";
    private static final ItemHome ORLANDO = new ActivityDestinationItem(ORL);

    private LastResortActArgBuilder itemBuilder = new LastResortActArgBuilder();

    @Autowired
    private GeoService geoService;

    @Resource(name = "rankings")
    private Properties rankings;

    @Override
    public List<Offer> buildOffers(Product home, Product prToShow, ActionRecommendation action, Param param) {
        ItemUtils.checkProduct(prToShow, Product.ACTIVITIES);
        return ItemUtils.buildOffers(home, ORLANDO, action, prToShow);
    }

    @Override
    public RowHome buildRow(Product home, Product prToShow, ActionRecommendation action, Param param) {

        Iterator<RankingItemDTO> it = this.getRankingIterator(prToShow, action, param);
        return ItemUtils.buildRow(home, prToShow, action, param, it, this.itemBuilder);
    }


    private Iterator<RankingItemDTO> getRankingIterator(Product prToShow, ActionRecommendation action, Param param) {

        StaticRankingTypes rankingType = StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC;
        List<RankingItemDTO> list = this.getRankingCountryList(prToShow, rankingType, action);
        list.add(0, new RankingItemDTO(DISNEY));
        list.add(1, new RankingItemDTO(UNIVERSAL));
        return list.iterator();

    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.LAST_RESORT_ACT_AR;
    }

    private class LastResortActArgBuilder
        implements ItemBuilder {

        @Override
        public ItemHome buildItem(Product home, Product prOffer, ActionRecommendation action, Param param, RankingItemDTO dto) {

            // this is mainly used for last resort cache
            // we need to set the origin because we have no user to geolocate
            // so origin is main city for country
            String hid = dto.getId();
            switch (hid) {
            case UNIVERSAL:
                return new ActivityItem(hid);
            case DISNEY:
                return new ActivityItem(hid);
            default:
                CityDto originCityDTO = LastResortActivitiesArg.this.geoService.getMainCityFromCountry(action
                    .getCountryCode());
                // For CountryCodes without main city, this is a temporal solution
                originCityDTO = originCityDTO == null ? this.getDefaultCityDTO() : originCityDTO;
                Preconditions.checkNotNull(originCityDTO, "No main city for " + action.getCountryCode());
                Preconditions.checkNotNull(originCityDTO.getCode());
                action.setOrigin(originCityDTO.getCode());

                return LastResortActivitiesArg.this.buildDestination(prOffer, param.getRankingType(), hid,
                    action.getOrigin(), action);
            }
        }

        private CityDto getDefaultCityDTO() {
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