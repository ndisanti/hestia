package com.despegar.p13n.hestia.api.data.model;

public class Titles {

	    private Title mainTitle;
	    private Title subtitleHighlighted;
	    private Title subtitleOffers;

	    public Titles() {
	        // constructor for serialization
	    }

	    public Titles(Title mainTitle, Title subtitleHighlighted, Title subtitleOffers) {
	        this.mainTitle = mainTitle;
	        this.subtitleHighlighted = subtitleHighlighted;
	        this.subtitleOffers = subtitleOffers;
	    }

	    public Title getMainTitle() {
	        return this.mainTitle;
	    }

	    public void setMainTitle(Title mainTitle) {
	        this.mainTitle = mainTitle;
	    }

	    public Title getSubtitleHighlighted() {
	        return this.subtitleHighlighted;
	    }

	    public void setSubtitleHighlighted(Title subtitleHighlighted) {
	        this.subtitleHighlighted = subtitleHighlighted;
	    }

	    public Title getSubtitleOffers() {
	        return this.subtitleOffers;
	    }

	    public void setSubtitleOffers(Title subtitleOffers) {
	        this.subtitleOffers = subtitleOffers;
	    }

	    @Override
	    public String toString() {
	        return "Titles [mainTitle=" + this.mainTitle + ", subtitleHighlighted=" + this.subtitleHighlighted
	            + ", subtitleOffers=" + this.subtitleOffers + "]";
	    }
}
