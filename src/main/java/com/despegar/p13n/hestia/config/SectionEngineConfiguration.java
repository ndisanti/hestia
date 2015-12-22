package com.despegar.p13n.hestia.config;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.despegar.p13n.hestia.recommend.allinone.rules.SectionFunctionEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionWrapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

@Configuration
public class SectionEngineConfiguration {
    @Autowired
    private ApplicationContext context;

    @Bean
    public SectionFunctionEngine sectionFunctionsEngine() {
        Map<SectionFunctionCode, SectionFunctionWrapper> map = Maps.newLinkedHashMap();
        Collection<BaseSectionFunction> beans = this.context.getBeansOfType(BaseSectionFunction.class).values();

        for (BaseSectionFunction bean : beans) {
            Preconditions.checkArgument(!map.containsKey(bean.getFunctionCode()));
            map.put(bean.getFunctionCode(), new SectionFunctionWrapper(bean));
        }

        // check that all function codes have their FunctionWrapper implementation
        for (SectionFunctionCode code : SectionFunctionCode.values()) {
            Preconditions
                .checkArgument(map.keySet().contains(code), "Function code %s has no implementation function", code);
        }
        return new SectionFunctionEngine(Collections.unmodifiableMap(map));
    }
}
