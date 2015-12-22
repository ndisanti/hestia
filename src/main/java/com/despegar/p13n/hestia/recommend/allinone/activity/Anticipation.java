package com.despegar.p13n.hestia.recommend.allinone.activity;

import com.google.common.base.Preconditions;


/**
 * Trip time anticipation = (Checkin date - current date)  
 */
public enum Anticipation {
    // basic time ranges
    LESS_1_WEEK("< 1 week", 0, 7),
    ONE_TO_THREE_WEEKS("1/3 weeks", 7, 21),
    LESS_THREE_WEEKS("<3 weeks", 0, 21),
    THREE_WEEKS_TO_TWO_MONTHS("3 w/2 months", 21, 60),
    TWO_MONTHS_TO_CHECKOUT("2 m/checkout", 60, Integer.MAX_VALUE),
    THREE_WEEKS_TO_CHECKOUT("3 w/checkout", 21, Integer.MAX_VALUE),
    MORE_ONE_WEEK(">1 week", 7, Integer.MAX_VALUE),
    ANY("Any", -1, Integer.MAX_VALUE);

    private String desc;
    private int fromDay;
    private int toDay;

    private Anticipation(String desc, int fromDay, int toDay) {
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

    public static Anticipation getRange(int days) {

        for (Anticipation range : Anticipation.values()) {
            if (range.inRange(days)) {
                return range;
            }
        }

        return ANY;
    }

    public static int any() {
        return Anticipation.ANY.fromDay;
    }

}
