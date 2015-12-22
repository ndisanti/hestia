package com.despegar.p13n.hestia.client;

import org.apache.commons.lang3.StringUtils;

import com.despegar.library.rest.utils.TypeReference;
import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.data.hbase.hot.RankingQuery;
import com.despegar.p13n.hestia.data.hbase.hot.types.RankingType;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.utils.URLUtils;
import com.google.common.base.Preconditions;

public class RankingsClient
    extends AbstractRestClient {

    private static final String IATA_NO_FALLBACK = "/euler-service/ranking/hot/product/:pr/city/:iata/";
    private static final String HOT_QUERY = "/euler-service/ranking/hot/query/";
    private static final String HOT_IP = "/euler-service/ranking/hot/product/:pr/ip/:ip/";
    private static final String NO_LOCATION = "/v3/rankings/type/:type/product/:pr/";
    private static final String HOT_COUNTRY = "/euler-service/ranking/hot/product/:pr/country/:cc/";
    
    public RankingTreeDTO getRankingFromIataNoFallback(Product product, String iata, RankingType rankingType,
        Integer limit) {
        Preconditions.checkArgument(product != null);
        Preconditions.checkArgument(rankingType != null);
        Preconditions.checkArgument(StringUtils.isNotBlank(iata));

        String url = StringUtils.replaceEach(IATA_NO_FALLBACK, new String[] {":pr", ":iata"},
            new String[] {product.name().toLowerCase(), iata});

        url = URLUtils.addParameter(url, "noFallback", "true");
        url = URLUtils.addParameter(url, "rankingType", rankingType.getTypeCode().toLowerCase());

        if (limit != null && limit > 0) {
            url = URLUtils.addParameter(url, "limit", Integer.toString(limit));
        }

        ResponseContainer<RankingTreeDTO> apiResponse = this.execute(url,
            new TypeReference<ResponseContainer<RankingTreeDTO>>() {});

        if (apiResponse.hasErrors()) {
            throw new RuntimeException("Error in response ->  " + apiResponse.getErrors().toString());
        }

        RankingTreeDTO dto = apiResponse.getData();
        return dto;
    }



    public RankingTreeDTO getRankingQuery(Product product, RankingType rankingType, CountryCode siteCC, String yearMonth,
        String origin, Integer limit, CountryCode originCountry) {
        Preconditions.checkArgument(product != null);
        Preconditions.checkArgument(rankingType != null);


        String url = HOT_QUERY;
        url = URLUtils.addParameter(url, "product", product.name().toLowerCase());
        url = URLUtils.addParameter(url, "rankingType", rankingType.getTypeCode().toLowerCase());
        url = URLUtils.addParameter(url, "siteCC", siteCC.name());
        url = URLUtils.addParameter(url, "origin", origin);
        url = URLUtils.addParameter(url, "yearMonth", yearMonth);

        if (originCountry != null) {
            url = URLUtils.addParameter(url, "countryCC", originCountry.name());
        }

        if (limit != null && limit > 0) {
            url = URLUtils.addParameter(url, "limit", Integer.toString(limit));
        }
        ResponseContainer<RankingTreeDTO> apiResponse = this.execute(url,
            new TypeReference<ResponseContainer<RankingTreeDTO>>() {});

        if (apiResponse.hasErrors()) {
            throw new RuntimeException("Error in response ->  " + apiResponse.getErrors().toString());
        }

        RankingTreeDTO dto = apiResponse.getData();
        return dto;
    }

	public RankingTreeDTO getRankingFromIp(Product product, String inetAddress, StaticRankingTypes type, Integer limit) {
		 
		Preconditions.checkArgument(product != null);
		Preconditions.checkArgument(type != null);
	    Preconditions.checkArgument(inetAddress != null);
		
	    String url = StringUtils.replaceEach(HOT_IP, new String[] {":pr", ":ip"},
	            new String[] {product.name().toLowerCase(), inetAddress});
	    url = URLUtils.addParameter(url, "rankingType", type.getTypeCode().toLowerCase());
	    if (limit != null && limit > 0) {
            url = URLUtils.addParameter(url, "limit", Integer.toString(limit));
        }
	    ResponseContainer<RankingTreeDTO> apiResponse = this.execute(url,
	            new TypeReference<ResponseContainer<RankingTreeDTO>>() {});

	    if (apiResponse.hasErrors()) {
	            throw new RuntimeException("Error in response ->  " + apiResponse.getErrors().toString());
	    }
	    
	    return apiResponse.getData();
	}

	public RankingTreeDTO getRankingNoLocation(RankingQuery query, String ipStr, String cityIataStr, CountryCode countryCode,
	        Integer limit) {
		
		String url = StringUtils.replaceEach(NO_LOCATION, new String[] {
				":type", ":pr" }, new String[] {
				query.getRankingType().getTypeCode(),
				query.getCountry().getIataCode() });

		ResponseContainer<RankingTreeDTO> apiResponse = this.execute(url,
				new TypeReference<ResponseContainer<RankingTreeDTO>>() {
				});

		if (apiResponse.hasErrors()) {
			throw new RuntimeException("Error in response ->  "
					+ apiResponse.getErrors().toString());
		}
		return apiResponse.getData();
	}
	
	  public RankingTreeDTO getRanking(Product product, RankingType rankingType, CountryCode cc, Integer limit) {
		        
		  Preconditions.checkArgument(product != null);
		  Preconditions.checkArgument(rankingType != null);
		  Preconditions.checkArgument(cc != null);
		  String url = StringUtils.replaceEach(HOT_COUNTRY, new String[] {":product", ":cc"},
		            new String[] {product.name().toLowerCase(), cc.name().toLowerCase()});
		        url = URLUtils.addParameter(url, "rankingType", rankingType.getTypeCode().toLowerCase());
		       
		        if (limit != null && limit > 0) {
		            url = URLUtils.addParameter(url, "limit", Integer.toString(limit));
		        }
		        ResponseContainer<RankingTreeDTO> apiResponse = this.execute(url,
		            new TypeReference<ResponseContainer<RankingTreeDTO>>() {});

		        if (apiResponse.hasErrors()) {
		            throw new RuntimeException("Error in response ->  " + apiResponse.getErrors().toString());
		        }
		        return apiResponse.getData();
		} 
}
