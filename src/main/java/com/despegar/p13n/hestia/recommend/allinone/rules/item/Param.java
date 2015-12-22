package com.despegar.p13n.hestia.recommend.allinone.rules.item;

import com.despegar.p13n.hestia.recommend.allinone.rules.RuleItem;
import com.google.common.base.Preconditions;

/**
 * <p> Immutable parameter class to configurate {@link RuleItem} implementations. </p> 
 */
public class Param {

    // 1-index based
    private final int itemFuncIdx;

    public Param() {
        this.itemFuncIdx = -1;
    }

    public Param(int itemFuncIdx) {
        Preconditions.checkArgument(itemFuncIdx == -1 || itemFuncIdx > 0, "Illegal index: %s", itemFuncIdx);
        this.itemFuncIdx = itemFuncIdx;
    }

    public static Param copy(Param param) {
        return new Param(param.itemFuncIdx);
    }

    public static ParamBuilder builder() {
        return new ParamBuilder();
    }

    public int getItemFuncIdx() {
        return this.itemFuncIdx;
    }

    public Param index(int itemFuncIdx) {
        return ParamBuilder.copy(this).itemFuncIdx(itemFuncIdx).build();
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.itemFuncIdx;
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
        Param other = (Param) obj;
        if (this.itemFuncIdx != other.itemFuncIdx) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "Param [itemFuncIdx=" + this.itemFuncIdx + "]";
    }

    public static class ParamBuilder {
        private int itemFuncIdx;

        public static ParamBuilder copy(Param param) {

            ParamBuilder build = new ParamBuilder();
            build.itemFuncIdx = param.itemFuncIdx;
            return build;
        }

        public ParamBuilder itemFuncIdx(int itemFuncIdx) {
            this.itemFuncIdx = itemFuncIdx;
            return this;
        }

        public Param build() {
            return new Param(this.itemFuncIdx);
        }

    }

    public boolean isEmpty() {
        return this.itemFuncIdx == -1;
    }

    public static void main(String[] args) {
        System.out.println(Param.builder().itemFuncIdx(1).build());
    }

    public String toDebug() {
        return this.isEmpty() ? "" : "(" + this.itemFuncIdx + ")";

    }

}
