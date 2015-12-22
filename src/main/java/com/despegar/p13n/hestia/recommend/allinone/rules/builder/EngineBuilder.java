package com.despegar.p13n.hestia.recommend.allinone.rules.builder;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.despegar.p13n.hestia.recommend.allinone.rules.EngineFacade;
import com.despegar.p13n.hestia.recommend.allinone.rules.ItemRuleEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.LastResortEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.NoHistoryEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionFunctionEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionRuleEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.version.DynamicServiceFunctionRules;
import com.despegar.p13n.hestia.recommend.allinone.rules.version.DynamicServiceRules;
import com.despegar.p13n.hestia.recommend.allinone.rules.version.LastResortRules;
import com.despegar.p13n.hestia.recommend.allinone.rules.version.MonoDestinationRules;
import com.despegar.p13n.hestia.recommend.allinone.rules.version.MultiDestinationRules;
import com.despegar.p13n.hestia.recommend.allinone.rules.version.NoHistoryRules;
import com.google.common.annotations.VisibleForTesting;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;

/**
 * <p>Rules definitions for rule engine</p>
 * 
 * <p>Based on: https://docs.google.com/spreadsheet/ccc?key=0AkRFlvl8wvPwdGJDc1FmSmFVY0pwVXpUTDk3cDIxWWc&usp=drive_web#gid=3</p>
 */
@Component
public class EngineBuilder {

    protected static final Logger LOG = LoggerFactory.getLogger(EngineBuilder.class);

    @Autowired
    private SectionFunctionEngine functionEngine;
    @Autowired
    private SectionRuleEngine sectionRuleEngine;
    @Autowired
    private LastResortEngine lastResortEngine;
    @Autowired
    private NoHistoryEngine noHistoryEngine;
    @Autowired
    private EngineFacade engineFacade;
    @Autowired
    private ItemRuleEngine itemRuleEngine;
   
    @PostConstruct
    public void build() {

        new LastResortRules(this.lastResortEngine);
        new NoHistoryRules(this.noHistoryEngine);

        this.noHistoryEngine.start();
        this.lastResortEngine.start();

        new DynamicServiceFunctionRules(this.functionEngine);
        // new StaticRules(this.sectionRuleEngine);
        new DynamicServiceRules(this.sectionRuleEngine);

        new MonoDestinationRules(this.itemRuleEngine);
        new MultiDestinationRules(this.itemRuleEngine);

        this.engineFacade.start();
    }

    // run every 20 minutes
    @Trace(dispatcher = true)
    @Scheduled(cron = "0 0/20 * * * *")
    public void fillLastResortCache() {
        NewRelic.setTransactionName("submit", "EngineBuilder.fillLastResortCache");
        this.lastResortEngine.buildLastResort();
    }

    @VisibleForTesting
    public void setFunctionEngine(SectionFunctionEngine functionEngine) {
        this.functionEngine = functionEngine;
    }

    @VisibleForTesting
    public void setSectionRuleEngine(SectionRuleEngine sectionRuleEngine) {
        this.sectionRuleEngine = sectionRuleEngine;
    }

    @VisibleForTesting
    public void setEngineFacade(EngineFacade engineFacade) {
        this.engineFacade = engineFacade;
    }

    @VisibleForTesting
    public void setNoHistoryEngine(NoHistoryEngine noHistoryEngine) {
        this.noHistoryEngine = noHistoryEngine;
    }

    @VisibleForTesting
    public void setLastResortEngine(LastResortEngine lastResortEngine) {
        this.lastResortEngine = lastResortEngine;
    }

    @VisibleForTesting
    public void setItemRuleEngine(ItemRuleEngine itemRuleEngine) {
        this.itemRuleEngine = itemRuleEngine;
    }

}
