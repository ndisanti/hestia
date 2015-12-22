package com.despegar.p13n.hestia.api.data.model;

import java.util.EnumSet;

public enum ItemType {

	CAR_CATEGORY(PriceCarCategoryItem.class, true), //
    CAR_DESTINATION(CarDestinationItem.class, true), //
    CLOSED_PACKAGES(ClosedPackagesItem.class, true), //
    CLOSED_PACKAGES_DESTINATION(ClosedPackagesDestinationItem.class, true), //
    COMBINED_PRODUCTS(CombinedProductsDestinationItem.class, true), //
    CRUISE(CruiseItem.class, true), //
    CRUISE_REGION(CruiseRegionItem.class, true), //
    HOTEL(HotelItem.class, true), //
    HOTEL_DESTINATION(HotelDestinationItem.class, true), //
    FLIGHT_DESTINATION(FlightDestinationItem.class, true), //
    ACTIVITY(ActivityItem.class, true), //
    ACTIVITY_DESTINATION(ActivityDestinationItem.class, true),
    APP_MOBILE(MobileAppItem.class, false),
    INSPIRATION(InspirationItem.class, false),
    CORPORATE(CorporateItem.class, false),
    FARE_ALERTS(AlertItem.class, false),
    HAPPY_CLIENT(HappyClientItem.class, false),
    NEWSLETTER(NewsletterItem.class, false),
    TRUST_PILOT(TrustPilotItem.class, false),
    GENERIC_INSPIRATION(GenericInspirationItem.class, false),
    CITY_REVIEW(CityReviewItem.class, false),
    VACATION_RENTAL(VacationRentalItem.class, true),
    VACATION_RENTAL_DESTINATION(VacationRentalDestinationItem.class, true),
    VACATION_RENTALS_BANNER(VacationRentalBanner.class, false),
    SECRET_OFFER(SecretOfferItem.class, false),
    BANK_OFFER(BankOfferItem.class, false),
    MARKETING(MarketingBannersItem.class, false),
    HOLIDAYS(HolidaysItem.class, false);//


    private boolean isTitleRequired;

    public static EnumSet<ItemType> destinations() {
        return EnumSet.of(CAR_DESTINATION, CLOSED_PACKAGES_DESTINATION, COMBINED_PRODUCTS, HOTEL_DESTINATION,
            FLIGHT_DESTINATION, ACTIVITY_DESTINATION, VACATION_RENTAL_DESTINATION);
    }

    public Class<? extends ItemHome> clazz;

    ItemType(Class<? extends ItemHome> clazz, boolean isTitleRequired) {
        this.clazz = clazz;
        this.isTitleRequired = isTitleRequired;
    }


    public static ItemType getItemType(Class<? extends ItemHome> clazz) {
        for (ItemType itemType : ItemType.values()) {
            if (itemType.clazz.equals(clazz)) {
                return itemType;
            }
        }

        throw new IllegalArgumentException("Class: " + clazz);
    }


    public boolean isTitleRequired() {
        return this.isTitleRequired;
    }
}
