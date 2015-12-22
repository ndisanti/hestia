package com.despegar.p13n.hestia.recommend.allinone.item;

import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;

public interface ItemStep {
    public ItemHome execute(String destination, ActionRecommendation action);
}
