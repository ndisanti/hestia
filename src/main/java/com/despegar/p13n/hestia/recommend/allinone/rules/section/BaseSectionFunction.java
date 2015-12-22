package com.despegar.p13n.hestia.recommend.allinone.rules.section;

import java.util.List;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;

@Service
public abstract class BaseSectionFunction
    extends BaseFunction
    implements SectionFunction {


    @Override
    public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {
        throw new UnsupportedOperationException("Offers building is not supported for this function");
    }

    @Override
    public RowHome buildRow(Product home, Product pr, ActionRecommendation action, Param param) {
        throw new UnsupportedOperationException("Row building is not supported for this function");
    }

}
