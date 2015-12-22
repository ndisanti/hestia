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

public class DynamicServiceRules {

    private SectionRuleEngine engine;

    public DynamicServiceRules(SectionRuleEngine engine) {
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
        this.buildBuyVacationRentals();
        this.buildBuyInsurance();
    }

    private void buildBuyVacationRentals() {
        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withRow1(Product.VACATIONRENTALS, Function.RANKING_HOTELS_DESTINATION)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.VACATION_RENTALS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, buyProduct);
        SectionRuleContent allCountries = SectionRuleBuilder.copy(buyProduct)//
            .overRow1(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION).build();
        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), allCountries);

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

    private void buildSearchVacationRentals() {
        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withRow1(Product.VACATIONRENTALS, Function.RANKING_ANY)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.VACATION_RENTALS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, buyProduct);
        SectionRuleContent allCountries = SectionRuleBuilder.copy(buyProduct)//
            .overRow1(Product.HOTELS, Function.RANKING_ANY).build();
        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), allCountries);

    }

    private void buildBuyFlights() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withDynamicFunction(Product.FLIGHTS, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.FLIGHTS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, buyProduct);

        SectionRuleContent allCountries = SectionRuleBuilder.copy(buyProduct)//
            .overRow1(Product.HOTELS, Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH)).build();

        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), allCountries);

        buyProduct = SectionRuleBuilder
            .create()
            .withOffer(Product.FLIGHTS, Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.THANKS).f2(Flow.THANKS))
            .withRow1(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow2(Product.HOTELS, Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow3(Product.HOTELS, Function.RANKING_ANY).build();
        this.engine.addForDynamicServ(RuleDefSection.forInternational(rta), buyProduct);
        this.engine.addForDynamicServ(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), buyProduct);
    }

    private void buildBuyHotels() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withDynamicFunction(Product.HOTELS, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.HOTELS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, buyProduct);
        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), buyProduct);

        buyProduct = SectionRuleBuilder
            .create()
            .withOffer(Product.HOTELS, Function.BUY_DESTINATION)
            .withRow1(Product.HOTELS, Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow2(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow3(Product.CARS, Function.BUY_OFFER_CARS).build();

        this.engine.addForDynamicServ(RuleDefSection.forInternational(rta), buyProduct);
        this.engine.addForDynamicServ(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), buyProduct);
    }

    private void buildBuyCars() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withDynamicFunction(Product.CARS, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.CARS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, buyProduct);
        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), buyProduct);
        buyProduct = SectionRuleBuilder
            .create()
            .withOffer(Product.CARS, Function.BUY_DESTINATION)
            .withRow1(Product.CARS, Function.CARS_CATEGORY_DESTINATION)
            .withRow2(Product.HOTELS, Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow3(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true)).build();

        this.engine.addForDynamicServ(RuleDefSection.forInternational(rta), buyProduct);
        this.engine.addForDynamicServ(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), buyProduct);
    }


    private void buildBuyPackages() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withDynamicFunction(Product.CLOSED_PACKAGES, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.CLOSED_PACKAGES)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, buyProduct);

        SectionRuleContent allCountries = SectionRuleBuilder.copy(buyProduct)//
            .overRow1(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION).build();

        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), allCountries);

        buyProduct = SectionRuleBuilder
            .create()
            .withOffer(Product.HOTELS, Function.BUY_DESTINATION)
            .withRow1(Product.HOTELS, Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))
            .withRow2(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))
            .withRow3(Product.CARS, Function.BUY_OFFER_CARS).build();
        this.engine.addForDynamicServ(RuleDefSection.forInternational(rta), buyProduct);
        this.engine.addForDynamicServ(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), buyProduct);
    }

    private void buildBuyCombined() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withDynamicFunction(Product.COMBINED_PRODUCTS, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.COMBINED_PRODUCTS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, buyProduct);

        SectionRuleContent allCountries = SectionRuleBuilder.copy(buyProduct)//
            .overRow1(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION).build();

        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), allCountries);

        buyProduct = SectionRuleBuilder
            .create()
            .withOffer(Product.HOTELS, Function.BUY_DESTINATION)
            .withRow1(Product.HOTELS, Function.RANKING_ANY)
            .withRow2(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow3(Product.CARS, Function.BUY_OFFER_CARS).build();
        this.engine.addForDynamicServ(RuleDefSection.forInternational(rta), buyProduct);
        this.engine.addForDynamicServ(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), buyProduct);
    }

    private void buildBuyCruises() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withDynamicFunction(Product.CRUISES, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.CRUISES)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, buyProduct);

        SectionRuleContent allCountries = SectionRuleBuilder.copy(buyProduct)//
            .overRow1(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION).build();

        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), allCountries);
    }

    private void buildBuyHome() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.THIRD, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.HOME_AS_PRODUCT)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, buyProduct);
        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), buyProduct);


        buyProduct = SectionRuleBuilder
            .create()
            .withOffer(Product.FLIGHTS, Function.BUY_DESTINATION)
            .withRow1(Product.FLIGHTS,
                Function.RECOMMEND.f1(Flow.SEARCH).f2(Flow.SEARCH).pr1(Product.FLIGHTS).addSearch(true))
            .withRow2(Product.HOTELS, Function.RANKING_ANY).withRow3(Product.CARS, Function.RANKING_ANY).build();
        this.engine.addForDynamicServ(RuleDefSection.forInternational(rta), buyProduct);
        this.engine.addForDynamicServ(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), buyProduct);
    }

    private void buildBuyActivities() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withDynamicFunction(Product.ACTIVITIES, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.ACTIVITIES)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, buyProduct);

        SectionRuleContent allCountries = SectionRuleBuilder.copy(buyProduct)//
            .overRow1(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION).build();

        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), allCountries);
    }

    private void buildBuyInsurance() {

        SectionRuleContent buyProduct = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.OFFER)//
            .withDynamicFunction(Product.HOTELS, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.ANY)//
            .home(HomeSupport.INSURANCE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, buyProduct);
        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), buyProduct);
    }

    private void buildSearchFlights() {

        SectionRuleContent searchFlights = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withDynamicFunction(Product.FLIGHTS, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.FLIGHTS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, searchFlights);

        SectionRuleContent searchFlightsDefault = SectionRuleBuilder.copy(searchFlights)//
            .overRow1(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION).build();

        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), searchFlightsDefault);

        searchFlights = SectionRuleBuilder
            .create()
            .withOffer(Product.FLIGHTS, Function.SEARCH_DESTINATION)
            .withRow1(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow2(Product.HOTELS, Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow3(Product.HOTELS, Function.RANKING_ANY).build();

        this.engine.addForDynamicServ(RuleDefSection.forInternational(rta), searchFlights);
        this.engine.addForDynamicServ(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), searchFlights);

    }

    private void buildSearchHotels() {

        SectionRuleContent searchHotels = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withDynamicFunction(Product.HOTELS, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.HOTELS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, searchHotels);
        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), searchHotels);

        searchHotels = SectionRuleBuilder
            .create()
            .withOffer(Product.HOTELS, Function.SEARCH_DESTINATION)
            .withRow1(Product.HOTELS, Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow2(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))//
            .withRow3(Product.CARS, Function.RANKING_ANY).build();

        this.engine.addForDynamicServ(RuleDefSection.forInternational(rta), searchHotels);
        this.engine.addForDynamicServ(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), searchHotels);
    }

    private void buildSearchCars() {

        SectionRuleContent searchCars = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withDynamicFunction(Product.CARS, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.CARS)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, searchCars);

        SectionRuleContent rcDefault = SectionRuleBuilder.copy(searchCars)//
            .overRow3(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION)//
            .build();

        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), rcDefault);

        searchCars = SectionRuleBuilder
            .create()
            .withOffer(Product.CARS, Function.SEARCH_DESTINATION)
            .withRow1(Product.CARS, Function.CARS_CATEGORY_DESTINATION)
            .withRow2(Product.HOTELS, Function.RANKING_ANY)
            .withRow3(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true)).build();

        this.engine.addForDynamicServ(RuleDefSection.forInternational(rta), searchCars);
        this.engine.addForDynamicServ(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), searchCars);
    }

    private void buildSearchPackages() {

        SectionRuleContent searchPackages = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withDynamicFunction(Product.CLOSED_PACKAGES, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.CLOSED_PACKAGES)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, searchPackages);
        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), searchPackages);

        searchPackages = SectionRuleBuilder
            .create()
            .withOffer(Product.HOTELS, Function.SEARCH_DESTINATION)
            .withRow1(Product.HOTELS, Function.RANKING_ANY)
            .withRow2(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))
            .withRow3(Product.CARS, Function.CARS_CATEGORY_DESTINATION).build();

        this.engine.addForDynamicServ(RuleDefSection.forInternational(rta), searchPackages);
        this.engine.addForDynamicServ(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), searchPackages);
    }


    private void buildSearchCruises() {

        SectionRuleContent searchCruises = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withDynamicFunction(Product.CRUISES, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.CRUISES)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, searchCruises);
        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), searchCruises);
    }


    private void buildSearchHome() {

        SectionRuleContent searchHome = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.THIRD, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.HOME_AS_PRODUCT)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, searchHome);
        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), searchHome);

        searchHome = SectionRuleBuilder
            .create()
            .withOffer(Product.HOTELS, Function.SEARCH_DESTINATION)
            .withRow1(Product.FLIGHTS,
                Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))
            .withRow2(Product.HOTELS, Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))
            .withRow3(Product.CARS, Function.RECOMMEND.pr1(Product.CARS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true))
            .build();

        this.engine.addForDynamicServ(RuleDefSection.forInternational(rta), searchHome);
        this.engine.addForDynamicServ(RuleDefSection.forCountrySupport(rta, CountrySupport.VE), searchHome);
    }


    private void buildSearchActivities() {

        SectionRuleContent searchActivities = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withDynamicFunction(Product.ACTIVITIES, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.ACTIVITIES)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, searchActivities);
        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), searchActivities);
    }

    private void buildSearchInsurance() {

        SectionRuleContent searchHotels = SectionRuleBuilder.create()//
            .withDynamicFunction(DynamicProduct.LAST, SectionsEnum.OFFER)//
            .withDynamicFunction(Product.HOTELS, SectionsEnum.ROW1)//
            .withDynamicFunction(DynamicProduct.FIRST, SectionsEnum.ROW2)//
            .withDynamicFunction(DynamicProduct.SECOND, SectionsEnum.ROW3).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .bought(BuyProductSupport.NONE)//
            .home(HomeSupport.INSURANCE)//
            .support(CountrySupport.SOME)//
            .build();

        this.engine.addForDynamicServ(rta, searchHotels);
        this.engine.addForDynamicServ(RuleDefSection.forAllCountries(rta), searchHotels);
    }
}
