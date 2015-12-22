package com.despegar.p13n.hestia.data.hbase.hot.types;

import java.util.HashMap;
import java.util.Map;

import com.despegar.p13n.hestia.api.data.model.RankingTypeCodes;
import com.despegar.p13n.hestia.data.hbase.hot.filter.CompositeRankingFilter;
import com.despegar.p13n.hestia.data.hbase.hot.filter.DidCheckInFilter;
import com.despegar.p13n.hestia.data.hbase.hot.filter.GeoRankingFilter;
import com.despegar.p13n.hestia.data.hbase.hot.filter.LimitRankingFilter;
import com.despegar.p13n.hestia.data.hbase.hot.filter.RankingFilter;
import com.despegar.p13n.hestia.data.hbase.hot.filter.ShipIdRankingFilter;
import com.despegar.p13n.hestia.data.hbase.hot.render.RankingRenderFilterConfig.RankingRenderFilterId;
import com.despegar.p13n.hestia.data.hbase.hot.types.grouping.RankingCarCategoryCheckoutGroupFunction;
import com.despegar.p13n.hestia.data.hbase.hot.types.grouping.RankingGroupFunction;

import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKey.PRODUCT_CODE_CC;
import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKey.PRODUCT_CODE_CC_CIMONTH_DESTINATION;
import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKey.PRODUCT_CODE_CC_CIMONTH_ORIGIN_DESTINATION;
import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKey.PRODUCT_CODE_CC_DESTINATION;
import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKey.PRODUCT_CODE_CC_LOC;
import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKey.PRODUCT_CODE_CC_ORIGIN_DESTINATION;
import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKey.PRODUCT_CODE_DEPARTURE;
import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKey.PRODUCT_CODE_DEST;
import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKey.PRODUCT_CODE_LOC;

public enum StaticRankingTypes implements RankingType {

