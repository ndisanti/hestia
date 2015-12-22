package com.despegar.p13n.hestia.recommend.allinone.activity;

import java.util.EnumSet;


/**
 * Most relevant type of user activity.
 * 
 * If a user buys it overrides any search.
 *
 */
public enum ActivityType {
    NO_HISTORY, //
    SEARCH, //
    BUY,
    LAST_RSRT;// used for last resort cache

    public static EnumSet<ActivityType> BIZ_TYPES = EnumSet.of(//
        BUY, //
        SEARCH,//
        NO_HISTORY);
}
