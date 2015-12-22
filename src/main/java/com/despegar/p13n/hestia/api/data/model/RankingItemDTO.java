package com.despegar.p13n.hestia.api.data.model;

public class RankingItemDTO {

    private String id;

    public RankingItemDTO() {
    }

    public RankingItemDTO(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RankingItemDTO [id=" + this.id + "]";
    }
}
