package com.despegar.p13n.hestia.recommend.allinone.rules.section;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.City;
import com.google.common.base.Preconditions;

/**
 * Wraps the function to execute and its optional params
 */
public class Function {

    public enum CarRankingType {
        SEARCH, CHECKOUT
    }

    public enum CountryType {
        DOMESTIC, INTERNATIONAL;
    }


    public static final Function INTERNATIONAL = Function.create(SectionFunctionCode.INTERNATIONAL);

    public static Function RANKING_ANY = Function.create(SectionFunctionCode.HOT_RANKING_IP_COUNTRY).rankingType(
        StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY);

    public static Function RANKING_INTERNATIONAL = Function.create(SectionFunctionCode.HOT_RANKING_IP_COUNTRY).rankingType(
        StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL);

    public static Function RANKING_DOMESTIC = Function.create(SectionFunctionCode.HOT_RANKING_IP_COUNTRY).rankingType(
        StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC);

    public static Function RANKING_DID = Function.create(SectionFunctionCode.HOT_RANKING_IP_COUNTRY).rankingType(
        StaticRankingTypes.CRUISE_DID);

    public static Function RANKING_REGION = Function.create(SectionFunctionCode.HOT_RANKING_IP_COUNTRY).rankingType(
        StaticRankingTypes.CRUISE_REGIONS);


    public static Function CAR_RANKING_COUNTRY_SEARCH = Function.create(SectionFunctionCode.CARS_RANKING_COUNTRY)
        .carRankingType(CarRankingType.SEARCH);

    public static Function CAR_RANKING_COUNTRY_CHECKOUT = Function.create(SectionFunctionCode.CARS_RANKING_COUNTRY)
        .carRankingType(CarRankingType.CHECKOUT);

    public static Function BUY_DESTINATION = Function.create(SectionFunctionCode.BUY_DESTINATION);

    public static Function SEARCH_DESTINATION = Function.create(SectionFunctionCode.SEARCH_DESTINATION);

    public static Function RANKING_HOTELS_DESTINATION = Function.create(SectionFunctionCode.RANKING_HOTELS_DESTINATION);

    public static Function RANKING_RENTALS_DESTINATION = Function.create(SectionFunctionCode.RANKING_RENTALS_DESTINATION);

    public static Function RECOMMEND = Function.create(SectionFunctionCode.RECOMMEND);

    public static Function NEARBY_BUY_DESTINATION = Function.create(SectionFunctionCode.NEARBY_BUY_DESTINATION);

    public static Function CARS_CATEGORY_DESTINATION = Function.create(SectionFunctionCode.CARS_CATEGORY_DESTINATION);

    public static Function CRUISE_BUY = Function.create(SectionFunctionCode.CRUISE_BUY);

    public static Function CRUISE_BUY_DESTINATION_RECOM = Function.create(SectionFunctionCode.CRUISE_BUY_DESTINATION_RECOM);

    public static Function CRUISE_BUY_OFFER_FLIGHTS = Function.create(SectionFunctionCode.CRUISE_BUY_OFFER_FLIGHTS);

    public static Function FLIGHTS_RANKING_BY_CLUSTER = Function.create(SectionFunctionCode.FLIGHTS_RANKING_BY_CLUSTER);

    public static Function HOTEL_SEARCH_AND_RECOMMEND = Function.create(SectionFunctionCode.HOTEL_SEARCH_AND_RECOMMEND);

    public static Function RANKING_DESTINATION_BY_CLUSTER = Function
        .create(SectionFunctionCode.RANKING_DESTINATION_BY_CLUSTER);

    public static Function CAR_SEARCH = Function.create(SectionFunctionCode.CARS_SEARCH);

    public static Function CRUISE_SEARCH = Function.create(SectionFunctionCode.CRUISE_SEARCH);

    public static Function CRUISE_RANKING_DESTINATION = Function.create(SectionFunctionCode.CRUISE_RANKING_DESTINATION);

    public static Function CRUISE_RANKING_IP = Function.create(SectionFunctionCode.CRUISE_RANKING_IP);

    public static Function RANKING_CRUISE_REGION = Function.create(SectionFunctionCode.RANKING_CRUISE_REGION);

    public static Function PACKAGE_SEARCH = Function.create(SectionFunctionCode.PACKAGE_SEARCH);

    public static Function LAST_RESORT = Function.create(SectionFunctionCode.LAST_RESORT);

    public static Function LAST_RESORT_ACT_AR = Function.create(SectionFunctionCode.LAST_RESORT_ACT_AR);

