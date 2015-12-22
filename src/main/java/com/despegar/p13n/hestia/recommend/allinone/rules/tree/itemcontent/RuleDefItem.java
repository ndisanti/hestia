package com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent;

import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.CountrySupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeSupport;
import com.despegar.p13n.hestia.recommend.decisiontree.RuleDef;
import com.google.common.base.Preconditions;

public class RuleDefItem
    implements RuleDef {
    private ActivityType activityType;
    private CountrySupport countrySupport;
    private HomeSupport home;

    public RuleDefItem(ActivityType activityType, CountrySupport countrySupport, HomeSupport home) {

        Preconditions.checkNotNull(activityType);
        Preconditions.checkNotNull(countrySupport);
        Preconditions.checkNotNull(home);

        this.activityType = activityType;
        this.countrySupport = countrySupport;
        this.home = home;
    }

    public static RuleDefItem copy(RuleDefItem rta) {
        return RuleBuilder.copy(rta) //
            .build();
    }

    public static RuleDefItem forAllCountries(RuleDefItem rta) {
        return RuleBuilder.copy(rta) //
            .support(CountrySupport.ALL)//
            .build();
    }

    public static RuleDefItem forOnlyHotels(RuleDefItem rta) {
        return RuleBuilder.copy(rta) //
            .support(CountrySupport.ONLY_HTLS)//
            .build();
    }


    public static RuleDefItem forLastResortSome(RuleDefItem rta) {
        return RuleBuilder.copy(rta) //
            .activity(ActivityType.LAST_RSRT)//
            .support(CountrySupport.SOME)//
            .home(HomeSupport.ANY)//
            .build();
    }

    public static RuleDefItem forLastResortAll(RuleDefItem rta) {
        return RuleBuilder.copy(rta) //
            .activity(ActivityType.LAST_RSRT)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.ANY)//
            .build();
    }

    public static RuleBuilder builder() {
        return RuleBuilder.create();
    }

    public ActivityType getActivityType() {
        return this.activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public HomeSupport getHome() {
        return this.home;
    }

    public void setHome(HomeSupport home) {
        this.home = home;
    }

    public CountrySupport getCountrySupport() {
        return this.countrySupport;
    }

    public void setCountrySupport(CountrySupport prSupport) {
        this.countrySupport = prSupport;
    }

    @Override
    public String toString() {
        return "Rule [activityType=" + this.activityType + ", countrySupport=" + this.countrySupport + ", home=" + this.home
            + "]";
    }


    public static class RuleBuilder {
        private ActivityType activityType;
        private CountrySupport prSupport;
        private HomeSupport home;

        private RuleBuilder() {
        }

        private RuleBuilder(RuleDefItem rule) {
            this.activityType = rule.getActivityType();
            this.prSupport = rule.getCountrySupport();
            this.home = rule.getHome();
        }

        public static RuleBuilder create() {
            return new RuleBuilder();
        }

        public static RuleBuilder create(RuleDefItem rule) {
            return new RuleBuilder(rule);
        }

        public static RuleBuilder copy(RuleDefItem rule) {
            RuleBuilder builder = new RuleBuilder();
            builder.activity(rule.activityType);
            builder.support(rule.getCountrySupport());
            builder.home(rule.getHome());
            return builder;
        }

        public RuleBuilder activity(ActivityType activity) {
            this.activityType = activity;
            return this;
        }

        public RuleBuilder support(CountrySupport prSupport) {
            this.prSupport = prSupport;
            return this;
        }


        public RuleBuilder home(HomeSupport home) {
            this.home = home;
            return this;
        }

        public RuleDefItem build() {
            return new RuleDefItem(this.activityType, this.prSupport, this.home);
        }

    }


}
