package com.despegar.p13n.hestia.api.data.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

/**
 * @author msarno
 * 
 *         <p>
 *         Wraps most checked out Cars locations by category.
 *         </p>
 *         <p>
 *         category -> Location list
 *         </p>
 * 
 */
/**
 * @author msarno
 * 
 */
/**
 * @author msarno
 *
 */
public class CarsCategoryRankingDto {

    private Map<String, List<CarsLocationDto>> categories = Maps.newLinkedHashMap();


    public CarsCategoryRankingDto() {
    }

    public CarsCategoryRankingDto(Map<String, List<CarsLocationDto>> categories) {
        this.categories = categories;
    }

    public Map<String, List<CarsLocationDto>> getCategoriesRanking() {
        return this.categories;
    }

    public List<CarsLocationDto> getCategory(String category) {
        return this.categories.get(category);
    }

    public void addCategory(String cat, List<CarsLocationDto> locations) {
        this.categories.put(cat, locations);
    }

    /**
     * Returns a cropped copy of the original object.
     * 
     * @param size
     * @return
     */
    public CarsCategoryRankingDto crop(int size) {

        CarsCategoryRankingDto ranking = new CarsCategoryRankingDto();

        for (Entry<String, List<CarsLocationDto>> e : this.categories.entrySet()) {

            List<CarsLocationDto> sortedRanking = e.getValue();
            int maxIndex = (sortedRanking.size() > size ? size : sortedRanking.size());

            ranking.addCategory(e.getKey(), sortedRanking.subList(0, maxIndex));
        }

        return ranking;
    }

    @Override
    public String toString() {
        return "CarsCategoryRankingDto [categories=" + this.categories + "]";
    }
}
