package com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.decisiontree.DecisionNode;
import com.despegar.p13n.hestia.recommend.decisiontree.Node;
import com.despegar.p13n.hestia.recommend.decisiontree.Query;
import com.despegar.p13n.hestia.recommend.decisiontree.RuleDef;
import com.despegar.p13n.hestia.utils.TreeUtils;
import com.google.common.collect.Maps;

public class ActivityTypeNode
    extends DecisionNode<HomeRuleContent> {

    private LinkedHashMap<ActivityType, Node<HomeRuleContent>> childs = Maps.newLinkedHashMap();

    @Override
    public Value<HomeRuleContent> add(RuleDef ruleDef, HomeRuleContent ruleContent) {

        RuleDefItem rule = (RuleDefItem) ruleDef;

        if (!this.childs.containsKey(rule.getActivityType())) {
            this.childs.put(rule.getActivityType(), this.getNewChild());
        }

        return this.childs.get(rule.getActivityType()).add(rule, ruleContent);

    }

    @Override
    public Node<HomeRuleContent> getNext(Query query) {

        QueryRuleItem q = (QueryRuleItem) query;

        return this.childs.get(q.getActivityType());
    }

    @Override
    public boolean hasChilds() {
        return !this.childs.isEmpty();
    }

    @Override
    public boolean containsRule(RuleDef ruleDef) {

        RuleDefItem rule = (RuleDefItem) ruleDef;

        Node<HomeRuleContent> decisionNode = this.childs.get(rule.getActivityType());

        return decisionNode != null && decisionNode.containsRule(rule);
    }


    @Override
    public Node<HomeRuleContent> getNewChild() {
        return new CountrySupportNode();
    }

    @Override
    public Collection<Node<HomeRuleContent>> getChilds() {
        return this.childs.values();
    }

    @Override
    public Map<String, Node<HomeRuleContent>> getChildsKeysAsString() {
        return TreeUtils.toStringList(this.childs);
    }

}
