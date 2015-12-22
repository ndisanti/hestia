package com.despegar.p13n.hestia.api.data.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class VacationRentalItem extends ItemHome {

    private String vrid;

    @Deprecated
    public VacationRentalItem() {
        super(ItemType.VACATION_RENTAL);
    }

    public VacationRentalItem(String vrid) {
        super(ItemType.VACATION_RENTAL);
        this.vrid = vrid;
    }

    public String getVrid() {
        return this.vrid;
    }

    @Override
    @JsonIgnore
    public String getId() {
        return this.vrid;
    }

    @Override
    public String toString() {
        return "VacationRentalItem [vrid=" + this.vrid + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.vrid == null) ? 0 : this.vrid.hashCode());
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
        VacationRentalItem other = (VacationRentalItem) obj;
        if (this.vrid == null) {
            if (other.vrid != null) {
                return false;
            }
        } else if (!this.vrid.equals(other.vrid)) {
            return false;
        }
        return true;
    }
}
