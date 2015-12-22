package com.despegar.p13n.hestia.recommend.allinone.rules.version;

import org.springframework.beans.factory.annotation.Autowired;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.City;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.Anticipation;
import com.despegar.p13n.hestia.recommend.allinone.activity.LastAction;
import com.despegar.p13n.hestia.recommend.allinone.rules.BuyProductSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.CountrySupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.NoHistoryEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.builder.SectionRuleBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function.CarRankingType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function.CountryType;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.RuleDefSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.SectionRuleContent;

public class NoHistoryRules {

    private NoHistoryEngine engine;

    @Autowired
    public NoHistoryRules(NoHistoryEngine engine) {
        this.engine = engine;
        this.build();
    }

    private void build() {
        this.buildNoHistory();
    }

    private void buildNoHistory() {
        this.buildNoHistoryFlights();
        this.buildNoHistoryHotels();
        this.buildNoHistoryPackages();
        this.buildNoHistoryCars();
        this.buildNoHistoryCruises();
        this.buildNoHistoryHome();
        this.buildNoHistoryActivities();
        this.buildNoHistoryInsurance();
        this.buildNoHistoryVacationRentals();
        this.buildNoHistoryInternationalSite();
    }

    private void buildNoHistoryInternationalSite() {

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.FLIGHTS)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.INT)//
            .build();
        SectionRuleContent international = SectionRuleBuilder.create()//
            .withOffer(Product.FLIGHTS, Function.RANKING_ANY)//
            .withRow1(Product.FLIGHTS, Function.RANKING_ANY)//
            .withRow2(Product.HOTELS, Function.RANKING_ANY)//
            .withRow3(Product.CARS, Function.CAR_RANKING_COUNTRY_SEARCH)//
            .build();

        this.engine.addForNoHistory(rta, international);

        rta = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.HOTELS)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.INT)//
            .build();
        international = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.RANKING_ANY)//
            .withRow1(Product.HOTELS, Function.RANKING_ANY)//
            .withRow2(Product.HOTELS, Function.RANKING_ANY)//
            .withRow3(Product.CARS, Function.CAR_RANKING_COUNTRY_SEARCH)//
            .build();
        this.engine.addForNoHistory(rta, international);

        rta = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.CARS)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.INT)//
            .build();
        international = SectionRuleBuilder.create()//
            .withOffer(Product.CARS, Function.CAR_RANKING_COUNTRY_SEARCH)//
            .withRow1(Product.CARS, Function.CAR_RANKING_COUNTRY_SEARCH)//
            .withRow2(Product.CARS, Function.CAR_RANKING_COUNTRY_SEARCH)//
            .withRow3(Product.HOTELS, Function.RANKING_ANY)//
            .build();
        this.engine.addForNoHistory(rta, international);

        rta = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.CLOSED_PACKAGES)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.INT)//
            .build();
        international = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.RANKING_INTERNATIONAL)//
            .withRow1(Product.HOTELS, Function.RANKING_DOMESTIC)//
            .withRow2(Product.FLIGHTS, Function.RANKING_ANY)//
            .withRow3(Product.CARS, Function.CAR_RANKING_COUNTRY_SEARCH)//
            .build();
        this.engine.addForNoHistory(rta, international);

        rta = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.HOME_AS_PRODUCT)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.INT)//
            .build();
        international = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.RANKING_ANY)//
            .withRow1(Product.HOTELS, Function.RANKING_ANY)//
            .withRow2(Product.FLIGHTS, Function.RANKING_ANY)//
            .withRow3(Product.CARS, Function.CAR_RANKING_COUNTRY_SEARCH)//
            .build();
        this.engine.addForNoHistory(rta, international);
    }

    private void buildNoHistoryFlights() {
        SectionRuleContent ruleFlightsContent = SectionRuleBuilder.create()//
            .withOffer(Product.FLIGHTS, Function.RANKING_INTERNATIONAL)//
            .withRow1(Product.FLIGHTS, Function.RANKING_INTERNATIONAL)//
            .withRow2(Product.FLIGHTS, Function.RANKING_DOMESTIC)//
            .withRow3(Product.HOTELS, Function.RANKING_ANY).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.FLIGHTS)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForNoHistory(rta, ruleFlightsContent);
        this.engine.addForNoHistory(RuleDefSection.forAllCountries(rta), ruleFlightsContent);

        SectionRuleContent prContent = SectionRuleBuilder.copy(ruleFlightsContent)//
            .overRow2(Product.FLIGHTS, Function.RANKING_INTERNATIONAL).build();
        this.engine.addForNoHistory(RuleDefSection.forCountrySupport(rta, CountrySupport.PR), prContent);
    }


    private void buildNoHistoryHotels() {
        SectionRuleContent ruleHotelsContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.RANKING_INTERNATIONAL)//
            .withRow1(Product.HOTELS, Function.RANKING_INTERNATIONAL)//
            .withRow2(Product.HOTELS, Function.RANKING_DOMESTIC)//
            .withRow3(Product.CLOSED_PACKAGES, Function.RANKING_ANY).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.HOTELS)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForNoHistory(rta, ruleHotelsContent);
        SectionRuleContent rcDefault = SectionRuleBuilder.copy(ruleHotelsContent)//
            .overRow3(Product.CARS, Function.CAR_RANKING_COUNTRY_SEARCH).build();
        this.engine.addForNoHistory(RuleDefSection.forAllCountries(rta), rcDefault);

        ruleHotelsContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.RANKING_INTERNATIONAL)//
            .withRow1(Product.HOTELS, Function.RANKING_INTERNATIONAL)//
            .withRow2(Product.HOTELS, Function.RANKING_DOMESTIC)//
            .withRow3(Product.CARS, Function.CAR_SEARCH).build();
        this.engine.addForNoHistory(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), rcDefault);

    }


    private void buildNoHistoryPackages() {
        SectionRuleContent packagesContent = SectionRuleBuilder.create()//
            .withOffer(Product.CLOSED_PACKAGES, Function.RANKING_INTERNATIONAL)//
            .withRow1(Product.CLOSED_PACKAGES, Function.RANKING_INTERNATIONAL)//
            .withRow2(Product.CLOSED_PACKAGES, Function.RANKING_DOMESTIC)//
            .withRow3(Product.FLIGHTS, Function.RANKING_ANY)//
            .build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.CLOSED_PACKAGES)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForNoHistory(rta, packagesContent);
        SectionRuleContent rcDefault = SectionRuleBuilder.copy(packagesContent)//
            .overRow3(Product.HOTELS, Function.RANKING_ANY).build();
        this.engine.addForNoHistory(RuleDefSection.forAllCountries(rta), rcDefault);

        packagesContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.RANKING_INTERNATIONAL)//
            .withRow1(Product.HOTELS, Function.RANKING_DOMESTIC)//
            .withRow2(Product.FLIGHTS, Function.RANKING_ANY)//
            .withRow3(Product.CARS, Function.CAR_RANKING_COUNTRY_SEARCH)//
            .build();
        this.engine.addForNoHistory(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), rcDefault);
    }

    private void buildNoHistoryCars() {
        SectionRuleContent ruleCarsContent = SectionRuleBuilder
            .create()
            //
            .withOffer(Product.CARS, Function.CAR_RANKING_COUNTRY_SEARCH.carRankingType(CarRankingType.SEARCH))
            //
            .withRow1(
                Product.CARS,
                Function.CAR_RANKING_COUNTRY_SEARCH.carRankingType(CarRankingType.SEARCH).countryType(
                    CountryType.INTERNATIONAL))//
            .withRow2(Product.CARS,
                Function.CAR_RANKING_COUNTRY_SEARCH.carRankingType(CarRankingType.SEARCH).countryType(CountryType.DOMESTIC))//
            .withRow3(Product.HOTELS, Function.RANKING_ANY).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.CARS)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForNoHistory(rta, ruleCarsContent);

        SectionRuleContent rcDefault = SectionRuleBuilder.copy(ruleCarsContent)
            .overRow1(Product.CARS, Function.CAR_RANKING_COUNTRY_SEARCH.carRankingType(CarRankingType.SEARCH))//
            .overRow2(Product.FLIGHTS, Function.RANKING_ANY).build();
        this.engine.addForNoHistory(RuleDefSection.forAllCountries(rta), rcDefault);
    }

    private void buildNoHistoryCruises() {
        SectionRuleContent ruleCruisesContent = SectionRuleBuilder.create()//
            .withOffer(Product.CRUISES, Function.RANKING_REGION)//
            .withRow1(Product.CRUISES, Function.RANKING_REGION)//
            .withRow2(Product.CRUISES, Function.RANKING_REGION)//
            .withRow3(Product.FLIGHTS, Function.RANKING_ANY).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.CRUISES)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForNoHistory(rta, ruleCruisesContent);
        SectionRuleContent rcDefault = SectionRuleBuilder.copy(ruleCruisesContent)//
            .overRow3(Product.HOTELS, Function.RANKING_ANY).build();
        this.engine.addForNoHistory(RuleDefSection.forAllCountries(rta), rcDefault);

    }


    private void buildNoHistoryHome() {
        SectionRuleContent ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.RANKING_ANY)//
            .withRow1(Product.HOTELS, Function.RANKING_ANY)//
            .withRow2(Product.FLIGHTS, Function.RANKING_ANY)//
            .withRow3(Product.HOTELS, Function.RANKING_ANY).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.HOME_AS_PRODUCT)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForNoHistory(rta, ruleHomeContent);
        SectionRuleContent rcDefault = SectionRuleBuilder.copy(ruleHomeContent)//
            .overRow1(Product.HOTELS, Function.RANKING_ANY)//
            .overRow2(Product.HOTELS, Function.RANKING_ANY)//
            .overRow3(Product.CARS, Function.CAR_RANKING_COUNTRY_CHECKOUT).build();
        this.engine.addForNoHistory(RuleDefSection.forAllCountries(rta), rcDefault);
    }



    private void buildNoHistoryActivities() {

        SectionRuleContent ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.ACTIVITIES, Function.ACTIVITIES_DISNEY)//
            .withRow1(Product.ACTIVITIES, Function.ACTIVITIES_UNIVERSAL.city(City.ORLANDO))//
            .withRow2(Product.ACTIVITIES, Function.RANKING_INTERNATIONAL)//
            .withRow3(Product.ACTIVITIES, Function.RANKING_DOMESTIC).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.ACTIVITIES)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForNoHistory(rta, ruleHomeContent);
        ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.ACTIVITIES, Function.RANKING_ANY)//
            .withRow1(Product.ACTIVITIES, Function.RANKING_ANY)//
            .withRow2(Product.ACTIVITIES, Function.RANKING_INTERNATIONAL)//
            .withRow3(Product.ACTIVITIES, Function.RANKING_DOMESTIC).build();
        this.engine.addForNoHistory(RuleDefSection.forAllCountries(rta), ruleHomeContent);
    }

    private void buildNoHistoryInsurance() {
        SectionRuleContent ruleHotelsContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.RANKING_INTERNATIONAL)//
            .withRow1(Product.HOTELS, Function.RANKING_INTERNATIONAL)//
            .withRow2(Product.HOTELS, Function.RANKING_DOMESTIC)//
            .withRow3(Product.CLOSED_PACKAGES, Function.RANKING_ANY).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.INSURANCE)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForNoHistory(rta, ruleHotelsContent);
        SectionRuleContent rcDefault = SectionRuleBuilder.copy(ruleHotelsContent)//
            .overRow3(Product.CARS, Function.CAR_RANKING_COUNTRY_SEARCH).build();
        this.engine.addForNoHistory(RuleDefSection.forAllCountries(rta), rcDefault);
    }

    private void buildNoHistoryVacationRentals() {
        SectionRuleContent ruleHotelsContent = SectionRuleBuilder.create()//
            .withOffer(Product.VACATIONRENTALS, Function.RANKING_ANY)//
            .withRow1(Product.VACATIONRENTALS, Function.RANKING_DOMESTIC)//
            .withRow2(Product.VACATIONRENTALS, Function.RANKING_INTERNATIONAL)//
            .withRow3(Product.HOTELS, Function.RANKING_ANY).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.VACATION_RENTALS)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForNoHistory(rta, ruleHotelsContent);

        SectionRuleContent rcDefault = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.RANKING_INTERNATIONAL)//
            .withRow1(Product.HOTELS, Function.RANKING_INTERNATIONAL)//
            .withRow2(Product.HOTELS, Function.RANKING_DOMESTIC)//
            .withRow3(Product.CARS, Function.CAR_RANKING_COUNTRY_SEARCH).build();
        this.engine.addForNoHistory(RuleDefSection.forAllCountries(rta), rcDefault);
    }
}
