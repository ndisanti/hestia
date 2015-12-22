package com.despegar.p13n.hestia.recommend.allinone.title;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemType;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.City;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function.CarRankingType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function.CountryType;

public class TitleRuleBuilder {

    private TitleEngine titleEngine;

    TitleRuleBuilder(TitleEngine titleService) {
        this.titleEngine = titleService;

        this.buildBuy();
        this.buildNoHistory();
        this.buildSearch();
        this.buildLastResort();
        this.buildMultiProductBuy();
        this.buildMultiProductSearch();
    }


    private void buildMultiProductSearch() {

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            true,//
            HomeSupport.MAIN),//
            TitleListEnum.L52);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH,//
            SectionType.ROW, //
            false, //
            HomeSupport.MAIN), //
            TitleListEnum.L54);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW, //
            true, //
            HomeSupport.FLIGHTS),//
            TitleListEnum.L4);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW, //
            false, //
            HomeSupport.FLIGHTS), //
            TitleListEnum.L3);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER, //
            true, //
            HomeSupport.FLIGHTS), //
            TitleListEnum.L4);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW, //
            true, //
            HomeSupport.HOTELS), //
            TitleListEnum.L1);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW, //
            false, //
            HomeSupport.HOTELS), //
            TitleListEnum.L3);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER, //
            true, //
            HomeSupport.HOTELS), //
            TitleListEnum.L1);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW, //
            true, //
            HomeSupport.CLOSED_PACKAGES), //
            TitleListEnum.L4);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW, //
            false, //
            HomeSupport.CLOSED_PACKAGES), //
            TitleListEnum.L3);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER, //
            true, //
            HomeSupport.CLOSED_PACKAGES), //
            TitleListEnum.L1);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            true,//
            HomeSupport.CARS),//
            TitleListEnum.L1);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW, //
            false, //
            HomeSupport.CARS), //
            TitleListEnum.L3);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER, //
            true, //
            HomeSupport.CARS), //
            TitleListEnum.L1);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW, //
            true, //
            HomeSupport.CRUISES), //
            TitleListEnum.L3);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW, //
            false, //
            HomeSupport.CRUISES), //
            TitleListEnum.L24);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER, //
            true, //
            HomeSupport.CRUISES),//
            TitleListEnum.L1);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW, //
            true, //
            HomeSupport.ACTIVITIES), //
            TitleListEnum.L3);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW, //
            false, //
            HomeSupport.ACTIVITIES), //
            TitleListEnum.L24);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER, //
            true, //
            HomeSupport.ACTIVITIES), //
            TitleListEnum.L1);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW, //
            true, //
            HomeSupport.VACATION_RENTALS), //
            TitleListEnum.L1);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW, //
            false, //
            HomeSupport.VACATION_RENTALS), //
            TitleListEnum.L3);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER, //
            true, //
            HomeSupport.VACATION_RENTALS), //
            TitleListEnum.L1);
    }



    private void buildMultiProductBuy() {

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.ROW, //
            true, //
            HomeSupport.MAIN), //
            TitleListEnum.L53);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.ROW, //
            false, //
            HomeSupport.MAIN), //
            TitleListEnum.L55);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.ROW, //
            true, //
            HomeSupport.FLIGHTS), //
            TitleListEnum.L4);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.ROW, //
            false, //
            HomeSupport.FLIGHTS), //
            TitleListEnum.L6);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER, //
            true, //
            HomeSupport.FLIGHTS), //
            TitleListEnum.L4);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.ROW, //
            true, //
            HomeSupport.HOTELS), //
            TitleListEnum.L1);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.ROW, //
            false, //
            HomeSupport.HOTELS), //
            TitleListEnum.L3);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER, //
            true, //
            HomeSupport.HOTELS), //
            TitleListEnum.L1);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.ROW, //
            true, //
            HomeSupport.CLOSED_PACKAGES), //
            TitleListEnum.L4);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.ROW, //
            false, //
            HomeSupport.CLOSED_PACKAGES),//
            TitleListEnum.L3);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER, //
            true, //
            HomeSupport.CLOSED_PACKAGES), //
            TitleListEnum.L3);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.ROW, //
            true, //
            HomeSupport.CARS), //
            TitleListEnum.L1);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.ROW, //
            false, //
            HomeSupport.CARS),//
            TitleListEnum.L3);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER, //
            true, //
            HomeSupport.CARS),//
            TitleListEnum.L1);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.ROW, //
            true, //
            HomeSupport.CRUISES), //
            TitleListEnum.L3);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.ROW, //
            false, //
            HomeSupport.CRUISES), //
            TitleListEnum.L24);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER, //
            true, //
            HomeSupport.CRUISES), //
            TitleListEnum.L1);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.ROW, //
            true, //
            HomeSupport.ACTIVITIES), //
            TitleListEnum.L3);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.ROW, //
            false, //
            HomeSupport.ACTIVITIES), //
            TitleListEnum.L24);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER, //
            true, //
            HomeSupport.ACTIVITIES),//
            TitleListEnum.L1);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.ROW, //
            true, //
            HomeSupport.VACATION_RENTALS), //
            TitleListEnum.L1);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.ROW, //
            false, //
            HomeSupport.VACATION_RENTALS), //
            TitleListEnum.L3);

        this.titleEngine.add(new MultiProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER, //
            true, //
            HomeSupport.VACATION_RENTALS), //
            TitleListEnum.L1);
    }

    private void buildBuy() {

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.THANKS).pr1(Product.ACTIVITIES).flow2(Flow.THANKS).build(),//
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L30);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.ACTIVITIES_RECOMMENDER,//
            ItemType.ACTIVITY), //
            TitleListEnum.L30);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_RENTALS_DESTINATION,//
            ItemType.VACATION_RENTAL_DESTINATION), //
            TitleListEnum.L7);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_RENTALS_DESTINATION,//
            ItemType.VACATION_RENTAL_DESTINATION), //
            TitleListEnum.L7);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.ACTIVITIES_DISNEY,//
            ItemType.ACTIVITY), //
            TitleListEnum.L48);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.ACTIVITIES_UNIVERSAL,//
            Param.builder().city(City.ORLANDO).build(),//
            ItemType.ACTIVITY), //
            TitleListEnum.L49);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.ACTIVITIES_RANKING_LOCATION,//
            ItemType.ACTIVITY), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.ACTIVITIES_RANKING_LOCATION,//
            ItemType.ACTIVITY), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.BUY_DESTINATION,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.BUY_DESTINATION,//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L4);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.BUY_DESTINATION,//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.BUY_DESTINATION,//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L4);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.CARS_CATEGORY_DESTINATION,//
            ItemType.CAR_CATEGORY), //
            TitleListEnum.L32);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.CARS_RANKING_COUNTRY,//
            Param.builder().carRankingType(CarRankingType.SEARCH).build(),//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.CARS_RANKING_COUNTRY,//
            Param.builder().carRankingType(CarRankingType.SEARCH).build(),//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L32);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.CARS_RANKING_COUNTRY,//
            Param.builder().carRankingType(CarRankingType.CHECKOUT).build(),//
            ItemType.CAR_CATEGORY), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.CARS_RANKING_COUNTRY,//
            Param.builder().carRankingType(CarRankingType.SEARCH).build(),//
            ItemType.CAR_CATEGORY), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.CRUISE_BUY_DESTINATION_RECOM,//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.CRUISE_BUY_DESTINATION_RECOM,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.CRUISE_RANKING_IP,//
            ItemType.CRUISE_REGION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_HOTELS_DESTINATION,//
            ItemType.HOTEL), //
            TitleListEnum.L33);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_HOTELS_DESTINATION,//
            ItemType.HOTEL), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.NEARBY_BUY_DESTINATION,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L14);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.SEARCH_DESTINATION,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.CAR_CATEGORY), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.CAR_CATEGORY), //
            TitleListEnum.L9);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L9);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L9);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L9);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L30);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.CRUISE_REGION), //
            TitleListEnum.L24);


        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build(),//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build(),//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.THANKS).pr1(Product.FLIGHTS).flow2(Flow.THANKS).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L9);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.THANKS).pr1(Product.FLIGHTS).flow2(Flow.THANKS).addBuy(true).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L9);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.FLIGHTS).flow2(Flow.SEARCH).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L9);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.THANKS).pr1(Product.FLIGHTS).flow2(Flow.THANKS).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.THANKS).pr1(Product.HOTELS).flow2(Flow.THANKS).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.HOTELS).flow2(Flow.SEARCH).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.HOTELS).flow2(Flow.SEARCH).addBuy(true).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.THANKS).pr1(Product.FLIGHTS).flow2(Flow.SEARCH).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.THANKS).pr1(Product.FLIGHTS).flow2(Flow.SEARCH).addBuy(true).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.THANKS).pr1(Product.HOTELS).flow2(Flow.THANKS).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.THANKS).pr1(Product.HOTELS).flow2(Flow.THANKS).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.CARS).flow2(Flow.SEARCH).build(),//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.CARS).flow2(Flow.SEARCH).countryType(CountryType.DOMESTIC)
                .build(),//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.CARS).flow2(Flow.SEARCH).countryType(CountryType.DOMESTIC)
                .build(),//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(
            new MonoProductTitleKey(ActivityType.BUY, //
                SectionType.ROW,//
                SectionFunctionCode.RECOMMEND,//
                Param.builder().flow1(Flow.SEARCH).pr1(Product.CARS).flow2(Flow.SEARCH).addSearch(true)
                    .countryType(CountryType.DOMESTIC).build(),//
                ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(
            new MonoProductTitleKey(ActivityType.BUY, //
                SectionType.OFFER,//
                SectionFunctionCode.RECOMMEND,//
                Param.builder().flow1(Flow.SEARCH).pr1(Product.CARS).flow2(Flow.SEARCH).addSearch(true)
                    .countryType(CountryType.DOMESTIC).build(),//
                ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.FLIGHTS).flow2(Flow.SEARCH).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.FLIGHTS).flow2(Flow.SEARCH).addBuy(true).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.CLOSED_PACKAGES).flow2(Flow.SEARCH).build(),//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC).build(),//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L19);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L18);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.CRUISE_REGIONS).build(),//
            ItemType.CRUISE_REGION), //
            TitleListEnum.L24);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.CRUISE_DID).build(),//
            ItemType.CRUISE), //
            TitleListEnum.L24);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L16);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L15);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L11);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L12);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L18);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.CRUISE_REGIONS).build(),//
            ItemType.CRUISE_REGION), //
            TitleListEnum.L24);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.CRUISE_BUY,//
            Param.builder().flow2(Flow.THANKS).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L15);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L11);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build(),//
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L30);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC).build(),//
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.ACTIVITIES_DISNEY,//
            ItemType.ACTIVITY), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_RENTALS_DESTINATION,//
            ItemType.VACATION_RENTAL), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_RENTALS_DESTINATION,//
            ItemType.VACATION_RENTAL), //
            TitleListEnum.L59);
    }

    private void buildNoHistory() {

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.CARS_RANKING_COUNTRY,//
            Param.builder().carRankingType(CarRankingType.CHECKOUT).build(),//
            ItemType.CAR_CATEGORY), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.CARS_RANKING_COUNTRY,//
            Param.builder().carRankingType(CarRankingType.SEARCH).build(),//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.OFFER,//
            SectionFunctionCode.CARS_RANKING_COUNTRY,//
            Param.builder().carRankingType(CarRankingType.SEARCH).build(),//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L23);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.CARS_RANKING_COUNTRY,//
            Param.builder().carRankingType(CarRankingType.CHECKOUT).flow1(Flow.SEARCH).pr1(Product.CARS).flow2(Flow.SEARCH)
                .countryType(CountryType.DOMESTIC).build(),//
            ItemType.CAR_CATEGORY), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.CRUISE_RANKING_IP,//
            ItemType.CRUISE_REGION), //
            TitleListEnum.L24);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build(),//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.CRUISE_REGIONS).build(),//
            ItemType.CRUISE_REGION), //
            TitleListEnum.L24);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC).build(),//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L19);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L16);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L12);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L18);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L15);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L11);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.OFFER,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.OFFER,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.CRUISE_REGIONS).build(),//
            ItemType.CRUISE_REGION), //
            TitleListEnum.L24);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.OFFER,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L18);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.OFFER,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L15);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.OFFER,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L11);

        this.titleEngine.add(new MonoProductTitleKey(
            ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC).build(),
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L51);

        this.titleEngine.add(new MonoProductTitleKey(
            ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L50);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.OFFER,//
            SectionFunctionCode.ACTIVITIES_DISNEY,//
            ItemType.ACTIVITY), //
            TitleListEnum.L48);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.ACTIVITIES_UNIVERSAL,//
            Param.builder().city(City.ORLANDO).build(), //
            ItemType.ACTIVITY), //
            TitleListEnum.L49);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.OFFER,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            ItemType.VACATION_RENTAL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC).build(),//
            ItemType.VACATION_RENTAL_DESTINATION), //
            TitleListEnum.L57);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.NO_HISTORY, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.VACATION_RENTAL_DESTINATION), //
            TitleListEnum.L56);
    }



    private void buildSearch() {

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_RENTALS_DESTINATION,//
            ItemType.VACATION_RENTAL), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_RENTALS_DESTINATION,//
            ItemType.VACATION_RENTAL), //
            TitleListEnum.L59);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.VACATIONRENTALS).flow2(Flow.SEARCH).addSearch(true).build(),//
            ItemType.VACATION_RENTAL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.ACTIVITIES_DISNEY,//
            ItemType.ACTIVITY), //
            TitleListEnum.L48);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.ACTIVITIES_UNIVERSAL,//
            Param.builder().city(City.ORLANDO).build(), //
            ItemType.ACTIVITY), //
            TitleListEnum.L49);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.ACTIVITIES_VIEWED,//
            ItemType.ACTIVITY), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.ACTIVITIES_VIEWED,//
            ItemType.ACTIVITY), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.ACTIVITIES_RANKING_LOCATION,//
            ItemType.ACTIVITY), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_DESTINATION_BY_CLUSTER,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_DESTINATION_BY_CLUSTER,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_DESTINATION_BY_CLUSTER,//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.CARS_RANKING_COUNTRY,//
            Param.builder().carRankingType(CarRankingType.SEARCH).build(),//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.CARS_RANKING_COUNTRY,//
            Param.builder().carRankingType(CarRankingType.CHECKOUT).build(),//
            ItemType.CAR_CATEGORY), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.CARS_RANKING_COUNTRY,//
            Param.builder().carRankingType(CarRankingType.SEARCH).build(),//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.CARS_CATEGORY_DESTINATION,//
            ItemType.CAR_CATEGORY), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.CARS_SEARCH,//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.FLIGHTS_RANKING_BY_CLUSTER,//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_CRUISE_REGION,//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_CRUISE_REGION,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.CRUISE_SEARCH,//
            ItemType.CRUISE), //
            TitleListEnum.L25);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.CRUISE_SEARCH,//
            ItemType.CRUISE_REGION), //
            TitleListEnum.L25);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.CRUISE_RANKING_IP,//
            ItemType.CRUISE_REGION), //
            TitleListEnum.L24);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.FLIGHTS_RANKING_BY_CLUSTER,//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_HOTELS_DESTINATION,//
            ItemType.HOTEL), //
            TitleListEnum.L33);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_HOTELS_DESTINATION,//
            ItemType.HOTEL), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.CRUISE_REGIONS).build(),//
            ItemType.CRUISE_REGION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L18);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L11);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build(),//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build(),//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.CRUISE_DID).build(),//
            ItemType.CRUISE), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.CRUISE_REGIONS).build(),//
            ItemType.CRUISE_REGION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC).build(),//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L19);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L12);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L18);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L11);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOTEL_SEARCH_AND_RECOMMEND,//
            ItemType.HOTEL), //
            TitleListEnum.L33);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOTEL_SEARCH_AND_RECOMMEND,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.HOTEL_SEARCH_AND_RECOMMEND,//
            ItemType.HOTEL), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.HOTEL_SEARCH_AND_RECOMMEND,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.ACTIVITIES).flow2(Flow.SEARCH).build(),//
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.ACTIVITIES).flow2(Flow.SEARCH).addBuy(true).build(),//
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.FLIGHTS).flow2(Flow.SEARCH).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.THANKS).pr1(Product.HOTELS).flow2(Flow.THANKS).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.HOTELS).flow2(Flow.SEARCH).addSearch(true).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.FLIGHTS).flow2(Flow.SEARCH).addSearch(true).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.CLOSED_PACKAGES).flow2(Flow.SEARCH).build(),//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.CLOSED_PACKAGES).flow2(Flow.SEARCH).addSearch(true).build(),//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.CLOSED_PACKAGES).flow2(Flow.SEARCH).addSearch(true).build(),//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.THANKS).pr1(Product.HOTELS).flow2(Flow.THANKS).addSearch(true).build(),//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.THANKS).pr1(Product.HOTELS).flow2(Flow.THANKS).addSearch(true).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.HOTELS).flow2(Flow.SEARCH).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.DETAIL).pr1(Product.HOTELS).flow2(Flow.DETAIL).build(),//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L33);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.CARS).flow2(Flow.SEARCH).build(),//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.CARS).flow2(Flow.SEARCH).addSearch(true).build(),//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(
            new MonoProductTitleKey(ActivityType.SEARCH, //
                SectionType.ROW,//
                SectionFunctionCode.RECOMMEND,//
                Param.builder().flow1(Flow.SEARCH).pr1(Product.CARS).flow2(Flow.SEARCH).addSearch(true)
                    .countryType(CountryType.DOMESTIC).build(),//
                ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);


        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.SEARCH).pr1(Product.CARS).flow2(Flow.SEARCH).countryType(CountryType.DOMESTIC)
                .build(),//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.DETAIL).pr1(Product.HOTELS).flow2(Flow.DETAIL).build(),//
            ItemType.HOTEL), //
            TitleListEnum.L33);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RECOMMEND,//
            Param.builder().flow1(Flow.DETAIL).pr1(Product.HOTELS).flow2(Flow.DETAIL).addSearch(true).build(),//
            ItemType.HOTEL), //
            TitleListEnum.L33);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.SEARCH_DESTINATION,//
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.SEARCH_DESTINATION,//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.SEARCH_DESTINATION,//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.SEARCH_DESTINATION,//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.SEARCH_DESTINATION,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.CAR_CATEGORY), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.CRUISE_REGION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L6);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.CAR_CATEGORY), //
            TitleListEnum.L9);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.CRUISE_REGION), //
            TitleListEnum.L9);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L9);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L9);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L9);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.RANKING_DYNAMIC_PRODUCT,//
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L3);


        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build(),//
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_DOMESTIC).build(),//
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L51);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.HOT_RANKING_IP_COUNTRY,//
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL).build(),//
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L50);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.ROW,//
            SectionFunctionCode.ACTIVITIES_UNIVERSAL,//
            ItemType.ACTIVITY), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.ACTIVITIES_DISNEY,//
            ItemType.ACTIVITY), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.LAST_DETAIL_FUNCTION,//
            ItemType.HOTEL), //
            TitleListEnum.L1);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.SEARCH, //
            SectionType.OFFER,//
            SectionFunctionCode.LIKE_HOTEL_FUNCTION,//
            ItemType.HOTEL), //
            TitleListEnum.L1);
    }

    private void buildLastResort() {
        this.titleEngine.add(new MonoProductTitleKey(ActivityType.LAST_RSRT, //
            SectionType.ROW,//
            SectionFunctionCode.LAST_RESORT,//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.LAST_RSRT, //
            SectionType.ROW,//
            SectionFunctionCode.LAST_RESORT,//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.LAST_RSRT, //
            SectionType.ROW,//
            SectionFunctionCode.LAST_RESORT,//
            ItemType.FLIGHT_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.LAST_RSRT, //
            SectionType.ROW,//
            SectionFunctionCode.LAST_RESORT,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.LAST_RSRT, //
            SectionType.ROW,//
            SectionFunctionCode.LAST_RESORT,//
            ItemType.VACATION_RENTAL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.LAST_RSRT, //
            SectionType.OFFER,//
            SectionFunctionCode.LAST_RESORT,//
            ItemType.VACATION_RENTAL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.LAST_RSRT, //
            SectionType.OFFER,//
            SectionFunctionCode.LAST_RESORT,//
            ItemType.CAR_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.LAST_RSRT, //
            SectionType.OFFER,//
            SectionFunctionCode.LAST_RESORT,//
            ItemType.CLOSED_PACKAGES_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.LAST_RSRT, //
            SectionType.OFFER,//
            SectionFunctionCode.LAST_RESORT,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.LAST_RSRT, //
            SectionType.OFFER,//
            SectionFunctionCode.LAST_RESORT,//
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.LAST_RSRT, //
            SectionType.ROW,//
            SectionFunctionCode.LAST_RESORT,//
            ItemType.ACTIVITY_DESTINATION), //
            TitleListEnum.L3);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.LAST_RSRT, //
            SectionType.OFFER,//
            SectionFunctionCode.LAST_RESORT,//
            ItemType.CRUISE_REGION), //
            TitleListEnum.L24);

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.LAST_RSRT, //
            SectionType.ROW,//
            SectionFunctionCode.LAST_RESORT,//
            ItemType.CRUISE_REGION), //
            TitleListEnum.L24);
    }

}
