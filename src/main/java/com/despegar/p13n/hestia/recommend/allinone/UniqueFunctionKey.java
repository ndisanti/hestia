package com.despegar.p13n.hestia.recommend.allinone;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.title.SectionType;

public class UniqueFunctionKey {

    private final SectionType sectionType;
    private final Product home;
    private final Product offer;
    private final Function function;

    public UniqueFunctionKey(SectionType sectionType, Product home, Product offer, Function function) {
        this.sectionType = sectionType;
        this.home = home;
        this.offer = offer;
        this.function = function;
    }

    public SectionType getSectionType() {
        return this.sectionType;
    }

    public Product getHome() {
        return this.home;
    }

    public Product getOffer() {
        return this.offer;
    }

    public Function getFunction() {
        return this.function;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.function == null) ? 0 : this.function.hashCode());
        result = prime * result + ((this.home == null) ? 0 : this.home.hashCode());
        result = prime * result + ((this.offer == null) ? 0 : this.offer.hashCode());
        result = prime * result + ((this.sectionType == null) ? 0 : this.sectionType.hashCode());
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
        UniqueFunctionKey other = (UniqueFunctionKey) obj;
        if (this.function == null) {
            if (other.function != null) {
                return false;
            }
        } else if (!this.function.equals(other.function)) {
            return false;
        }
        if (this.home != other.home) {
            return false;
        }
        if (this.offer != other.offer) {
            return false;
        }
        if (this.sectionType != other.sectionType) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UniqueFunctionKey [sectionType=" + this.sectionType + ", home=" + this.home + ", offer=" + this.offer
            + ", function=" + this.function + "]";
    }



}
