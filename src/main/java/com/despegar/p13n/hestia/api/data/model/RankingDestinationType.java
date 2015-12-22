package com.despegar.p13n.hestia.api.data.model;

public enum RankingDestinationType {

	/**
     *Iata for ranking is based on the user location
     */
    USER_LOCATION,
    /**
     * Iata for ranking is the iata destination
     */
    DESTINATION,
    /**
     * Iata for ranking Used for cruises, destination departure will be the first city in the itinerary.
     */
    DESTINATION_DEPARTURE,

    /**
     * When the ranking does not has the part RankingKeyPart.COUNTRY_AREA_OID_IATA.
     * Since we are forced to choose one type, we should select this one to avoid confusion.
     */
    NOT_APPLICABLE;
}
