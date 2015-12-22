package com.despegar.p13n.hestia.api.data.model;

import javax.naming.directory.SearchResult;

import com.despegar.p13n.euler.commons.client.model.UserAction;



/**
 * Represents the event of a user having performed a search.
 * 
 * @author lbernardi
 * 
 */
public class SearchAction
    extends UserAction {

    /*
     * Keys
     */
    public static final String NUMBER_RESULTS = "nr";
    public static final String DATE_FORMAT = "yyyy-MM-dd";


    private SearchResult searchResult;

    public SearchResult getSearchResult() {
        return this.searchResult;
    }

    public void setSearchResult(final SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public Integer numberResults() {
        return this.getIntegerFromActionMap(NUMBER_RESULTS);
    }

    @Override
    public boolean isResultEmpty() {
        try {
            Integer resultSize = this.numberResults();
            return (resultSize != null) && resultSize == 0;
        } catch (RuntimeException e) {
            return false;
        }
    }


}
