package com.despegar.p13n.hestia.api.data.model;

import java.util.Map;
import java.util.Queue;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.google.common.collect.Maps;

public class HomeContent {

	    private Queue<String> debug;
	    private HomeContentTrace trace;
	    private Map<Product, HomeProduct> products = Maps.newHashMap();
	    private boolean singleProduct = true;
	    private Product recommendedHome;
	    private String userLocation = "";
	    private String lastActivity;
	    private boolean isAppMobileInstalled;
	    private String checkIn;

	    public HomeContent() {
	        this.recommendedHome = Product.HOTELS;
	    }

	    public Queue<String> getDebug() {
	        return this.debug;
	    }

	    public void setDebug(Queue<String> debug) {
	        this.debug = debug;
	    }

	    public HomeContent addProduct(Product product, HomeProduct home) {
	        this.products.put(product, home);
	        return this;
	    }

	    public Map<Product, HomeProduct> getProducts() {
	        return this.products;
	    }

	    public void setProducts(Map<Product, HomeProduct> products) {
	        this.products = products;
	    }


	    public boolean isSingleProduct() {
	        return this.singleProduct;
	    }


	    public void setSingleProduct(boolean singleProduct) {
	        this.singleProduct = singleProduct;
	    }

	    public HomeContentTrace getTrace() {
	        return this.trace;
	    }

	    public void setTrace(HomeContentTrace trace) {
	        this.trace = trace;
	    }

	    public Product getRecommendedHome() {
	        return this.recommendedHome;
	    }

	    public void setRecommendedHome(Product recommendedHome) {
	        this.recommendedHome = recommendedHome;
	    }

	    public String getUserLocation() {
	        return this.userLocation;
	    }

	    public void setUserLocation(String userLocation) {
	        this.userLocation = userLocation;
	    }

	    public String getLastActivity() {
	        return this.lastActivity;
	    }

	    public void setLastActivity(String lastActivity) {
	        this.lastActivity = lastActivity;
	    }

	    public boolean isAppMobileInstalled() {
	        return this.isAppMobileInstalled;
	    }

	    public void setAppMobileInstalled(boolean isAppMobileInstalled) {
	        this.isAppMobileInstalled = isAppMobileInstalled;
	    }

	    public String getCheckIn() {
	        return this.checkIn;
	    }

	    public void setCheckIn(String checkIn) {
	        this.checkIn = checkIn;
	    }

	    @Override
	    public String toString() {
	        return "HomeContent [debug=" + this.debug + ", trace=" + this.trace + ", products=" + this.products
	            + ", singleProduct=" + this.singleProduct + ", recommendedHome=" + this.recommendedHome + ", userLocation="
	            + this.userLocation + ", lastActivity=" + this.lastActivity + ", isAppMobileInstalled="
	            + this.isAppMobileInstalled + ", checkIn=" + this.checkIn + "]";
	    }
}
