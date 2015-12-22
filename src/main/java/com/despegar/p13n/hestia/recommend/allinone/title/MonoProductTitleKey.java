package com.despegar.p13n.hestia.recommend.allinone.title;

import com.despegar.p13n.hestia.api.data.model.ItemType;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;

public class MonoProductTitleKey {

    private ActivityType activityType;
    private SectionType sectionType;
    private ItemType itemType;
    private SectionFunctionCode functionCode;
    private Param param;

    public MonoProductTitleKey(ActivityType activityType, SectionType sectionType, SectionFunctionCode functionCode,
        Param param, ItemType itemType) {
        this.activityType = activityType;
        this.sectionType = sectionType;
        this.itemType = itemType;
        this.functionCode = functionCode;
        this.param = param;
    }

    public MonoProductTitleKey(ActivityType activityType, SectionType sectionType, SectionFunctionCode functionCode,
        ItemType itemType) {
        this(activityType, sectionType, functionCode, new Param(), itemType);
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

    public ItemType getItemType() {
        return this.itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public SectionFunctionCode getFunctionCode() {
        return this.functionCode;
    }

    public void setFunctionCode(SectionFunctionCode functionCode) {
        this.functionCode = functionCode;
    }

    public Param getParam() {
        return this.param;
    }

    public void setParam(Param param) {
        this.param = param;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.activityType == null) ? 0 : this.activityType.hashCode());
        result = prime * result + ((this.functionCode == null) ? 0 : this.functionCode.hashCode());
        result = prime * result + ((this.itemType == null) ? 0 : this.itemType.hashCode());
        result = prime * result + ((this.param == null) ? 0 : this.param.hashCode());
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
        MonoProductTitleKey other = (MonoProductTitleKey) obj;
        if (this.activityType != other.activityType) {
            return false;
        }
        if (this.functionCode != other.functionCode) {
            return false;
        }
        if (this.itemType != other.itemType) {
            return false;
        }
        if (this.param == null) {
            if (other.param != null) {
                return false;
            }
        } else if (!this.param.equals(other.param)) {
            return false;
        }
        if (this.sectionType != other.sectionType) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MonoProductTitleKey [activityType=" + this.activityType + ", sectionType=" + this.sectionType
            + ", itemType=" + this.itemType + ", functionCode=" + this.functionCode + ", param=" + this.param + "]";
    }

}
