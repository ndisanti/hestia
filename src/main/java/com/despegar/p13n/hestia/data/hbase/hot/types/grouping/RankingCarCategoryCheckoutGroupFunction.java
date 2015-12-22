package com.despegar.p13n.hestia.data.hbase.hot.types.grouping;

import com.despegar.p13n.hestia.data.hbase.cars.CarsRankingWrapper;
import com.despegar.p13n.hestia.model.RankingPosition;

public class RankingCarCategoryCheckoutGroupFunction  extends RankingGroupFunction {

    @Override
    public String groupOf(RankingPosition rankingPosition) {
        // Get category from label "destination" of podium
        return CarsRankingWrapper.fromCheckoutLabel(rankingPosition.getName()).getLeft();
    }


}
