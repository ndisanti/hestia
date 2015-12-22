package com.despegar.p13n.hestia.recommend.allinone.activity;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.HomeUtils;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserAction;
import com.despegar.p13n.hestia.recommend.allinone.matrix.SearchCount;
import com.google.common.base.Preconditions;

public class UserActivity {

    private String destination;
    private String productBusinessId;
    private int lastActionDays;
    private int anticipationDays;
    private UserAction action;
    private SearchCount searchCount;

    public UserActivity(int lastActionDays, String destination, String productBusinessId, int anticipationDays,
        UserAction action, SearchCount searchCount) {
        super();

        Preconditions.checkArgument(lastActionDays >= LastAction.any());
        Preconditions.checkArgument(anticipationDays >= Anticipation.any());
        this.lastActionDays = lastActionDays;
        this.destination = destination;
        this.productBusinessId = productBusinessId;
        this.anticipationDays = anticipationDays;
        this.action = action;
        this.searchCount = searchCount;
    }

    public Flow getFlow() {
        return this.action.getFlow();
    }

    public Product getProduct() {
        return this.action.getProduct();
    }

    public String getDestination() {
        return this.destination;
    }

    public String getProductBusinessId() {
        return this.productBusinessId;
    }

    public void setProductBusinessId(String productBusinessId) {
        this.productBusinessId = productBusinessId;
    }

    public int getLastActionDays() {
        return this.lastActionDays;
    }

    public int getAnticipationDays() {
        return this.anticipationDays;
    }

    public UserAction getAction() {
        return this.action;
    }

    public void setAction(UserAction action) {
        this.action = action;
    }

    public SearchCount getSearchCount() {
        return this.searchCount;
    }

    public void setSearchCount(SearchCount searchCount) {
        this.searchCount = searchCount;
    }

    public boolean isSearch() {
        return HomeUtils.isSearch(this.action.getFlow());
    }

    public boolean isDetailOrCheckout() {
        return HomeUtils.isDetailOrCheckout(this.action.getFlow());
    }


    @Override
    public String toString() {
        return "LastActivity [destination=" + this.destination + ", productBusinessId=" + this.productBusinessId
            + ", lastActionDays=" + this.lastActionDays + ", anticipationDays=" + this.anticipationDays + ", flow="
            + this.getFlow() + ", product=" + this.getProduct() + ", searchCount=" + this.searchCount + "]";
    }


}
