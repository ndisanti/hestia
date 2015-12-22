package com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority;

import java.util.Collection;
import java.util.Map;

import com.despegar.p13n.hestia.recommend.allinone.VisitFlow;
import com.despegar.p13n.hestia.recommend.decisiontree.DecisionNode;
import com.despegar.p13n.hestia.recommend.decisiontree.Node;
import com.despegar.p13n.hestia.recommend.decisiontree.Query;
import com.despegar.p13n.hestia.recommend.decisiontree.RuleDef;
import com.despegar.p13n.hestia.utils.TreeUtils;
import com.google.common.collect.Maps;

public class VisitFlowNode
    extends DecisionNode<SectionFunctionContent> {

    private Map<VisitFlow, Node<SectionFunctionContent>> childs = Maps.newLinkedHashMap();

    @Override
    public Value<SectionFunctionContent> add(RuleDef ruleDef, SectionFunctionContent function) {

        RuleFunction rule = (RuleFunction) ruleDef;

        if (!this.childs.containsKey(rule.getVisitFlow())) {
            this.childs.put(rule.getVisitFlow(), this.getNewChild());
        }

        return this.childs.get(rule.getVisitFlow()).add(rule, function);
    }

    @Override
    public Node<SectionFunctionContent> getNext(Query query) {

        QueryFunction q = (QueryFunction) query;

        if (this.childs.containsKey(q.getVisitFlow())) {
            return this.childs.get(q.getVisitFlow());
        }

        return this.childs.get(VisitFlow.ANY);
    }


    @Override
    public boolean hasChilds() {
        return !this.childs.isEmpty();
    }

    @Override
    public boolean containsRule(RuleDef ruleDef) {

        RuleFunction rule = (RuleFunction) ruleDef;

        Node<SectionFunctionContent> node = this.childs.get(rule.getVisitFlow());

        return node != null && node.containsRule(rule);
    }

    @Override
    public Node<SectionFunctionContent> getNewChild() {
        return new VisitCityNode();
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
