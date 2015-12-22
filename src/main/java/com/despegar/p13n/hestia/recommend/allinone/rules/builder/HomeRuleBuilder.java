package com.despegar.p13n.hestia.recommend.allinone.rules.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.despegar.p13n.hestia.recommend.allinone.rules.RuleItem;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.item.IdFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent.HomeRuleContent;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class HomeRuleBuilder {

    public static final int OFFERS_SIZE = 1;
    public static final int ROWS_SIZE = 3;
    public static final int COL_SIZE = 7;

    private List<RuleItem> offers;
    private List<List<RuleItem>> rowList;

    private HomeRuleBuilder() {
        this.offers = Lists.newArrayList(Arrays.asList(new RuleItem[OFFERS_SIZE]));
        this.rowList = new ArrayList<>();
        for (int i = 0; i < ROWS_SIZE; i++) {
            List<RuleItem> items = Lists.newArrayList(Arrays.asList(new RuleItem[COL_SIZE]));
            this.rowList.add(items);
        }
    }

    public static HomeRuleBuilder create() {
        return new HomeRuleBuilder();
    }

    public HomeRuleBuilder withOffers(IdFunction itemIdFunction, ProductFuncCode productFunction) {

        for (int i = 0; i < OFFERS_SIZE; i++) {
            this.withOffers(i, new RuleItem(itemIdFunction, productFunction));
        }

        return this;
    }

    public HomeRuleBuilder withRow1(IdFunction itemIdFunction, ProductFuncCode productFunction) {
        return this.withRow(0, new RuleItem(itemIdFunction, productFunction));
    }

    public HomeRuleBuilder withRow2(IdFunction itemIdFunction, ProductFuncCode productFunction) {
        return this.withRow(1, new RuleItem(itemIdFunction, productFunction));
    }

    public HomeRuleBuilder withRow3(IdFunction itemIdFunction, ProductFuncCode productFunction) {
        return this.withRow(2, new RuleItem(itemIdFunction, productFunction));
    }

    /**
     * one-based indexes
     */
    public HomeRuleBuilder withItem(int row, int col, IdFunction itemIdFunction, ProductFuncCode productFunction) {
        return this.withItem(row - 1, col - 1, new RuleItem(itemIdFunction, productFunction));
    }

    private HomeRuleBuilder withOffers(int index, RuleItem itemSection) {
        this.offers.set(index, itemSection);
        return this;
    }

    private HomeRuleBuilder withRow(int row, RuleItem itemSection) {

        for (int col = 0; col < COL_SIZE; col++) {
            this.withItem(row, col, itemSection);
        }
        return this;
    }

    private HomeRuleBuilder withItem(int row, int col, RuleItem itemSection) {

        Preconditions.checkArgument(row >= 0 && row <= ROWS_SIZE, "Invalid row: %s", row);
        Preconditions.checkArgument(col >= 0 && col <= COL_SIZE, "Invalid col: %s", col);

        this.rowList.get(row).set(col, itemSection);
        return this;
    }

    public HomeRuleContent build() {

        for (int i = 0; i < OFFERS_SIZE; i++) {
            Preconditions.checkNotNull(this.offers.get(i), "offer %s not set", i);
        }

        for (int i = 0; i < ROWS_SIZE; i++) {
            Preconditions.checkNotNull(this.rowList.get(i), "row %s not set", i + 1);
            for (int j = 0; j < COL_SIZE; j++) {
                Preconditions.checkNotNull(this.rowList.get(i).get(j), "item (%s,%s) not set", i + 1, j + 1);
            }

        }
        return new HomeRuleContent(this.offers, this.rowList);
    }

}
