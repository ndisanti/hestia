package com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent;

import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.Anticipation;
import com.despegar.p13n.hestia.recommend.allinone.activity.LastAction;
import com.despegar.p13n.hestia.recommend.allinone.rules.BuyProductSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.CountrySupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeSupport;
import com.despegar.p13n.hestia.recommend.decisiontree.RuleDef;
import com.google.common.base.Preconditions;

public class RuleDefSection
    implements RuleDef {
    private ActivityType activityType;
    private LastAction lastAction;
    private Anticipation anticipation;
    private BuyProductSupport prBought;
    private HomeSupport home;
    private CountrySupport countrySupport;

    public RuleDefSection(ActivityType activityType, LastAction lastAction, Anticipation anticipation,
        BuyProductSupport prBought, HomeSupport home, CountrySupport prSupport) {

        Preconditions.checkNotNull(activityType);
        Preconditions.checkNotNull(lastAction);
        Preconditions.checkNotNull(anticipation);
        Preconditions.checkNotNull(prBought);
        Preconditions.checkNotNull(home);
        Preconditions.checkNotNull(prSupport);

        this.activityType = activityType;
        this.lastAction = lastAction;
        this.anticipation = anticipation;
        this.prBought = prBought;
        this.home = home;
        this.countrySupport = prSupport;
    }

    public static RuleDefSection copy(RuleDefSection rta) {
        return RuleBuilder.copy(rta) //
            .build();
    }

    public static RuleDefSection forAllCountries(RuleDefSection rta) {
        return RuleBuilder.copy(rta) //
            .support(CountrySupport.ALL)//
            .build();
    }

    public static RuleDefSection forInternational(RuleDefSection rta) {
        return RuleBuilder.copy(rta) //
            .support(CountrySupport.INT)//
            .build();
    }

    public static RuleDefSection forCountrySupport(RuleDefSection rta, CountrySupport cs) {
        return RuleBuilder.copy(rta) //
            .support(cs)//
            .build();
    }

    public static RuleDefSection forOnlyHotels(RuleDefSection rta) {
        return RuleBuilder.copy(rta) //
            .support(CountrySupport.ONLY_HTLS)//
            .build();
    }


    public static RuleDefSection forLastResortSome(RuleDefSection rta) {
        return RuleBuilder.copy(rta) //
            .home(HomeSupport.ANY)//
            .support(CountrySupport.SOME)//
            .activity(ActivityType.LAST_RSRT)//
            .bought(BuyProductSupport.NONE)//
            .anticipation(Anticipation.ANY)//
            .last(LastAction.ANY)//
            .build();
    }

    public static RuleDefSection forLastResortAll(RuleDefSection rta) {
        return RuleBuilder.copy(rta) //
            .home(HomeSupport.ANY)//
            .support(CountrySupport.ALL)//
            .activity(ActivityType.LAST_RSRT)//
            .bought(BuyProductSupport.NONE)//
            .anticipation(Anticipation.ANY)//
            .last(LastAction.ANY)//
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

    public LastAction getLastAction() {
        return this.lastAction;
    }

    public void setLastAction(LastAction lastAction) {
        this.lastAction = lastAction;
    }

    public Anticipation getAnticipation() {
        return this.anticipation;
    }

    public void setAnticipation(Anticipation anticipation) {
        this.anticipation = anticipation;
    }


    public BuyProductSupport getPrBought() {
        return this.prBought;
    }

    public void setPrBought(BuyProductSupport prBought) {
        this.prBought = prBought;
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
        return "Rule [activityType=" + this.activityType + ", lastAction=" + this.lastAction + ", anticipation="
            + this.anticipation + ", prBought=" + this.prBought + ", home=" + this.home + ", prSupport="
            + this.countrySupport + "]";
    }


    public static class RuleBuilder {
        private ActivityType activityType;
        private LastAction lastAction;
        private Anticipation anticipation;
        private BuyProductSupport prBought;
        private HomeSupport home;
        private CountrySupport prSupport;

        private RuleBuilder() {
        }

        private RuleBuilder(RuleDefSection rule) {
            this.activityType = rule.getActivityType();
            this.lastAction = rule.getLastAction();
            this.anticipation = rule.getAnticipation();
            this.prBought = rule.getPrBought();
            this.home = rule.getHome();
            this.prSupport = rule.getCountrySupport();
        }

        public static RuleBuilder create() {
            return new RuleBuilder();
        }

        public static RuleBuilder create(RuleDefSection rule) {
            return new RuleBuilder(rule);
        }

        public static RuleBuilder copy(RuleDefSection rule) {
            RuleBuilder builder = new RuleBuilder();
            builder.activity(rule.activityType);
            builder.last(rule.getLastAction());
            builder.anticipation(rule.anticipation);
            builder.bought(rule.getPrBought());
            builder.home(rule.getHome());
            builder.support(rule.getCountrySupport());
            return builder;
        }

        public RuleBuilder activity(ActivityType activity) {
            this.activityType = activity;
            return this;
        }

        public RuleBuilder last(LastAction lastAction) {
            this.lastAction = lastAction;
            return this;
        }

        public RuleBuilder anticipation(Anticipation anticipation) {
            this.anticipation = anticipation;
            return this;
        }

        public RuleBuilder bought(BuyProductSupport prBought) {
            this.prBought = prBought;
            return this;
        }

        public RuleBuilder home(HomeSupport home) {
            this.home = home;
            return this;
        }

        public RuleBuilder support(CountrySupport prSupport) {
            this.prSupport = prSupport;
            return this;
        }

        public RuleDefSection build() {
            return new RuleDefSection(this.activityType, this.lastAction, this.anticipation, this.prBought, this.home,
                this.prSupport);
        }

    }


}
