package com.despegar.p13n.hestia.recommend.allinone;

import java.util.List;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;

public interface ProfilesService {

    void registerProfile(HomeParam homeParam, Product homeToCheck, RulesVersion version, boolean isLastResort,
        ActivityType activity);

    List<String> dumpLastResortProfiles();

}
