package com.despegar.p13n.hestia.data.hbase.hot.types.grouping;

import com.despegar.p13n.hestia.model.RankingPosition;

public class RankingDefaultGroupFunction   extends RankingGroupFunction {

    @Override
    public String groupOf(RankingPosition rankingPosition) {
        return "";
    }
}
