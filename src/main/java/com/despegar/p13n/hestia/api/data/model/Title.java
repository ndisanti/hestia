package com.despegar.p13n.hestia.api.data.model;

public class Title {

	private String titleId;
    private String product;
    private String destination;
    private String origin;
    private String titleDesc;

    @Deprecated
    public Title() {
        // constructor for serialization
    }

    public Title(String titleId) {
        super();
        this.titleId = titleId;
    }

    public Title(String titleId, String product, String destination, String origin) {
        super();
        this.titleId = titleId;
        this.product = product;
        this.destination = destination;
        this.origin = origin;
    }

    public String getTitleId() {
        return this.titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getProduct() {
        return this.product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTitleDesc() {
        return this.titleDesc;
    }

    public void setTitleDesc(String titleDesc) {
        this.titleDesc = titleDesc;
    }

    @Override
    public String toString() {
        return "Title [titleId=" + this.titleId + ", product=" + this.product + ", destination=" + this.destination
            + ", origin=" + this.origin + ", titleDesc=" + this.titleDesc + "]";
    }
}
