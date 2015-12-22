package com.despegar.p13n.hestia.model;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;

public class Ranking {
	private Set<RankingPosition> values;

    public Ranking() {
        this.values = new TreeSet<RankingPosition>();
    }

    public Ranking(Collection<RankingPosition> values) {
        super();
        this.values = new TreeSet<RankingPosition>(values);
    }

    public RankingTreeDTO toRankingDTO(String origin, int limit) {
        final RankingTreeDTO result = new RankingTreeDTO(origin);
        Iterator<RankingPosition> it = this.getValues().iterator();
        int positions = 0;
        while (it.hasNext() && positions < limit) {
            RankingPosition pos = it.next();
            result.addPosition(new RankingPositionDTO(pos.getName(), pos.getValue(), pos.getMeta()));
            positions++;
        }
        return result;
    }

    public RankingTreeDTO toRankingDTO(String origin) {
        final RankingTreeDTO result = new RankingTreeDTO(origin);
        Iterator<RankingPosition> it = this.getValues().iterator();
        while (it.hasNext()) {
            RankingPosition pos = it.next();
            result.addPosition(new RankingPositionDTO(pos.getName(), pos.getValue(), pos.getMeta()));
        }
        return result;
    }

    public static Function<Ranking, RankingTreeDTO> toRankingTreeDTOFunction(final String origin, final Integer limit) {
        return new Function<Ranking, RankingTreeDTO>() {
            public RankingTreeDTO apply(Ranking input) {
                return input.toRankingDTO(origin, limit);
            }
        };
    }

    public static Function<Ranking, RankingTreeDTO> toRankingTreeDTOFunction(final String origin) {
        return new Function<Ranking, RankingTreeDTO>() {
            public RankingTreeDTO apply(Ranking input) {
                return input.toRankingDTO(origin);
            }
        };
    }

    public static Function<Collection<RankingPosition>, Ranking> fromRankingPositionFunction() {
        return new Function<Collection<RankingPosition>, Ranking>() {
            public Ranking apply(Collection<RankingPosition> input) {
                return new Ranking(input);
            }
        };
    }

    /**
     * Gets the podium. If 2 members of the podium has the same value, they are positioned in the order natural order of their names.
     *
     * @return the podium
     */
    public List<RankingPosition> podium() {
        return new ArrayList<RankingPosition>(this.values);
    }

    public Set<RankingPosition> getValues() {
        return this.values;
    }

    public void setValues(TreeSet<RankingPosition> values) {
        this.values = values;
    }

    public void insert(long quantity, String value) {
        RankingPosition pos = new RankingPosition(quantity, value);
        this.addRankingPosition(pos);
    }

    public void insert(long quantity, String value, Map<String, String> extraData) {
        RankingPosition pos = new RankingPosition(quantity, value);
        pos.setMeta(extraData);

        this.addRankingPosition(pos);
    }

    public void addRankingPosition(RankingPosition pos) {
        this.values.add(pos);
    }

    public boolean empty() {
        return this.values.isEmpty();
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.values == null) ? 0 : this.values.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Ranking other = (Ranking) obj;
        if (this.values == null) {
            if (other.values != null) {
                return false;
            }
        } else if (!this.values.equals(other.values)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ranking [values=" + this.values + "]";
    }
}
