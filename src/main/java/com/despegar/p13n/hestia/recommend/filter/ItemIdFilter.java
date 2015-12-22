package com.despegar.p13n.hestia.recommend.filter;

import java.util.Set;

public class ItemIdFilter
    implements Filter<String> {

    private Set<String> itemIds;
    private FilterType type;

    enum FilterType {
        EXCLUDE, INCLUDE
    }

    public ItemIdFilter(FilterType type, Set<String> ids) {
        this.type = type;
        this.itemIds = ids;
    }

    @Override
    public boolean filter(String item) {
        boolean isOnList = this.itemIds.contains(item);
        if (this.type == FilterType.EXCLUDE) {
            return isOnList;
        } else {
            return !isOnList;
        }
    }
}
