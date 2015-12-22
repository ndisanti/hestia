package com.despegar.p13n.hestia.data.hbase.hot.filter;
import java.util.Map;

import org.apache.hadoop.hbase.util.Pair;
public interface RecommendFilter {

	public boolean include(Pair<String, Map<String, String>> flow1, Pair<String, Map<String, String>> flow2);
}
