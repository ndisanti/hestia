package com.despegar.p13n.hestia.api.data.model;

public class CarsLocationDto {

    private String pul;
    private String pultype;

    public CarsLocationDto() {

    }

    public CarsLocationDto(String pul, String pulType) {
        this.setPul(pul);
        this.setPultype(pulType);
    }

    public String getPul() {
        return this.pul;
    }

    public void setPul(String pul) {
        this.pul = pul;
    }

    public String getPultype() {
        return this.pultype;
    }

    public void setPultype(String pultype) {
        this.pultype = pultype;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.pul == null) ? 0 : this.pul.hashCode());
        result = prime * result + ((this.pultype == null) ? 0 : this.pultype.hashCode());
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
        CarsLocationDto other = (CarsLocationDto) obj;
        if (this.pul == null) {
            if (other.pul != null) {
                return false;
            }
        } else if (!this.pul.equals(other.pul)) {
            return false;
        }
        if (this.pultype == null) {
            if (other.pultype != null) {
                return false;
            }
        } else if (!this.pultype.equals(other.pultype)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CarsLocationDto [pul=" + this.pul + ", pultype=" + this.pultype + "]";
    }

}
