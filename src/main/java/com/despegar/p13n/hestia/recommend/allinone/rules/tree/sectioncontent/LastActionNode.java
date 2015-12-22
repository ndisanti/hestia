package com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.despegar.p13n.hestia.recommend.allinone.activity.LastAction;
import com.despegar.p13n.hestia.recommend.decisiontree.DecisionNode;
import com.despegar.p13n.hestia.recommend.decisiontree.Node;
import com.despegar.p13n.hestia.recommend.decisiontree.Query;
import com.despegar.p13n.hestia.recommend.decisiontree.RuleDef;
import com.despegar.p13n.hestia.utils.TreeUtils;
import com.google.common.collect.Maps;

public class LastActionNode
    extends DecisionNode<SectionRuleContent> {

    private TreeMap<LastAction, Node<SectionRuleContent>> childs = Maps.newTreeMap();

    @Override
    public Value<SectionRuleContent> add(RuleDef ruleDef, SectionRuleContent ruleContent) {

        RuleDefSection rule = (RuleDefSection) ruleDef;

        if (!this.childs.containsKey(rule.getLastAction())) {
            this.childs.put(rule.getLastAction(), this.getNewChild());
        }

        return this.childs.get(rule.getLastAction()).add(rule, ruleContent);

    }

    @Override
    public Node<SectionRuleContent> getNext(Query query) {

        QueryRuleSection q = (QueryRuleSection) query;

        LastAction lastAction = this.getLastAction(q.getLastActionDays());

        if (lastAction != null) {
            return this.childs.get(lastAction);
        }

        return null;
    }


    private LastAction getLastAction(int lastActionDays) {
        Set<LastAction> lastActionKeys = this.childs.keySet();

        for (LastAction lastAction : lastActionKeys) {
            if (lastAction.inRange(lastActionDays)) {
                return lastAction;
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

        Node<SectionRuleContent> node = this.childs.get(rule.getLastAction());

        return node != null && node.containsRule(rule);
    }

    @Override
    public Node<SectionRuleContent> getNewChild() {
        return new AnticipationNode();
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
