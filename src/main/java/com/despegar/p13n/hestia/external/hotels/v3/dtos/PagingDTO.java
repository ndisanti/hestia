package com.despegar.p13n.hestia.external.hotels.v3.dtos;

import java.io.Serializable;

public class PagingDTO
    implements Serializable {


    private static final long serialVersionUID = 1L;
    private long offset;
    private long limit;
    private long total;

    public PagingDTO() {
    }

    public long getOffset() {
        return this.offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getLimit() {
        return this.limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "PagingDTO [offset=" + this.offset + ", limit=" + this.limit + ", total=" + this.total + "]";
    }
}
