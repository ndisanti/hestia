package com.despegar.p13n.hestia.recommend.allinone.matrix;


public enum SearchCount {
    ZERO("ZERO", 0, 0), //
    ONE_TO_THREE("1 TO 3", 1, 3), //
    FOUR_OR_GREATER(">=4", 4, Integer.MAX_VALUE), //
    NA("NA", Integer.MIN_VALUE, Integer.MAX_VALUE);

    private int from;
    private int to;
    private String desc;

    private SearchCount(String desc, int from, int to) {
        this.desc = desc;
        this.from = from;
        this.to = to;
    }

    public boolean inRange(int value) {
        return (this.from <= value && value <= this.to);
    }

    public static SearchCount getRange(int days) {

        for (SearchCount range : SearchCount.values()) {
            if (range.inRange(days)) {
                return range;
            }
        }

        return NA;
    }

    public String getDesc() {
        return this.desc;
    }

    public static int na() {
        return SearchCount.NA.from;
    }
}
