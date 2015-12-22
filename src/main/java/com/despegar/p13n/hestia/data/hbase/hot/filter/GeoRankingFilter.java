package com.despegar.p13n.hestia.data.hbase.hot.filter;

import static com.google.common.base.Joiner.on;
import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.collections.MapUtils.isEmpty;
import static com.despegar.p13n.hestia.data.hbase.hot.geo.GeoData.KEY;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.despegar.p13n.hestia.data.hbase.hot.geo.GeoData;
import com.despegar.p13n.hestia.model.Ranking;
import com.despegar.p13n.hestia.model.RankingPosition;
import com.google.common.base.Optional;

public class GeoRankingFilter
    implements RankingFilter {

    private static final String KEY_SEPARATOR = "-";

    private static final Logger LOGGER = LoggerFactory.getLogger(GeoRankingFilter.class);

    private static final int DEFAULT_SIZE = 40;

    private int maxSize;

    private ObjectMapper mapper;

    public GeoRankingFilter(int maxSize) {
        this.maxSize = maxSize;
        this.mapper = new ObjectMapper();

    }

    public GeoRankingFilter() {
        this(DEFAULT_SIZE);
    }

    public Ranking apply(Ranking ranking) {
        Set<RankingPosition> rankingValues = ranking.getValues();
        Map<String, Integer> counters = newHashMap();
        Ranking result = new Ranking();
        for (RankingPosition value : rankingValues) {
            if (value.getValue() > 0) {
                Optional<String> key = this.buildKey(value.getMeta());
                if (key.isPresent()) {
                    Integer count = this.getCount(counters, key.get());
                    if (count <= this.maxSize) {
                        result.addRankingPosition(value);
                        counters.put(key.get(), ++count);
                    }
                }
            }
        }
        return result;
    }

    private Integer getCount(Map<String, Integer> counters, String key) {
        Integer count = counters.get(key);
        if (count == null) {
            count = Integer.valueOf(0);
        }
        return count;
    }

    private Optional<String> buildKey(Map<String, String> meta) {
        if (!isEmpty(meta)) {
            String src = meta.get(KEY);
            try {
                GeoData data = this.mapper.readValue(src, GeoData.class);
                String generatedKey = on(KEY_SEPARATOR).join(data.getBrand(), data.getCountry(), data.getProduct());
                return of(generatedKey);
            } catch (IOException e) {
                LOGGER.error("Something went wrong serializing GeoData with values {}. Cause: {}", src, e);
            }
        }
        return absent();
    }
}
