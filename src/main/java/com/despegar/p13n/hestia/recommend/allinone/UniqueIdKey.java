package com.despegar.p13n.hestia.recommend.allinone;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;

public class UniqueIdKey {

    private Product home;
    private Product offer;
    private ItemTypeId idType;

    public UniqueIdKey(Product home, Product offer, ItemTypeId idType) {
        this.home = home;
        this.offer = offer;
        this.idType = idType;
    }

    public Product getHome() {
        return this.home;
    }

    public void setHome(Product home) {
        this.home = home;
    }

    public Product getOffer() {
        return this.offer;
    }

    public void setOffer(Product offer) {
        this.offer = offer;
    }

    public ItemTypeId getIdType() {
        return this.idType;
    }

    public void setIdType(ItemTypeId idType) {
        this.idType = idType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.home == null) ? 0 : this.home.hashCode());
        result = prime * result + ((this.idType == null) ? 0 : this.idType.hashCode());
        result = prime * result + ((this.offer == null) ? 0 : this.offer.hashCode());
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
        UniqueIdKey other = (UniqueIdKey) obj;
        if (this.home != other.home) {
            return false;
        }
        if (this.idType != other.idType) {
            return false;
        }
        if (this.offer != other.offer) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UniqueIdKey [home=" + this.home + ", offer=" + this.offer + ", idType=" + this.idType + "]";
    }

}
