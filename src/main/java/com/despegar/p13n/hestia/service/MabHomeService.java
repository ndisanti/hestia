package com.despegar.p13n.hestia.service;

import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.mab.MabTest;

public interface MabHomeService {

    MabTest<ActionRecommendation, HomeContent> getRulesMabTest();

    HomeContent callEngine(ActionRecommendation action);

}
