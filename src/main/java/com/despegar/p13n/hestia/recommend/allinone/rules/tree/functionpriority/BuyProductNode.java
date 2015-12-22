package com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority;

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
    extends DecisionNode<SectionFunctionContent> {

    private Map<BuyProductSupport, Node<SectionFunctionContent>> childs = Maps.newLinkedHashMap();

    @Override
    public Value<SectionFunctionContent> add(RuleDef ruleDef, SectionFunctionContent function) {

        RuleFunction rule = (RuleFunction) ruleDef;

        if (!this.childs.containsKey(rule.getPrBought())) {
            this.childs.put(rule.getPrBought(), this.getNewChild());
        }

        return this.childs.get(rule.getPrBought()).add(rule, function);
    }

    @Override
    public Node<SectionFunctionContent> getNext(Query query) {

        QueryFunction q = (QueryFunction) query;

        if (q.getActivity() == ActivityType.BUY) {

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

        RuleFunction rule = (RuleFunction) ruleDef;

        Node<SectionFunctionContent> node = this.childs.get(rule.getPrBought());

        return node != null && node.containsRule(rule);
    }

    @Override
    public Node<SectionFunctionContent> getNewChild() {
        return new VisitFlowNode();
    }

    @Override
    public Collection<Node<SectionFunctionContent>> getChilds() {
        return this.childs.values();
    }

    @Override
    public Map<String, Node<SectionFunctionContent>> getChildsKeysAsString() {
        return TreeUtils.toStringList(this.childs);
    }


}
