package com.despegar.p13n.hestia.recommend.allinone.rules.function.core;

public enum ItemIdFuncCode {
    SEARCH_DESTINATIONS("SRCH_DESTS"), //
    BUY_DESTINATIONS("BUY_DESTS"), //
    BUY_DESTINATION("BUY_DEST"), //
    LAST_SEARCH_DESTINATIONS("LAST_SRCH_DESTS");

    private final String debugCode;

    private ItemIdFuncCode(String debugCode) {
        this.debugCode = debugCode;
    }

    public String getDebugCode() {
        return this.debugCode;
    }

}
