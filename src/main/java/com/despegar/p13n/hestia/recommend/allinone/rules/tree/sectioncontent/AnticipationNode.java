package com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.despegar.p13n.hestia.recommend.allinone.activity.Anticipation;
import com.despegar.p13n.hestia.recommend.decisiontree.DecisionNode;
import com.despegar.p13n.hestia.recommend.decisiontree.Node;
import com.despegar.p13n.hestia.recommend.decisiontree.Query;
import com.despegar.p13n.hestia.recommend.decisiontree.RuleDef;
import com.despegar.p13n.hestia.utils.TreeUtils;
import com.google.common.collect.Maps;

public class AnticipationNode
    extends DecisionNode<SectionRuleContent> {

    private TreeMap<Anticipation, Node<SectionRuleContent>> childs = Maps.newTreeMap();

    @Override
    public Value<SectionRuleContent> add(RuleDef ruleDef, SectionRuleContent ruleContent) {

        RuleDefSection rule = (RuleDefSection) ruleDef;

        if (!this.childs.containsKey(rule.getAnticipation())) {
            this.childs.put(rule.getAnticipation(), this.getNewChild());
        }

        return this.childs.get(rule.getAnticipation()).add(rule, ruleContent);

    }

    @Override
    public Node<SectionRuleContent> getNext(Query query) {

        QueryRuleSection q = (QueryRuleSection) query;

        Anticipation anticipation = this.getAnticipation(q.getAnticipationDays());

        if (anticipation != null) {
            return this.childs.get(anticipation);
        }

        return null;
    }


    private Anticipation getAnticipation(int anticipationDays) {
        Set<Anticipation> anticipationKeys = this.childs.keySet();

        for (Anticipation anticipation : anticipationKeys) {
            if (anticipation.inRange(anticipationDays)) {
                return anticipation;
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

        Node<SectionRuleContent> node = this.childs.get(rule.getAnticipation());

        return node != null && node.containsRule(rule);
    }


    @Override
    public Node<SectionRuleContent> getNewChild() {
        return new CountrySupportNode();
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
