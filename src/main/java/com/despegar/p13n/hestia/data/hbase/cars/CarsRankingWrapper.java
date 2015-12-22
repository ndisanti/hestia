package com.despegar.p13n.hestia.data.hbase.cars;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.despegar.framework.lang.Pair;
import com.despegar.p13n.hestia.api.data.model.CarsCategoryRankingDto;
import com.despegar.p13n.hestia.api.data.model.CarsLocationDto;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CarsRankingWrapper {
	@Autowired
    private GeoService geoService;

    private static final String FIELD_SEPARATOR = "/";
    private static final Integer CITY_POS = 0;
    private static final Integer PULTYPE_POS = 1;
    private static final Integer CATEGORY_POS = 2;

    public Pair<String, List<CarsLocationDto>> fromSearchRanking(RankingTreeDTO rankingTreeDTO) {
        String country = this.geoService.getCountryFromIata(rankingTreeDTO.getCity());
        return Pair.of(country, Lists.transform(rankingTreeDTO.getPodium(), fromSearchPodiumFunction()));
    }

    public Pair<String, CarsCategoryRankingDto> fromCheckoutRanking(RankingTreeDTO rankingTreeDTO) {
        String country = this.geoService.getCountryFromIata(rankingTreeDTO.getCity());
        Map<String, List<CarsLocationDto>> categoryMap = Maps.newLinkedHashMap();
        for (RankingPositionDTO ranking : rankingTreeDTO.getPodium()) {
            String category = ranking.getDestination().split(FIELD_SEPARATOR)[CATEGORY_POS];
            CarsLocationDto carLocationDto = carLocationDtoFromLabel(ranking.getDestination());

            List<CarsLocationDto> carLocations = categoryMap.get(category);
            if (carLocations == null) {
                carLocations = Lists.newArrayList();
                categoryMap.put(category, carLocations);
            }

            carLocations.add(carLocationDto);
        }

        CarsCategoryRankingDto carsCategoryRankingDto = new CarsCategoryRankingDto(categoryMap);

        return Pair.of(country, carsCategoryRankingDto);
    }

    public static Function<RankingPositionDTO, CarsLocationDto> fromSearchPodiumFunction() {
        return new Function<RankingPositionDTO, CarsLocationDto>() {
            public CarsLocationDto apply(RankingPositionDTO input) {
                return carLocationDtoFromLabel(input.getDestination());
            }
        };
    }

    public static String toSearchLabel(CarsLocationDto carsLocationDto) {
        return carsLocationDto.getPul().toLowerCase() + FIELD_SEPARATOR + carsLocationDto.getPultype().toLowerCase();
    }

    public static String toCheckoutLabel(Pair<String, CarsLocationDto> pair) {
        return pair.getRight().getPul().toLowerCase() + FIELD_SEPARATOR + pair.getRight().getPultype().toLowerCase()
            + FIELD_SEPARATOR + pair.getLeft().toLowerCase();
    }

    public static CarsLocationDto carLocationDtoFromLabel(String label) {
        String[] fields = label.split(FIELD_SEPARATOR);
        return new CarsLocationDto(fields[CITY_POS], fields[PULTYPE_POS]);
    }

    public static Pair<String, CarsLocationDto> fromCheckoutLabel(String destination) {
        String[] fields = destination.split(FIELD_SEPARATOR);
        return Pair.of(fields[CATEGORY_POS], new CarsLocationDto(fields[CITY_POS], fields[PULTYPE_POS]));
    }
}
