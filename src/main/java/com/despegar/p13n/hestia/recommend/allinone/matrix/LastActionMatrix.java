package com.despegar.p13n.hestia.recommend.allinone.matrix;

import com.google.common.base.Preconditions;

/**
 * Time since last action
 */
public enum LastActionMatrix {

    SAME_DAY("SameDay", 0, 1), //
    LESS_ONE_WEEK(" <1 week", 0, 7),
    MORE_ONE_WEEK(" >1 week", 7, Integer.MAX_VALUE), //
    NA("Not available", Integer.MIN_VALUE, Integer.MAX_VALUE);

    private String desc;
    private int fromDay;
    private int toDay;

    private LastActionMatrix(String desc, int fromDay, int toDay) {
        this.desc = desc;
        this.fromDay = fromDay;
        this.toDay = toDay;
    }

    public String getDesc() {
        return this.desc;
    }

    public boolean inRange(int days) {
        Preconditions.checkArgument(days >= na());
        return (this.fromDay <= days && days < this.toDay);
    }

    public static LastActionMatrix getRange(int days) {

        for (LastActionMatrix range : LastActionMatrix.values()) {
            if (range.inRange(days)) {
                return range;
            }
        }

        return NA;
    }


    public static int na() {
        return LastActionMatrix.NA.fromDay;
    }
}
