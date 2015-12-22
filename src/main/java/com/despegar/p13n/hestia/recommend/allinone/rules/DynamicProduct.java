package com.despegar.p13n.hestia.recommend.allinone.rules;

import java.util.EnumSet;

/** 
 * Strategy to be applied to resolve the product
 */
public enum DynamicProduct {
    FIRST, //
    SECOND, //
    THIRD, //
    LAST; // last search


    public static EnumSet<DynamicProduct> rows() {
        return EnumSet.of(FIRST, SECOND, THIRD);
    }
}
