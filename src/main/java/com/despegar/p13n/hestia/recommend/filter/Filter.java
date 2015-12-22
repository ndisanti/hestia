package com.despegar.p13n.hestia.recommend.filter;

public interface Filter<T> {


    /**
     * Decide if item must be filtered or not
     *
     * @param item the item
     * @return true, if must be filtered. False otherwise
     */
    public boolean filter(T item);

}
