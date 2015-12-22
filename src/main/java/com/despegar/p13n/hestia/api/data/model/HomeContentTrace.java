package com.despegar.p13n.hestia.api.data.model;

import java.util.List;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.google.common.collect.Lists;

public class HomeContentTrace {

	 private String mab;
	    private RulesVersion ruleVersion;
	    private int historyActions;
	    private int historicalTransactionSize;
	    private List<String> searchActivity;
	    private String buyActivity;
	    private List<String> lastDestinations;
	    private boolean timeOut;
	    private long executionTimeMs;
	    private boolean error;
	    private List<Product> lastResort;

	    public HomeContentTrace() {
	        this.initialize();
	    }

	    private void initialize() {
	        this.error = false;
	        this.lastResort = Lists.newArrayList();
	        this.lastDestinations = Lists.newArrayList();
	        this.executionTimeMs = 0;
	        this.timeOut = false;
	        this.historicalTransactionSize = 0;
	        this.historyActions = 0;
	    }

	    public String getMab() {
	        return this.mab;
	    }

	    public void setMab(String mab) {
	        this.mab = mab;
	    }

	    public RulesVersion getRuleVersion() {
	        return this.ruleVersion;
	    }

	    public void setRuleVersion(RulesVersion ruleVersion) {
	        this.ruleVersion = ruleVersion;
	    }

	    public int getHistoryActions() {
	        return this.historyActions;
	    }

	    public void setHistoryActions(int historyActions) {
	        this.historyActions = historyActions;
	    }

	    public int getHistoricalTransactionSize() {
	        return this.historicalTransactionSize;
	    }

	    public void setHistoricalTransactionSize(int historicalTransactionSize) {
	        this.historicalTransactionSize = historicalTransactionSize;
	    }

	    public List<String> getSearchActivity() {
	        return this.searchActivity;
	    }

	    public void setSearchActivity(List<String> searchActivity) {
	        this.searchActivity = searchActivity;
	    }

	    public String getBuyActivity() {
	        return this.buyActivity;
	    }

	    public void setBuyActivity(String buyActivity) {
	        this.buyActivity = buyActivity;
	    }

	    public List<String> getLastDestinations() {
	        return this.lastDestinations;
	    }

	    public void setLastDestinations(List<String> lastDestinations) {
	        this.lastDestinations = lastDestinations;
	    }

	    public boolean isTimeOut() {
	        return this.timeOut;
	    }

	    public void setTimeOut(boolean timeOut) {
	        this.timeOut = timeOut;
	    }

	    public long getExecutionTimeMs() {
	        return this.executionTimeMs;
	    }

	    public void setExecutionTimeMs(long executionTimeMs) {
	        this.executionTimeMs = executionTimeMs;
	    }

	    public boolean isError() {
	        return this.error;
	    }

	    public void setError(boolean error) {
	        this.error = error;
	    }

	    public List<Product> getLastResort() {
	        return this.lastResort;
	    }

	    public void setLastResort(List<Product> lastResort) {
	        this.lastResort = lastResort;
	    }
	
}
