package com.despegar.p13n.hestia.external.hotels.v3.dtos;

import java.io.Serializable;

public class DescriptionsDTO
    implements Serializable {


    private static final long serialVersionUID = 1L;
    private String pt;
    private String en;
    private String es;

    public DescriptionsDTO() {
    }

    public String getPt() {
        return this.pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getEn() {
        return this.en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getEs() {
        return this.es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    @Override
    public String toString() {
        return "DescriptionsDTO [pt=" + this.pt + ", en=" + this.en + ", es=" + this.es + "]";
    }
}
