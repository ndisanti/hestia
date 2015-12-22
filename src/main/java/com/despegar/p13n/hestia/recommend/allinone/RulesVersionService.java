package com.despegar.p13n.hestia.recommend.allinone;

import org.apache.commons.collections.map.DefaultedMap;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;

@Service
public class RulesVersionService {


    private DefaultedMap rulesVersionStrategyMap;

    public RulesVersionService() {
        this.rulesVersionStrategyMap = new DefaultedMap(RulesVersion.getDefault());
        this.rulesVersionStrategyMap.put(CountryCode.PE, RulesVersion.MULTI_DESTINATION);
    }

    public RulesVersion getRulesVersion(CountryCode countryCode) {
        return (RulesVersion) this.rulesVersionStrategyMap.get(countryCode);
    }

}
