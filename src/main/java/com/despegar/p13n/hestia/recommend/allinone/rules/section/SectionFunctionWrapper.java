package com.despegar.p13n.hestia.recommend.allinone.rules.section;

import java.util.List;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;


/**
 *  <p>Function wrapper to allow home content transformation.</p>
 *  
 *  <p>Normalize all hotels destinations. airport -> city</p>
 *
 */
public class SectionFunctionWrapper
    extends BaseSectionFunction {

    private BaseSectionFunction function;

    public SectionFunctionWrapper(BaseSectionFunction function) {
        this.function = function;
    }

    @Override
    public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {
        return this.function.buildOffers(home, pr, action, param);
    }

    @Override
    public RowHome buildRow(Product home, Product pr, ActionRecommendation action, Param param) {
        RowHome rowHome = this.function.buildRow(home, pr, action, param);
        return rowHome;
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return this.function.getFunctionCode();
    }

    public BaseSectionFunction getFunction() {
        return this.function;
    }

    @Override
    public String getDescription() {
        return this.function.getDescription();
    }

    @Override
    public String toString() {
        return "FunctionWrapper [function=" + this.function + "]";
    }

}
