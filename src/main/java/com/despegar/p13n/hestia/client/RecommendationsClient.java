package com.despegar.p13n.hestia.client;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.despegar.library.rest.Query;
import com.despegar.library.rest.utils.TypeReference;
import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.utils.URLUtils;
import com.google.common.base.Preconditions;

public class RecommendationsClient
    extends AbstractRestClient {

    private static final String SIMPLE_ITEM_BASED = "/euler-service/recommendations/linear/:product/:flow/item/:item/limit/:limit";
    private static final String CROSS_RECOMMENDATION = "euler-service/recommendations/cross/:pr1/:fl1/:pr2/:fl2/item/:item/limit/:limit";
    private static final String CROSS_DOMESTIC = "euler-service/recommendations/cross/domestic/:pr1/:fl1/:pr2/:fl2/item/:item/limit/:limit";
    
    public List<Recommendation> recommend(Product p, Flow f, String item, int limit) {
        Preconditions.checkArgument(p != null);
        Preconditions.checkArgument(f != null);
        Preconditions.checkArgument(StringUtils.isNotBlank(item));
        Preconditions.checkArgument(limit >= 0);

        Query query = Query.build(SIMPLE_ITEM_BASED,
            new Object[] {p.name().toLowerCase(), f.name().toLowerCase(), item, Integer.toString(limit)});
        ResponseContainer<List<Recommendation>> apiResponse = this.execute(query,
            new TypeReference<ResponseContainer<List<Recommendation>>>() {});

        if (apiResponse.hasErrors()) {
            throw new RuntimeException("Error in response ->  " + apiResponse.getErrors().toString());
        }

        return apiResponse.getData();
    }

	public List<Recommendation> recommend(Product product1, Flow flow1, Product product2, Flow flow2, String itemId,
			Integer size) {
		
		Preconditions.checkNotNull(product1);
		Preconditions.checkNotNull(product2);
        Preconditions.checkNotNull(flow1);
        Preconditions.checkNotNull(flow2);
        Preconditions.checkArgument(StringUtils.isNotBlank(itemId));
        Preconditions.checkArgument(size >= 0);
             
        String url = StringUtils.replaceEach(CROSS_RECOMMENDATION, new String[] {":pr1", ":fl1",":pr2",":fl2",":item",":limit"},
	            new String[] {product1.name().toLowerCase(), flow1.name().toLowerCase(),product2.name().toLowerCase(), flow2.name().toLowerCase(),itemId,String.valueOf(size)});
         
        ResponseContainer<List<Recommendation>> apiResponse = this.execute(url,
                new TypeReference<ResponseContainer<List<Recommendation>>>() {});

            if (apiResponse.hasErrors()) {
                throw new RuntimeException("Error in response ->  " + apiResponse.getErrors().toString());
            }
            return apiResponse.getData();
	}
	
	public List<Recommendation> recommend(Product product1, Flow flow1, Product product2, Flow flow2, String itemId,
			Integer size,CountryCode cc) {
		
		Preconditions.checkNotNull(product1);
		Preconditions.checkNotNull(product2);
        Preconditions.checkNotNull(flow1);
        Preconditions.checkNotNull(flow2);
        Preconditions.checkArgument(StringUtils.isNotBlank(itemId));
        Preconditions.checkArgument(size >= 0);
        Preconditions.checkNotNull(cc);
        
        String url = StringUtils.replaceEach(CROSS_DOMESTIC, new String[] {":pr1", ":fl1",":pr2",":fl2",":item",":limit"},
	            new String[] {product1.name().toLowerCase(), flow1.name().toLowerCase(),product2.name().toLowerCase(), flow2.name().toLowerCase(),itemId,String.valueOf(size)});
        url = URLUtils.addParameter(url, "country", cc.name());
        
        ResponseContainer<List<Recommendation>> apiResponse = this.execute(url,
                new TypeReference<ResponseContainer<List<Recommendation>>>() {});

            if (apiResponse.hasErrors()) {
                throw new RuntimeException("Error in response ->  " + apiResponse.getErrors().toString());
            }
            return apiResponse.getData();
	}
}
