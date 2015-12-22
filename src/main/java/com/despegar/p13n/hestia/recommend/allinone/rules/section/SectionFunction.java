package com.despegar.p13n.hestia.recommend.allinone.rules.section;

import java.util.List;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;

/**
 * <p>Based on a actionRecommendation it builds the section</p>
 * 
 * <p>Section can be the offer section or any row</p>
 * 
 * <p>A sectionFunction is associated with a unique {@link SectionFunctionCode}</p>
 */
public interface SectionFunction {

    /**
     * @param home product home
     * @param pr product to offer
     * @param action action recommendation
     * @param param function parameters
     * @param title title for this section
     * @return home item content or null
     */
    List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param);

    /**
     * @param home product home
     * @param pr product to offer
     * @param action action recommendation
     * @param param function parameters
     * @param title title for this section
     * @return home row content or null
     */
    RowHome buildRow(Product home, Product pr, ActionRecommendation action, Param param);

    SectionFunctionCode getFunctionCode();


}
