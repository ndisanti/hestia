package com.despegar.p13n.hestia.recommend.allinone.rules;

import com.despegar.p13n.hestia.recommend.allinone.Usage;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.item.IdFunction;

public class RuleItem {

    private final IdFunction itemIdFunction;
    private final ProductFuncCode productFunction;
    private final Usage usage;

    public RuleItem(IdFunction itemIdFunction, ProductFuncCode productFunction) {
        this.itemIdFunction = itemIdFunction;
        this.productFunction = productFunction;
        this.usage = new Usage();
    }

    public IdFunction getItemIdFunction() {
        return this.itemIdFunction;
    }

    public ProductFuncCode getProductFunction() {
        return this.productFunction;
    }

    public Usage getUsage() {
        return this.usage;
    }

    public String toDebug() {
        return this.itemIdFunction.toDebug() + " " + this.productFunction.getDebugCode();
    }

    @Override
    public String toString() {
        return "RuleItem [itemIdFunction=" + this.itemIdFunction + ", productFunction=" + this.productFunction + ", usage="
            + this.usage + "]";
    }


}
