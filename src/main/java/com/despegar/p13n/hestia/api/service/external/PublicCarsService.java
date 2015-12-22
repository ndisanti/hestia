package com.despegar.p13n.hestia.api.service.external;

import java.util.List;
import java.util.Map;

import com.despegar.p13n.hestia.api.data.model.CarsCategoryRankingDto;
import com.despegar.p13n.hestia.api.data.model.CarsLocationDto;
import com.despegar.p13n.hestia.api.data.model.PultypeCounterDTO;

public interface PublicCarsService {

    /**
     * <p>Returns the checkout counter by car category for the given country code and pick-up location.</p>
     * 
     * @param cc country code
     * @param pul pick-up location
     * @return category counter
     */
    Map<String, Long> getCategoryCounterForCheckout(String cc, String pul);

    /**
     * <p>Returns the checkout ranking by car category for the given country code and pick-up location.</p>
     * 
     * @param cc country code
     * @param pul pick-up location
     * @return category counter
     */
    List<String> getCategoryRankingForCheckout(final String cc, final String pul);

    /**
     * Get top searches ranking by country and location, filtering by country code.
     * 
     * @param cc
     * @param topn
     * @return
     */
    Map<String, List<CarsLocationDto>> getTopSearchesRanking(final String cc, final int topn);

    /**
     * Get top checked out destinations by category, filtering by country code.
     * 
     * @param cc
     * @param pul
     * @return
     */
    Map<String, CarsCategoryRankingDto> getTopCheckoutsRanking(final String cc, final int topn);

    /**
     * Get Top checked out categories by location for a cc and pul
     * 
     * @param cc
     * @param pul
     * @return
     */
    Map<String, CarsLocationDto> getTopCategoriesByDestination(String cc, String pul);

    /**
     * Returns a ranking of the most searched pultypes for a given cc and pul
     * 
     * @param cc
     * @param pul
     * @return
     */
    List<PultypeCounterDTO> getPultypeRankingForSearch(String cc, String pul);
}
