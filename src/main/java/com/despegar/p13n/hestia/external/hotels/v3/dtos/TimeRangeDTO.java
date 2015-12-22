package com.despegar.p13n.hestia.external.hotels.v3.dtos;

import java.io.Serializable;

public class TimeRangeDTO
    implements Serializable {



    private static final long serialVersionUID = 1L;
    private String from;
    private String to;

    public TimeRangeDTO() {
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "TimeRangeDTO [from=" + this.from + ", to=" + this.to + "]";
    }
}
