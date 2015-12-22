package com.despegar.p13n.hestia.client;

import com.despegar.library.rest.utils.TypeReference;
import com.despegar.p13n.hestia.recommend.allinone.matrix.MatrixKey;
import com.despegar.p13n.hestia.recommend.allinone.matrix.ProductMetricCount;
import com.despegar.p13n.hestia.utils.URLUtils;
import com.google.common.base.Preconditions;

public class BuyMatrixCounterClient  extends AbstractRestClient {

    private static final String PRODUCT_METRIC_COUNT = "/euler-service/v3/buymatrixcounter/productmatrixcounter/";
    private static final String MATRIX_COUNT = "/euler-service/v3/buymatrixcounter/matrixcounter/";
    
	public ProductMetricCount getProductMetricCount(MatrixKey key) {
		
		Preconditions.checkNotNull(key);
		String url = addMatrixKeyURLParameters(key, PRODUCT_METRIC_COUNT);
		
		ResponseContainer<ProductMetricCount> apiResponse = this
				.execute(
						url,
						new TypeReference<ResponseContainer<ProductMetricCount>>() {
						});

		if (apiResponse.hasErrors()) {
			throw new RuntimeException("Error in response ->  "
					+ apiResponse.getErrors().toString());
		}
		return apiResponse.getData();
	}

	public ProductMetricCount getMatrixCounter(MatrixKey key) {
		
		Preconditions.checkNotNull(key);
		String url = addMatrixKeyURLParameters(key, MATRIX_COUNT);
		
		ResponseContainer<ProductMetricCount> apiResponse = this
				.execute(
						url,
						new TypeReference<ResponseContainer<ProductMetricCount>>() {
						});

		if (apiResponse.hasErrors()) {
			throw new RuntimeException("Error in response ->  "
					+ apiResponse.getErrors().toString());
		}
		return apiResponse.getData();
	}

	private String addMatrixKeyURLParameters(MatrixKey key, String url) {
		
		if(key.getSearchCount()!=null){
			url = URLUtils.addParameter(url, "searchCount", key.getSearchCount().name());	
		}
		if(key.getCountry()!=null){
			url = URLUtils.addParameter(url, "cc", key.getCountry().name());
		}
		url = URLUtils.addParameter(url, "buy", key.isBuy().toString());
		if(key.getProduct()!=null){
			url = URLUtils.addParameter(url, "product", key.getProduct().name());
		}
		if(key.getLastAction()!=null){
			url = URLUtils.addParameter(url, "lastAction", key.getLastAction().name());
		}
		if(key.getAnticipation()!=null){
			url = URLUtils.addParameter(url, "anticipation", key.getAnticipation().name());
		}
		if(key.getRouteType()!=null){
			url = URLUtils.addParameter(url, "routeType", key.getRouteType().name());
		}
		if(key.getDestination()!=null){
			url = URLUtils.addParameter(url, "destination", key.getDestination());
		}
		return url;
	}
}
