package com.despegar.p13n.hestia.recommend.allinone.rules.version;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.VisitCity;
import com.despegar.p13n.hestia.recommend.allinone.VisitFlow;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.BuyProductSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionFunctionEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority.FunctionPriority;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority.RuleFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority.RuleFunction.RuleFunctionBuilder;
import com.despegar.p13n.hestia.recommend.allinone.title.SectionType;

public class DynamicServiceFunctionRules {

    private SectionFunctionEngine engine;

    public DynamicServiceFunctionRules(SectionFunctionEngine engine) {
        this.engine = engine;
        this.build();
    }

    public void build() {
        this.buildFlights();
        this.buildHotels();
        this.buildCars();
        this.buildPackages();
        this.buildActivities();
        this.buildCruises();
        this.buildVacationRentals();
    }

    private void buildFlights() {
        this.buildFlightsBuyStar();
        this.buildFlightsBuyRow();

        this.buildFlightsSearchStar();
        this.buildFlightsSearchRow();
    }

    private void buildHotels() {
        this.buildHotelsBuyStar();
        this.buildHotelsBuyRow();

        this.buildHotelsSearchStar();
        this.buildHotelsSearchRow();

    }

    private void buildVacationRentals() {
        this.buildVacationRentalsBuyStar();
        this.buildVacationRentalsBuyRow();

        this.buildVacationRentalsSearchStar();
        this.buildVacationRentalsSearchRow();

    }