    /**
     * DON'T FORGET TO ADD NEW RANKING TYPE CODES IN #RankingTypeCodes
     */
    HOT_SEARCHES_DESTINATIONS_ANY(RankingTypeCodes.HOT_SEARCHES_DESTINATIONS_ANY, PRODUCT_CODE_LOC,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.IATA_EXISTS)),
    MOBILE_HOT_SEARCHES_DESTINATIONS_ANY(RankingTypeCodes.MOBILE_HOT_SEARCHES_DESTINATIONS_ANY, PRODUCT_CODE_LOC,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.IATA_EXISTS)),
    HOT_SEARCHES_DESTINATIONS_DOMESTIC(RankingTypeCodes.HOT_SEARCHES_DESTINATIONS_DOMESTIC, PRODUCT_CODE_LOC,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.IATA_EXISTS)),
    MOBILE_HOT_SEARCHES_DESTINATIONS_DOMESTIC(RankingTypeCodes.MOBILE_HOT_SEARCHES_DESTINATIONS_DOMESTIC, PRODUCT_CODE_LOC,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.IATA_EXISTS)),
    HOT_SEARCHES_DESTINATIONS_INTERNATIONAL(RankingTypeCodes.HOT_SEARCHES_DESTINATIONS_INTERNATIONAL, PRODUCT_CODE_LOC,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.IATA_EXISTS)),
    MOBILE_HOT_SEARCHES_DESTINATIONS_INTERNATIONAL(RankingTypeCodes.MOBILE_HOT_SEARCHES_DESTINATIONS_INTERNATIONAL,
                    PRODUCT_CODE_LOC,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.IATA_EXISTS)),
    CRUISE_REGIONS(RankingTypeCodes.CRUISE_REGIONS, PRODUCT_CODE_LOC,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    CRUISE_DID(RankingTypeCodes.CRUISE_DEPARTURE_ID, PRODUCT_CODE_LOC, new RankingTypeParams(
        new CompositeRankingFilter(new DidCheckInFilter(), new ShipIdRankingFilter(), new LimitRankingFilter()), true,
        RankingRenderFilterId.ALL)),
    CRUISE_DID_THEME(RankingTypeCodes.CRUISE_DID_THEME, PRODUCT_CODE_LOC, new RankingTypeParams(
        new CompositeRankingFilter(new DidCheckInFilter(), new ShipIdRankingFilter(), new LimitRankingFilter()), true,
        RankingRenderFilterId.ALL)),
    VACATIONS_RENTALS_VRID(RankingTypeCodes.VACATION_RENTALS_VRID, PRODUCT_CODE_LOC,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    VACATIONS_RENTALS_CONTACT_VRID(RankingTypeCodes.VACATIONS_RENTALS_CONTACT_VRID, PRODUCT_CODE_DEST,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    VACATIONS_RENTALS_VRID_BY_DESTINATION(RankingTypeCodes.VACATION_RENTALS_VRID_IN_DESTINATION, PRODUCT_CODE_DEST,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    SEO_HOT(RankingTypeCodes.SEO_HOT, PRODUCT_CODE_CC_LOC,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    ACTIVITIES_DETAIL_ACTID(RankingTypeCodes.ACTIVITIES_DETAIL_ACTID, PRODUCT_CODE_LOC,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    ACTIVITIES_DETAIL_ACTID_HB(RankingTypeCodes.ACTIVITIES_DETAIL_ACTID_HB, PRODUCT_CODE_LOC,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    ACTIVITIES_DETAIL_ACTID_HB_ORL(RankingTypeCodes.ACTIVITIES_DETAIL_ACTID_HB_ORL, PRODUCT_CODE_LOC,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    HOTEL_DETAIL_DESTINATON(RankingTypeCodes.HOTEL_DETAIL_DESTINATION, PRODUCT_CODE_DEST,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    MOBILE_HOTEL_DETAIL_DESTINATON(RankingTypeCodes.MOBILE_HOTEL_DETAIL_DESTINATION, PRODUCT_CODE_DEST,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    CRUISE_DETAIL_DESTINATION(RankingTypeCodes.CRUISE_DETAIL_DESTINATION, PRODUCT_CODE_DEST, new RankingTypeParams(
        new CompositeRankingFilter(new DidCheckInFilter(), new ShipIdRankingFilter(), new LimitRankingFilter()), true,
        RankingRenderFilterId.ALL)),
    PACKAGE_DETAIL_DESTINATION(RankingTypeCodes.PACKAGE_DETAIL_DESTINATION, PRODUCT_CODE_DEST,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    COMBINED_PACK_DURATION(RankingTypeCodes.DURATION_CC_DESTINATION, PRODUCT_CODE_CC_CIMONTH_ORIGIN_DESTINATION,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    COMBINED_PACK_ABS_DURATION(RankingTypeCodes.DURATION_CC_ABS_DESTINATION, PRODUCT_CODE_CC_CIMONTH_ORIGIN_DESTINATION,
                    new RankingTypeParams(new LimitRankingFilter(), false, RankingRenderFilterId.ALL)),
    PACKAGES_ABS_DURATION(RankingTypeCodes.PACKAGE_DURATION_CC_ABS_DESTIONATION, PRODUCT_CODE_CC_CIMONTH_DESTINATION,
                    new RankingTypeParams(new LimitRankingFilter(), false, RankingRenderFilterId.ALL)),
    DURATION_DI_ANY_DESTINATION(RankingTypeCodes.DURATION_CC_DI_ANY_DESTINATION, PRODUCT_CODE_CC_CIMONTH_DESTINATION,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    DURATION_DI_ALONE_DESTINATION(RankingTypeCodes.DURATION_CC_DI_ALONE_DESTINATION, PRODUCT_CODE_CC_CIMONTH_DESTINATION,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    DURATION_DI_MANY_DESTINATION(RankingTypeCodes.DURATION_CC_DI_MANY_DESTINATION, PRODUCT_CODE_CC_CIMONTH_DESTINATION,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    ACTIVITIES_THANKS_ACTID_DEST(RankingTypeCodes.ACTIVITIES_ID_THANKS_DEST, PRODUCT_CODE_DEST,
                    new RankingTypeParams(new LimitRankingFilter(), false, RankingRenderFilterId.ALL)),
    ACTIVITIES_DETAIL_ACTID_DEST(RankingTypeCodes.ACTIVITIES_ID_DEST, PRODUCT_CODE_DEST,
                    new RankingTypeParams(new LimitRankingFilter(), false, RankingRenderFilterId.ALL)),
    ACTIVITIES_DETAIL_ACTID_DEST_HB(RankingTypeCodes.ACTIVITIES_ID_DEST_HB, PRODUCT_CODE_DEST,
                    new RankingTypeParams(new LimitRankingFilter(), false, RankingRenderFilterId.ALL)),
    PACK_DATA_DEST(RankingTypeCodes.PACK_DATA_DEST, PRODUCT_CODE_CC_CIMONTH_ORIGIN_DESTINATION,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    FLIGHT_TRIP_TYPE(RankingTypeCodes.FLIGHT_TRIP_TYPE, PRODUCT_CODE_CC_ORIGIN_DESTINATION,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),
    HOTEL_STARS_DESTINATION(RankingTypeCodes.HOTEL_STARS_DESTINATION, PRODUCT_CODE_CC_DESTINATION,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),

    VIAJEROS_DESTINATIONS_ANY(RankingTypeCodes.VIAJEROS_DESTINATIONS_ANY, PRODUCT_CODE_LOC,
                    new RankingTypeParams(new LimitRankingFilter(), true, RankingRenderFilterId.ALL)),

    CRUISE_DEPARTURE_DID(RankingTypeCodes.CRUISE_DEPARTURE_DID, PRODUCT_CODE_DEPARTURE, new RankingTypeParams(
        new CompositeRankingFilter(new DidCheckInFilter(), new ShipIdRankingFilter(), new LimitRankingFilter()), true,
        RankingRenderFilterId.ALL)),
    CAR_SEARCH(RankingTypeCodes.CARS_SEARCH, PRODUCT_CODE_CC_LOC,
                    new RankingTypeParams(new LimitRankingFilter(), false, RankingRenderFilterId.ALL)),
    CAR_CAT_CHECKOUT(RankingTypeCodes.CARS_CAT_CHECKOUT, PRODUCT_CODE_CC_LOC, new RankingTypeParams(new LimitRankingFilter(),
        false, RankingRenderFilterId.ALL, new RankingCarCategoryCheckoutGroupFunction())),

    USER_PROFILE(RankingTypeCodes.USER_PROFILE, null, new RankingTypeParams(null, false, RankingRenderFilterId.IATA_EXISTS)),

    // @Deprecated
    GEO_AUTOCOMPLETE(RankingTypeCodes.GEO_AUTOCOMPLETE, PRODUCT_CODE_CC,
                    new RankingTypeParams(new LimitRankingFilter(), false, RankingRenderFilterId.ALL)),

    GEO_RANKING(RankingTypeCodes.GEO_EVENT_AUTO, RankingKey.GEO_CODE_BRAND,
                    new RankingTypeParams(new GeoRankingFilter(), true, RankingRenderFilterId.ALL)),

    CRUISE_DETAIL_CN(RankingTypeCodes.CRUISE_DETAIL_CN, PRODUCT_CODE_CC,
                    new RankingTypeParams(new DidCheckInFilter(), false, RankingRenderFilterId.ALL)),
    CRUISE_THANKS_CN(RankingTypeCodes.CRUISE_THANKS_CN, PRODUCT_CODE_CC,
                    new RankingTypeParams(new DidCheckInFilter(), false, RankingRenderFilterId.ALL));

    private static Map<String, StaticRankingTypes> lookup = new HashMap<String, StaticRankingTypes>();

    static {
        for (StaticRankingTypes type : values()) {
            lookup.put(type.getTypeCode().toUpperCase(), type);
        }
    }

    private RankingTypeCodes type;
    private RankingKey key;
    private RankingFilter filter;
    private boolean fallback;
    private RankingRenderFilterId renderFilter;
    private RankingGroupFunction rankingGroupFunction;


    private StaticRankingTypes(RankingTypeCodes code, RankingKey rankingKey, RankingTypeParams params) {
        this.type = code;
        this.key = rankingKey;
        this.filter = params.getRankingFilter();
        this.fallback = params.isFallback();
        this.renderFilter = params.getRenderFilter();
        this.rankingGroupFunction = params.getRankingGroupFunction();
    }

    public String getTypeCode() {
        return this.type.code();
    }

    public RankingTypeCodes getType() {
        return this.type;
    }

    public boolean hasFilter() {
        return this.filter != null;
    }

    public boolean isFallback() {
        return this.fallback;
    }

    public RankingFilter getFilter() {
        return this.filter;
    }

    public static StaticRankingTypes fromType(String code) {
        StaticRankingTypes result = null;
        if (code != null) {
            result = lookup.get(code.toUpperCase());
        }
        return result;
    }

    public RankingKey getRankingKey() {
        return this.key;
    }

    public RankingRenderFilterId getRankingRenderFilterId() {
        return this.renderFilter;
    }

    public RankingGroupFunction getRankingGroupFunction() {
        return this.rankingGroupFunction;
    }
}
