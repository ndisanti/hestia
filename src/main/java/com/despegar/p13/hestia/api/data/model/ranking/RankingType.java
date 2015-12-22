package com.despegar.p13.hestia.api.data.model.ranking;

public enum RankingType {
    HOTEL_IDS(false, false, true), //
    HOTEL_STARS(false, false, true),
    FLIGHT_AIRLINES(true, true, true),
    FLIGHT_CLASS(true, true, true),
    FLIGHT_STOPS(true, true, true),
    CAR_CATEGORY(false, false, true),
    DESTINATION(false, true, false);

    private boolean usesRouteType;
    private boolean usesOriginCC;
    private boolean usesDestination;

    private RankingType(boolean usesRouteType, boolean usesOriginCC, boolean usesDestination) {
        this.usesRouteType = usesRouteType;
        this.usesOriginCC = usesOriginCC;
        this.usesDestination = usesDestination;
    }

    public boolean usesRouteType() {
        return this.usesRouteType;
    }

    public boolean usesOriginCC() {
        return this.usesOriginCC;
    }

    public boolean usesDestination() {
        return this.usesDestination;
    }

}
