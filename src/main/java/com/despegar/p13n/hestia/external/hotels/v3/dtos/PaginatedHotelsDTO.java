package com.despegar.p13n.hestia.external.hotels.v3.dtos;

import java.io.Serializable;
import java.util.List;

public class PaginatedHotelsDTO
    implements Serializable {


    private static final long serialVersionUID = 1L;

    private List<HotelDTO> items;
    private PagingDTO paging;

    public PaginatedHotelsDTO() {
    }

    public List<HotelDTO> getItems() {
        return this.items;
    }

    public void setItems(List<HotelDTO> items) {
        this.items = items;
    }

    public PagingDTO getPaging() {
        return this.paging;
    }

    public void setPaging(PagingDTO paging) {
        this.paging = paging;
    }

    @Override
    public String toString() {
        return "PaginatedHotelDTO [items=" + this.items + ", paging=" + this.paging + "]";
    }
}
