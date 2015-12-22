package com.despegar.p13n.hestia.data.hbase.hot.types;

import com.despegar.p13n.hestia.data.hbase.hot.filter.RankingFilter;
import com.despegar.p13n.hestia.data.hbase.hot.render.RankingRenderFilterConfig.RankingRenderFilterId;
import com.despegar.p13n.hestia.data.hbase.hot.types.grouping.RankingDefaultGroupFunction;
import com.despegar.p13n.hestia.data.hbase.hot.types.grouping.RankingGroupFunction;

public class RankingTypeParams {
	  
	    private final RankingFilter rankingFilter;
	    private final boolean fallback;
	    private final RankingRenderFilterId renderFilter;
	    private final RankingGroupFunction rankingGroupFunction;

	    public RankingTypeParams(RankingFilter rankingFilter, boolean fallback, RankingRenderFilterId renderFilter) {
	        this.rankingFilter = rankingFilter;
	        this.fallback = fallback;
	        this.renderFilter = renderFilter;
	        this.rankingGroupFunction = new RankingDefaultGroupFunction();
	    }

	    public RankingTypeParams(RankingFilter rankingFilter, boolean fallback, RankingRenderFilterId renderFilter,
	        RankingGroupFunction rankingGroupFunction) {
	        this.rankingFilter = rankingFilter;
	        this.fallback = fallback;
	        this.renderFilter = renderFilter;
	        this.rankingGroupFunction = rankingGroupFunction;
	    }

	    public RankingFilter getRankingFilter() {
	        return this.rankingFilter;
	    }

	    public boolean isFallback() {
	        return this.fallback;
	    }

	    public RankingRenderFilterId getRenderFilter() {
	        return this.renderFilter;
	    }

	    public RankingGroupFunction getRankingGroupFunction() {
	        return rankingGroupFunction;
	    }
}
