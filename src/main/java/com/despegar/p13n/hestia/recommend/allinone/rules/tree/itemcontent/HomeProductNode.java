package com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent;

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
    extends DecisionNode<HomeRuleContent> {

    private Map<HomeSupport, Node<HomeRuleContent>> childs = Maps.newLinkedHashMap();

    @Override
    public Value<HomeRuleContent> add(RuleDef ruleDef, HomeRuleContent ruleContent) {

        RuleDefItem rule = (RuleDefItem) ruleDef;

        if (!this.childs.containsKey(rule.getHome())) {
            this.childs.put(rule.getHome(), this.getNewChild());
        }

        return this.childs.get(rule.getHome()).add(rule, ruleContent);
    }

    @Override
    public Node<HomeRuleContent> getNext(Query query) {

        QueryRuleItem q = (QueryRuleItem) query;

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

        RuleDefItem rule = (RuleDefItem) ruleDef;

        Node<HomeRuleContent> node = this.childs.get(rule.getHome());

        return node != null && node.containsRule(rule);
    }

    @Override
    public Node<HomeRuleContent> getNewChild() {
        return new RuleContentNode();
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
