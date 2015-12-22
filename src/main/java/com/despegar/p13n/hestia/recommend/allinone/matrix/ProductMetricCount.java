package com.despegar.p13n.hestia.recommend.allinone.matrix;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import com.despegar.framework.lang.Pair;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ProductMetricCount {

    public static final String TOTAL = "TOTAL";
    public static final String PROB_METRIC = "prob";
    public static final String COUNT_METRIC = "count";

    private Map<String, Map<String, Long>> counters = Maps.newLinkedHashMap();
    private List<Pair<Product, Long>> productListByHighProb = Lists.newArrayList();

    private AtomicInteger call = new AtomicInteger();

    public ProductMetricCount() {

    }

    public ProductMetricCount(ProductMetricCount pmc) {
        this.counters = Maps.newLinkedHashMap();

        for (Entry<String, Map<String, Long>> e : pmc.getCounters().entrySet()) {
            this.counters.put(e.getKey(), Maps.newLinkedHashMap(e.getValue()));
        }
    }


    public ProductMetricCount add(Product pr, String metric, long value) {
        String prStr = pr.toString();
        return this.add(prStr, metric, value);
    }

    private ProductMetricCount add(String pr, String metric, long value) {

        if (this.counters.containsKey(pr)) {
            Map<String, Long> m = this.counters.get(pr);

            if (m.containsKey(metric)) {
                long prev = m.get(metric);
                m.put(metric, prev + value);
            } else {
                this.counters.get(pr).put(metric, value);
            }
        } else {
            Map<String, Long> m = Maps.newLinkedHashMap();
            m.put(metric, value);
            this.counters.put(pr, m);
        }
        return this;
    }

    public ProductMetricCount add(ProductMetricCount pmc) {

        for (Entry<String, Map<String, Long>> entry : pmc.getCounters().entrySet()) {
            for (Entry<String, Long> entryMetric : entry.getValue().entrySet()) {
                this.add(entry.getKey(), entryMetric.getKey(), entryMetric.getValue());
            }
        }
        return this;
    }

    public ProductMetricCount addTotalCount() {

        long totalCount = 0;

        for (Entry<String, Map<String, Long>> entry : this.getCounters().entrySet()) {
            for (Entry<String, Long> entryMetric : entry.getValue().entrySet()) {

                if (entryMetric.getKey().equals(COUNT_METRIC)) {
                    totalCount += entryMetric.getValue();
                }
            }
        }

        this.add(TOTAL, COUNT_METRIC, totalCount);

        return this;
    }


    public ProductMetricCount addProbability() {

        Map<String, Long> probs = Maps.newHashMap();

        for (Entry<String, Map<String, Long>> entry : this.getCounters().entrySet()) {
            for (Entry<String, Long> entryMetric : entry.getValue().entrySet()) {

                if (entryMetric.getKey().equals(COUNT_METRIC)) {
                    probs.put(entry.getKey(), entryMetric.getValue() * 100 / this.getTotalCount());
                }
            }
        }

        for (Entry<String, Long> entry : probs.entrySet()) {
            this.add(entry.getKey(), PROB_METRIC, entry.getValue());
        }

        return this;
    }


    public ProductMetricCount calculateProductPriority() {
        List<Pair<Product, Long>> list = Lists.newArrayList();

        for (Entry<String, Map<String, Long>> entry : this.counters.entrySet()) {
            long prob = entry.getValue().get(PROB_METRIC);
            String prod = entry.getKey();
            Product product = Product.fromString(prod);

            if (product != null) {
                list.add(Pair.of(product, prob));
            }

        }

        Collections.sort(list, new Comparator<Pair<Product, Long>>() {

            public int compare(Pair<Product, Long> o1, Pair<Product, Long> o2) {
                return -o1.getRight().compareTo(o2.getRight());
            }
        });

        this.productListByHighProb = list;
        return this;
    }

    private long getTotalCount() {
        return this.getCounters().get(TOTAL).get(COUNT_METRIC);
    }

    public Long getMetric(Product pr, String metric) {
        return this.counters.get(pr.toString()).get(metric);
    }

    public Map<String, Map<String, Long>> getCounters() {
        return this.counters;
    }


    public void setCounters(Map<String, Map<String, Long>> count) {
        this.counters = count;
    }

    /**
     * Returns list of products sorted in descending order by its buy probability
     */
    public List<Pair<Product, Long>> getProductSortByHighProb() {
        return this.productListByHighProb;
    }

    public void incCall() {
        this.call.incrementAndGet();
    }

    public AtomicInteger getCall() {
        return this.call;
    }

    public List<String> asString() {
        List<String> asString = Lists.newArrayList();

        List<Pair<Product, Long>> list = this.getProductSortByHighProb();

        for (Pair<Product, Long> pair : list) {

            long count = this.getMetric(pair.getLeft(), COUNT_METRIC);
            String product = pair.getLeft().toString().substring(0, 2);
            asString.add("(" + product + "," + pair.getRight() + "%" + "," + count + ")");
        }

        return asString;
    }

    public boolean isEmpty() {
        return this.counters.isEmpty();
    }


}
