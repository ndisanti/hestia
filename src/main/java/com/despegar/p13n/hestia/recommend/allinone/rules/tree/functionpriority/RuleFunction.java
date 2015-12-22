package com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.recommend.allinone.VisitCity;
import com.despegar.p13n.hestia.recommend.allinone.VisitFlow;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.BuyProductSupport;
import com.despegar.p13n.hestia.recommend.allinone.title.SectionType;
import com.despegar.p13n.hestia.recommend.decisiontree.RuleDef;
import com.google.common.base.Preconditions;

public class RuleFunction
    implements RuleDef {

    private Product offer;
    private ActivityType activityType;
    private SectionType section;
    private BuyProductSupport prBought;
    private VisitFlow visitFlow;
    private VisitCity visitCity;
    private FunctionPriority priority;

    public RuleFunction(Product offer, ActivityType activityType, SectionType section, BuyProductSupport prBought,
        VisitFlow visitFlow, VisitCity visitCity, FunctionPriority priority) {
        Preconditions.checkNotNull(offer);
        Preconditions.checkNotNull(activityType);
        Preconditions.checkNotNull(section);
        Preconditions.checkNotNull(prBought);
        Preconditions.checkNotNull(visitFlow);
        Preconditions.checkNotNull(visitCity);
        Preconditions.checkNotNull(priority);

        // if not buy -> BuyProductSupport == NONE
        // if buy -> BuyProductSupport != NONE
        boolean checkActivityAndPrBuy = ((activityType == ActivityType.SEARCH || activityType == ActivityType.NO_HISTORY)//
            && prBought == BuyProductSupport.NONE)//
            || //
            (activityType == ActivityType.BUY && prBought != BuyProductSupport.NONE);

        Preconditions.checkArgument(checkActivityAndPrBuy);

        this.offer = offer;
        this.activityType = activityType;
        this.section = section;
        this.prBought = prBought;
        this.visitFlow = visitFlow;
        this.visitCity = visitCity;
        this.priority = priority;
    }

    public static RuleFunction copy(RuleFunction rta) {
        return RuleFunctionBuilder.copy(rta) //
            .build();
    }

    public static RuleFunction forVersion(RuleFunction rta, RulesVersion version) {
        return RuleFunctionBuilder.copy(rta) //
            .build();
    }

    public static RuleFunctionBuilder builder() {
        return RuleFunctionBuilder.create();
    }

    public Product getOffer() {
        return this.offer;
    }

    public void setOffer(Product offer) {
        this.offer = offer;
    }

    public ActivityType getActivityType() {
        return this.activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public SectionType getSection() {
        return this.section;
    }

    public void setSection(SectionType section) {
        this.section = section;
    }

    public BuyProductSupport getPrBought() {
        return this.prBought;
    }

    public void setPrBought(BuyProductSupport prBought) {
        this.prBought = prBought;
    }

    public VisitFlow getVisitFlow() {
        return this.visitFlow;
    }

    public void setVisitFlow(VisitFlow visitFlow) {
        this.visitFlow = visitFlow;
    }

    public VisitCity getVisitCity() {
        return this.visitCity;
    }

    public void setVisitCity(VisitCity visitCity) {
        this.visitCity = visitCity;
    }

    public FunctionPriority getPriority() {
        return this.priority;
    }

    public void setPriority(FunctionPriority priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "RuleFunction [offer=" + this.offer + ", activityType=" + this.activityType + ", section=" + this.section
            + ", prBought=" + this.prBought + ", visitFlow=" + this.visitFlow + ", visitCity=" + this.visitCity
            + ", priority=" + this.priority + "]";
    }



    public static class RuleFunctionBuilder {
        private Product offer;
        private ActivityType activityType;
        private SectionType section;
        private BuyProductSupport prBought;
        private VisitFlow visitFlow;
        private VisitCity visitCity;
        private FunctionPriority priority;

        private RuleFunctionBuilder() {
        }

        private RuleFunctionBuilder(RuleFunction rule) {
            this.offer = rule.getOffer();
            this.activityType = rule.getActivityType();
            this.section = rule.getSection();
            this.prBought = rule.getPrBought();
            this.visitFlow = rule.getVisitFlow();
            this.visitCity = rule.getVisitCity();
            this.priority = rule.getPriority();
        }

        public static RuleFunctionBuilder create() {
            return new RuleFunctionBuilder();
        }

        public static RuleFunctionBuilder create(RuleFunction rule) {
            return new RuleFunctionBuilder(rule);
        }

        public static RuleFunctionBuilder copy(RuleFunction rule) {
            RuleFunctionBuilder builder = new RuleFunctionBuilder();
            builder.offer(rule.offer);
            builder.activity(rule.activityType);
            builder.section(rule.section);
            builder.bought(rule.prBought);
            builder.visitFlow(rule.visitFlow);
            builder.visitCity(rule.visitCity);
            builder.priority(rule.priority);
            return builder;
        }

        public RuleFunctionBuilder offer(Product offer) {
            this.offer = offer;
            return this;
        }

        public RuleFunctionBuilder activity(ActivityType activity) {
            this.activityType = activity;
            return this;
        }

        public RuleFunctionBuilder section(SectionType section) {
            this.section = section;
            return this;
        }

        public RuleFunctionBuilder bought(BuyProductSupport prBought) {
            this.prBought = prBought;
            return this;
        }

        public RuleFunctionBuilder visitFlow(VisitFlow visitFlow) {
            this.visitFlow = visitFlow;
            return this;
        }

        public RuleFunctionBuilder visitCity(VisitCity visitCity) {
            this.visitCity = visitCity;
            return this;
        }

        public RuleFunctionBuilder priority(FunctionPriority priority) {
            this.priority = priority;
            return this;
        }

        public RuleFunction build() {
            return new RuleFunction(this.offer, this.activityType, this.section, this.prBought, this.visitFlow,
                this.visitCity, this.priority);
        }

    }


}
