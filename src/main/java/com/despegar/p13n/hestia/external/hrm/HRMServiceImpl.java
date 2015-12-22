package com.despegar.p13n.hestia.external.hrm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.despegar.hotel.dto.AmenitySiteDTO;
import com.despegar.library.routing.uow.UowHelper;
import com.despegar.p13n.commons.zk.ZKFactory;
import com.despegar.p13n.commons.zk.ZKSharedLock;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.hestia.external.geo.GeoCity;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.external.hotels.v3.dtos.AmenityDTO;
import com.despegar.p13n.hestia.external.hotels.v3.dtos.DescriptionsDTO;
import com.despegar.p13n.hestia.external.hotels.v3.dtos.HotelDTO;
import com.despegar.p13n.hestia.external.hotels.v3.dtos.PaginatedHotelsDTO;
import com.despegar.p13n.hestia.newrelic.CacheReporting;
import com.despegar.p13n.hestia.snapshot.AbstractAsyncSnapshot;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.newrelic.api.agent.Trace;


public class HRMServiceImpl
    extends AbstractAsyncSnapshot<HRMSnapshot>
    implements HRMService, CacheReporting {

    private static final Logger logger = LoggerFactory.getLogger(HRMServiceImpl.class);

    private Cache<String, HotelItem> cacheHotels;

    /** Version of this snapshot.
     * Change this number after every change to {@link HRMSnapshot}
     * to avoid unmarshalling/marshalling problems*/
    private static final int VERSION = 5;

    private HRMClient hrmClient;

    private GeoService geoService;

    private ZKFactory zkRecipesFactory;

    @Override
    protected HRMSnapshot getData() {
        HRMSnapshot result = null;
        if (this.snapshot != null) {
            // it is a reload, we reload the snapshot one server at a time
            ZKSharedLock lock = this.zkRecipesFactory.createSharedLock("reloadHrmSnapshot");
            lock.acquire();
            result = this.createSnapshot();
            lock.release();
        } else {
            result = this.createSnapshot();
        }
        return result;
    }

    public void init() {
        this.cacheHotels = CacheBuilder.newBuilder()//
            .expireAfterWrite(8, TimeUnit.HOURS)//
            .maximumSize(10000)//
            .recordStats()//
            .build();
    }

    private HRMSnapshot createSnapshot() {
        logger.info("Fetching hotel lists");
        HRMSnapshot snapshot = new HRMSnapshot();

        UowHelper.createAndSetNewUnitOfWork("euler-snapshot");

        this.populateHotelData(snapshot);
        this.populateAllAmenities(snapshot);

        UowHelper.clearUnitOfWork();

        return snapshot;
    }

    private void populateAllAmenities(HRMSnapshot snapshot) {
        List<AmenityDTO> amenities = this.hrmClient.getAllAmenities();

        List<AmenitySiteDTO> amenitiesEN = Lists.newArrayListWithCapacity(amenities.size());
        List<AmenitySiteDTO> amenitiesES = Lists.newArrayListWithCapacity(amenities.size());
        List<AmenitySiteDTO> amenitiesPT = Lists.newArrayListWithCapacity(amenities.size());

        for (AmenityDTO amenity : amenities) {
            DescriptionsDTO descriptions = amenity.getDescriptions();

            amenitiesEN.add(this.getAmenitySiteDTO(amenity.getId(), descriptions.getEn()));
            amenitiesES.add(this.getAmenitySiteDTO(amenity.getId(), descriptions.getEs()));
            amenitiesPT.add(this.getAmenitySiteDTO(amenity.getId(), descriptions.getPt()));
        }

        Map<Language, List<AmenitySiteDTO>> languageMap = Maps.newEnumMap(Language.class);

        languageMap.put(Language.EN, amenitiesEN);
        languageMap.put(Language.ES, amenitiesES);
        languageMap.put(Language.PT, amenitiesPT);

        snapshot.setAmenitiesByLanguage(languageMap);
    }

    private AmenitySiteDTO getAmenitySiteDTO(String code, String description) {
        AmenitySiteDTO amenitySiteDTO = new AmenitySiteDTO();
        amenitySiteDTO.setCode(code);
        amenitySiteDTO.setDescription(description);

        return amenitySiteDTO;
    }

    private void populateHotelData(HRMSnapshot snapshot) {
        Set<String> testHotelIds = Sets.newHashSet();
        long totalFetched = 0;
        long itemsFetched;
        long total;
        Map<Long, Integer> starsByHotel = Maps.newHashMap();
        do {
            PaginatedHotelsDTO hotelsPage = this.hrmClient.getHotelList(totalFetched);
            List<HotelDTO> hotels = hotelsPage.getItems();

            for (HotelDTO hotel : hotels) {
                long hotelOid = Long.parseLong(hotel.getId());

                if (!hotel.isPublished()) {
                    snapshot.addUnpublishedHotel(hotelOid);
                }

                if (hotel.isTest()) {
                    testHotelIds.add(hotel.getId());
                }

                long cityOid = Long.parseLong(hotel.getLocation().getCity().getId());

                GeoCity city = this.geoService.getCityInfo(cityOid);
                if (city == null) {
            //        logger.error("No city found for oid {}", cityOid);
                } else {
                    String iata = city.getIataCode();
                    logger.debug("Mapping city " + iata + " with hotel " + hotelOid);
                    snapshot.addCityMapping(iata, hotelOid);
                }

                starsByHotel.put(hotelOid, hotel.getStars());

            }

            itemsFetched = hotelsPage.getItems().size();
            totalFetched += itemsFetched;
            total = hotelsPage.getPaging().getTotal();

            logger.info(String.format("Fetched page of hotel list. Result size: %d - Total fetched: %d - Total: %d",
                itemsFetched, totalFetched, total));

        } while (totalFetched < total);
        snapshot.setStarsByHotel(starsByHotel);
        snapshot.setHotelIdsBlacklist(testHotelIds);
    }

    @Override
    public String getIdentifier() {
        return "hrm-hotels-mapping-" + VERSION;
    }

    @Override
    @Trace
    public List<String> getHotelIdsForCity(String city) {
        if (this.snapshotState != SnapshotState.LOADED) {
            logger.warn("Ignoring request because snapshot is being loaded");
            return Collections.emptyList();
        }
        return this.snapshot.hotelForCity(city);
    }

    @Override
    @Trace
    public List<String> getHotelIdsForCountry(String country) {
        if (this.snapshotState != SnapshotState.LOADED) {
            logger.warn("Ignoring request because snapshot is being loaded");
            return Collections.emptyList();
        }
        List<String> cities = this.geoService.getCitiesForCountry(country);
        List<String> hotels = Lists.newArrayList();
        for (String city : cities) {
            hotels.addAll(this.getHotelIdsForCity(city));
        }
        return hotels;
    }

    @Override
    public String getHotelCity(String hotelId) {
        if (this.snapshotState != SnapshotState.LOADED) {
            return null;
        }
        return this.snapshot.cityForHotel(hotelId);
    }

    @Override
    public Set<String> getUnpublishedHotels() {
        if (this.snapshotState != SnapshotState.LOADED) {
            return Collections.emptySet();
        }
        return this.snapshot.getUnpublishedHotels();
    }

    @Override
    public Set<String> getHotelIdsBlacklist() {
        if (this.snapshotState != SnapshotState.LOADED) {
            return Collections.emptySet();
        }
        return this.snapshot.getHotelIdsBlacklist();
    }

    @Override
    @Trace
    public Map<String, HotelItem> getHotelForIds(Set<String> hotelIds) {

        Map<String, HotelItem> hotelMap = Maps.newHashMap();
        List<String> hotelIdsToQuery = Lists.newArrayList();

        for (String hotelId : hotelIds) {
            HotelItem cachedHotel = this.cacheHotels.getIfPresent(hotelId);
            if (cachedHotel != null) {
                hotelMap.put(hotelId, cachedHotel);
            } else {
                hotelIdsToQuery.add(hotelId);
            }
        }

        if (!hotelIdsToQuery.isEmpty()) {

            List<HotelDTO> hotels = this.hrmClient.getHotelsForIds(hotelIdsToQuery);

            for (HotelDTO hotel : hotels) {
                HotelItem hotelItem = this.toHotelItem(hotel);

                String hotelId = hotelItem.getId().toString();
                this.cacheHotels.put(hotelId, hotelItem);
                hotelMap.put(hotelId, hotelItem);
            }
        }
        return hotelMap;
    }

    private HotelItem toHotelItem(HotelDTO hotel) {
        return new HotelItem(Long.parseLong(hotel.getId()), new BigDecimal(hotel.getStars()), hotel.getAmenities());
    }

    @SuppressWarnings("rawtypes")
    public Map<String, Cache> getCachesForReporting() {
        return ImmutableMap.of("hotelItems", (Cache) this.cacheHotels);
    }

    @Override
    public List<AmenitySiteDTO> getAllAmenities(Language language) {
        if (this.snapshotState != SnapshotState.LOADED) {
            return Collections.emptyList();
        }
        return new ArrayList<>(this.snapshot.getAmenitiesByLanguage().get(language));
    }

    @Override
    public Map<String, AmenitySiteDTO> getAmenitiesByCode(Language language) {
        List<AmenitySiteDTO> list = this.getAllAmenities(language);
        Map<String, AmenitySiteDTO> map = Maps.newHashMap();
        for (AmenitySiteDTO dto : list) {
            String code = dto.getCode();
            map.put(code, dto);
        }
        return map;
    }

    @Override
    public Integer getHotelStars(Long hotelId) {
        if (this.snapshotState != SnapshotState.LOADED) {
            return -1;
        }
        return this.snapshot.getStarsByHotel().get(hotelId);
    }

    public void setGeoService(GeoService geoService) {
        this.geoService = geoService;
    }

    public void setHrmClient(HRMClient hrmClient) {
        this.hrmClient = hrmClient;
    }

    public void setZkRecipesFactory(ZKFactory zkRecipesFactory) {
        this.zkRecipesFactory = zkRecipesFactory;
    }


}
