package com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.VisitCity;
import com.despegar.p13n.hestia.recommend.allinone.VisitFlow;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.title.SectionType;
import com.despegar.p13n.hestia.recommend.decisiontree.Query;
import com.google.common.base.Preconditions;

public class QueryFunction
    implements Query {

    private Product offer;
    private ActivityType activity;
    private SectionType section;
    private Product prBought;
    private VisitFlow visitFlow;
    private VisitCity visitCity;
    private FunctionPriority priority;

    public QueryFunction(Product offer, ActivityType activityType, SectionType section, Product prBought,
        VisitFlow visitFlow, VisitCity visitCity, FunctionPriority priority) {

        Preconditions.checkNotNull(offer);
        Preconditions.checkNotNull(activityType);
        Preconditions.checkNotNull(section);
        // Preconditions.checkNotNull(prBought); can be null if not buy
        Preconditions.checkNotNull(visitFlow);
        Preconditions.checkNotNull(priority);

        this.offer = offer;
        this.activity = activityType;
        this.section = section;
        this.prBought = prBought;
        this.visitFlow = visitFlow;
        this.visitCity = visitCity;
        this.priority = priority;
    }


    public Product getOffer() {
        return this.offer;
    }

    public void setOffer(Product offer) {
        this.offer = offer;
    }

    public ActivityType getActivity() {
        return this.activity;
    }

    public void setActivity(ActivityType activityType) {
        this.activity = activityType;
    }

    public SectionType getSection() {
        return this.section;
    }

    public void setSection(SectionType section) {
        this.section = section;
    }

    public Product getPrBought() {
        return this.prBought;
    }

    public void setPrBought(Product prBought) {
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
        return "QueryFunction [offer=" + this.offer + ", activity=" + this.activity + ", section=" + this.section + ", buy="
            + this.prBought + ", visitFlow=" + this.visitFlow + ", visitCity=" + this.visitCity + ", priority="
            + this.priority + "]";
    }

    public static class QueryFunctionBuilder {
        private Product offer;
        private ActivityType activityType;
        private SectionType section;
        private Product prBought;
        private VisitFlow visitFlow;
        private VisitCity visitCity;
        private FunctionPriority priority;

        private QueryFunctionBuilder() {
        }

        private QueryFunctionBuilder(QueryFunction query) {
            this.offer = query.getOffer();
            this.activityType = query.getActivity();
            this.section = query.getSection();
            this.prBought = query.getPrBought();
            this.visitFlow = query.getVisitFlow();
            this.visitCity = query.getVisitCity();
            this.priority = query.getPriority();
        }

        private QueryFunctionBuilder(QueryFunctionBuilder queryBuilder) {
            this.offer = queryBuilder.offer;
            this.activityType = queryBuilder.activityType;
            this.section = queryBuilder.section;
            this.prBought = queryBuilder.prBought;
            this.visitFlow = queryBuilder.visitFlow;
            this.visitCity = queryBuilder.visitCity;
            this.priority = queryBuilder.priority;
        }

        public static QueryFunctionBuilder create() {
            return new QueryFunctionBuilder();
        }

        public static QueryFunctionBuilder create(QueryFunction query) {
            return new QueryFunctionBuilder(query);
        }

        public static QueryFunctionBuilder create(QueryFunctionBuilder queryBuilder) {
            return new QueryFunctionBuilder(queryBuilder);
        }


        public QueryFunctionBuilder offer(Product offer) {
            this.offer = offer;
            return this;
        }

        public QueryFunctionBuilder activity(ActivityType activity) {
            this.activityType = activity;
            return this;
        }

        public QueryFunctionBuilder section(SectionType section) {
            this.section = section;
            return this;
        }

        public QueryFunctionBuilder bought(Product prBought) {
            this.prBought = prBought;
            return this;
        }

        public QueryFunctionBuilder visitFlow(VisitFlow visitFlow) {
            this.visitFlow = visitFlow;
            return this;
        }

        public QueryFunctionBuilder visitCity(VisitCity visitCity) {
            this.visitCity = visitCity;
            return this;
        }

        public QueryFunctionBuilder priority(FunctionPriority priority) {
            this.priority = priority;
            return this;
        }

        public QueryFunction build() {
            return new QueryFunction(this.offer, this.activityType, this.section, this.prBought, this.visitFlow,
                this.visitCity, this.priority);
        }

        @Override
        public String toString() {
            return "QueryFunctionBuilder [offer=" + this.offer + ", activity=" + this.activityType + ", sec=" + this.section
                + ", buy=" + this.prBought + ", visitFlow=" + this.visitFlow + ", visitCity=" + this.visitCity
                + ", priority=" + this.priority + "]";
        }


    }

}
