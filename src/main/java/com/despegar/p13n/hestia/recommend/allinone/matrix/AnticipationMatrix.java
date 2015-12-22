package com.despegar.p13n.hestia.recommend.allinone.matrix;



/**
 * Trip time anticipation = (Checkin date - current date)  
 */
public enum AnticipationMatrix {
    // basic time ranges
    LESS_ONE_WEEK("LastWeek", 0, 7),
    ONE_TO_THREE_WEEKS("1/3 weeks", 7, 21),
    THREE_WEEKS_TO_TWO_MONTHS("3 w/2 months", 21, 60),
    MORE_TWO_MONTHS("> 2 month", 60, Integer.MAX_VALUE),
    NA("Not available", Integer.MIN_VALUE, Integer.MAX_VALUE);

    private String desc;
    private int fromDay;
    private int toDay;

    private AnticipationMatrix(String desc, int fromDay, int toDay) {
        this.desc = desc;
        this.fromDay = fromDay;
        this.toDay = toDay;
    }


    public String getDesc() {
        return this.desc;
    }

    public boolean inRange(int days) {
        return (this.fromDay <= days && days < this.toDay);
    }

    public static AnticipationMatrix getRange(int days) {

        for (AnticipationMatrix range : AnticipationMatrix.values()) {
            if (range.inRange(days)) {
                return range;
            }
        }

        return NA;
    }

    public static int na() {
        return AnticipationMatrix.NA.fromDay;
    }

}
