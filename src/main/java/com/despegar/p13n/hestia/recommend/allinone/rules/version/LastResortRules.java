package com.despegar.p13n.hestia.recommend.allinone.rules.version;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.Anticipation;
import com.despegar.p13n.hestia.recommend.allinone.activity.LastAction;
import com.despegar.p13n.hestia.recommend.allinone.rules.BuyProductSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.CountrySupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.LastResortEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.builder.SectionRuleBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.RuleDefSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.SectionRuleContent;

public class LastResortRules {

    private LastResortEngine engine;

    public LastResortRules(LastResortEngine engine) {
        this.engine = engine;
        this.build();
    }

    private void build() {
        this.buildLastResortActivities();
        this.buildLastResortFlights();
        this.buildLastResortHotels();
        this.buildLastResortCars();
        this.buildLastResortClosed();
        this.buildLastResortCruises();
        this.buildLastResortInsurances();
        this.buildLastResortHome();
        this.buildLastResortVacationRentals();
    }

    private void buildLastResortActivities() {

        SectionRuleContent ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.ACTIVITIES, Function.LAST_RESORT)//
            .withRow1(Product.ACTIVITIES, Function.LAST_RESORT)//
            .withRow2(Product.ACTIVITIES, Function.LAST_RESORT)//
            .withRow3(Product.ACTIVITIES, Function.LAST_RESORT).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.LAST_RSRT)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.ACTIVITIES)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForAny(rta, ruleHomeContent);
        this.engine.addForAny(RuleDefSection.forAllCountries(rta), ruleHomeContent);
        this.engine.addForAny(RuleDefSection.forOnlyHotels(rta), ruleHomeContent);
    }

    private void buildLastResortFlights() {

        SectionRuleContent ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.FLIGHTS, Function.LAST_RESORT)//
            .withRow2(Product.FLIGHTS, Function.LAST_RESORT)//
            .withRow3(Product.HOTELS, Function.LAST_RESORT).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.LAST_RSRT)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.FLIGHTS)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForAny(rta, ruleHomeContent);

        ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.HOTELS, Function.LAST_RESORT)//
            .withRow3(Product.CARS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forAllCountries(rta), ruleHomeContent);

        ruleHomeContent = SectionRuleBuilder.copy(ruleHomeContent)//
            .overRow3(Product.HOTELS, Function.LAST_RESORT)//
            .build();

        this.engine.addForAny(RuleDefSection.forOnlyHotels(rta), ruleHomeContent);

        SectionRuleContent international = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.FLIGHTS, Function.LAST_RESORT)//
            .withRow2(Product.FLIGHTS, Function.LAST_RESORT)//
            .withRow3(Product.CARS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forInternational(rta), international);
    }

    private void buildLastResortHotels() {

        SectionRuleContent ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.FLIGHTS, Function.LAST_RESORT)//
            .withRow3(Product.HOTELS, Function.LAST_RESORT).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.LAST_RSRT)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.HOTELS)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForAny(rta, ruleHomeContent);

        ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.HOTELS, Function.LAST_RESORT)//
            .withRow3(Product.CARS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forAllCountries(rta), ruleHomeContent);

        ruleHomeContent = SectionRuleBuilder.copy(ruleHomeContent)//
            .overRow3(Product.HOTELS, Function.LAST_RESORT)//
            .build();

        this.engine.addForAny(RuleDefSection.forOnlyHotels(rta), ruleHomeContent);

        SectionRuleContent international = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.FLIGHTS, Function.LAST_RESORT)//
            .withRow3(Product.CARS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forInternational(rta), international);
    }


    private void buildLastResortCars() {

        SectionRuleContent ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.CARS, Function.LAST_RESORT)//
            .withRow1(Product.CARS, Function.LAST_RESORT)//
            .withRow2(Product.HOTELS, Function.LAST_RESORT)//
            .withRow3(Product.FLIGHTS, Function.LAST_RESORT).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.LAST_RSRT)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.CARS)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForAny(rta, ruleHomeContent);

        ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.CARS, Function.LAST_RESORT)//
            .withRow1(Product.CARS, Function.LAST_RESORT)//
            .withRow2(Product.HOTELS, Function.LAST_RESORT)//
            .withRow3(Product.HOTELS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forAllCountries(rta), ruleHomeContent);

        ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.HOTELS, Function.LAST_RESORT)//
            .withRow3(Product.HOTELS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forOnlyHotels(rta), ruleHomeContent);

        SectionRuleContent international = SectionRuleBuilder.create()//
            .withOffer(Product.CARS, Function.LAST_RESORT)//
            .withRow1(Product.CARS, Function.LAST_RESORT)//
            .withRow2(Product.HOTELS, Function.LAST_RESORT)//
            .withRow3(Product.FLIGHTS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forInternational(rta), international);
    }

    private void buildLastResortClosed() {

        SectionRuleContent ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.FLIGHTS, Function.LAST_RESORT)//
            .withRow3(Product.HOTELS, Function.LAST_RESORT).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.LAST_RSRT)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.CLOSED_PACKAGES)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForAny(rta, ruleHomeContent);

        ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.HOTELS, Function.LAST_RESORT)//
            .withRow3(Product.CARS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forAllCountries(rta), ruleHomeContent);

        ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.HOTELS, Function.LAST_RESORT)//
            .withRow3(Product.HOTELS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forOnlyHotels(rta), ruleHomeContent);

        SectionRuleContent international = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.FLIGHTS, Function.LAST_RESORT)//
            .withRow3(Product.CARS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forInternational(rta), international);
    }

    private void buildLastResortCruises() {

        SectionRuleContent ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.CRUISES, Function.LAST_RESORT)//
            .withRow1(Product.CRUISES, Function.LAST_RESORT)//
            .withRow2(Product.HOTELS, Function.LAST_RESORT)//
            .withRow3(Product.FLIGHTS, Function.LAST_RESORT).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.LAST_RSRT)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.CRUISES)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForAny(rta, ruleHomeContent);

        ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.HOTELS, Function.LAST_RESORT)//
            .withRow3(Product.CARS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forAllCountries(rta), ruleHomeContent);

        ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.HOTELS, Function.LAST_RESORT)//
            .withRow3(Product.HOTELS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forOnlyHotels(rta), ruleHomeContent);
    }



    private void buildLastResortHome() {

        SectionRuleContent ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.FLIGHTS, Function.LAST_RESORT)//
            .withRow3(Product.HOTELS, Function.LAST_RESORT).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.LAST_RSRT)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.HOME_AS_PRODUCT)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForAny(rta, ruleHomeContent);

        ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.HOTELS, Function.LAST_RESORT)//
            .withRow3(Product.CARS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forAllCountries(rta), ruleHomeContent);

        ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.HOTELS, Function.LAST_RESORT)//
            .withRow3(Product.HOTELS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forOnlyHotels(rta), ruleHomeContent);

        SectionRuleContent international = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.FLIGHTS, Function.LAST_RESORT)//
            .withRow3(Product.CARS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forInternational(rta), international);
    }


    /**
         * Criteria is copied from Hotels
         */
    private void buildLastResortInsurances() {

        SectionRuleContent ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.FLIGHTS, Function.LAST_RESORT)//
            .withRow3(Product.CLOSED_PACKAGES, Function.LAST_RESORT).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.LAST_RSRT)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.INSURANCE)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForAny(rta, ruleHomeContent);

        ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.HOTELS, Function.LAST_RESORT)//
            .withRow3(Product.CARS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forAllCountries(rta), ruleHomeContent);
    }

    private void buildLastResortVacationRentals() {

        SectionRuleContent ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.VACATIONRENTALS, Function.LAST_RESORT)//
            .withRow1(Product.VACATIONRENTALS, Function.LAST_RESORT)//
            .withRow2(Product.HOTELS, Function.LAST_RESORT)//
            .withRow3(Product.FLIGHTS, Function.LAST_RESORT).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.LAST_RSRT)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.VACATION_RENTALS)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForAny(rta, ruleHomeContent);

        ruleHomeContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.LAST_RESORT)//
            .withRow1(Product.HOTELS, Function.LAST_RESORT)//
            .withRow2(Product.HOTELS, Function.LAST_RESORT)//
            .withRow3(Product.CARS, Function.LAST_RESORT).build();

        this.engine.addForAny(RuleDefSection.forAllCountries(rta), ruleHomeContent);
    }
}
