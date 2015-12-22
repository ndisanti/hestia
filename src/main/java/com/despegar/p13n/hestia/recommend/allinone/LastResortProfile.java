package com.despegar.p13n.hestia.recommend.allinone;

import java.util.Comparator;

import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;

public class LastResortProfile
    implements Comparator<LastResortProfile> {

    private UserProfile userProfile;
    private int fullContent;
    private int emptyContent;
    private float lastResortRate;
    private ActivityType activity;

    public LastResortProfile(UserProfile userProfile, int fullContent, int emptyContent, float lastResortRate,
        ActivityType activity) {
        super();
        this.userProfile = userProfile;
        this.fullContent = fullContent;
        this.emptyContent = emptyContent;
        this.lastResortRate = lastResortRate;
        this.setActivity(activity);
    }

    public LastResortProfile() {

    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public int getFullContent() {
        return this.fullContent;
    }

    public void setFullContent(int fullContent) {
        this.fullContent = fullContent;
    }

    public int getEmptyContent() {
        return this.emptyContent;
    }

    public void setEmptyContent(int emptyContent) {
        this.emptyContent = emptyContent;
    }

    public float getLastResortRate() {
        return this.lastResortRate;
    }

    public void setLastResortRate(float lastResortRate) {
        this.lastResortRate = lastResortRate;
    }

    @Override
    public int compare(LastResortProfile o1, LastResortProfile o2) {
        if (o1.lastResortRate == o2.lastResortRate) {
            return 0;
        } else if (o1.lastResortRate > o2.lastResortRate) {
            return -1;
        } else {
            return 1;
        }
    }

    public ActivityType getActivity() {
        return this.activity;
    }

    public void setActivity(ActivityType activity) {
        this.activity = activity;
    }

}
