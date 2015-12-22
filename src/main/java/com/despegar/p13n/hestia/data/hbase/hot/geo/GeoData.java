package com.despegar.p13n.hestia.data.hbase.hot.geo;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class GeoData {
	 public static final String KEY = "rankingData";

	    private String brand;
	    private String product;
	    private String country;
	    private String selid;
	    private String facet;

	    public GeoData() {
	    }

	    public GeoData(String brand, String product, String country, String facet, String selid) {
	        this.facet = facet;
	        this.brand = brand;
	        this.product = product;
	        this.country = country;
	        this.selid = selid;
	    }

	    public String getBrand() {
	        return this.brand;
	    }

	    public String getProduct() {
	        return this.product;
	    }

	    public String getCountry() {
	        return this.country;
	    }

	    public void setBrand(String brand) {
	        this.brand = brand;
	    }

	    public void setProduct(String product) {
	        this.product = product;
	    }

	    public void setCountry(String country) {
	        this.country = country;
	    }

	    public String getSelid() {
	        return this.selid;
	    }

	    public void setSelid(String selid) {
	        this.selid = selid;
	    }

	    public String getFacet() {
	        return this.facet;
	    }

	    public void setFacet(String facet) {
	        this.facet = facet;
	    }

	    @Override
	    public String toString() {
	        return ReflectionToStringBuilder.toString(this);
	    }
}
