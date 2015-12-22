package com.despegar.p13n.hestia.recommend.allinone.rules.section;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.FunctionPrecalc;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleData;

/**
 * Wraps current section data that is being building
 */
public class Current {

    public Current() {
    }

    // home level
    public Product currentHome;
    public FunctionPrecalc functionPrecalc;

    // section level
    public SectionsEnum section;
    public TitleData titleData;
    public TitleData subtitleHighlightData;
    public TitleData subtitleOfferData;

}
