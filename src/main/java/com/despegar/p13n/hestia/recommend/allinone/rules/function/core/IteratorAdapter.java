package com.despegar.p13n.hestia.recommend.allinone.rules.function.core;

import java.util.Iterator;

import com.google.common.base.Preconditions;

/**
 * Wrapper class to convert from  a iterator type to another iterator type
 * 
 * @param <F> from type 
 * @param <T> to type
 */
public abstract class IteratorAdapter<F, T> {

    private Iterator<F> it;

    public IteratorAdapter(Iterator<F> itToConvert) {
        Preconditions.checkNotNull(itToConvert);
        this.it = itToConvert;
    }


    public Iterator<T> newIterator() {
        return new Iterator<T>() {

            public boolean hasNext() {
                return IteratorAdapter.this.it.hasNext();
            }

            public T next() {
                F from = IteratorAdapter.this.it.next();
                return IteratorAdapter.this.convert(from);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public abstract T convert(F f);
}
