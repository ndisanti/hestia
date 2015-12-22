package com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority;

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
    extends DecisionNode<SectionFunctionContent> {

    private LinkedHashMap<ActivityType, Node<SectionFunctionContent>> childs = Maps.newLinkedHashMap();

    @Override
    public Value<SectionFunctionContent> add(RuleDef ruleDef, SectionFunctionContent function) {

        RuleFunction rule = (RuleFunction) ruleDef;

        if (!this.childs.containsKey(rule.getActivityType())) {
            this.childs.put(rule.getActivityType(), this.getNewChild());
        }

        return this.childs.get(rule.getActivityType()).add(rule, function);

    }

    @Override
    public Node<SectionFunctionContent> getNext(Query query) {

        QueryFunction q = (QueryFunction) query;

        return this.childs.get(q.getActivity());
    }

    @Override
    public boolean hasChilds() {
        return !this.childs.isEmpty();
    }

    @Override
    public boolean containsRule(RuleDef ruleDef) {

        RuleFunction rule = (RuleFunction) ruleDef;

        Node<SectionFunctionContent> decisionNode = this.childs.get(rule.getActivityType());

        return decisionNode != null && decisionNode.containsRule(rule);
    }


    @Override
    public Node<SectionFunctionContent> getNewChild() {
        return new SectionTypeNode();
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
