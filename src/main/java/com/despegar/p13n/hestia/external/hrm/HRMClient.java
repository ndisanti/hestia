package com.despegar.p13n.hestia.external.hrm;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.despegar.library.rest.utils.TypeReference;
import com.despegar.p13n.hestia.client.AbstractRestClient;
import com.despegar.p13n.hestia.external.hotels.v3.dtos.AmenityDTO;
import com.despegar.p13n.hestia.external.hotels.v3.dtos.HotelDTO;
import com.despegar.p13n.hestia.external.hotels.v3.dtos.PaginatedHotelsDTO;
import com.newrelic.api.agent.Trace;


public class HRMClient
    extends AbstractRestClient {

    private static final String HOTEL_SERVICE_LIST_HOTELS = "/v3/hotels?include=unpublished,test&offset={offset}";
    private static final String HOTEL_SERVICE_HOTELS_FOR_IDS = "/v3/hotels?ids={hotelOids}&options=amenities&include=unpublished,test";
    private static final String HOTEL_SERIVCE_ALL_AMENITIES = "/v3/hotels/amenities";

    /**
     * Get hotel list by using pagination
     * @param offset the number of rows skipped before returning a page of hotels.
     *        The page size is 1000.
     * @return
     */
    public PaginatedHotelsDTO getHotelList(long offset) {

        String url = HOTEL_SERVICE_LIST_HOTELS.replace("{offset}", String.valueOf(offset));

        return this.execute(url, new TypeReference<PaginatedHotelsDTO>() {});
    }

    @Trace
    public List<HotelDTO> getHotelsForIds(final List<String> hotelOids) {
        final String oids = StringUtils.join(hotelOids, ',');
        if (hotelOids.size() > 1000) {
            throw new IllegalArgumentException(com.despegar.framework.utils.string.StringUtils
                .concat("getHotelsForIds: oid list size ", hotelOids.size(), " greater than limit of 1000"));
        }

        String url = HOTEL_SERVICE_HOTELS_FOR_IDS.replace("{hotelOids}", oids);

        return this.execute(url, new TypeReference<List<HotelDTO>>() {});
    }

    public List<AmenityDTO> getAllAmenities() {
        return this.execute(HOTEL_SERIVCE_ALL_AMENITIES, new TypeReference<List<AmenityDTO>>() {});
    }
}
