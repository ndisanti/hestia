package com.despegar.p13n.hestia.recommend.allinone.rules.version;

import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.CountrySupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.ItemRuleEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.builder.HomeRuleBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.item.IdFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent.HomeRuleContent;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent.RuleDefItem;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent.RuleDefItem.RuleBuilder;

public class MultiDestinationRules {

    private ItemRuleEngine itemRuleEngine;

    public MultiDestinationRules(ItemRuleEngine itemRuleEngine) {
        this.itemRuleEngine = itemRuleEngine;
        this.build();
    }

    private void build() {
        this.buildBuy();
        this.buildSearch();
    }


    private void buildBuy() {
        this.buildBuyFlights();
        this.buildBuyHotels();
        this.buildBuyCars();
        this.buildBuyPackages();
        this.buildBuyCruises();
        this.buildBuyActivities();
        this.buildBuyHome();
        this.buildBuyInsurance();
        this.buildBuyVacationRentals();
    }

    private void buildBuyHome() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.HOTELS)//
            .withRow1(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.BUY_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.BUY)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.HOME_AS_PRODUCT).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);


    }

    private void buildBuyActivities() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.ACTIVITIES)//
            .withRow1(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.BUY_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.BUY)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.ACTIVITIES).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);
    }

    private void buildBuyCruises() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.CRUISES)//
            .withRow1(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.BUY_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.BUY)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.CRUISES).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);

    }

    private void buildBuyPackages() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.BUY_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow1(IdFunction.BUY_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.BUY_DESTINATIONS.index(2), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.BUY_DESTINATIONS.index(3), ProductFuncCode.PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.BUY)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.CLOSED_PACKAGES).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);
    }

    private void buildBuyCars() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.CARS)//
            .withRow1(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.BUY_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.BUY)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.CARS).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);
    }

    private void buildBuyHotels() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.BUY_DESTINATION, ProductFuncCode.HOTELS)//
            .withRow1(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.BUY_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.BUY)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.HOTELS).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);

    }

    private void buildBuyVacationRentals() {

        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.VACATION_RENTALS)//
            .withRow1(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.BUY_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.BUY_DESTINATIONS.index(2), ProductFuncCode.PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.BUY)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.VACATION_RENTALS).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);

    }

    private void buildBuyFlights() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.BUY_DESTINATIONS.index(2), ProductFuncCode.FLIGHTS)//
            .withRow1(IdFunction.BUY_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.PRODUCTS)//
            .build();
        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.BUY)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.FLIGHTS).build();
        this.itemRuleEngine.addForMultiDestination(rule, hrc);
    }

    private void buildBuyInsurance() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.BUY_DESTINATION, ProductFuncCode.HOTELS)//
            .withRow1(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.BUY_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.BUY_DESTINATION.index(1), ProductFuncCode.PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.BUY)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.INSURANCE).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);

    }

    private void buildSearch() {
        this.buildSearchFlights();
        this.buildSearchHotels();
        this.buildSearchCars();
        this.buildSearchPackages();
        this.buildSearchCruises();
        this.buildSearchActivities();
        this.buildSearchHome();
        this.buildSearchInsurance();
        this.buildSearchVacationRentals();
    }

    private void buildSearchHome() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.HOTELS)//
            .withRow1(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.SEARCH_DESTINATIONS.index(2), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.SEARCH_DESTINATIONS, ProductFuncCode.MULTIDESTINATION_SEARCH_PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.SEARCH)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.HOME_AS_PRODUCT).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);

    }

    private void buildSearchActivities() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.ACTIVITIES)//
            .withRow1(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.SEARCH_DESTINATIONS.index(2), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.SEARCH_DESTINATIONS, ProductFuncCode.MULTIDESTINATION_SEARCH_PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.SEARCH)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.ACTIVITIES).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);

    }

    private void buildSearchCruises() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.CRUISES)//
            .withRow1(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.SEARCH_DESTINATIONS.index(2), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.SEARCH_DESTINATIONS, ProductFuncCode.MULTIDESTINATION_SEARCH_PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.SEARCH)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.CRUISES).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);

    }

    private void buildSearchPackages() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow1(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.SEARCH_DESTINATIONS.index(2), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.SEARCH_DESTINATIONS, ProductFuncCode.MULTIDESTINATION_SEARCH_PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.SEARCH)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.CLOSED_PACKAGES).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);

    }

    private void buildSearchCars() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.CARS)//
            .withRow1(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.SEARCH_DESTINATIONS.index(2), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.SEARCH_DESTINATIONS, ProductFuncCode.MULTIDESTINATION_SEARCH_PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.SEARCH)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.CARS).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);

    }

    private void buildSearchHotels() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.HOTELS)//
            .withRow1(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.SEARCH_DESTINATIONS.index(2), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.SEARCH_DESTINATIONS, ProductFuncCode.MULTIDESTINATION_SEARCH_PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.SEARCH)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.HOTELS).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);

    }

    private void buildSearchVacationRentals() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.VACATION_RENTALS)//
            .withRow1(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.SEARCH_DESTINATIONS.index(2), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.SEARCH_DESTINATIONS, ProductFuncCode.MULTIDESTINATION_SEARCH_PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.SEARCH)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.VACATION_RENTALS).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);
    }

    private void buildSearchFlights() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.FLIGHTS)//
            .withRow1(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.SEARCH_DESTINATIONS.index(2), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.SEARCH_DESTINATIONS, ProductFuncCode.MULTIDESTINATION_SEARCH_PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.SEARCH)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.FLIGHTS).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);

    }

    private void buildSearchInsurance() {
        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.HOTELS)//
            .withRow1(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.SEARCH_DESTINATIONS.index(2), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.SEARCH_DESTINATIONS, ProductFuncCode.MULTIDESTINATION_SEARCH_PRODUCTS)//
            .build();

        RuleDefItem rule = RuleBuilder.create().//
            activity(ActivityType.SEARCH)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.INSURANCE).build();

        this.itemRuleEngine.addForMultiDestination(rule, hrc);

    }


}
