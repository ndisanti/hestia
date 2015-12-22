package com.despegar.p13n.hestia.api.data.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.google.common.base.Preconditions;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "offerType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ClosedPackagesItem.class, name = "CLOSED_PACKAGES"),
    @JsonSubTypes.Type(value = CruiseItem.class, name = "CRUISE"),
    @JsonSubTypes.Type(value = CruiseRegionItem.class, name = "CRUISE_REGION"),
    @JsonSubTypes.Type(value = CarDestinationItem.class, name = "CAR_DESTINATION"),
    @JsonSubTypes.Type(value = ClosedPackagesDestinationItem.class, name = "CLOSED_PACKAGES_DESTINATION"),
    @JsonSubTypes.Type(value = CombinedProductsDestinationItem.class, name = "COMBINED_PRODUCTS"),
    @JsonSubTypes.Type(value = FlightDestinationItem.class, name = "FLIGHT_DESTINATION"),
    @JsonSubTypes.Type(value = HotelDestinationItem.class, name = "HOTEL_DESTINATION"),
    @JsonSubTypes.Type(value = HotelItem.class, name = "HOTEL"),
    @JsonSubTypes.Type(value = PriceCarCategoryItem.class, name = "CAR_CATEGORY"),
    @JsonSubTypes.Type(value = ActivityItem.class, name = "ACTIVITY"),
    @JsonSubTypes.Type(value = ActivityDestinationItem.class, name = "ACTIVITY_DESTINATION"),
    @JsonSubTypes.Type(value = AlertItem.class, name = "FARE_ALERTS"),
    @JsonSubTypes.Type(value = CityReviewItem.class, name = "CITY_REVIEW"),
    @JsonSubTypes.Type(value = CorporateItem.class, name = "CORPORATE"),
    @JsonSubTypes.Type(value = GenericInspirationItem.class, name = "GENERIC_INSPIRATION"),
    @JsonSubTypes.Type(value = HappyClientItem.class, name = "HAPPY_CLIENT"),
    @JsonSubTypes.Type(value = InspirationItem.class, name = "INSPIRATION"),
    @JsonSubTypes.Type(value = MobileAppItem.class, name = "APP_MOBILE"),
    @JsonSubTypes.Type(value = NewsletterItem.class, name = "NEWSLETTER"),
    @JsonSubTypes.Type(value = VacationRentalDestinationItem.class, name = "VACATION_RENTAL_DESTINATION"),
    @JsonSubTypes.Type(value = VacationRentalItem.class, name = "VACATION_RENTAL"),
    @JsonSubTypes.Type(value = VacationRentalBanner.class, name = "VACATION_RENTALS_BANNER"),
    @JsonSubTypes.Type(value = NewsletterItem.class, name = "NEWSLETTER"),
    @JsonSubTypes.Type(value = SecretOfferItem.class, name = "SECRET_OFFER"),
    @JsonSubTypes.Type(value = BankOfferItem.class, name = "BANK_OFFER"),
    @JsonSubTypes.Type(value = MarketingBannersItem.class, name = "MARKETING_BANNERS"),
    @JsonSubTypes.Type(value = HolidaysItem.class, name = "HOLIDAY"),
    @JsonSubTypes.Type(value = TrustPilotItem.class, name = "TRUST_PILOT")})

public abstract class ItemHome {

	 @JsonIgnore
	    private ItemType offerType;

	    protected ItemHome(ItemType offerType) {
	        super();
	        Preconditions.checkArgument(this.getClass().isAssignableFrom(offerType.clazz),
	            "Invalid argument: type %s not compatible with classs %s", offerType, this.getClass());
	        this.offerType = offerType;
	    }

	    public ItemType getOfferType() {
	        return this.offerType;
	    }

	    public void setOfferType(ItemType offerType) {
	        this.offerType = offerType;
	    }

	    @JsonIgnore
	    public abstract String getId();


	    @Override
	    public abstract String toString();

}
