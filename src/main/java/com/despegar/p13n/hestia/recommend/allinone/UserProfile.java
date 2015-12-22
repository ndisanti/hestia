package com.despegar.p13n.hestia.recommend.allinone;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;

public class UserProfile {

    private CountryCode cc;
    private RulesVersion rule;
    private Product product;
    private ActivityType activity;

    public UserProfile(CountryCode cc, RulesVersion rule, Product product, ActivityType activity) {
        super();
        this.cc = cc;
        this.rule = rule;
        this.product = product;
        this.activity = activity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.activity == null) ? 0 : this.activity.hashCode());
        result = prime * result + ((this.cc == null) ? 0 : this.cc.hashCode());
        result = prime * result + ((this.product == null) ? 0 : this.product.hashCode());
        result = prime * result + ((this.rule == null) ? 0 : this.rule.hashCode());
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
        UserProfile other = (UserProfile) obj;
        if (this.activity != other.activity) {
            return false;
        }
        if (this.cc != other.cc) {
            return false;
        }
        if (this.product != other.product) {
            return false;
        }
        if (this.rule != other.rule) {
            return false;
        }
        return true;
    }

    public CountryCode getCc() {
        return this.cc;
    }

    public void setCc(CountryCode cc) {
        this.cc = cc;
    }

    public RulesVersion getRule() {
        return this.rule;
    }

    public void setRule(RulesVersion rule) {
        this.rule = rule;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ActivityType getActivity() {
        return this.activity;
    }

    public void setActivity(ActivityType activity) {
        this.activity = activity;
    }



}
