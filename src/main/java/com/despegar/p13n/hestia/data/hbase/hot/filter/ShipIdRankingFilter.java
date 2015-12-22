package com.despegar.p13n.hestia.data.hbase.hot.filter;

import java.util.ArrayList;
import java.util.List;

import com.despegar.p13n.euler.commons.client.model.data.CruiseData;
import com.despegar.p13n.hestia.model.Ranking;
import com.despegar.p13n.hestia.model.RankingPosition;

public class ShipIdRankingFilter   implements RankingFilter {

    public Ranking apply(Ranking ranking) {
        Ranking result = new Ranking();
        List<String> shipsIds = new ArrayList<String>();
        for (RankingPosition position : ranking.getValues()) {
            String shipId = position.getMeta().get(CruiseData.SHIP_ID);
            if (shipId == null) {
                result.addRankingPosition(position);
            } else if (!shipsIds.contains(shipId)) { // avoid positions with same ship
                shipsIds.add(shipId);
                result.addRankingPosition(position);
            }
        }
        return result;
    }

}
