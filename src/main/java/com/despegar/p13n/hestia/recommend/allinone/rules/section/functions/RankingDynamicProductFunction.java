package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function.CarRankingType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.google.common.base.Preconditions;

/**   
 *  <p>The product that are passed to this function are based on the buy probability matrix.</p>  
 */
@Service
public class RankingDynamicProductFunction
    extends BaseSectionFunction {

    @Autowired
    private HotRankingIpCountryFunction hotFunction;

    @Autowired
    private CarsRankingCountryFunction carsRankingFunction;

    @Override
    public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {

        Preconditions.checkNotNull(home);
        Preconditions.checkNotNull(pr);

        if (pr == Product.CARS) {
            param = param.carRankingType(CarRankingType.CHECKOUT);
            return this.carsRankingFunction.buildOffers(home, pr, action, param);
        }

        if (pr == Product.CRUISES) {
            param = param.rankingType(StaticRankingTypes.CRUISE_REGIONS);
        } else {
            param = param.rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY);
        }

        return this.hotFunction.buildOffers(home, pr, action, param);
    }

    @Override
    public RowHome buildRow(Product home, Product pr, ActionRecommendation action, Param param) {

        Preconditions.checkNotNull(home);
        Preconditions.checkNotNull(pr);

        if (pr == Product.CARS) {
            param = param.carRankingType(CarRankingType.CHECKOUT);
            return this.carsRankingFunction.buildRow(home, pr, action, param);
        }

        if (pr == Product.CRUISES) {
            param = param.rankingType(StaticRankingTypes.CRUISE_REGIONS);
        } else {
            param = param.rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY);
        }
        return this.hotFunction.buildRow(home, pr, action, param);
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.RANKING_DYNAMIC_PRODUCT;
    }

    public String getDescription() {
        return "Usado para la version de reglas de dynamicProduct. "
            + "El producto que recibe esta funcion esta calculado en base a la matriz de probabilidad. "
            + "Para cars usa la function CARS_RANKING_COUNTRY. "
            + "Para cruises usa HOT_RANKING_IP_COUNTRY para CRUISE_REGIONS. "
            + "Para todo lo demas usa HOT_RANKING_IP_COUNTRY para HOT_SEARCHES_DESTINATIONS_ANY.";
    }

}
