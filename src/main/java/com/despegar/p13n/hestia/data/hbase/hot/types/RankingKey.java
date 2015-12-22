package com.despegar.p13n.hestia.data.hbase.hot.types;

import static com.despegar.p13n.hestia.api.data.model.RankingDestinationType.DESTINATION;
import static com.despegar.p13n.hestia.api.data.model.RankingDestinationType.DESTINATION_DEPARTURE;
import static com.despegar.p13n.hestia.api.data.model.RankingDestinationType.NOT_APPLICABLE;
import static com.despegar.p13n.hestia.api.data.model.RankingDestinationType.USER_LOCATION;
import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKeyPart.BRAND;
import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKeyPart.CC;
import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKeyPart.CIMONTH;
import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKeyPart.CODE;
import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKeyPart.COUNTRY_AREA_OID_IATA;
import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKeyPart.GEO_RANKING;
import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKeyPart.ORIGIN;
import static com.despegar.p13n.hestia.data.hbase.hot.types.RankingKeyPart.PRODUCT;

import java.util.Arrays;
import java.util.List;

import com.despegar.p13n.hestia.api.data.model.RankingDestinationType;

public enum RankingKey {

	 // PRODUCT should always come first
    // CODE always second (use to determine RankingType)
    PRODUCT_CODE_LOC(USER_LOCATION, PRODUCT, CODE, COUNTRY_AREA_OID_IATA),
    PRODUCT_CODE_DEST(DESTINATION, PRODUCT, CODE, COUNTRY_AREA_OID_IATA),
    PRODUCT_CODE_CC_LOC(USER_LOCATION, PRODUCT, CODE, CC),
    PRODUCT_CODE_CC_CIMONTH_ORIGIN_DESTINATION(DESTINATION, PRODUCT, CODE, CC, CIMONTH, ORIGIN, COUNTRY_AREA_OID_IATA),
    PRODUCT_CODE_CC_CIMONTH_DESTINATION(DESTINATION, PRODUCT, CODE, CC, CIMONTH, COUNTRY_AREA_OID_IATA),
    PRODUCT_CODE_CC_ORIGIN_DESTINATION(DESTINATION, PRODUCT, CODE, CC, ORIGIN, COUNTRY_AREA_OID_IATA),
    PRODUCT_CODE_CC_DESTINATION(DESTINATION, PRODUCT, CODE, CC, COUNTRY_AREA_OID_IATA),
    PRODUCT_CODE_DEPARTURE(DESTINATION_DEPARTURE, PRODUCT, CODE, COUNTRY_AREA_OID_IATA),
    PRODUCT_CODE_CC(NOT_APPLICABLE, PRODUCT, CODE, CC),

    // GEO
    GEO_CODE_BRAND(NOT_APPLICABLE, GEO_RANKING, CODE, BRAND);

    private List<RankingKeyPart> keyParts;
    private RankingDestinationType locationType;

    /**
     * @param locationType Used only when keyParts contains part RankingKeyPart.COUNTRY_AREA_OID_IATA
     * @param keyParts
     */
    private RankingKey(RankingDestinationType locationType, RankingKeyPart... keyParts) {
        this.locationType = locationType;
        this.keyParts = Arrays.asList(keyParts);
    }

    public List<RankingKeyPart> keyParts() {
        return this.keyParts;
    }

    public RankingDestinationType getLocationType() {
        return this.locationType;
    }

    public int size() {
        int length = 0;
        for (RankingKeyPart part : this.keyParts) {
            length += part.length();
        }
        return length;
    }
}
