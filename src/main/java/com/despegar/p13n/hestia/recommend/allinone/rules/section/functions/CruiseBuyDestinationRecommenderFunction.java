package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.google.common.base.Preconditions;

/**
 * Cruise destination: first iata from itinerary
 *  
 * Row: recommendation thanks/thanks for cruise destination
 */
@Service
public class CruiseBuyDestinationRecommenderFunction
    extends BaseSectionFunction {

    @Autowired
    private RecommendFunction recommendFunction;

    @Override
    public RowHome buildRow(Product home, Product pr, ActionRecommendation action, Param param) {

        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.BUY);
        Preconditions.checkArgument(action.getBuyActivity().getProduct() == Product.CRUISES,
            "Only Cruises is supported:(%s)", action.getBuyActivity().getProduct());

        String destination = action.getBuyActivity().getActivity().getDestination();

        action.getTitleData().addDestination(destination);

        String origin = action.getOrigin();

        // you saw
        ItemHome youSaw = this.buildDestination(pr, null, destination, origin, action);
        youSaw = ItemUtils.checkUnique(home, youSaw, action, pr);

        param = param.flow1(Flow.THANKS).crossFlow2(Flow.THANKS).pr1(pr);

        RowHome rowHome = this.recommendFunction.buildRow(home, pr, action, param);

        if (rowHome != null) {
            rowHome.setHighlighted(youSaw);
            ItemUtils.addSubtitles(action);
        }

        return rowHome;
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.CRUISE_BUY_DESTINATION_RECOM;
    }

    @Override
    public String getDescription() {
        return "Recomendador de destinos luego de una compra de cruceros. "
            + "Destino crucero: primer iata del itinerario. "
            + "Para ROW: recomendacion thanks/thanks para el destino del crucero";
    }

}
