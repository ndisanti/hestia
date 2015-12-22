package com.despegar.p13n.hestia.data.hbase.hot.filter;

import java.util.Iterator;

import com.despegar.p13n.hestia.model.Ranking;
import com.despegar.p13n.hestia.model.RankingPosition;

public class LimitRankingFilter  implements RankingFilter {

    private static final int DEFAULT_MAP_POSITIONS = 80;
    private int maxPositions;

    public LimitRankingFilter(int maxPositions) {
        this.maxPositions = maxPositions;
    }

    public LimitRankingFilter() {
        this.maxPositions = DEFAULT_MAP_POSITIONS;
    }

    public Ranking apply(Ranking ranking) {
        Ranking result = new Ranking();
        int pos = 0;
        Iterator<RankingPosition> it = ranking.getValues().iterator();
        while (it.hasNext() && pos < this.maxPositions) {
            result.addRankingPosition(it.next());
            pos++;
        }
        return result;
    }
}
