package com.despegar.p13n.hestia.recommend.allinone.rules;

import java.util.EnumSet;
import java.util.Set;
import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.google.common.collect.ImmutableSet;

public enum CountrySupport {
    /** Specific countries may be handled separately */
    PR, // puerto rico
    AR, // argentina
    VE, // venezuela

    /**Key to be used when only hotels is offered */
    ONLY_HTLS, //

    /** Key to be used for any (product, country) pair, some country may not support the product */
    SOME, //

    /** Key to be used for countries that only support Hotels, Cars and Flights */
    INT, //

    /**Key to be used when (product, country) combination is not supported */
    ALL;

    /**
     * In these countries only hotels will be offered. By now, this is used only for last resort
     */
    private static EnumSet<CountryCode> ONLY_HOTELS_CC = //
    EnumSet.of(CountryCode.NI, CountryCode.HN, CountryCode.BO, CountryCode.SV, CountryCode.GT, CountryCode.PY);


    private static EnumSet<CountrySupport> COUNTRY_LEVEL_RULE = EnumSet.of(AR, PR, VE);

    public static Set<CountryCode> RELEVANTS_CC = ImmutableSet.<CountryCode> builder().addAll(CountryCode.DESPEGAR_CC)
        .addAll(CountryCode.RELEVANT_INTERNATIONAL).build();


    public static CountrySupport fromCountry(CountryCode cc) {

        if (cc == CountryCode.AR) {
            return AR;
        }
        if (cc == CountryCode.PR) {
            return PR;
        }
        if (cc == CountryCode.VE) {
            return VE;
        }

        if (ONLY_HOTELS_CC.contains(cc)) {
            return ONLY_HTLS;
        }

        if (CountryCode.isInternational(cc)) {
            return INT;
        }

        return SOME;
    }

    public static CountrySupport nextPriority(CountrySupport support) {

        // the next step for every country is the first general step
        if (COUNTRY_LEVEL_RULE.contains(support)) {
            return ONLY_HTLS;
        }

        switch (support) {
        case ONLY_HTLS:
            return SOME;
        case INT:
            return SOME;
        case SOME:
            return ALL;
        case ALL:
        default:
            throw new UnsupportedOperationException("No rules supported for: " + support);
        }

    }
}
