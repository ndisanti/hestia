package com.despegar.p13n.hestia.recommend.allinone;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Usage counter
 */
public class Usage {
    /**
     * Number of times that was called
     */
    private AtomicInteger full = new AtomicInteger();

    /**
     * Number of times that its returns no data
     */
    private AtomicInteger empty = new AtomicInteger();

    public AtomicInteger getFull() {
        return this.full;
    }

    public AtomicInteger getEmpty() {
        return this.empty;
    }

    public void incEmpty() {
        this.empty.incrementAndGet();
    }

    public void incFull() {
        this.full.incrementAndGet();
    }

    @Override
    public String toString() {
        return "Usage [full=" + this.full + ", empty=" + this.empty + "]";
    }

}
