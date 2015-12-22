package com.despegar.p13n.hestia.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.despegar.p13n.hestia.recommend.allinone.item.ItemHomeService;
import com.despegar.p13n.hestia.recommend.allinone.rules.EngineFacade;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeContentValidator;
import com.despegar.p13n.hestia.recommend.allinone.rules.ItemRuleEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.LastResortEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.NoHistoryEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleContentFiller;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionFunctionEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionRuleEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ItemIdFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ItemIdFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFunctionWrapper;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleEngine;
import com.despegar.p13n.hestia.service.MultiObjectVersionService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Configuration
public class HomeContentConfiguration {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private HomeContentValidator validator;

    @Autowired
    private TitleEngine titleEngine;

    @Autowired
    private ItemHomeService itemHomeService;

    @Autowired
    private RuleContentFiller ruleContentFiller;

    @Autowired
    private MultiObjectVersionService multiObjectService;
    
    @Autowired
    private SectionFunctionEngine sectionFunctionsEngine;

    @Bean
    public Map<ItemIdFuncCode, ItemIdFunction> itemFunctionsMap() {
        Map<ItemIdFuncCode, ItemIdFunction> map = Maps.newLinkedHashMap();
        Collection<ItemIdFunction> beans = this.context.getBeansOfType(ItemIdFunction.class).values();

        for (ItemIdFunction bean : beans) {
            Preconditions.checkArgument(!map.containsKey(bean.getFunctionCode()));
            map.put(bean.getFunctionCode(), bean);
        }

        // check that all function codes have their FunctionWrapper implementation
        for (ItemIdFuncCode code : ItemIdFuncCode.values()) {
            Preconditions.checkArgument(map.keySet().contains(code),
                "Item id function code %s has no implementation function", code);
        }

        return Collections.unmodifiableMap(map);

    }

    @Bean
    public Map<ProductFuncCode, ProductFunction> productFunctionsMap() {
        Map<ProductFuncCode, ProductFunction> map = Maps.newLinkedHashMap();
        Collection<ProductFunction> beans = this.context.getBeansOfType(ProductFunction.class).values();

        for (ProductFunction bean : beans) {

            Preconditions.checkArgument(!map.containsKey(bean.getFunctionCode()));
            map.put(bean.getFunctionCode(), new ProductFunctionWrapper(bean));
        }

        // check that all function codes have their FunctionWrapper implementation
        for (ProductFuncCode code : ProductFuncCode.values()) {
            Preconditions.checkArgument(map.keySet().contains(code),
                "Item id function code %s has no implementation function", code);
        }
        return Collections.unmodifiableMap(map);

    }

    @Bean
    public ItemRuleEngine createItemRuleEngine() {
        return new ItemRuleEngine(this.itemFunctionsMap(), this.productFunctionsMap(), this.itemHomeService,
            this.createNoHistoryEngine(), this.titleEngine, this.lastResortEngine());
    }

    @Bean
    public NoHistoryEngine createNoHistoryEngine() {
        return new NoHistoryEngine(this.titleEngine, this.sectionFunctionsEngine);
    }

    @Bean
    public LastResortEngine lastResortEngine() {
        return new LastResortEngine(this.validator, this.titleEngine, this.multiObjectService, this.sectionFunctionsEngine);
    }

    @Bean
    public SectionRuleEngine createSectionRuleEngine() {
        return new SectionRuleEngine(this.createNoHistoryEngine(), this.ruleContentFiller, this.sectionFunctionsEngine,
            this.titleEngine, this.validator, this.lastResortEngine());
    }

    private List<RuleEngine> engines() {
        return Lists.newArrayList(this.createItemRuleEngine(), this.createSectionRuleEngine());
    }

    @Bean
    public EngineFacade createRuleEngine() {
        return new EngineFacade(this.engines(), this.lastResortEngine(), this.createNoHistoryEngine(), this.validator);
    }

}
