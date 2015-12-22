package com.despegar.p13n.hestia.api.data.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class CruiseItem  extends ItemHome {

    private String did;

    @Deprecated
    public CruiseItem() {
        super(ItemType.CRUISE);
        // constructor for serializacion
    }

    /**
     * @param did
     */
    public CruiseItem(String did) {
        super(ItemType.CRUISE);
        this.did = did;
    }

    public String getDid() {
        return this.did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    @Override
    @JsonIgnore
    public String getId() {
        return this.did;
    }

    @Override
    public String toString() {
        return "CruiseItem [did=" + this.did + "]";
    }


}
