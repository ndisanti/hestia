package com.despegar.p13n.hestia.data.hbase.hot.filter;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.util.Pair;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.despegar.p13n.euler.commons.client.model.data.CruiseData;
import com.despegar.p13n.hestia.model.Ranking;
import com.despegar.p13n.hestia.model.RankingPosition;
import com.despegar.p13n.hestia.utils.DateUtils;

public class DidCheckInFilter   implements RankingFilter, RecommendFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DidCheckInFilter.class);

    public Ranking apply(Ranking ranking) {
        LocalDate now = LocalDate.now();
        Iterator<RankingPosition> iterator = ranking.getValues().iterator();
        while (iterator.hasNext()) {
            RankingPosition position = iterator.next();
            String startDate = position.getMeta().get(CruiseData.CI);
            if (this.inThePast(startDate, now)) {
                iterator.remove();
            }
        }
        return ranking;
    }

    public boolean include(Pair<String, Map<String, String>> flow1, Pair<String, Map<String, String>> flow2) {
        Map<String, String> extraData = flow2.getSecond();
        if (extraData == null) {
            LOGGER.debug("missing extra data for item1 {} , item2 {} ", flow1.getFirst(), flow2.getFirst());
            return true;
        }
        String startDate = extraData.get(CruiseData.CI);
        if (startDate == null) {
            LOGGER.debug("missing CI data for item1 {} , item2 {} ", flow1.getFirst(), flow2.getFirst());
            return true;
        }
        if (this.inThePast(startDate, LocalDate.now())) {
            return false;
        }
        return true;
    }

    private boolean inThePast(String startDate, LocalDate now) {
        if (StringUtils.isNotBlank(startDate)) {
            try {
                LocalDate parseLocalDate = DateUtils.FORMAT_yyyy_MM_dd.parseLocalDate(startDate);
                if (parseLocalDate.compareTo(now) <= 0) {
                    return true;
                }
            } catch (IllegalArgumentException e) {
            }
        }
        return false;
    }
}
