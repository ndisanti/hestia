package com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.CountrySupport;
import com.despegar.p13n.hestia.recommend.decisiontree.Query;
import com.google.common.base.Preconditions;

public class QueryRuleSection
    implements Query {

    private ActivityType activityType;
    private int lastActionDays;
    private int anticipationDays;
    private Product prBought;
    private Product home;
    private CountrySupport prSupport;

    public QueryRuleSection(ActivityType activityType, int lastActionDays, int anticipationDays, Product prBought,
        Product home, CountrySupport prSupport) {

        Preconditions.checkNotNull(activityType);
        // Preconditions.checkNotNull(prBought); can be null if not buy
        Preconditions.checkNotNull(home);
        // Preconditions.checkNotNull(prSupport); going to be set later

        this.activityType = activityType;
        this.lastActionDays = lastActionDays;
        this.anticipationDays = anticipationDays;
        this.prBought = prBought;
        this.home = home;
        this.prSupport = prSupport;
    }

    public ActivityType getActivityType() {
        return this.activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public int getLastActionDays() {
        return this.lastActionDays;
    }

    public void setLastActionDays(int lastActionDays) {
        this.lastActionDays = lastActionDays;
    }

    public int getAnticipationDays() {
        return this.anticipationDays;
    }

    public void setAnticipationDays(int anticipationDays) {
        this.anticipationDays = anticipationDays;
    }

    public Product getPrBought() {
        return this.prBought;
    }

    public void setPrBought(Product prBought) {
        this.prBought = prBought;
    }

    public Product getHome() {
        return this.home;
    }

    public void setHome(Product home) {
        this.home = home;
    }

    public CountrySupport getPrSupport() {
        return this.prSupport;
    }

    public void setPrSupport(CountrySupport prSupport) {
        this.prSupport = prSupport;
    }


    public static class QueryRuleSectionBuilder {
        private ActivityType activityType;
        private int lastActionDays;
        private int anticipationDays;
        private Product prBought;
        private Product home;
        private CountrySupport prSupport;

        private QueryRuleSectionBuilder() {
        }

        private QueryRuleSectionBuilder(QueryRuleSection query) {
            this.activityType = query.getActivityType();
            this.lastActionDays = query.getLastActionDays();
            this.anticipationDays = query.getAnticipationDays();
            this.prBought = query.getPrBought();
            this.home = query.getHome();
            this.prSupport = query.getPrSupport();
        }

        public static QueryRuleSectionBuilder create() {
            return new QueryRuleSectionBuilder();
        }

        public static QueryRuleSectionBuilder create(QueryRuleSection query) {
            return new QueryRuleSectionBuilder(query);
        }

        public QueryRuleSectionBuilder activity(ActivityType activity) {
            this.activityType = activity;
            return this;
        }

        public QueryRuleSectionBuilder lastAction(int lastActionDays) {
            this.lastActionDays = lastActionDays;
            return this;
        }

        public QueryRuleSectionBuilder anticipation(int anticipationDays) {
            this.anticipationDays = anticipationDays;
            return this;
        }

        public QueryRuleSectionBuilder bought(Product prBought) {
            this.prBought = prBought;
            return this;
        }

        public QueryRuleSectionBuilder home(Product home) {
            this.home = home;
            return this;
        }

        public QueryRuleSectionBuilder support(CountrySupport prSupport) {
            this.prSupport = prSupport;
            return this;
        }

        public QueryRuleSection build() {
            return new QueryRuleSection(this.activityType, this.lastActionDays, this.anticipationDays, this.prBought,
                this.home, this.prSupport);
        }
    }


    @Override
    public String toString() {
        return "QueryRuleContent [ activity=" + this.activityType + ", lastActionDays=" + this.lastActionDays
            + ", anticipationDays=" + this.anticipationDays + ", buy=" + this.prBought + ", home=" + this.home
            + ", prSupport=" + this.prSupport + "]";
    }


}
