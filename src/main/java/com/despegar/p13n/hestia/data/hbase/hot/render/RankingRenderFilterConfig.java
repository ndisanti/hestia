package com.despegar.p13n.hestia.data.hbase.hot.render;

public class RankingRenderFilterConfig {
	public enum RankingRenderFilterId {

        IATA_EXISTS("iataRenderFilter"), ALL("allRenderFilter");

        private String id;

        private RankingRenderFilterId(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }
    }
}
