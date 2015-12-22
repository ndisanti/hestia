package com.despegar.p13n.hestia.recommend.allinone.rules.function.core;

public enum ProductFuncCode {
    PRODUCTS("PROD"),
    FLIGHTS("FLI"),
    HOTELS("HOT"),
    CARS("CAR"),
    PACKAGES("PAC"),
    ACTIVITIES("ACT"),
    CRUISES("CRU"),
    MULTIDESTINATION_SEARCH_PRODUCTS("MUL"),
    VACATION_RENTALS("VR");

    private final String debugCode;

    private ProductFuncCode(String debugCode) {
        this.debugCode = debugCode;
    }

    public String getDebugCode() {
        return this.debugCode;
    }

}
