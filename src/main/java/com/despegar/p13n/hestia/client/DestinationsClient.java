package com.despegar.p13n.hestia.client;

import org.apache.commons.lang3.StringUtils;

import com.despegar.library.rest.utils.TypeReference;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.DestinationRanking;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.utils.URLUtils;
import com.google.common.base.Preconditions;

public class DestinationsClient extends AbstractRestClient {

	private static final String NEAR_BY_CITIES = "/euler-service/searches/nearbycities/";

	public DestinationRanking<String> searchesNearByCities(Product product,
			String iata, Integer limit) {
		
		Preconditions.checkNotNull(product);
		Preconditions.checkArgument(StringUtils.isNotBlank(iata));

		limit = limit == null || limit < 1 ? BaseFunction.RANKING_SIZE : limit;
		String url = NEAR_BY_CITIES;
		url = URLUtils.addParameter(url, "product", product.name());
		url = URLUtils.addParameter(url, "destination", iata);
		url = URLUtils.addParameter(url, "limit", Integer.toString(limit));

		ResponseContainer<DestinationRanking<String>> apiResponse = this
				.execute(
						url,
						new TypeReference<ResponseContainer<DestinationRanking<String>>>() {
						});

		if (apiResponse.hasErrors()) {
			throw new RuntimeException("Error in response ->  "
					+ apiResponse.getErrors().toString());
		}
		return apiResponse.getData();
	}
}