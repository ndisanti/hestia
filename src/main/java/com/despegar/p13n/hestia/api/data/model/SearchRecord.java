package com.despegar.p13n.hestia.api.data.model;

import java.util.Map;

import javax.naming.directory.SearchResult;

public class SearchRecord {

    public static SearchRecord newSearchRecord(SearchAction searchAction) {
        // TODO Ver que valores hace falta copiar del search action
        SearchRecord searchRecord = new SearchRecord();
        searchRecord.setFilters(searchAction.getActionData());// FIXME Data?
        searchRecord.setResult(searchAction.getSearchResult());
        return searchRecord;
    }
    
    private SearchRecord() {
    }

    private Map<String, Object> filters;
    private SearchResult result;

    public Map<String, Object> getFilters() {
        return this.filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public SearchResult getResult() {
        return this.result;
    }

    public void setResult(SearchResult searchResult) {
        this.result = searchResult;
    }


}
