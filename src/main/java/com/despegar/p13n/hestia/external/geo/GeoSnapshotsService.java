package com.despegar.p13n.hestia.external.geo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.hsr.geohash.GeoHash;

import com.despegar.library.rest.RestConnector;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.newrelic.CacheReporting;
import com.despegar.p13n.hestia.snapshot.EulerSnapshot;
import com.despegar.p13n.hestia.utils.CollectionsUtils;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;

@Component
public class GeoSnapshotsService
    implements CacheReporting {

    private static final Logger logger = LoggerFactory.getLogger(GeoSnapshotsService.class);
    // GeoHashing
    private static final int MAX_GEOHASH_PRECISION = 25;
    private static final int MIN_GEOHASH_PRECISION = 5;

    @Autowired
    @Qualifier("geoRestConnector")
    private RestConnector geoRestConnector;

    // Snapshots
    @Value("${geo.service.airportSnapshot.url}")
    private String urlAirportSnapshot;
    @Value("${geo.service.countrySnapshot.url}")
    private String urlCountrySnapshot;
    @Value("${geo.service.citySnapshot.url}")
    private String urlCitySnapshot;
    @Value("${geo.service.portSnapshot.url}")
    private String urlPortSnapshot;
    private GeoSnapshot snapshot = new GeoSnapshot();
    private EulerSnapshot<GeoPointDto> airportSnapshot;
    private EulerSnapshot<CountryDto> countrySnapshot;
    private EulerSnapshot<CityDto> citySnapshot;
    private EulerSnapshot<GeoPointDto> portSnapshot;

    // Properties
    private PropertiesConfiguration cruiseIataProperties;
    private PropertiesConfiguration nearbyCitiesServiceProperties;
    private PropertiesConfiguration searchRankingServiceProperties;

    private LoadingCache<LocationDto, GeoCity[]> closestCityCache;
    private SortedMap<String, List<CityDto>> geohashedCities;
    private SortedMap<String, List<GeoPointDto>> geohashedAirports;

    /** Cruise (Country, Region) -> iata ranking based on sales */
    private ListMultimap<CountryRegion, String> cruiseIataRanking;
    /** Nearby cities: Relevant cities by product and country */
    private Map<Product, Map<Long, List<CityDto>>> nearbyCitiesByProduct;
    /** All whitelisted cities */
    private List<CityDto> whitelistedCities;
    private List<String> whitelistedCitiesIatas;
    /** All whitelisted cities indexed by countries. */
    private Map<Long, List<CityDto>> whitelistedCitiesByCountry;
    /** Main cities indexed by countries. */
    private Map<String, List<CityDto>> mainCitiesByCountry;


    @Value("${geo.snapshot.update.enable:true}")
    private boolean updateSnapshot;

    public void initSnapshots(final GeoService geoService) throws Exception {
        logger.info("initializing snapshots begins");

        this.citySnapshot = new EulerSnapshot<CityDto>(this.geoRestConnector, CityDto.class, this.urlCitySnapshot,
            this.updateSnapshot) {
            @Override
            protected void afterUpdate() {
                logger.info("afterUpdate called citySnapshot");
                GeoSnapshotsService.this.populateCities(geoService);
            }
        };
        this.citySnapshot.init();
        this.populateCities(geoService);

        this.countrySnapshot = new EulerSnapshot<CountryDto>(this.geoRestConnector, CountryDto.class,
            this.urlCountrySnapshot, this.updateSnapshot) {
            @Override
            protected void afterUpdate() {
                logger.info("afterUpdate called countrySnapshot");
                GeoSnapshotsService.this.populateCountries();
            }
        };
        this.countrySnapshot.init();
        this.populateCountries();


        logger.info("initSnapshots urlAirportSnapshot={}", this.urlAirportSnapshot);
        this.airportSnapshot = new EulerSnapshot<GeoPointDto>(this.geoRestConnector, GeoPointDto.class,
            this.urlAirportSnapshot, this.updateSnapshot) {
            @Override
            protected void afterUpdate() {
                logger.info("afterUpdate called airportSnapshot");
                GeoSnapshotsService.this.populateAirports();
            }
        };
        this.airportSnapshot.init();
        this.populateAirports();

        this.portSnapshot = new EulerSnapshot<GeoPointDto>(this.geoRestConnector, GeoPointDto.class, this.urlPortSnapshot,
            this.updateSnapshot) {
            @Override
            protected void afterUpdate() {
                logger.info("afterUpdate called portSnapshot");
                GeoSnapshotsService.this.populatePorts();
            }
        };
        this.portSnapshot.init();
        this.populatePorts();

        logger.info("initializing snapshots ended");
        this.indexCruiseIataRankingProperties();
    }

    public void shutdownSnapshots() throws Exception {
        logger.info("shutting down snapshots begins");
        if (this.airportSnapshot != null) {
            this.airportSnapshot.shutdown();
        }

        if (this.countrySnapshot != null) {
            this.countrySnapshot.shutdown();
        }
        if (this.citySnapshot != null) {
            this.citySnapshot.shutdown();
        }

        if (this.portSnapshot != null) {
            this.portSnapshot.shutdown();
        }
        logger.info("shutting down snapshots ended");
    }


    private void populateAirports() {
        long airportCounter = 0;
        long descriptionsCounter = 0;
        try {
            Collection<GeoPointDto> airports = this.airportSnapshot.getAll();

            @SuppressWarnings({"unchecked", "rawtypes"})
            Map<String, GeoPointDto> airportsByIata = CollectionsUtils.indexBy(airports, new CollectionsUtils.Indexer() {
                @Override
                public Object getIndex(Object object) {
                    return ((GeoPointDto) object).getCode();
                }
            });
            this.snapshot.setAirportsByIata(airportsByIata);

            if (logger.isInfoEnabled()) {
                for (GeoPointDto airport : this.snapshot.getAirportsByIata().values()) {
                    airportCounter++;
                    if (airport.getDescriptions() != null) {
                        descriptionsCounter += airport.getDescriptions().size();
                    }
                }
            }

            this.fillGeohashedAirports();

        } finally {
            logger.info("finish populating airports: airports={} , descriptions={} ", airportCounter, descriptionsCounter);
        }
    }


    private void populateCountries() {
        logger.info("Getting countries from geo");

        long countryCounter = 0;
        long descriptionsCounter = 0;
        try {
            Collection<CountryDto> geoCountries = this.countrySnapshot.getAll();

            HashMap<String, CountryDto> countryMapByIata = new HashMap<String, CountryDto>();
            HashMap<Long, CountryDto> countryMapByOid = new HashMap<Long, CountryDto>();
            for (CountryDto countryDto : geoCountries) {
                countryCounter++;
                if (countryDto.getDescriptions() != null) {
                    descriptionsCounter += countryDto.getDescriptions().size();
                }
                countryMapByIata.put(countryDto.getCode(), countryDto);
                countryMapByOid.put(countryDto.getLongId(), countryDto);
            }
            this.snapshot.setCountriesByIata(countryMapByIata);
            this.snapshot.setCountriesByOid(countryMapByOid);
        } finally {
            logger.info("finish populating countries: countries={} , descriptions={} ", countryCounter, descriptionsCounter);
        }
    }

    private void populateCities(GeoService geoService) {
        logger.info("Getting cities from geo");
        long citiesCounter = 0;
        long picturesCounter = 0;
        long descriptionsCounter = 0;
        try {
            Collection<CityDto> geoCities = this.citySnapshot.getAll();

            HashMap<String, CityDto> citiesByIata = new HashMap<String, CityDto>();
            Map<Long, Collection<CityDto>> citiesByCountryOid = new HashMap<Long, Collection<CityDto>>();
            HashMap<Long, CityDto> citiesByOid = new HashMap<Long, CityDto>();

            int count = 0;
            Map<Long, PictureDto> geoPictures = new HashMap<Long, PictureDto>();

            for (CityDto cityDTO : geoCities) {
                citiesCounter++;
                if (logger.isDebugEnabled()) {
                    String iata = cityDTO != null ? cityDTO.getCode() : null;
                    String oid = cityDTO != null ? cityDTO.getId() : null;
                    logger.debug("city #{}, iata={}, oid={}", count, iata, oid);
                }
                for (PictureDto picture : cityDTO.getPictures()) {
                    picturesCounter++;
                    geoPictures.put(cityDTO.getLongId(), picture);
                }
                if (cityDTO.getDescriptions() != null) {
                    descriptionsCounter += cityDTO.getDescriptions().size();
                }
                citiesByIata.put(cityDTO.getCode(), cityDTO);
                citiesByOid.put(Long.valueOf(cityDTO.getId()), cityDTO);

                Collection<CityDto> countryCities = citiesByCountryOid.get(cityDTO.getCountry_id());
                if (countryCities == null) {
                    countryCities = new ArrayList<CityDto>();
                    citiesByCountryOid.put(cityDTO.getCountry_id(), countryCities);
                }
                countryCities.add(cityDTO);
            }
            this.snapshot.setCitiesByIata(citiesByIata);
            this.snapshot.setCitiesByCountryCode(citiesByCountryOid);
            this.snapshot.setCitiesByCityCode(citiesByOid);
            this.snapshot.setPicturesByCityOid(geoPictures);

            this.indexSearchRankingProperties();
            this.indexNearbyCitiesProperties(true);
            this.fillGeohashedCities(geoService);

        } catch (ConfigurationException e) {
            logger.error(e.getMessage(), e);
        } finally {
            logger.info("finish populating cities: cities={} , pictures={} , descriptions={} ", citiesCounter,
                picturesCounter, descriptionsCounter);
        }
    }

    private void populatePorts() {
        logger.info("Getting ports from geo");

        long portCounter = 0;
        long descriptionsCounter = 0;
        try {
            Collection<GeoPointDto> geoPorts = this.portSnapshot.getAll();
            HashMap<String, GeoPointDto> portsMapByIata = new HashMap<String, GeoPointDto>();
            for (GeoPointDto portDto : geoPorts) {
                portCounter++;
                if (portDto.getDescriptions() != null) {
                    descriptionsCounter += portDto.getDescriptions().size();
                }
                portsMapByIata.put(portDto.getCode(), portDto);
            }

            this.snapshot.setPortByIata(portsMapByIata);

        } finally {
            logger.info("finish populating ports: ports={} , descriptions={} ", portCounter, descriptionsCounter);
        }
    }

    public void fillGeohashedCities(final GeoService geoService) {
        SortedMap<String, List<CityDto>> updatedGeohashedCities = Maps.newTreeMap();
        for (CityDto dto : this.snapshot().getCitiesByIata().values()) {
            if (this.isCityLongLatValuesOk(dto)) {
                String geohash = GeoHash.withBitPrecision(dto.getLocation().getLatitude(), dto.getLocation().getLongitude(),
                    MAX_GEOHASH_PRECISION).toBinaryString();
                for (int i = geohash.length() - 1; i > MIN_GEOHASH_PRECISION; i--) {
                    String key = geohash.substring(0, i);
                    List<CityDto> cities = updatedGeohashedCities.get(key);
                    if (cities == null) {
                        cities = Lists.newArrayList();
                        updatedGeohashedCities.put(key, cities);
                    }
                    cities.add(dto);
                }
            }
        }
        SortedMap<String, List<CityDto>> unmodifiableSortedMap = Collections.unmodifiableSortedMap(updatedGeohashedCities);
        this.setGeohashedCities(unmodifiableSortedMap);

        this.closestCityCache = CacheBuilder.newBuilder().recordStats().maximumSize(10000)
            .build(new CacheLoader<LocationDto, GeoCity[]>() {
                @Override
                public GeoCity[] load(LocationDto key) {

                    // use of array to handle nulls
                    return new GeoCity[] {geoService.getCityInfoGeoLocation(key)};
                }
            });
    }

    public void fillGeohashedAirports() {
        SortedMap<String, List<GeoPointDto>> updatedGeohashedAirports = Maps.newTreeMap();

        for (GeoPointDto dto : this.snapshot().getAirportsByIata().values()) {
            if (dto.getLocation() != null && dto.getLocation().getLatitude() != null
                && dto.getLocation().getLongitude() != null && dto.isCommercial()) {
                String geohash = GeoHash.withBitPrecision(dto.getLocation().getLatitude(), dto.getLocation().getLongitude(),
                    MAX_GEOHASH_PRECISION).toBinaryString();
                for (int i = geohash.length() - 1; i > MIN_GEOHASH_PRECISION; i--) {
                    String key = geohash.substring(0, i);
                    List<GeoPointDto> airports = updatedGeohashedAirports.get(key);
                    if (airports == null) {
                        airports = Lists.newArrayList();
                        updatedGeohashedAirports.put(key, airports);
                    }
                    airports.add(dto);
                }
            }
        }

        SortedMap<String, List<GeoPointDto>> unmodifiableSortedMap = Collections
            .unmodifiableSortedMap(updatedGeohashedAirports);

        this.setGeohashedAirports(unmodifiableSortedMap);
    }


    @SuppressWarnings("unchecked")
    private void indexCruiseIataRankingProperties() throws ConfigurationException {
        logger.info("Indexing cruise iata ranking");
        this.cruiseIataProperties = new PropertiesConfiguration("conf/app/cruise-country-region-iata.properties");

        ListMultimap<CountryRegion, String> updatedCruiseIataRanking = ArrayListMultimap.create();

        Iterator<String> it = this.cruiseIataProperties.getKeys();

        while (it.hasNext()) {
            String countryRegion = it.next();

            List<String> values = this.cruiseIataProperties.getList(countryRegion);

            String[] countryRegionArr = StringUtils.split(countryRegion, ',');

            CountryRegion cr = new CountryRegion(countryRegionArr[0], countryRegionArr[1]);

            for (String iata : values) {
                updatedCruiseIataRanking.put(cr, iata);
            }
        }

        this.cruiseIataRanking = Multimaps.unmodifiableListMultimap(updatedCruiseIataRanking);

        logger.debug("Cruise iata ranking: {}", this.cruiseIataRanking);
    }

    @VisibleForTesting
    public void indexNearbyCitiesProperties(boolean logMissingCities) throws ConfigurationException {
        this.nearbyCitiesServiceProperties = new PropertiesConfiguration("conf/app/destination.properties");
        this.indexNearbyCities(logMissingCities);
    }

    @VisibleForTesting
    void indexSearchRankingProperties() throws ConfigurationException {
        this.searchRankingServiceProperties = new PropertiesConfiguration("conf/app/hot-ranking.properties");
        this.indexMostImportantCities();
        this.indexWhitelistedCities();
    }

    @SuppressWarnings("unchecked")
    private void indexNearbyCities(boolean logMissingCities) {
        logger.info("Indexing nearby cities list");
        Map<Product, Map<Long, List<CityDto>>> updatedNearbyCitiesByProduct = new EnumMap<Product, Map<Long, List<CityDto>>>(
            Product.class);

        // list of keys, one key for each product
        Iterator<String> keys = this.nearbyCitiesServiceProperties.getKeys("destination.nearbyCities.whitelist");
        while (keys.hasNext()) {
            String key = keys.next();

            String productStr = key.substring(key.lastIndexOf('.') + 1);
            Product product = Product.fromString(productStr);

            List<String> cities = this.nearbyCitiesServiceProperties.getList(key);

            for (String city : cities) {
                CityDto cityDto = this.snapshot().getCitiesByIata().get(city);
                if (cityDto == null) {
                    if (logMissingCities) {
                        logger.warn("City {} from nearby city whiteslist, (product: {}) not found in snapshot.", city,
                            product);
                    }
                } else {
                    long countryOid = cityDto.getCountry_id();

                    if (!updatedNearbyCitiesByProduct.containsKey(product)) {
                        updatedNearbyCitiesByProduct.put(product, new HashMap<Long, List<CityDto>>());
                    }

                    if (!updatedNearbyCitiesByProduct.get(product).containsKey(countryOid)) {
                        updatedNearbyCitiesByProduct.get(product).put(countryOid, new ArrayList<CityDto>());
                    }
                    updatedNearbyCitiesByProduct.get(product).get(countryOid).add(cityDto);
                }
            }
        }

        this.nearbyCitiesByProduct = Collections.unmodifiableMap(updatedNearbyCitiesByProduct);
    }

    @SuppressWarnings("unchecked")
    private void indexWhitelistedCities() {
        logger.info("Indexing whitelisted cities");
        Map<Long, List<CityDto>> updatedWhitelistedCitiesByCountry = Maps.newHashMap();
        List<CityDto> updatedWhitelistedCities = Lists.newArrayList();
        this.whitelistedCitiesIatas = this.searchRankingServiceProperties.getList("closestcity.iata.whitelist");

        logger.info("Whitelist cities size: {}", this.whitelistedCitiesIatas.size());
        for (String city : this.whitelistedCitiesIatas) {
            CityDto whiteCity = this.snapshot().getCitiesByIata().get(city);

            if (whiteCity == null) {
                logger.warn("Iata {} from city whitelist not found in geosnapshot", city);
            } else {
                updatedWhitelistedCities.add(whiteCity);
                List<CityDto> countryCities = updatedWhitelistedCitiesByCountry.get(whiteCity.getCountry_id());
                if (countryCities == null) {
                    countryCities = new ArrayList<CityDto>();
                    updatedWhitelistedCitiesByCountry.put(whiteCity.getCountry_id(), countryCities);
                }
                countryCities.add(whiteCity);
            }
        }

        this.whitelistedCitiesByCountry = Collections.unmodifiableMap(updatedWhitelistedCitiesByCountry);
        this.whitelistedCities = Collections.unmodifiableList(updatedWhitelistedCities);
    }

    @SuppressWarnings("unchecked")
    private void indexMostImportantCities() {
        logger.info("Indexing main cities list");
        Map<String, List<CityDto>> updatedMainCitiesByCountry = new HashMap<String, List<CityDto>>();
        Iterator<String> keys = this.searchRankingServiceProperties.getKeys("hotRanking.mostImportantCities");
        while (keys.hasNext()) {
            String key = keys.next();
            String cityName = key.substring(key.lastIndexOf('.') + 1);
            List<String> cities = this.searchRankingServiceProperties.getList(key);
            List<CityDto> citiesForCountry = new ArrayList<CityDto>();
            for (String city : cities) {
                CityDto cityDto = this.snapshot().getCitiesByIata().get(city);
                if (cityDto == null) {
                    logger.warn("Iata {} from most important cities not found in geosnapshot", city);
                } else {
                    citiesForCountry.add(cityDto);
                }
            }
            updatedMainCitiesByCountry.put(cityName, citiesForCountry);
        }
        this.mainCitiesByCountry = Collections.unmodifiableMap(updatedMainCitiesByCountry);
    }

    private boolean isCityLongLatValuesOk(CityDto dto) {

        LocationDto location = dto.getLocation();
        if (location == null) {
            return false;
        }

        boolean validValues = GeoUtils.isLongLatValuesOk(location.getLatitude(), location.getLongitude());
        if (!validValues) {
            Log.error("City " + dto.toString() + "has wrong latitude/longitude values");
        }
        return validValues;
    }

    public void setGeohashedCities(SortedMap<String, List<CityDto>> geohashedCities) {
        this.geohashedCities = geohashedCities;
    }

    public void setGeohashedAirports(SortedMap<String, List<GeoPointDto>> geohashedAirports) {
        this.geohashedAirports = geohashedAirports;
    }

    public GeoSnapshot snapshot() {
        return this.snapshot;
    }

    public LoadingCache<LocationDto, GeoCity[]> closestCityCache() {
        return this.closestCityCache;
    }

    public Map<Product, Map<Long, List<CityDto>>> nearbyCitiesByProduct() {
        return this.nearbyCitiesByProduct;
    }

    public ListMultimap<CountryRegion, String> cruiseIataRanking() {
        return this.cruiseIataRanking;
    }

    public SortedMap<String, List<GeoPointDto>> geohashedAirports() {
        return this.geohashedAirports;
    }

    public SortedMap<String, List<CityDto>> geohashedCities() {
        return this.geohashedCities;
    }

    public List<CityDto> whitelistedCities() {
        return this.whitelistedCities;
    }

    public List<String> whitelistedCitiesIatas() {
        return this.whitelistedCitiesIatas;
    }

    public Map<Long, List<CityDto>> whitelistedCitiesByCountry() {
        return this.whitelistedCitiesByCountry;
    }

    public Map<String, List<CityDto>> mainCitiesByCountry() {
        return this.mainCitiesByCountry;
    }

    @VisibleForTesting
    void setWhitelistedCities(List<CityDto> whitelist) {
        this.whitelistedCities = whitelist;
    }

    @VisibleForTesting
    void setWhitelistedCitiesByCountry(Map<Long, List<CityDto>> mapWhitelist) {
        this.whitelistedCitiesByCountry = mapWhitelist;
    }

    @VisibleForTesting
    void setClosestCityCache(LoadingCache<LocationDto, GeoCity[]> cache) {
        this.closestCityCache = cache;
    }

    @VisibleForTesting
    public void setGeoSnapshot(GeoSnapshot geoSnapshot) {
        this.snapshot = geoSnapshot;
    }

    public void setSearchRankingServiceProperties(PropertiesConfiguration searchRankingServiceProperties) {
        this.searchRankingServiceProperties = searchRankingServiceProperties;
    }

    public void setNearbyCitiesServiceProperties(PropertiesConfiguration nearbyDestinationsServiceProperties) {
        this.nearbyCitiesServiceProperties = nearbyDestinationsServiceProperties;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map<String, Cache> getCachesForReporting() {
        return ImmutableMap.of("closestCity", (Cache) this.closestCityCache);
    }

}
