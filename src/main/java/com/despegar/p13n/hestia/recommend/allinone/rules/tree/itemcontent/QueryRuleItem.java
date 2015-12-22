package com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.CountrySupport;
import com.despegar.p13n.hestia.recommend.decisiontree.Query;
import com.google.common.base.Preconditions;

public class QueryRuleItem
    implements Query {
    private ActivityType activityType;
    private CountrySupport prSupport;
    private Product home;

    public QueryRuleItem(ActivityType activityType, CountrySupport prSupport, Product home) {
        Preconditions.checkNotNull(activityType);
        // Preconditions.checkNotNull(prSupport); going to be set later
        Preconditions.checkNotNull(home);
        this.activityType = activityType;
        this.prSupport = prSupport;
        this.home = home;
    }

    public ActivityType getActivityType() {
        return this.activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public CountrySupport getPrSupport() {
        return this.prSupport;
    }

    public void setPrSupport(CountrySupport prSupport) {
        this.prSupport = prSupport;
    }

    public Product getHome() {
        return this.home;
    }

    public void setHome(Product home) {
        this.home = home;
    }



    public static class QueryRuleItemBuilder {
        private ActivityType activityType;
        private CountrySupport prSupport;
        private Product home;

        private QueryRuleItemBuilder() {
        }

        private QueryRuleItemBuilder(QueryRuleItem query) {
            this.activityType = query.getActivityType();
            this.prSupport = query.getPrSupport();
            this.home = query.getHome();
        }

        public static QueryRuleItemBuilder create() {
            return new QueryRuleItemBuilder();
        }

        public static QueryRuleItemBuilder create(QueryRuleItem query) {
            return new QueryRuleItemBuilder(query);
        }

        public QueryRuleItemBuilder activity(ActivityType activity) {
            this.activityType = activity;
            return this;
        }

        public QueryRuleItemBuilder support(CountrySupport prSupport) {
            this.prSupport = prSupport;
            return this;
        }

        public QueryRuleItemBuilder home(Product home) {
            this.home = home;
            return this;
        }

        public QueryRuleItem build() {
            return new QueryRuleItem(this.activityType, this.prSupport, this.home);
        }
    }

    @Override
    public String toString() {
        return "QueryRuleItemHome [activityType=" + this.activityType + ", prSupport=" + this.prSupport + ", home="
            + this.home + "]";
    }

}
