package com.despegar.p13n.hestia.recommend.allinone.matrix;

import java.util.Map;

import com.google.common.collect.Maps;

public class MatrixCounter {

    private Map<MatrixKey, ProductMetricCount> map = Maps.newLinkedHashMap();

    public MatrixCounter() {

    }

    public MatrixCounter(MatrixKey mKey, ProductMetricCount prCount) {
        this.map.put(mKey, prCount);
    }


    public void add(MatrixKey mKey, ProductMetricCount prCount) {
        if (!this.map.containsKey(mKey)) {
            this.map.put(mKey, prCount);
        } else {
            this.map.get(mKey).add(prCount);
        }
    }

    public Map<MatrixKey, ProductMetricCount> getMap() {
        return this.map;
    }

    public void setMap(Map<MatrixKey, ProductMetricCount> map) {
        this.map = map;
    }

    public int size() {
        return this.map.size();
    }


    // public List<String> dumpAsString(MatrixKey filter) {
    //
    // List<MatrixRow> list = this.asMatrixRowList();
    // List<String> dump = Lists.newArrayList();
    // final String format = "%-12s %-10s %-10s %-15s %-15s %-10s %-15s %-90s %-10s";
    // String header = String.format(format, "SearchCount", "Buy", "Product", "LastAction", "Anticipation", "Country",
    // "RouteType", "Metrics", "Call");
    // dump.add(header);
    //
    // for (MatrixRow matrixRow : list) {
    // MatrixKey key = matrixRow.getKey();
    //
    // if (filter.getSearchCount() != null && filter.getSearchCount() != key.getSearchCount()) {
    // continue;
    // }
    //
    // if (filter.isBuy() != null && filter.isBuy() != key.isBuy()) {
    // continue;
    // }
    //
    // if (filter.getProduct() != null && filter.getProduct() != key.getProduct()) {
    // continue;
    // }
    //
    // if (filter.getLastAction() != null && filter.getLastAction() != key.getLastAction()) {
    // continue;
    // }
    //
    // if (filter.getAnticipation() != null && filter.getAnticipation() != key.getAnticipation()) {
    // continue;
    // }
    //
    // if (filter.getCountry() != null && filter.getCountry() != key.getCountry()) {
    // continue;
    // }
    //
    // if (filter.getRouteType() != null && filter.getRouteType() != key.getRouteType()) {
    // continue;
    // }
    //
    // String searchcount = key.getSearchCount() == null ? null : key.getSearchCount().getDesc();
    // String product = key.getProduct() == null ? null : DumpUtils.dumpProduct(key.getProduct().toString());
    // String lastAction = key.getLastAction() == null ? null : key.getLastAction().getDesc();
    // String anticipation = key.getAnticipation() == null ? null : key.getAnticipation().getDesc();
    // int call = matrixRow.getMetrics().getCall().get();
    //
    // String row = String.format(format, searchcount, key.isBuy(), product, lastAction, anticipation,
    // key.getCountry(), key.getRouteType(), matrixRow.getMetrics().asString(), call);
    // dump.add(row);
    // }
    //
    // return dump;
    // }



    @Override
    public String toString() {
        return "MatrixCounter [map=" + this.map + "]";
    }


}
