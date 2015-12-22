package com.despegar.p13.hestia.api.data.model.ranking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class RankingDTO<T extends Comparable<T>>
    implements Serializable {

    private static final long serialVersionUID = 1840123064738416581L;
    private SortedSet<RankingEntry<T>> values;

    public RankingDTO() {
        this.values = new TreeSet<RankingEntry<T>>();
    }

    public RankingDTO(Collection<RankingEntry<T>> values) {
        super();
        this.values = new TreeSet<RankingEntry<T>>(values);
    }

    /**
     * Gets the podium. If 2 members of the podium has the same value, they are positioned in the order natural order of their names.
     *
     * @return the podium
     */
    public List<RankingEntry<T>> podium() {
        return new ArrayList<RankingEntry<T>>(this.values);
    }

    public SortedSet<RankingEntry<T>> getValues() {
        return this.values;
    }

    public void setValues(TreeSet<RankingEntry<T>> values) {
        this.values = values;
    }

    public void addRankingEntry(RankingEntry<T> pos) {
        this.values.add(pos);
    }

    public boolean empty() {
        return this.values.isEmpty();
    }

    @Override
    public String toString() {
        return "Ranking [values=" + this.values + "]";
    }
}
