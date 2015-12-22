package com.despegar.p13n.hestia.data.hbase.hot.types;

public enum RankingKeyPart {

	PRODUCT("PRODUCT", 1), //
    CODE("RANKINGCODE", 1), //
    COUNTRY_AREA_OID_IATA("COID|AOID|IATA", 3), //
    CC("CC", 1), //
    CIMONTH("CIMONTH", 1), //
    ORIGIN("ORIGIN", 1),
    IATA_CODE("IATA", 1),
    GEO_RANKING("GEOAUTO", 1),
    BRAND("BRAND", 1);

    private String code;
    private int length;

    private RankingKeyPart(String code, int length) {
        this.code = code;
        this.length = length;
    }

    public String code() {
        return this.code;
    }

    public int length() {
        return this.length;
    }

    @Override
    public String toString() {
        return this.code();
    }
}
