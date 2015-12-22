package com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent;

import java.util.List;

import com.despegar.p13n.hestia.recommend.allinone.rules.RuleItem;
import com.despegar.p13n.hestia.recommend.allinone.rules.builder.HomeRuleBuilder;
import com.google.common.base.Preconditions;

public class HomeRuleContent {

    private final List<RuleItem> offers;
    private final List<List<RuleItem>> rowList;

    public HomeRuleContent(List<RuleItem> offers, List<List<RuleItem>> rowList) {
        Preconditions.checkArgument(offers.size() == HomeRuleBuilder.OFFERS_SIZE);
        Preconditions.checkArgument(rowList.size() == HomeRuleBuilder.ROWS_SIZE);

        for (int i = 0; i < HomeRuleBuilder.ROWS_SIZE; i++) {
            Preconditions.checkArgument(rowList.get(i).size() == HomeRuleBuilder.COL_SIZE);
        }

        this.offers = offers;
        this.rowList = rowList;
    }

    public List<RuleItem> getOffers() {
        return this.offers;
    }

    public List<List<RuleItem>> getRowList() {
        return this.rowList;
    }

    @Override
    public String toString() {
        return "HomeRuleContent [offers=" + this.offers + ", rowList=" + this.rowList + "]";
    }

}