    public static Function RANKING_DYNAMIC_PRODUCT = Function.create(SectionFunctionCode.RANKING_DYNAMIC_PRODUCT);

    public static Function LAST_DETAIL_FUNCTION = Function.create(SectionFunctionCode.LAST_DETAIL_FUNCTION);

    public static Function LIKE_HOTEL_FUNCTION = Function.create(SectionFunctionCode.LIKE_HOTEL_FUNCTION);

    public static Function BUY_OFFER_CARS = Function.create(SectionFunctionCode.BUY_OFFER_CARS);

    public static Function ACTIVITIES_DISNEY = Function.create(SectionFunctionCode.ACTIVITIES_DISNEY);

    public static Function ACTIVITIES_UNIVERSAL = Function.create(SectionFunctionCode.ACTIVITIES_UNIVERSAL);

    public static Function ACTIVITIES_RANKING_LOCATION = Function.create(SectionFunctionCode.ACTIVITIES_RANKING_LOCATION);

    public static Function ACTIVITIES_VIEWED = Function.create(SectionFunctionCode.ACTIVITIES_VIEWED);

    public static Function ACTIVITIES_RECOMMENDER = Function.create(SectionFunctionCode.ACTIVITIES_RECOMMENDER);

    public static Function ACTIVITIES_ORL_DOMESTIC = Function.create(SectionFunctionCode.ACTIVITIES_ORL_DOMESTIC);

    private final SectionFunctionCode functionCode;
    private final Param param;

    public Param getParam() {
        return this.param;
    }

    public Function(SectionFunctionCode f) {
        Preconditions.checkNotNull(f);
        this.functionCode = f;
        this.param = new Param();
    }

    public Function(SectionFunctionCode f, Param param) {
        Preconditions.checkNotNull(f);
        Preconditions.checkNotNull(param);
        this.functionCode = f;
        this.param = param;
    }

    public static Function create(SectionFunctionCode functionCode) {
        return new Function(functionCode);
    }

    public Function rankingType(StaticRankingTypes type) {
        return new Function(this.functionCode, this.param.rankingType(type));
    }

    public Function carRankingType(CarRankingType type) {
        return new Function(this.functionCode, this.param.carRankingType(type));
    }

    public Function f1(Flow flow) {
        return new Function(this.functionCode, this.param.flow1(flow));
    }

    public Function youSaw(boolean youSaw) {
        return new Function(this.functionCode, this.param.seen(youSaw));
    }

    public Function addSearch(boolean addSearch) {
        return new Function(this.functionCode, this.param.addSearch(addSearch));
    }

    public Function addBuy(boolean addBuy) {
        return new Function(this.functionCode, this.param.addBuy(addBuy));
    }

    public Function countryType(CountryType countryType) {
        return new Function(this.functionCode, this.param.countryType(countryType));
    }

    public Function city(City city) {
        return new Function(this.functionCode, this.param.city(city));
    }

    public StaticRankingTypes getRankingType() {
        return this.param.getRankingType();
    }

    public SectionFunctionCode getCode() {
        return this.functionCode;
    }

    public CarRankingType getCarRankingType() {
        return this.param.getCarRankingType();
    }

    public Function pr1(Product pr1) {
        return new Function(this.functionCode, this.param.pr1(pr1));
    }

    public Product getPr1() {
        return this.param.getPr1();
    }

    public Function f2(Flow flow) {
        return new Function(this.functionCode, this.param.crossFlow2(flow));
    }

    public Flow getFlow2() {
        return this.param.getFlow2();
    }

    public Function build() {
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.functionCode == null) ? 0 : this.functionCode.hashCode());
        result = prime * result + ((this.param == null) ? 0 : this.param.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Function other = (Function) obj;
        if (this.functionCode != other.functionCode) {
            return false;
        }
        if (this.param == null) {
            if (other.param != null) {
                return false;
            }
        } else if (!this.param.equals(other.param)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

        if (this.functionCode == SectionFunctionCode.RANKING_DYNAMIC_PRODUCT) {
            return this.functionCode.toString();
        } else {
            return this.functionCode.toString() + (this.param.isEmpty() ? "" : ":" + this.param);
        }

    }

    public Function searchedDestination(boolean searchedDestination) {
        return new Function(this.functionCode, this.param.searchedDestination(searchedDestination));
    }

    public Function checkIsDetail(boolean checkIsDetail) {
        return new Function(this.functionCode, this.param.checkIsDetail(checkIsDetail));
    }
}
