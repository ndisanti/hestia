package com.despegar.p13n.hestia.data.hbase.hot.filter;

import com.despegar.p13n.hestia.model.Ranking;

public interface RankingFilter {

	 Ranking apply(Ranking ranking);
}
