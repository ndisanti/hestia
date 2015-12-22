package com.despegar.p13n.hestia.recommend.allinone.rules.section;

import java.util.EnumSet;
import java.util.List;

import com.despegar.p13n.hestia.api.data.model.ItemType;
import com.google.common.collect.Lists;

public enum SectionFunctionCode {
    HOT_RANKING_IP_COUNTRY(ItemType.destinations(), ItemType.CRUISE, ItemType.CRUISE_REGION), //
    RANKING_HOTELS_DESTINATION(ItemType.HOTEL, ItemType.HOTEL_DESTINATION), //
    CARS_RANKING_COUNTRY(ItemType.CAR_CATEGORY, ItemType.CAR_DESTINATION), //
    SEARCH_DESTINATION(ItemType.destinations()), //
    BUY_DESTINATION(ItemType.destinations()), //
    RECOMMEND(ItemType.destinations(), ItemType.HOTEL, ItemType.ACTIVITY, ItemType.CRUISE_REGION), //
    NEARBY_BUY_DESTINATION(ItemType.destinations()), //
    CARS_CATEGORY_DESTINATION(ItemType.CAR_CATEGORY), //
    CRUISE_BUY(ItemType.HOTEL_DESTINATION, ItemType.FLIGHT_DESTINATION), //
    CRUISE_BUY_DESTINATION_RECOM(ItemType.destinations()), //
    FLIGHTS_RANKING_BY_CLUSTER(ItemType.FLIGHT_DESTINATION), //

    BUY_OFFER_CARS(ItemType.CAR_DESTINATION),

    // last seen hotel (detail) or destination (search), and recommendation
    HOTEL_SEARCH_AND_RECOMMEND(ItemType.HOTEL, ItemType.HOTEL_DESTINATION),


    RANKING_DESTINATION_BY_CLUSTER(ItemType.destinations()),
    CARS_SEARCH(ItemType.CAR_DESTINATION), // car category and destination that saw or first category for destination
    CRUISE_SEARCH(ItemType.CRUISE, ItemType.CRUISE_REGION), // last cruise did from detail or region from last search
    CRUISE_RANKING_DESTINATION(ItemType.CRUISE), // ranking did (detail) or ranking region (search)
    CRUISE_RANKING_IP(ItemType.CRUISE_REGION), // region ranking by user ip
    CRUISE_BUY_OFFER_FLIGHTS(ItemType.FLIGHT_DESTINATION),


    // first itinerary destination (seen) and ranking destination within region (next blocks)
    RANKING_CRUISE_REGION(ItemType.CRUISE_REGION, ItemType.FLIGHT_DESTINATION, ItemType.HOTEL_DESTINATION),
    PACKAGE_SEARCH(ItemType.CLOSED_PACKAGES_DESTINATION), // Last seen packages or package in the destination that saw

    LAST_DETAIL_FUNCTION(ItemType.HOTEL),
    LIKE_HOTEL_FUNCTION(ItemType.HOTEL),

    LAST_RESORT(ItemType.destinations(), ItemType.CRUISE_REGION, ItemType.ACTIVITY),
    RANKING_DYNAMIC_PRODUCT(ItemType.destinations(), ItemType.CAR_CATEGORY, ItemType.CRUISE_REGION),

    ACTIVITIES_RANKING_LOCATION(ItemType.ACTIVITY),
    ACTIVITIES_DISNEY(ItemType.ACTIVITY),
    ACTIVITIES_UNIVERSAL(ItemType.ACTIVITY),
    ACTIVITIES_VIEWED(ItemType.ACTIVITY),
    ACTIVITIES_RECOMMENDER(ItemType.ACTIVITY),
    ACTIVITIES_ORL_DOMESTIC(ItemType.ACTIVITY, ItemType.ACTIVITY_DESTINATION),

    // TODO ver esto
    RANKING_RENTALS_DESTINATION(ItemType.VACATION_RENTAL, ItemType.VACATION_RENTAL_DESTINATION),
    LAST_RESORT_ACT_AR(ItemType.destinations(), ItemType.ACTIVITY),

    INTERNATIONAL(ItemType.destinations());

    private List<ItemType> itemTypes;

    SectionFunctionCode(ItemType... itemTypes) {
        this.itemTypes = Lists.newArrayList(itemTypes);
    }

    SectionFunctionCode(EnumSet<ItemType> itemTypes) {
        this.itemTypes = Lists.newArrayList(itemTypes);
    }

    SectionFunctionCode(EnumSet<ItemType> itemTypes, ItemType... moreItemTypes) {
        this.itemTypes = Lists.newArrayList(moreItemTypes);
        this.itemTypes.addAll(itemTypes);
    }

    public List<ItemType> getItemTypes() {
        return this.itemTypes;
    }


    public static List<String> dumpItempTypes() {

        String format = "%-30s%s";

        List<String> dump = Lists.newArrayList();

        for (SectionFunctionCode value : SectionFunctionCode.values()) {
            String row = String.format(format, value, value.getItemTypes());
            dump.add(row);
        }

        return dump;
    }


}
