package com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority;

import com.despegar.p13n.hestia.recommend.allinone.Usage;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.google.common.base.Preconditions;

public class SectionFunctionContent {

    private final Function function;
    private final Usage usage;

    public SectionFunctionContent(Function function) {
        Preconditions.checkNotNull(function);
        this.function = function;
        this.usage = new Usage();
    }

    public Function getFunction() {
        return this.function;
    }

    public Usage getUsage() {
        return this.usage;
    }

    @Override
    public String toString() {
        return this.function.toString();
    }

}
