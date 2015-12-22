package com.despegar.p13n.hestia.recommend.allinone.rules.function.core;

import java.util.List;

import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;

/**
 * <p>Based on a actionRecommendation it builds a item id list</p>
 * 
 * <p>It can returns destinations, hotel ids, cruise dids...
 * 
 * <p>A {@link ItemIdFunction} is associated with a unique {@link ItemIdFuncCode}</p>
 */
public interface ItemIdFunction {

    List<String> getItemIds(ActionRecommendation action);

    ItemIdFuncCode getFunctionCode();

    ItemTypeId getItemTypeId();
}
