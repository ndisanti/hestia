package com.despegar.p13n.hestia.external.hrm;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.despegar.hotel.dto.AmenitySiteDTO;
import com.despegar.p13n.euler.commons.client.model.Language;

public interface HRMService {

    public Set<String> getUnpublishedHotels();

    public String getHotelCity(String hotelId);

    public List<String> getHotelIdsForCity(String city);

    public List<String> getHotelIdsForCountry(String country);

    public Map<String, HotelItem> getHotelForIds(Set<String> hids);

    public Set<String> getHotelIdsBlacklist();

    List<AmenitySiteDTO> getAllAmenities(Language language);

    Map<String, AmenitySiteDTO> getAmenitiesByCode(Language language);

    Integer getHotelStars(Long hotelId);

}
