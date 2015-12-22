package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;

/**
 * Offer: hotel id from detail or destination from search
 * 
 * Row: hotel id from detail or destination from search
 *      detail/detail recommendation if was detail, else search/search recommendation
 */
@Service
public class HotelSearchAndRecommendFunction
    extends BaseSectionFunction {

    @Autowired
    private RecommendFunction recommendFunction;

    @Override
    public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {

        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH);
        ItemUtils.checkProduct(pr, Product.HOTELS);

        UserActivity activity = action.getSearchActivity().getActivityOrLast(Product.HOTELS);

        action.getTitleData().addDestination(activity.getDestination());

        if (activity.isDetailOrCheckout() && activity.getProduct() == Product.HOTELS) {
            param = param.flow1(Flow.DETAIL).crossFlow2(Flow.DETAIL).pr1(Product.HOTELS).addSearch(true);
        } else {
            param = param.flow1(Flow.SEARCH).crossFlow2(Flow.SEARCH).pr1(Product.HOTELS).addSearch(true);
        }


        return this.recommendFunction.buildOffers(home, pr, action, param);
    }

    @Override
    public RowHome buildRow(Product home, Product pr, ActionRecommendation action, Param param) {

        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH);
        ItemUtils.checkProduct(pr, Product.HOTELS);

        UserActivity activity = action.getSearchActivity().getLastActivity();

        if (activity.isDetailOrCheckout() && activity.getProduct() == Product.HOTELS) {
            param = param.flow1(Flow.DETAIL).crossFlow2(Flow.DETAIL).pr1(Product.HOTELS).addSearch(true);
        } else {
            param = param.flow1(Flow.SEARCH).crossFlow2(Flow.SEARCH).pr1(Product.HOTELS).addSearch(true);
        }

        action.getTitleData().addDestination(activity.getDestination());

        return this.recommendFunction.buildRow(home, pr, action, param);
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.HOTEL_SEARCH_AND_RECOMMEND;
    }

    @Override
    public String getDescription() {
        return "STAR: hotel id del detail (si llego) o destination del search. "
            + "ROW: recomendacion detail/detail (si llego) recomendacion search/search. Se agregan ultimos destinos buscados";
    }

}
