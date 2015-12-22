package com.despegar.p13n.hestia.recommend.allinone.matrix;

public class MatrixRow {

    private MatrixKey key;
    private ProductMetricCount metrics;


    public MatrixRow(MatrixKey key, ProductMetricCount count) {
        this.key = key;
        this.metrics = count;
    }

    public MatrixKey getKey() {
        return this.key;
    }

    public void setKey(MatrixKey key) {
        this.key = key;
    }

    public ProductMetricCount getMetrics() {
        return this.metrics;
    }

    public void setMetrics(ProductMetricCount count) {
        this.metrics = count;
    }
}