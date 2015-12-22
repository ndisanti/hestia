package com.despegar.p13n.hestia.data.hbase.hot.filter;

import java.util.List;

import com.despegar.p13n.hestia.model.Ranking;
import com.google.common.collect.Lists;

public class CompositeRankingFilter  implements RankingFilter {

    private List<RankingFilter> filters;

    public CompositeRankingFilter(RankingFilter... filters) {
        this.filters = Lists.newArrayList(filters);
    }

    public Ranking apply(Ranking ranking) {
        Ranking result = ranking;
        for (RankingFilter filter : this.filters) {
            result = filter.apply(result);
        }
        return result;
    }}
