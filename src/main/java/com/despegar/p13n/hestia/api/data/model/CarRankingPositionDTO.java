package com.despegar.p13n.hestia.api.data.model;

public class CarRankingPositionDTO {

    private String pul;
    private String pultype;
    private String carcat;
    private int price;
    private String currency;

    public CarRankingPositionDTO(String pul, String pultype, String carcat, int price, String currency) {
        super();
        this.pul = pul;
        this.pultype = pultype;
        this.carcat = carcat;
        this.price = price;
        this.currency = currency;
    }

    public String getPul() {
        return this.pul;
    }

    public void setPul(String pul) {
        this.pul = pul;
    }

    public String getPultype() {
        return this.pultype;
    }

    public void setPultype(String pultype) {
        this.pultype = pultype;
    }

    public String getCarcat() {
        return this.carcat;
    }

    public void setCarcat(String carcat) {
        this.carcat = carcat;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "CarRankingPositionDTO [pul=" + this.pul + ", pultype=" + this.pultype + ", carcat=" + this.carcat
            + ", price=" + this.price + ", currency=" + this.currency + "]";
    }

}
