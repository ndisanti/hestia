package com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority;

import java.util.Collection;
import java.util.Map;

import com.despegar.p13n.hestia.recommend.decisiontree.DecisionNode;
import com.despegar.p13n.hestia.recommend.decisiontree.Node;
import com.despegar.p13n.hestia.recommend.decisiontree.Query;
import com.despegar.p13n.hestia.recommend.decisiontree.RuleDef;
import com.despegar.p13n.hestia.utils.TreeUtils;
import com.google.common.collect.Maps;

public class PriorityNode
    extends DecisionNode<SectionFunctionContent> {

    private Map<FunctionPriority, Node<SectionFunctionContent>> childs = Maps.newLinkedHashMap();

    @Override
    public Value<SectionFunctionContent> add(RuleDef ruleDef, SectionFunctionContent function) {

        RuleFunction rule = (RuleFunction) ruleDef;

        if (!this.childs.containsKey(rule.getPriority())) {
            this.childs.put(rule.getPriority(), this.getNewChild());
        }

        return this.childs.get(rule.getPriority()).add(rule, function);
    }

    @Override
    public Node<SectionFunctionContent> getNext(Query query) {

        QueryFunction q = (QueryFunction) query;

        return this.childs.get(q.getPriority());

    }

    @Override
    public boolean hasChilds() {
        return !this.childs.isEmpty();
    }

    @Override
    public boolean containsRule(RuleDef ruleDef) {

        RuleFunction rule = (RuleFunction) ruleDef;

        Node<SectionFunctionContent> node = this.childs.get(rule.getPriority());

        return node != null && node.containsRule(rule);
    }

    @Override
    public Node<SectionFunctionContent> getNewChild() {
        return new FunctionNode();
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
