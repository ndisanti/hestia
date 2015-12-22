package com.despegar.p13n.hestia.recommend.allinone.rules.version;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.Anticipation;
import com.despegar.p13n.hestia.recommend.allinone.activity.LastAction;
import com.despegar.p13n.hestia.recommend.allinone.rules.BuyProductSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.CountrySupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.DynamicProduct;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionRuleEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;
import com.despegar.p13n.hestia.recommend.allinone.rules.builder.SectionRuleBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.RuleDefSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.SectionRuleContent;

public class DynamicProductRules {

    private SectionRuleEngine engine;

    public DynamicProductRules(SectionRuleEngine engine) {
        this.engine = engine;
        this.build();
    }

    public void build() {

        this.buildBuy();
        this.buildSearch();
    }


    private void buildBuy() {

        // the product that was bought is considered only by the matrix probability key

        this.buildBuyFlights();
        this.buildBuyHotels();
        this.buildBuyCars();
        this.buildBuyPackages();
        this.buildBuyCombined();
        this.buildBuyCruises();
        this.buildBuyHome();
        this.buildBuyActivities();
        this.buildVacationRentals();
        this.buildBuyInsurance();
    }

    private void buildSearch() {
        this.buildSearchFlights();
        this.buildSearchHotels();
        this.buildSearchCars();
        this.buildSearchPackages();
        this.buildSearchCruises();
        this.buildSearchHome();
        this.buildSearchActivities();
        this.buildSearchVacationRentals();
        this.buildSearchInsurance();
    }

