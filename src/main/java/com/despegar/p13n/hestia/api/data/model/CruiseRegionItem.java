package com.despegar.p13n.hestia.api.data.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class CruiseRegionItem   extends ItemHome {

    public String reg;

    @Deprecated
    public CruiseRegionItem() {
        super(ItemType.CRUISE_REGION);
        // constructor for serializacion
    }

    /**
     * @param reg
     */
    public CruiseRegionItem(String reg) {
        super(ItemType.CRUISE_REGION);
        this.reg = reg;
    }

    public String getReg() {
        return this.reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    @Override
    @JsonIgnore
    public String getId() {
        return this.reg;
    }

    @Override
    public String toString() {
        return "CruiseRegionItem [reg=" + this.reg + "]";
    }
}
