package com.despegar.p13n.hestia.service;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.despegar.p13n.hestia.api.data.model.CarsCategoryRankingDto;
import com.despegar.p13n.hestia.api.data.model.CarsLocationDto;
import com.despegar.p13n.hestia.api.data.model.PultypeCounterDTO;
import com.despegar.p13n.hestia.api.service.BaseJsonService;
import com.despegar.p13n.hestia.api.service.EulerResponseContainer;
import com.despegar.p13n.hestia.api.service.external.PublicCarsService;

@Service("carsServiceImpl")
public class CarsServiceImpl
    extends BaseJsonService
    implements PublicCarsService {

    protected static final Logger LOG = LoggerFactory.getLogger(CarsServiceImpl.class);

    private static final String CATEGORY_COUNTER = "/cars/category/counter/country/{cc}/pul/{pul}";

    private static final String CATEGORY_RANKING = "/cars/category/ranking/country/{cc}/pul/{pul}";

    private static final String SEARCH_RANKING = "/cars/search-ranking?topn={topn}&cc={cc}";

    private static final String CHECKOUT_RANKING = "/cars/category-ranking?topn={topn}&cc={cc}";

    private static final String CATEGORY_PULTYPE_RANKING = "/cars/category-ranking-destination?cc={cc}&pul={pul}";

    private static final String PULTYPE_RANKING_SEARCH = "/cars/pultype-ranking?cc={cc}&pul={pul}";

    /*
     * Placeholders
     */
    private static final String PH_PUL = "{pul}"; // pick-up location

    private static final String PH_CC = "{cc}"; // country code

    private static final String PH_TOPN = "{topn}"; // topn

    @Override
    public Map<String, Long> getCategoryCounterForCheckout(String cc, String pul) {
        String url = CATEGORY_COUNTER;
        url = url.replace(PH_CC, String.valueOf(cc));
        url = url.replace(PH_PUL, String.valueOf(pul));
        return this.getForObject(url, new TypeReference<EulerResponseContainer<Map<String, Long>>>() {});
    }

    @Override
    public List<String> getCategoryRankingForCheckout(String cc, String pul) {
        String url = CATEGORY_RANKING;
        url = url.replace(PH_CC, String.valueOf(cc));
        url = url.replace(PH_PUL, String.valueOf(pul));
        return this.getForObject(url, new TypeReference<EulerResponseContainer<List<String>>>() {});
    }

    @Override
    public Map<String, List<CarsLocationDto>> getTopSearchesRanking(String cc, int topn) {
        String url = SEARCH_RANKING;
        url = url.replace(PH_CC, String.valueOf(cc));
        url = url.replace(PH_TOPN, String.valueOf(topn));
        return this.getForObject(url, new TypeReference<EulerResponseContainer<Map<String, List<CarsLocationDto>>>>() {});
    }

    @Override
    public Map<String, CarsCategoryRankingDto> getTopCheckoutsRanking(String cc, int topn) {
        String url = CHECKOUT_RANKING;
        url = url.replace(PH_CC, String.valueOf(cc));
        url = url.replace(PH_TOPN, String.valueOf(topn));
        return this.getForObject(url, new TypeReference<EulerResponseContainer<Map<String, CarsCategoryRankingDto>>>() {});
    }

    @Override
    public Map<String, CarsLocationDto> getTopCategoriesByDestination(String cc, String pul) {
        String url = CATEGORY_PULTYPE_RANKING;
        url = url.replace(PH_CC, String.valueOf(cc));
        url = url.replace(PH_PUL, String.valueOf(pul));
        return this.getForObject(url, new TypeReference<EulerResponseContainer<Map<String, CarsLocationDto>>>() {});
    }

    @Override
    public List<PultypeCounterDTO> getPultypeRankingForSearch(String cc, String pul) {
        String url = PULTYPE_RANKING_SEARCH;
        url = url.replace(PH_CC, String.valueOf(cc));
        url = url.replace(PH_PUL, String.valueOf(pul));
        return this.getForObject(url, new TypeReference<EulerResponseContainer<List<PultypeCounterDTO>>>() {});
    }
}