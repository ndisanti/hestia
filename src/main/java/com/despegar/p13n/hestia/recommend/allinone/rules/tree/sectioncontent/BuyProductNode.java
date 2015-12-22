package com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.BuyProductSupport;
import com.despegar.p13n.hestia.recommend.decisiontree.DecisionNode;
import com.despegar.p13n.hestia.recommend.decisiontree.Node;
import com.despegar.p13n.hestia.recommend.decisiontree.Query;
import com.despegar.p13n.hestia.recommend.decisiontree.RuleDef;
import com.despegar.p13n.hestia.utils.TreeUtils;
import com.google.common.collect.Maps;

public class BuyProductNode
    extends DecisionNode<SectionRuleContent> {

    private Map<BuyProductSupport, Node<SectionRuleContent>> childs = Maps.newLinkedHashMap();

    @Override
    public Value<SectionRuleContent> add(RuleDef ruleDef, SectionRuleContent ruleContent) {

        RuleDefSection rule = (RuleDefSection) ruleDef;


        if (!this.childs.containsKey(rule.getPrBought())) {
            this.childs.put(rule.getPrBought(), this.getNewChild());
        }

        return this.childs.get(rule.getPrBought()).add(rule, ruleContent);
    }

    @Override
    public Node<SectionRuleContent> getNext(Query query) {

        QueryRuleSection q = (QueryRuleSection) query;


        if (q.getActivityType() == ActivityType.BUY) {

            BuyProductSupport home = this.getBuyProductSupport(q.getPrBought());

            if (this.childs.containsKey(home)) {
                return this.childs.get(home);
            }

            return this.childs.get(BuyProductSupport.ANY);

        } else {
            return this.childs.get(BuyProductSupport.NONE);
        }

    }


    private BuyProductSupport getBuyProductSupport(Product pr) {
        Set<BuyProductSupport> buyProductKeys = this.childs.keySet();

        for (BuyProductSupport buyProductSupport : buyProductKeys) {
            if (buyProductSupport.contains(pr)) {
                return buyProductSupport;
            }
        }

        return null;
    }

    @Override
    public boolean hasChilds() {
        return !this.childs.isEmpty();
    }

    @Override
    public boolean containsRule(RuleDef ruleDef) {

        RuleDefSection rule = (RuleDefSection) ruleDef;

        Node<SectionRuleContent> node = this.childs.get(rule.getPrBought());

        return node != null && node.containsRule(rule);
    }

    @Override
    public Node<SectionRuleContent> getNewChild() {
        return new LastActionNode();
    }

    @Override
    public Collection<Node<SectionRuleContent>> getChilds() {
        return this.childs.values();
    }

    @Override
    public Map<String, Node<SectionRuleContent>> getChildsKeysAsString() {
        return TreeUtils.toStringList(this.childs);
    }


}