    private void buildVacationRentals() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withRow1(Product.VACATIONRENTALS, Function.RANKING_HOTELS_DESTINATION)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.VACATION_RENTALS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, buyProduct);
        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), buyProduct);

    }

    private void buildBuyFlights() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withRow1(Product.FLIGHTS, Function.RANKING_ANY)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.FLIGHTS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, buyProduct);

        SectionRuleContent allCountries = SectionRuleBuilder.copy(buyProduct)//
            .overRow1(Product.HOTELS, Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH)).build();

        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), allCountries);

        buyProduct = SectionRuleBuilder
            .create()
            .withOffer(Product.FLIGHTS, Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.THANKS).f2(Flow.THANKS))
            .withRow1(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow2(Product.HOTELS, Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow3(Product.HOTELS, Function.RANKING_ANY).build();

        this.engine.addForDynamicPr(RuleDefSection.forInternational(rta), buyProduct);
        this.engine.addForDynamicPr(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), buyProduct);

    }

    private void buildBuyInsurance() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withRow1(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.INSURANCE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, buyProduct);
        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), buyProduct);
    }

    private void buildBuyHotels() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withRow1(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.HOTELS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, buyProduct);
        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), buyProduct);

        buyProduct = SectionRuleBuilder
            .create()
            .withOffer(Product.HOTELS, Function.BUY_DESTINATION)
            .withRow1(Product.HOTELS, Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow2(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow3(Product.CARS, Function.BUY_OFFER_CARS).build();

        this.engine.addForDynamicPr(RuleDefSection.forInternational(rta), buyProduct);
        this.engine.addForDynamicPr(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), buyProduct);
    }

    private void buildBuyCars() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withRow1(Product.CARS, Function.CARS_CATEGORY_DESTINATION)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.CARS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, buyProduct);
        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), buyProduct);

        buyProduct = SectionRuleBuilder
            .create()
            .withOffer(Product.CARS, Function.BUY_DESTINATION)
            .withRow1(Product.CARS, Function.CARS_CATEGORY_DESTINATION)
            .withRow2(Product.HOTELS, Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow3(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true)).build();

        this.engine.addForDynamicPr(RuleDefSection.forInternational(rta), buyProduct);
        this.engine.addForDynamicPr(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), buyProduct);

    }


    private void buildBuyPackages() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withRow1(Product.CLOSED_PACKAGES, Function.RANKING_ANY)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.CLOSED_PACKAGES)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, buyProduct);

        SectionRuleContent allCountries = SectionRuleBuilder.copy(buyProduct)//
            .overRow1(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION).build();

        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), allCountries);

        buyProduct = SectionRuleBuilder
            .create()
            .withOffer(Product.HOTELS, Function.BUY_DESTINATION)
            .withRow1(Product.HOTELS, Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))
            .withRow2(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))
            .withRow3(Product.CARS, Function.BUY_OFFER_CARS).build();
        this.engine.addForDynamicPr(RuleDefSection.forInternational(rta), buyProduct);
        this.engine.addForDynamicPr(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), buyProduct);
    }

    private void buildBuyCombined() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withRow1(Product.COMBINED_PRODUCTS, Function.RANKING_ANY)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.COMBINED_PRODUCTS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, buyProduct);

        SectionRuleContent allCountries = SectionRuleBuilder.copy(buyProduct)//
            .overRow1(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION).build();

        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), allCountries);

        buyProduct = SectionRuleBuilder
            .create()
            .withOffer(Product.HOTELS, Function.BUY_DESTINATION)
            .withRow1(Product.HOTELS, Function.RANKING_ANY)
            .withRow2(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow3(Product.CARS, Function.BUY_OFFER_CARS).build();
        this.engine.addForDynamicPr(RuleDefSection.forInternational(rta), buyProduct);
        this.engine.addForDynamicPr(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), buyProduct);
    }

    private void buildBuyCruises() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withRow1(Product.CRUISES, Function.RANKING_REGION)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.CRUISES)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, buyProduct);

        SectionRuleContent allCountries = SectionRuleBuilder.copy(buyProduct)//
            .overRow1(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION).build();

        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), allCountries);
    }

    private void buildBuyHome() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW1)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.THIRD, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.HOME_AS_PRODUCT)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, buyProduct);
        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), buyProduct);

        buyProduct = SectionRuleBuilder
            .create()
            .withOffer(Product.FLIGHTS, Function.BUY_DESTINATION)
            .withRow1(Product.FLIGHTS,
                Function.RECOMMEND.f1(Flow.SEARCH).f2(Flow.SEARCH).pr1(Product.FLIGHTS).addSearch(true))
            .withRow2(Product.HOTELS, Function.RANKING_ANY).withRow3(Product.CARS, Function.RANKING_ANY).build();
        this.engine.addForDynamicPr(RuleDefSection.forInternational(rta), buyProduct);
        this.engine.addForDynamicPr(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), buyProduct);
    }


    private void buildBuyActivities() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withRow1(Product.ACTIVITIES, Function.RANKING_ANY)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.ACTIVITIES)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, buyProduct);

        SectionRuleContent allCountries = SectionRuleBuilder.copy(buyProduct)//
            .overRow1(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION).build();

        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), allCountries);
    }



    private void buildSearchFlights() {

        SectionRuleContent searchFlights = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withRow1(Product.FLIGHTS, Function.RANKING_ANY)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.FLIGHTS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, searchFlights);

        SectionRuleContent searchFlightsDefault = SectionRuleBuilder.copy(searchFlights)//
            .overRow1(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION).build();

        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), searchFlightsDefault);

        searchFlights = SectionRuleBuilder
            .create()
            .withOffer(Product.FLIGHTS, Function.SEARCH_DESTINATION)
            .withRow1(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow2(Product.HOTELS, Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow3(Product.HOTELS, Function.RANKING_ANY).build();

        this.engine.addForDynamicPr(RuleDefSection.forInternational(rta), searchFlights);
        this.engine.addForDynamicPr(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), searchFlights);

    }

    private void buildSearchVacationRentals() {
        SectionRuleContent searchHotels = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withRow1(Product.VACATIONRENTALS, Function.RANKING_ANY)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.VACATION_RENTALS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, searchHotels);
        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), searchHotels);
    }

    private void buildSearchHotels() {

        SectionRuleContent searchHotels = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withRow1(Product.HOTELS, Function.RANKING_ANY)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.HOTELS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, searchHotels);
        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), searchHotels);

        searchHotels = SectionRuleBuilder
            .create()
            .withOffer(Product.HOTELS, Function.SEARCH_DESTINATION)
            .withRow1(Product.HOTELS, Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow2(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow3(Product.CARS, Function.RANKING_ANY).build();

        this.engine.addForDynamicPr(RuleDefSection.forInternational(rta), searchHotels);
        this.engine.addForDynamicPr(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), searchHotels);
    }

    private void buildSearchInsurance() {

        SectionRuleContent searchHotels = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withRow1(Product.HOTELS, Function.RANKING_ANY)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.INSURANCE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, searchHotels);
        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), searchHotels);
    }

    private void buildSearchCars() {

        SectionRuleContent searchCars = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withRow1(Product.CARS, Function.CAR_RANKING_COUNTRY_SEARCH)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.CARS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, searchCars);

        SectionRuleContent rcDefault = SectionRuleBuilder.copy(searchCars)//
            .overRow3(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION)//
            .build();

        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), rcDefault);

        searchCars = SectionRuleBuilder
            .create()
            .withOffer(Product.CARS, Function.SEARCH_DESTINATION)
            .withRow1(Product.CARS, Function.CARS_CATEGORY_DESTINATION)
            .withRow2(Product.HOTELS, Function.RANKING_ANY)
            .withRow3(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true)).build();

        this.engine.addForDynamicPr(RuleDefSection.forInternational(rta), searchCars);
        this.engine.addForDynamicPr(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), searchCars);
    }

    private void buildSearchPackages() {

        SectionRuleContent searchPackages = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withRow1(Product.CLOSED_PACKAGES, Function.RANKING_ANY)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.CLOSED_PACKAGES)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, searchPackages);
        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), searchPackages);

        searchPackages = SectionRuleBuilder
            .create()
            .withOffer(Product.HOTELS, Function.SEARCH_DESTINATION)
            .withRow1(Product.HOTELS, Function.RANKING_ANY)
            .withRow2(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))
            .withRow3(Product.CARS, Function.CARS_CATEGORY_DESTINATION).build();

        this.engine.addForDynamicPr(RuleDefSection.forInternational(rta), searchPackages);
        this.engine.addForDynamicPr(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), searchPackages);
    }


    private void buildSearchCruises() {

        SectionRuleContent searchCruises = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withRow1(Product.CRUISES, Function.RANKING_REGION)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.CRUISES)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, searchCruises);
        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), searchCruises);
    }


    private void buildSearchHome() {

        SectionRuleContent searchHome = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW1)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.THIRD, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.HOME_AS_PRODUCT)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, searchHome);
        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), searchHome);

        searchHome = SectionRuleBuilder
            .create()
            .withOffer(Product.HOTELS, Function.SEARCH_DESTINATION)
            .withRow1(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))
            .withRow2(Product.HOTELS, Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))
            .withRow3(Product.CARS, Function.RECOMMEND.pr1(Product.CARS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))
            .build();

        this.engine.addForDynamicPr(RuleDefSection.forInternational(rta), searchHome);
        this.engine.addForDynamicPr(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), searchHome);
    }


    private void buildSearchActivities() {

        SectionRuleContent searchActivities = SectionRuleBuilder.create()//
            .withDynamicRanking(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withRow1(Product.ACTIVITIES, Function.RANKING_ANY)//
            .withDynamicRanking(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicRanking(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.ACTIVITIES)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicPr(rta, searchActivities);
        this.engine.addForDynamicPr(RuleDefSection.forAllCountries(rta), searchActivities);
    }

}