    private void buildVacationRentalsSearchRow() {

        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.RECOMMEND.pr1(Product.VACATIONRENTALS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RANKING_DOMESTIC;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_INTERNATIONAL;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.DETAIL)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();
        // TODO pendiente lo de hsort
        function = Function.RECOMMEND.pr1(Product.VACATIONRENTALS).f1(Flow.DETAIL).f2(Flow.DETAIL).addSearch(true);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.DETAIL)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RANKING_RENTALS_DESTINATION;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.DETAIL)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RECOMMEND.pr1(Product.VACATIONRENTALS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.DETAIL)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FOURTH)//
            .build();

        function = Function.RANKING_ANY;
        this.engine.add(rule, function);
    }

    private void buildVacationRentalsSearchStar() {

        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.LAST_DETAIL_FUNCTION;
        this.engine.add(rule, function);


        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.SEARCH_DESTINATION.pr1(Product.VACATIONRENTALS);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.SEARCH_DESTINATION;
        this.engine.add(rule, function);

    }

    private void buildVacationRentalsBuyRow() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        // TODO cambiar funcion
        Function function = Function.RECOMMEND.addSearch(true).f1(Flow.SEARCH).f2(Flow.SEARCH).pr1(Product.VACATIONRENTALS);

        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RANKING_DOMESTIC;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_ANY;
        this.engine.add(rule, function);

        // TODO agregar hsort
        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.VACATION_RENTALS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.RECOMMEND.pr1(Product.VACATIONRENTALS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true);

        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.VACATION_RENTALS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RECOMMEND.pr1(Product.VACATIONRENTALS).f1(Flow.THANKS).f2(Flow.THANKS);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.VACATION_RENTALS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RECOMMEND.pr1(Product.VACATIONRENTALS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.VACATION_RENTALS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FOURTH)//
            .build();

        function = Function.RANKING_ANY;
        this.engine.add(rule, function);
    }

    private void buildVacationRentalsBuyStar() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.LAST_DETAIL_FUNCTION;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.SEARCH_DESTINATION.pr1(Product.VACATIONRENTALS);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.BUY_DESTINATION;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.VACATION_RENTALS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.BUY_DESTINATION;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.VACATIONRENTALS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.VACATION_RENTALS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RECOMMEND.pr1(Product.VACATIONRENTALS).f1(Flow.THANKS).f2(Flow.THANKS);
        this.engine.add(rule, function);

    }

    private void buildCars() {
        this.buildCarsBuyStar();
        this.buildCarsBuyRow();

        this.buildCarsSearchStar();
        this.buildCarsSearchRow();

    }


    private void buildPackages() {
        this.buildPackagesBuyStar();
        this.buildPackagesBuyRow();

        this.buildPackagesSearchStar();
        this.buildPackagesSearchRow();

    }


    private void buildActivities() {
        this.buildActivitiesBuyStar();
        this.buildActivitiesBuyRow();

        this.buildActivitiesSearchStar();
        this.buildActivitiesSearchRow();

    }

    private void buildCruises() {
        this.buildCruisesBuyStar();
        this.buildCruisesBuyRow();

        this.buildCruisesSearchStar();
        this.buildCruisesSearchRow();

    }

    private void buildFlightsBuyStar() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.FLIGHTS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.THANKS).f2(Flow.THANKS).build();
        this.engine.add(rule, function);


        rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.COMBINED_PRODUCTS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.THANKS).f2(Flow.THANKS).build();
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.CRUISES)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.CRUISE_BUY_OFFER_FLIGHTS;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.BUY_DESTINATION;
        this.engine.add(rule, function);
    }

    private void buildFlightsBuyRow() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.FLIGHTS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.THANKS).f2(Flow.THANKS).build();
        this.engine.add(rule, function);


        rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.FLIGHTS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RANKING_DOMESTIC;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.FLIGHTS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_INTERNATIONAL;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.HOTELS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.THANKS).f2(Flow.THANKS).addBuy(true).build();
        this.engine.add(rule, function);


        rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.HOTELS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).build();
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.HOTELS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_DOMESTIC;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.HOTELS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FOURTH)//
            .build();

        function = Function.RANKING_INTERNATIONAL;
        this.engine.add(rule, function);


        rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addBuy(true).build();
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RANKING_DOMESTIC;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_INTERNATIONAL;
        this.engine.add(rule, function);

    }


    private void buildFlightsSearchStar() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.SEARCH_DESTINATION;
        this.engine.add(rule, function);
    }



    private void buildFlightsSearchRow() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true).build();
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RANKING_DOMESTIC;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.FLIGHTS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_INTERNATIONAL;
        this.engine.add(rule, function);

    }

    /**
     * 
     */
    private void buildHotelsBuyStar() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.HOTELS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.THANKS).f2(Flow.THANKS).build();
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();
        function = Function.LIKE_HOTEL_FUNCTION;
        this.engine.add(rule, function);


        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();
        function = Function.LAST_DETAIL_FUNCTION;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.SEARCH_DESTINATION;
        this.engine.add(rule, function);

        // Destino comprado de otro producto
        // como hay una regla anterior para Hotels, aca tuvo que haber comprado otro producto que no es un hotel
        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FOURTH)//
            .build();

        function = Function.BUY_DESTINATION;
        this.engine.add(rule, function);

    }

    private void buildHotelsBuyRow() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.HOTELS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.THANKS).f2(Flow.THANKS).build();
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.HOTELS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).build();
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.HOTELS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_ANY;
        this.engine.add(rule, function);

        // si compro vuelos
        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.FLIGHTS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.THANKS).f2(Flow.SEARCH).addBuy(true).build();
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.FLIGHTS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).build();
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.FLIGHTS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_ANY;
        this.engine.add(rule, function);

        // si compro combined products
        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.COMBINED_PRODUCTS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.THANKS).f2(Flow.SEARCH).addBuy(true).build();
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.COMBINED_PRODUCTS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).build();
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.COMBINED_PRODUCTS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_ANY;
        this.engine.add(rule, function);

        // si compro otro producto

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addBuy(true).build();
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RANKING_ANY;
        this.engine.add(rule, function);

    }



    private void buildHotelsSearchStar() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.LIKE_HOTEL_FUNCTION;
        this.engine.add(rule, function);


        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.LAST_DETAIL_FUNCTION;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.SEARCH_DESTINATION;
        this.engine.add(rule, function);

    }


    private void buildHotelsSearchRow() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.DETAIL)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.DETAIL).f2(Flow.DETAIL).addSearch(true);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.DETAIL)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RANKING_HOTELS_DESTINATION;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.DETAIL)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.DETAIL)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FOURTH)//
            .build();

        function = Function.RANKING_ANY;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.RECOMMEND.pr1(Product.HOTELS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RANKING_DOMESTIC;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.HOTELS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_INTERNATIONAL;
        this.engine.add(rule, function);

    }

    private void buildCarsBuyStar() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.CARS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.HOTELS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.BUY_DESTINATION;
        this.engine.add(rule, function);


        rule = RuleFunctionBuilder.create()//
            .offer(Product.CARS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.FLIGHTS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.BUY_OFFER_CARS;
        this.engine.add(rule, function);


        rule = RuleFunctionBuilder.create()//
            .offer(Product.CARS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.FLIGHTS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.BUY_OFFER_CARS;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.CARS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.COMBINED_PRODUCTS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.BUY_OFFER_CARS;
        this.engine.add(rule, function);


        rule = RuleFunctionBuilder.create()//
            .offer(Product.CARS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.COMBINED_PRODUCTS)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.BUY_OFFER_CARS;
        this.engine.add(rule, function);


        rule = RuleFunctionBuilder.create()//
            .offer(Product.CARS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.BUY_DESTINATION;
        this.engine.add(rule, function);
    }


    private void buildCarsBuyRow() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.CARS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.CARS_CATEGORY_DESTINATION;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.CARS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RECOMMEND.pr1(Product.CARS).f1(Flow.SEARCH).f2(Flow.SEARCH);
        this.engine.add(rule, function);


        rule = RuleFunctionBuilder.create()//
            .offer(Product.CARS)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_ANY;
        this.engine.add(rule, function);

    }



    private void buildCarsSearchStar() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.CARS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.SEARCH_DESTINATION;
        this.engine.add(rule, function);

    }


    private void buildCarsSearchRow() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.CARS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.CARS_CATEGORY_DESTINATION;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.CARS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RECOMMEND.pr1(Product.CARS).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.CARS)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_ANY;
        this.engine.add(rule, function);
    }

    private void buildPackagesBuyStar() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.CLOSED_PACKAGES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.BUY_DESTINATION;
        this.engine.add(rule, function);

    }

    private void buildPackagesBuyRow() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.CLOSED_PACKAGES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.CLOSED_PACKAGES)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.RECOMMEND.pr1(Product.CLOSED_PACKAGES).f1(Flow.THANKS).f2(Flow.THANKS).addBuy(true);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.CLOSED_PACKAGES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.CLOSED_PACKAGES)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RECOMMEND.pr1(Product.CLOSED_PACKAGES).f1(Flow.SEARCH).f2(Flow.SEARCH);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.CLOSED_PACKAGES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.CLOSED_PACKAGES)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_ANY;
        this.engine.add(rule, function);

        // si compro otro producto
        rule = RuleFunctionBuilder.create()//
            .offer(Product.CLOSED_PACKAGES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.RECOMMEND.pr1(Product.CLOSED_PACKAGES).f1(Flow.THANKS).f2(Flow.THANKS).addBuy(true);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.CLOSED_PACKAGES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RANKING_DOMESTIC;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.CLOSED_PACKAGES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_INTERNATIONAL;
        this.engine.add(rule, function);

    }



    private void buildPackagesSearchStar() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.CLOSED_PACKAGES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.SEARCH_DESTINATION;
        this.engine.add(rule, function);

    }



    private void buildPackagesSearchRow() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.CLOSED_PACKAGES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.RECOMMEND.pr1(Product.CLOSED_PACKAGES).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.CLOSED_PACKAGES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RANKING_DOMESTIC;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.CLOSED_PACKAGES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_INTERNATIONAL;
        this.engine.add(rule, function);

    }

    private void buildActivitiesBuyStar() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.ACTIVITIES)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.RECOMMEND.pr1(Product.ACTIVITIES).f1(Flow.DETAIL).f2(Flow.DETAIL);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.ACTIVITIES)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.ACTIVITIES_RANKING_LOCATION;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.ACTIVITIES_RANKING_LOCATION;
        this.engine.add(rule, function);
    }

    private void buildActivitiesBuyRow() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ACTIVITIES)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.ACTIVITIES_RECOMMENDER;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ACTIVITIES)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.ACTIVITIES_RANKING_LOCATION;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ACTIVITIES)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RECOMMEND.pr1(Product.ACTIVITIES).f1(Flow.THANKS).f2(Flow.THANKS);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ACTIVITIES)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FOURTH)//
            .build();

        function = Function.RECOMMEND.pr1(Product.ACTIVITIES).f1(Flow.SEARCH).f2(Flow.SEARCH);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ACTIVITIES)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIFTH)//
            .build();

        function = Function.RANKING_ANY;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.MIAMI_ORLANDO)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.ACTIVITIES_DISNEY;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.ACTIVITIES_RANKING_LOCATION;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RANKING_INTERNATIONAL;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_DOMESTIC;
        this.engine.add(rule, function);
    }

    private void buildActivitiesSearchStar() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.MIAMI_ORLANDO)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.ACTIVITIES_DISNEY;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.MIAMI_ORLANDO)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.ACTIVITIES_UNIVERSAL;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.MIAMI_ORLANDO)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.ACTIVITIES_VIEWED;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.DETAIL)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.ACTIVITIES_VIEWED;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.SEARCH_DESTINATION;
        this.engine.add(rule, function);
    }

    private void buildActivitiesSearchRow() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.MIAMI_ORLANDO)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.ACTIVITIES_DISNEY;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.MIAMI_ORLANDO)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.ACTIVITIES_RANKING_LOCATION;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.MIAMI_ORLANDO)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RECOMMEND.pr1(Product.ACTIVITIES).f1(Flow.SEARCH).f2(Flow.SEARCH);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.DETAIL)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.ACTIVITIES_VIEWED;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.DETAIL)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.ACTIVITIES_RANKING_LOCATION;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.DETAIL)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RECOMMEND.pr1(Product.ACTIVITIES).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.ACTIVITIES_RANKING_LOCATION;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RECOMMEND.pr1(Product.ACTIVITIES).f1(Flow.SEARCH).f2(Flow.SEARCH).addSearch(true);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.ACTIVITIES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_ANY;
        this.engine.add(rule, function);
    }

    private void buildCruisesBuyStar() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.CRUISES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.CRUISE_SEARCH;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.CRUISES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.CRUISE_RANKING_IP;
        this.engine.add(rule, function);

    }

    private void buildCruisesBuyRow() {

        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.CRUISES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        Function function = Function.RECOMMEND.pr1(Product.CRUISES).f1(Flow.SEARCH).f2(Flow.SEARCH);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.CRUISES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        function = Function.CRUISE_RANKING_IP;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.CRUISES)//
            .activity(ActivityType.BUY)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.ANY)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_DID;
        this.engine.add(rule, function);
    }

    private void buildCruisesSearchStar() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.CRUISES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.CRUISE_SEARCH;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.CRUISES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.OFFER)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.CRUISE_RANKING_IP;
        this.engine.add(rule, function);

    }


    private void buildCruisesSearchRow() {
        RuleFunction rule = RuleFunctionBuilder.create()//
            .offer(Product.CRUISES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.FIRST)//
            .build();

        Function function = Function.CRUISE_RANKING_IP;
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.CRUISES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.SECOND)//
            .build();

        function = Function.RECOMMEND.pr1(Product.CRUISES).f1(Flow.SEARCH).f2(Flow.SEARCH);
        this.engine.add(rule, function);

        rule = RuleFunctionBuilder.create()//
            .offer(Product.CRUISES)//
            .activity(ActivityType.SEARCH)//
            .section(SectionType.ROW)//
            .bought(BuyProductSupport.NONE)//
            .visitFlow(VisitFlow.ANY)//
            .visitCity(VisitCity.ANY)//
            .priority(FunctionPriority.THIRD)//
            .build();

        function = Function.RANKING_DID;
        this.engine.add(rule, function);

    }

}
