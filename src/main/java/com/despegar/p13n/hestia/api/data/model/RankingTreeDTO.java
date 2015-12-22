package com.despegar.p13n.hestia.api.data.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

public class RankingTreeDTO {
	
	private String city;
	    private List<RankingPositionDTO> podium;
	    private RankingTreeDTO child;

	    public RankingTreeDTO() {
	        this.podium = Lists.newArrayList();
	    }

	    public RankingTreeDTO(String city) {
	        this.podium = new ArrayList<RankingPositionDTO>();
	        this.city = city;
	    }

	    public List<RankingPositionDTO> getPodium() {
	        return this.podium;
	    }

	    public void setPodium(List<RankingPositionDTO> podium) {
	        this.podium = podium;
	    }

	    public RankingTreeDTO getChild() {
	        return this.child;
	    }

	    public void setChild(RankingTreeDTO child) {
	        this.child = child;
	    }

	    public String getCity() {
	        return this.city;
	    }

	    public void setCity(String city) {
	        this.city = city;
	    }

	    public void addPosition(String dest, Long freq) {
	        this.podium.add(new RankingPositionDTO(dest, freq));
	    }

	    public void addPosition(RankingPositionDTO dto) {
	        this.podium.add(dto);
	    }


	    @Override
	    public String toString() {
	        return "RankingTreeDTO [city=" + this.city + ", podium=" + this.podium + ", child=" + this.child + "]";
	    }

	    public static RankingTreeDTO merge(String city, Collection<RankingTreeDTO> rankings) {
	        if (rankings != null && !rankings.isEmpty()) {
	            List<RankingPositionDTO> positions = Lists.newArrayList();

	            List<RankingTreeDTO> childs = Lists.newArrayList();

	            for (RankingTreeDTO ranking : rankings) {
	                if (city != null && !ranking.getCity().equalsIgnoreCase(city)) {
	                    throw new RuntimeException("Error merging rankings");
	                }

	                positions.addAll(ranking.getPodium());
	                if (ranking.getChild() != null) {
	                    childs.add(ranking.getChild());
	                }
	            }

	            Collections.sort(positions, Ordering.natural());

	            RankingTreeDTO result = new RankingTreeDTO(city);
	            result.setPodium(positions);
	            if (!childs.isEmpty()) {
	                result.setChild(RankingTreeDTO.merge(city, childs));
	            }

	            return result;
	        }

	        return new RankingTreeDTO(city);
	    }
}
