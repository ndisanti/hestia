package com.despegar.p13n.hestia.data.hbase.hot.types.grouping;

import com.despegar.p13n.hestia.model.RankingPosition;
import com.google.common.base.Function;

public abstract class RankingGroupFunction implements Function<RankingPosition, String> {

    public String apply(RankingPosition rankingPosition) {
        return this.groupOf(rankingPosition);
    }

    public abstract String groupOf(RankingPosition rankingPosition);
}
