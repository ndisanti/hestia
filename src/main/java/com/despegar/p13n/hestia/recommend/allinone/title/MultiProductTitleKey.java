package com.despegar.p13n.hestia.recommend.allinone.title;

import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeSupport;

public class MultiProductTitleKey {

    private ActivityType activityType;
    private SectionType sectionType;
    private boolean singleDestination;
    private HomeSupport support;

    public MultiProductTitleKey(ActivityType activityType, SectionType sectionType, boolean singleDestination,
        HomeSupport support) {
        this.activityType = activityType;
        this.sectionType = sectionType;
        this.singleDestination = singleDestination;
        this.support = support;
    }

    public ActivityType getActivityType() {
        return this.activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public SectionType getSectionType() {
        return this.sectionType;
    }

    public void setSectionType(SectionType sectionType) {
        this.sectionType = sectionType;
    }

    public boolean isSingleDestination() {
        return this.singleDestination;
    }

    public void setSingleDestination(boolean singleDestination) {
        this.singleDestination = singleDestination;
    }

    public HomeSupport getSupport() {
        return this.support;
    }

    public void setSupport(HomeSupport support) {
        this.support = support;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.activityType == null) ? 0 : this.activityType.hashCode());
        result = prime * result + ((this.sectionType == null) ? 0 : this.sectionType.hashCode());
        result = prime * result + (this.singleDestination ? 1231 : 1237);
        result = prime * result + ((this.support == null) ? 0 : this.support.hashCode());
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
        MultiProductTitleKey other = (MultiProductTitleKey) obj;
        if (this.activityType != other.activityType) {
            return false;
        }
        if (this.sectionType != other.sectionType) {
            return false;
        }
        if (this.singleDestination != other.singleDestination) {
            return false;
        }
        if (this.support != other.support) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MultiProductTitleKey [activityType=" + this.activityType + ", sectionType=" + this.sectionType
            + ", singleDestination=" + this.singleDestination + ", support=" + this.support + "]";
    }
}
