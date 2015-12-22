package com.despegar.p13n.hestia.recommend.decisiontree;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import com.despegar.p13n.hestia.recommend.decisiontree.Node.Value;
import com.google.common.collect.Lists;

public class DecisionTree<T> {

    private Node<T> root;

    private AtomicInteger id = new AtomicInteger();

    public DecisionTree(Node<T> firstNode) {
        this.root = firstNode;
    }

    public T getValue(Query query) {
        Node<T> node = this.root;

        while (node != null && !node.isLeaf()) {
            node = node.getNext(query);
        }

        if (node == null) {
            throw new IllegalStateException("Can't find value for query " + query);
        }

        return node.getValue().getValue();
    }

    public boolean hasValue(Query query) {
        Node<T> node = this.root;

        while (node != null && !node.isLeaf()) {
            node = node.getNext(query);
        }

        if (node == null) {
            return false;
        } else {
            return true;
        }
    }

    public void addRule(RuleDef rule, T t) {
        if (this.root.containsRule(rule)) {
            throw new IllegalArgumentException("Rule " + rule + " already added");
        }

        Value<T> value = this.root.add(rule, t);
        value.setId(this.id.incrementAndGet());
    }

    public int leavesCount() {
        return this.root.leaves();
    }

    public boolean containsRule(RuleDef ruleDef) {
        return this.root.containsRule(ruleDef);
    }

    public String dumpRuleDecisionOrder() {
        StringBuilder sb = new StringBuilder();

        Node<?> node = this.root;

        while (node != null) {
            if (sb.length() != 0) {
                sb.append("->");
            }
            sb.append(node.getClass().getSimpleName());

            try {
                node = node.getClass().newInstance().getNewChild();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return sb.toString();
    }


    public List<Path<T>> traverse() {

        List<Path<T>> allPaths = Lists.newArrayList();

        this.traverse("ROOT", this.root, new Path<T>(), allPaths);

        Collections.sort(allPaths);

        return allPaths;
    }

    private void traverse(String key, Node<T> root, Path<T> path, List<Path<T>> allPaths) {

        path.add(root);

        // ignoring root for path
        if (!root.equals(this.root)) {
            path.add(key);
        }

        if (root.isLeaf()) {
            Path<T> copyPath = Path.copy(path);
            copyPath.setValue(root.getValue());
            allPaths.add(copyPath);
        } else {
            for (Entry<String, Node<T>> node : root.getChildsKeysAsString().entrySet()) {

                Path<T> newPath = Path.copy(path);

                this.traverse(node.getKey(), node.getValue(), newPath, allPaths);
            }
        }
    }

    public static class Path<T>
        implements Comparable<Path<T>> {

        private List<String> path = Lists.newArrayList();
        private List<Node<?>> nodes = Lists.newArrayList();

        private Value<T> value;

        public static <T> Path<T> copy(Path<T> path) {
            Path<T> newPath = new Path<T>();
            newPath.setPath(Lists.newArrayList(path.getPath()));
            newPath.setNodes(Lists.newArrayList(path.getNodes()));
            return newPath;
        }

        public void add(String s) {
            this.path.add(s);
        }

        public void add(Node<?> n) {
            this.nodes.add(n);
        }

        public List<String> getPath() {
            return this.path;
        }

        public List<Node<?>> getNodes() {
            return this.nodes;
        }

        public Value<T> getValue() {
            return this.value;
        }

        public void setValue(Value<T> value) {
            this.value = value;
        }

        private void setPath(List<String> path) {
            this.path = path;
        }

        private void setNodes(List<Node<?>> nodes) {
            this.nodes = nodes;
        }

        @Override
        public String toString() {
            return this.path.toString();
        }

        public int compareTo(Path<T> o) {
            return this.getValue().getId() < o.getValue().getId() ? -1 : //
                this.getValue().getId() > o.getValue().getId() ? 1 : 0;
        }

    }

}
