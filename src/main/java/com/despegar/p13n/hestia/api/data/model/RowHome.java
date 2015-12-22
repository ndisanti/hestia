package com.despegar.p13n.hestia.api.data.model;

import java.util.List;

import com.google.common.collect.Lists;

public class RowHome {

	private Titles titles;
    private ItemHome highlighted;
    private List<? extends ItemHome> offers = Lists.newArrayList();
    private boolean isMonoDestination = false;

    @Deprecated
    public RowHome() {
        // constructor for serialization
    }

    public RowHome(ItemHome highlighted, List<? extends ItemHome> offers) {
        super();
        this.highlighted = highlighted;
        this.offers = offers;
        this.titles = new Titles();
    }

    public ItemHome getHighlighted() {
        return this.highlighted;
    }

    public void setHighlighted(ItemHome highlighted) {
        this.highlighted = highlighted;
    }

    public List<? extends ItemHome> getOffers() {
        return this.offers;
    }

    public void setOffers(List<? extends ItemHome> offers) {
        this.offers = offers;
    }

    public Titles getTitles() {
        return this.titles;
    }

    public void setTitles(Titles title) {
        this.titles = title;
    }

    @Override
    public String toString() {
        return "RowHome [highlighted=" + this.highlighted + ", offers=" + this.offers + ", title=" + this.titles + "]";
    }

    public boolean isMonoDestination() {
        return this.isMonoDestination;
    }

    public void setMonoDestination(boolean isMonoDestination) {
        this.isMonoDestination = isMonoDestination;
    }
}
