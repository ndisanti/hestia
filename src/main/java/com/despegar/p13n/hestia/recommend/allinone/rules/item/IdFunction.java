package com.despegar.p13n.hestia.recommend.allinone.rules.item;

import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ItemIdFuncCode;
import com.google.common.base.Preconditions;

/**
 * Wraps the function to execute and its optional params
 */
public class IdFunction {

    public static IdFunction LAST_SEARCH_DESTINATIONS = IdFunction.create(ItemIdFuncCode.LAST_SEARCH_DESTINATIONS);
    public static IdFunction SEARCH_DESTINATIONS = IdFunction.create(ItemIdFuncCode.SEARCH_DESTINATIONS);
    public static IdFunction BUY_DESTINATIONS = IdFunction.create(ItemIdFuncCode.BUY_DESTINATIONS);
    public static IdFunction BUY_DESTINATION = IdFunction.create(ItemIdFuncCode.BUY_DESTINATION);

    private final ItemIdFuncCode functionCode;
    private final Param param;

    public Param getParam() {
        return this.param;
    }

    public IdFunction(ItemIdFuncCode f) {
        Preconditions.checkNotNull(f);
        this.functionCode = f;
        this.param = new Param();
    }

    public IdFunction(ItemIdFuncCode f, Param param) {
        Preconditions.checkNotNull(f);
        Preconditions.checkNotNull(param);
        this.functionCode = f;
        this.param = param;
    }

    public static IdFunction create(ItemIdFuncCode functionCode) {
        return new IdFunction(functionCode);
    }

    public IdFunction index(int index) {
        return new IdFunction(this.functionCode, this.param.index(index));
    }

    public IdFunction build() {
        return this;
    }

    public ItemIdFuncCode getFunctionCode() {
        return this.functionCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.functionCode == null) ? 0 : this.functionCode.hashCode());
        result = prime * result + ((this.param == null) ? 0 : this.param.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        IdFunction other = (IdFunction) obj;
        if (this.functionCode != other.functionCode) {
            return false;
        }
        if (this.param == null) {
            if (other.param != null) {
                return false;
            }
        } else if (!this.param.equals(other.param)) {
            return false;
        }
        return true;
    }

    public String toDebug() {
        return this.functionCode.getDebugCode() + this.param.toDebug();
    }

    @Override
    public String toString() {
        return "Function [functionCode=" + this.functionCode + ", param=" + this.param + "]";
    }

}
