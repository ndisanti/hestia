package com.despegar.p13n.hestia.external.hrm;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.despegar.hotel.dto.AmenitySiteDTO;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class HRMSnapshot
    implements Serializable {

    private static final long serialVersionUID = 7954173639890175736L;

    private Map<String, List<String>> hotelsByCity = Maps.newHashMap();
    private Map<String, String> hotelsCities = Maps.newHashMap();
    private Set<String> unpublishedHotels = Sets.newHashSet();
    private Set<String> hotelIdsBlacklist = Sets.newHashSet();
    private Map<Language, List<AmenitySiteDTO>> amenitiesByLanguage = Maps.newHashMap();
    private Map<Long, Integer> starsByHotel = Maps.newHashMap();


    public void addCityMapping(String iata, Long hotelOid) {
        List<String> hotels = this.hotelsByCity.get(iata);
        if (hotels == null) {
            hotels = Lists.newArrayList();
            this.hotelsByCity.put(iata, hotels);
        }
        String sHotelId = String.valueOf(hotelOid);
        hotels.add(sHotelId);
        this.hotelsCities.put(sHotelId, iata);
    }



    public String cityForHotel(String hotelOid) {
        return this.hotelsCities.get(hotelOid);
    }

    public List<String> hotelForCity(String iata) {
        List<String> result = this.hotelsByCity.get(iata);
        if (result == null) {
            return Collections.emptyList();
        } else {
            return result;
        }
    }

    public Map<String, String> getHotelsCities() {
        return this.hotelsCities;
    }

    public void setHotelsCities(Map<String, String> hotelsCities) {
        this.hotelsCities = hotelsCities;
    }

    public Map<String, List<String>> getHotelsByCity() {
        return this.hotelsByCity;
    }

    public void setHotelsByCity(Map<String, List<String>> hotelsByCity) {
        this.hotelsByCity = hotelsByCity;
    }

    public void addUnpublishedHotel(Long hotelOid) {
        this.unpublishedHotels.add(String.valueOf(hotelOid));
    }

    public Set<String> getUnpublishedHotels() {
        return this.unpublishedHotels;
    }

    public Set<String> getHotelIdsBlacklist() {
        return this.hotelIdsBlacklist;
    }

    public void setUnpublishedHotels(Set<String> unpublishedHotels) {
        this.unpublishedHotels = unpublishedHotels;
    }

    public void setHotelIdsBlacklist(Set<String> hotelIds) {
        this.hotelIdsBlacklist = hotelIds;
    }

    public Map<Language, List<AmenitySiteDTO>> getAmenitiesByLanguage() {
        return this.amenitiesByLanguage;
    }

    public void setAmenitiesByLanguage(Map<Language, List<AmenitySiteDTO>> amenitiesByLanguage) {
        this.amenitiesByLanguage = amenitiesByLanguage;
    }

    public Map<Long, Integer> getStarsByHotel() {
        return this.starsByHotel;
    }

    public void setStarsByHotel(Map<Long, Integer> starsByHotel) {
        this.starsByHotel = starsByHotel;
    }



}
