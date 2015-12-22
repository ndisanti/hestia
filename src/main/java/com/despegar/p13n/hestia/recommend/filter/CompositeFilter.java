package com.despegar.p13n.hestia.recommend.filter;

import java.util.List;

public class CompositeFilter
    implements Filter<String> {

    private List<Filter<String>> filters;

    public CompositeFilter(List<Filter<String>> filters) {
        this.filters = filters;
    }

    @Override
    public boolean filter(String item) {
        for (Filter<String> filter : this.filters) {
            if (filter.filter(item)) {
                return true;
            }
        }
        return false;
    }
}