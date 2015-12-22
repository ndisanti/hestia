package com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeSupport;
import com.despegar.p13n.hestia.recommend.decisiontree.DecisionNode;
import com.despegar.p13n.hestia.recommend.decisiontree.Node;
import com.despegar.p13n.hestia.recommend.decisiontree.Query;
import com.despegar.p13n.hestia.recommend.decisiontree.RuleDef;
import com.despegar.p13n.hestia.utils.TreeUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class HomeProductNode
    extends DecisionNode<SectionRuleContent> {

    private Map<HomeSupport, Node<SectionRuleContent>> childs = Maps.newLinkedHashMap();

    @Override
    public Value<SectionRuleContent> add(RuleDef ruleDef, SectionRuleContent ruleContent) {

        RuleDefSection rule = (RuleDefSection) ruleDef;

        if (!this.childs.containsKey(rule.getHome())) {
            this.childs.put(rule.getHome(), this.getNewChild());
        }

        return this.childs.get(rule.getHome()).add(rule, ruleContent);
    }

    @Override
    public Node<SectionRuleContent> getNext(Query query) {

        QueryRuleSection q = (QueryRuleSection) query;

        Preconditions.checkNotNull(q.getHome());

        HomeSupport home = this.getHomeSupport(q.getHome());

        if (home != null) {
            return this.childs.get(home);
        }

        return null;

    }

    private HomeSupport getHomeSupport(Product pr) {
        Set<HomeSupport> homeSupportKeys = this.childs.keySet();

        for (HomeSupport homeSupport : homeSupportKeys) {
            if (homeSupport.contains(pr)) {
                return homeSupport;
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

        Node<SectionRuleContent> node = this.childs.get(rule.getHome());

        return node != null && node.containsRule(rule);
    }

    @Override
    public Node<SectionRuleContent> getNewChild() {
        return new BuyProductNode();
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
