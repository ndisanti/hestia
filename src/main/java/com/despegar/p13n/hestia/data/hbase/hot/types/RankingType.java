package com.despegar.p13n.hestia.data.hbase.hot.types;

import com.despegar.p13n.hestia.data.hbase.hot.filter.RankingFilter;
import com.despegar.p13n.hestia.data.hbase.hot.render.RankingRenderFilterConfig.RankingRenderFilterId;
import com.despegar.p13n.hestia.data.hbase.hot.types.grouping.RankingGroupFunction;

public interface RankingType {

	   public String getTypeCode();

	    public RankingKey getRankingKey();

	    public boolean hasFilter();

	    public RankingFilter getFilter();

	    public boolean isFallback();

	    public RankingRenderFilterId getRankingRenderFilterId();

	    public RankingGroupFunction getRankingGroupFunction();
}
