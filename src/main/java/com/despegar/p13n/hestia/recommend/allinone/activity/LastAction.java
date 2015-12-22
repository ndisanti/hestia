package com.despegar.p13n.hestia.recommend.allinone.activity;

import com.google.common.base.Preconditions;

/**
 * Time since last action
 */
public enum LastAction {

    SAME_DAY("SameDay", 0, 1), //
    LESS_ONE_WEEK("<1 week", 0, 7),
    LESS_TWO_WEEK("<2 week", 0, 14),
    MORE_ONE_WEEK(">1 week", 7, Integer.MAX_VALUE),
    MORE_TWO_WEEK(">2 week", 14, Integer.MAX_VALUE),
    MORE_ONE_DAY("MoreOneDAy", 1, Integer.MAX_VALUE),
    ANY("Any", -1, Integer.MAX_VALUE);

    private String desc;
    private int fromDay;
    private int toDay;

    private LastAction(String desc, int fromDay, int toDay) {
        this.desc = desc;
        this.fromDay = fromDay;
        this.toDay = toDay;
    }

    public String getDesc() {
        return this.desc;
    }

    public boolean inRange(int days) {
        Preconditions.checkArgument(days >= any());
        return (this.fromDay <= days && days < this.toDay);
    }

    public static LastAction getRange(int days) {

        for (LastAction range : LastAction.values()) {
            if (range.inRange(days)) {
                return range;
            }
        }

        return ANY;
    }


    public static int any() {
        return LastAction.ANY.fromDay;
    }
}
