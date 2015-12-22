package com.despegar.p13n.hestia.api.data.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.common.base.Preconditions;

public class ActivityItem     extends ItemHome {

    private String actid;

    @Deprecated
    public ActivityItem() {
        super(ItemType.ACTIVITY);
        // constructor for serializacion
    }

    /**
     * @param actid
     */
    public ActivityItem(String actid) {
        super(ItemType.ACTIVITY);
        Preconditions.checkNotNull(actid);
        Preconditions.checkArgument(!actid.equalsIgnoreCase("null"));
        Preconditions.checkArgument(actid.length() > 5);
        this.actid = actid;
    }


    public String getActid() {
        return this.actid;
    }

    public void setActid(String actid) {
        this.actid = actid;
    }

    @Override
    @JsonIgnore
    public String getId() {
        return this.actid;
    }

    @Override
    public String toString() {
        return "ActivityItem [actid=" + this.actid + "]";
    }
}
