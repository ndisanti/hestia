package com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent;

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
    extends DecisionNode<SectionRuleContent> {

    private LinkedHashMap<ActivityType, Node<SectionRuleContent>> childs = Maps.newLinkedHashMap();

    @Override
    public Value<SectionRuleContent> add(RuleDef ruleDef, SectionRuleContent ruleContent) {

        RuleDefSection rule = (RuleDefSection) ruleDef;

        if (!this.childs.containsKey(rule.getActivityType())) {
            this.childs.put(rule.getActivityType(), this.getNewChild());
        }

        return this.childs.get(rule.getActivityType()).add(rule, ruleContent);

    }

    @Override
    public Node<SectionRuleContent> getNext(Query query) {

        QueryRuleSection q = (QueryRuleSection) query;

        return this.childs.get(q.getActivityType());
    }

    @Override
    public boolean hasChilds() {
        return !this.childs.isEmpty();
    }

    @Override
    public boolean containsRule(RuleDef ruleDef) {

        RuleDefSection rule = (RuleDefSection) ruleDef;

        Node<SectionRuleContent> decisionNode = this.childs.get(rule.getActivityType());

        return decisionNode != null && decisionNode.containsRule(rule);
    }


    @Override
    public Node<SectionRuleContent> getNewChild() {
        return new HomeProductNode();
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
