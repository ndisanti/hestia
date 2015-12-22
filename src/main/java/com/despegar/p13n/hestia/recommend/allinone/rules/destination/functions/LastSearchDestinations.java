package com.despegar.p13n.hestia.recommend.allinone.rules.destination.functions;

import java.util.List;

import org.springframework.stereotype.Service;

import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ItemIdFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ItemIdFunction;
import com.google.common.collect.Lists;

@Service
public class LastSearchDestinations
    implements ItemIdFunction {

    @Override
    public List<String> getItemIds(ActionRecommendation action) {
        return Lists.newArrayList(action.getLastDestination());
    }

    @Override
    public ItemIdFuncCode getFunctionCode() {
        return ItemIdFuncCode.LAST_SEARCH_DESTINATIONS;
    }

    @Override
    public ItemTypeId getItemTypeId() {
        return ItemTypeId.DESTINATION;
    }

}
