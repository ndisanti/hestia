package com.despegar.p13n.hestia.external.geo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.queries.GeoHashCircleQuery;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.external.geo.GeoSpot.Spot;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.newrelic.api.agent.Trace;

@Service
public class GeoServiceImpl
    implements GeoService {

    private static final Logger logger = LoggerFactory.getLogger(GeoServiceImpl.class);
    static final String DEFAULT_LANGUAGE_CODE = "es";
    private static final int ONE_KILOMETER = 1000;
    private static final int MAX_RADIUS = 300 * ONE_KILOMETER;
    private static final int MAX_NEARBY_CITIES_RADIUS = 800 * ONE_KILOMETER;
    private static final int MAX_AIRPORT_RADIUS = 500 * ONE_KILOMETER;
    // Pictures
    public static final String GEO_PICTURE_PREFIX = "media.staticontent.com/media/pictures/";
    public static final int GEO_PICTURE_PREFIX_LENGTH = GEO_PICTURE_PREFIX.length();
    public static final String GEO_PICTURE_HTTPS_PREFIX = "https://a248.e.akamai.net/www.despegar.com/media/pictures/";

    @Autowired
    private GeoSnapshotsService geoSnapshotsService;

    @PostConstruct
    public void initSnapshots() throws Exception {
        this.geoSnapshotsService.initSnapshots(this);
    }

    @PreDestroy
    public void shutdownSnapshots() throws Exception {
        this.geoSnapshotsService.shutdownSnapshots();
    }

    @Override
    public GeoCountry getCountryInfo(String countryCode) {
        CountryDto dto = this.snapshot().getCountriesByIata().get(countryCode);
        if (dto == null) {
            return null;
        }
        return new GeoCountry(dto);
    }

    @Override
    public GeoCountry getCountryInfo(long countryOid) {
        CountryDto dto = this.snapshot().getCountriesByOid().get(countryOid);
        if (dto == null) {
            return null;
        }
        return new GeoCountry(dto);
    }

    @Override
    public String getCountryFromIata(String iata) {
        GeoCity city = this.getCityInfo(iata);

        if (city == null) {
            return null;
        }
        CountryDto dto = this.snapshot().getCountriesByOid().get(city.getCountryOid());

        if (dto == null) {
            return null;
        }
        return dto.getCode();
    }

    public CountryCode getCountryCodeFromCity(CityDto dto) {

        if (dto == null) {
            return null;
        }

        GeoCountry geoCountry = this.getCountryInfo(dto.getCountry_id());
        if (geoCountry == null) {
            return null;
        }

        return CountryCode.fromString(geoCountry.getIataCode());
    }

    @Override
    public GeoCity getCityInfo(String iata) {

        iata = this.normalizeIata(iata);
        CityDto city = this.snapshot().getCitiesByIata().get(iata);
        if (city == null) {
            return null;
        }
        LocationDto location = city.getLocation();
        if (location == null) {
            return null;
        }

        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        if (latitude == null || longitude == null) {
            return null;
        }
        return this.buildGeoCityFromDTO(city);
    }

    @Override
    public GeoCity getCityInfo(Long cityOid) {
        CityDto city = this.snapshot().getCitiesByCityCode().get(cityOid);
        if (city == null) {
            return null;
        }
        return this.buildGeoCityFromDTO(city);
    }

    @Override
    public boolean iataExists(String iata) {
        CityDto city = this.snapshot().getCitiesByIata().get(iata);
        return (city != null);
    }

    @Override
    @Trace
    public GeoCity getClosestCity(LocationDto geoLocation) {

        try {
            return this.geoSnapshotsService.closestCityCache().get(geoLocation)[0];
        } catch (ExecutionException e) {
            return null;
        }
    }

    @Trace
    public GeoCity getCityInfoGeoLocation(LocationDto geoLocation) {

        double latitude = geoLocation.getLatitude();
        double longitude = geoLocation.getLongitude();

        Collection<CityDto> closestCitiesByGeoHash = this.closestCities(latitude, longitude, MAX_RADIUS);

        Iterator<CityDto> it = closestCitiesByGeoHash.iterator();

        // removing not supported countries
        while (it.hasNext()) {
            CityDto dto = it.next();

            if (this.getCountryCodeFromCity(dto) == null) {
                it.remove();
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Closest cities by hash{}", this.getIataCodes(closestCitiesByGeoHash));
        }

        CityDto closestCity = (CityDto) this.findClosestGeoPoint(latitude, longitude, closestCitiesByGeoHash);

        if (closestCity == null) {
            return null;
        }

        GeoCity result = GeoServiceImpl.buildGeoCityFromDTO(closestCity, this.getCountryCodeFromCity(closestCity));

        logger.debug("Closest city to ({}, {}) is {}", latitude, longitude, result.getIataCode());
        return result;
    }

    @Override
    @Trace
    public GeoCity findClosestWhitelistedCity(GeoCity geoCity) {
        List<CityDto> countryWhitelistedCities = this.whitelistedCitiesByCountry().get(geoCity.getCountryOid());
        Double latitude = geoCity.getLocation().getLatitude();
        Double longitude = geoCity.getLocation().getLongitude();
        CityDto closestCity = (CityDto) this.findClosestGeoPoint(latitude, longitude, countryWhitelistedCities);

        // if city is not found within the country, we search through all whitelisted cities
        if (closestCity == null) {
            closestCity = (CityDto) this.findClosestGeoPoint(latitude, longitude, this.whitelistedCities());
        }

        logger.debug("Closest whitelisted city to ({}, {}) is {}", latitude, longitude, closestCity.getCode());
        return this.buildGeoCityFromDTO(closestCity);
    }

    @Override
    @Trace
    public GeoCity findClosestRadiusWhitelistedCity(final GeoCity geoCity, int kilometersRadius) {
        Preconditions.checkArgument(kilometersRadius >= 0, "Radius to find closest whitelisted cities can not be negative");

        if (kilometersRadius == 0) {
            return null;
        }
        GeoLocation location = geoCity.getLocation();
        if (location.getLatitude() == null || location.getLongitude() == null) {
            logger.error("Longitude and Langitude can not be null to calculate the closest cities to the given city");
            return null;
        }

        Predicate<CityDto> notSameCity = new Predicate<CityDto>() {
            @Override
            public boolean apply(CityDto city) {
                return !city.getCode().equalsIgnoreCase(geoCity.getIataCode());
            }
        };

        List<CityDto> whitelist = null;
        List<CityDto> closestCities = null;
        Double latitude = geoCity.getLocation().getLatitude();
        Double longitude = geoCity.getLocation().getLongitude();

        List<CityDto> countryWhitelistedCities = this.whitelistedCitiesByCountry().get(geoCity.getCountryOid());
        if (countryWhitelistedCities != null) {
            whitelist = FluentIterable.from(countryWhitelistedCities).filter(notSameCity).toList();
            closestCities = this.closestCitiesByHaversine(latitude.doubleValue(), longitude.doubleValue(), kilometersRadius,
                whitelist);
        }

        // if city is not found within the country, we search through all whitelisted cities
        if (closestCities == null || closestCities.isEmpty()) {
            whitelist = FluentIterable.from(this.whitelistedCities()).filter(notSameCity).toList();
            closestCities = this.closestCitiesByHaversine(latitude.doubleValue(), longitude.doubleValue(), kilometersRadius,
                whitelist);
        }

        if (closestCities == null || closestCities.isEmpty()) {
            return null;
        }
        CityDto closestCity = FluentIterable.from(closestCities).first().get();
        logger.debug("Closest whitelisted city to ({}, {}) is {}", latitude, longitude, closestCity.getCode());
        return this.buildGeoCityFromDTO(closestCity);
    }

    public boolean areNearbyCities(String firstCityIata, String secondCityIata, int kmRadius) {
        GeoCity firstCity = this.getCityInfo(firstCityIata);
        GeoCity secondCity = this.getCityInfo(secondCityIata);
        GeoPoint firstPoint = new GeoPoint(firstCity.getLocation().getLatitude(), firstCity.getLocation().getLongitude());
        GeoPoint secondPoint = new GeoPoint(secondCity.getLocation().getLatitude(), secondCity.getLocation().getLongitude());
        return GeoUtils.areNearbyPoints(firstPoint, secondPoint, kmRadius * ONE_KILOMETER);
    }

    @Override
    @Trace
    public GeoCity getMainCountryCity(GeoCity city) {
        GeoCountry country = this.getCountryInfo(city.getCountryOid());
        String iataCode = country.getIataCode();
        List<CityDto> mainCitiesForCountry = this.mainCitiesByCountry().get(iataCode);
        if (mainCitiesForCountry != null) {
            CityDto mainCity = (CityDto) this.findClosestGeoPoint(city.getLocation().getLatitude(), city.getLocation()
                .getLongitude(), mainCitiesForCountry);
            return this.buildGeoCityFromDTO(mainCity);
        } else {
            logger.error("Main cities configuration not found for city {}, country {}", city, iataCode);
            return null;
        }
    }

    @Override
    public List<String> getCitiesForCountry(String countryCode) {
        CountryDto country = this.snapshot().getCountriesByIata().get(countryCode);
        if (country != null) {
            Collection<CityDto> cities = this.snapshot().getCitiesByCountryCode().get(country.getLongId());
            List<String> result = Lists.newArrayList();
            for (CityDto dto : cities) {
                result.add(dto.getCode());
            }
            return result;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<String> getNearbyCitiesByGeoHashing(Product product, String cityIata, int nTop) {
        CityDto city = this.snapshot().getCitiesByIata().get(cityIata);
        if (city == null) {
            logger.error("City not found {}", cityIata);
            return Collections.emptyList();
        }
        Map<Long, List<CityDto>> relevantCities = this.nearbyCitiesByProduct().get(product);
        if (relevantCities == null) {
            logger.error("Nearby cities not supported for product " + product);
            return Collections.emptyList();
        }
        List<String> countryCities = this.getIataCodes(relevantCities.get(city.getCountry_id()));
        countryCities.remove(cityIata);
        int radius = 200 * ONE_KILOMETER;
        Set<CityDto> cities = null;
        do {
            cities = this.closestCities(city.getLocation().getLatitude(), city.getLocation().getLongitude(), radius,
                countryCities);
            radius *= 2;
        } while (cities.size() < nTop && radius < MAX_NEARBY_CITIES_RADIUS);
        return this.getIataCodes((List<CityDto>) this.orderClosestGeoPointsByHaversine(city, cities, nTop));
    }

    @Override
    public List<String> getNearbyCities(Product product, String cityIata, int nTop) {
        return this.getNearbyCitiesByGeoHashing(product, cityIata, nTop);
    }

    @Override
    public List<String> getNearbyProductCitiesByHaversine(Product product, String cityIata, int nTop, int maxKmRadius) {
        Preconditions.checkArgument(nTop > 0, "nTop must be positive");
        CityDto city = this.snapshot().getCitiesByIata().get(cityIata);
        if (city == null) {
            logger.error("City not found {}", cityIata);
            return Collections.emptyList();
        }
        double latitude = city.getLocation().getLatitude();
        double longitude = city.getLocation().getLongitude();

        Function<String, CityDto> fromIataToCityDto = new Function<String, CityDto>() {
            @Override
            public CityDto apply(String cityIata) {
                return GeoServiceImpl.this.snapshot().getCitiesByIata().get(cityIata);
            }
        };
        Function<CityDto, String> fromCityDtoToIata = new Function<CityDto, String>() {
            @Override
            public String apply(CityDto city) {
                return city.getCode();
            }
        };

        Map<Long, List<CityDto>> relevantCities = this.nearbyCitiesByProduct().get(product);
        if (relevantCities == null) {
            logger.error("Nearby cities not supported for product " + product);
            return Collections.emptyList();
        }
        List<String> countryCityIatas = this.getIataCodes(relevantCities.get(city.getCountry_id()));
        countryCityIatas.remove(cityIata); // to avoid considering the same city
        List<CityDto> countryCities = FluentIterable.from(countryCityIatas).transform(fromIataToCityDto).toList();
        List<CityDto> closestCities = this.closestCitiesByHaversine(latitude, longitude, maxKmRadius, countryCities);
        return FluentIterable.from(closestCities).limit(nTop).transform(fromCityDtoToIata).toList();
    }

    @Override
    public CityDto closestCity(double latitude, double longitude) {
        return this.closestCityForWhitelist(latitude, longitude, null);
    }

    @Override
    public CityDto closestWhitelistedCity(double lat, double lon) {
        return this.closestCityForWhitelist(lat, lon, this.whitelistedCitiesIatas());
    }

    private CityDto closestCityForWhitelist(double latitude, double longitude, List<String> citiesWhitelist) {
        CityDto result = null;
        int _50K = 50 * ONE_KILOMETER;
        int radius = _50K;
        while (result == null && radius < MAX_RADIUS) {
            Set<CityDto> cities = this.closestCities(latitude, longitude, radius, citiesWhitelist);
            Set<GeoSpot> spots = new HashSet<GeoSpot>();
            spots.addAll(cities);
            if (!cities.isEmpty()) {
                result = (CityDto) this.findClosestGeoPoint(latitude, longitude, spots);
            } else {
                radius = radius + _50K;
            }
        }
        return result;
    }

    @Override
    public GeoPointDto closestAirport(double lat, double lon) {
        GeoPointDto result = null;
        int _100K = 100 * ONE_KILOMETER;
        int radius = _100K;
        while (result == null && radius < MAX_AIRPORT_RADIUS) {
            Set<GeoPointDto> aiports = this.closestAirports(lat, lon, radius);
            if (!aiports.isEmpty()) {
                result = (GeoPointDto) this.findClosestGeoPoint(lat, lon, aiports);
            } else {
                radius += _100K;
            }
        }
        return result;
    }

    private Set<GeoPointDto> closestAirports(double lat, double lon, int radius) {
        WGS84Point point = new WGS84Point(lat, lon);
        GeoHashCircleQuery query = new GeoHashCircleQuery(point, radius);
        Set<GeoPointDto> result = Sets.newHashSet();
        for (GeoHash hash : query.getSearchHashes()) {
            List<GeoPointDto> matchedAirports = this.geohashedAirports().get(hash.toBinaryString());
            if (matchedAirports != null) {
                for (GeoPointDto city : matchedAirports) {
                    if (query.contains(GeoUtils.toWGS84Point(city))) {
                        result.add(city);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Finds the cities that are within the area defined by the given latitude-longitude and the given radius
     * @param latitude
     * @param longitude
     * @param kilometersRadius radius to consider if a city is close (in kilometers)
     * @param whitelistedCities the filtered cities
     * @return the cities that satisfy the radius restriction, ordered by distance
     */
    @SuppressWarnings("unchecked")
    public List<CityDto> closestCitiesByHaversine(double latitude, double longitude, final int kilometersRadius,
        List<CityDto> whitelistedCities) {
        Preconditions.checkArgument(kilometersRadius > 0, "kilometersRadius must be positive");
        if (whitelistedCities == null || whitelistedCities.isEmpty()) {
            return Collections.emptyList();
        }

        final GeoPoint firstPoint = new GeoPoint(latitude, longitude);
        final Map<String, Integer> distances = new HashMap<String, Integer>();

        List<CityDto> nearbyCities = FluentIterable.from(whitelistedCities).filter(new Predicate<CityDto>() {

            @Override
            public boolean apply(CityDto cityDto) {
                LocationDto location = cityDto.getLocation();
                if (!GeoUtils.isLongLatValuesOk(location.getLatitude(), location.getLongitude())) {
                    return false;
                }
                GeoPoint secondPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                Integer distance = GeoServiceImpl.this.haverineDistanceBetweenGeoPoints(firstPoint, secondPoint);
                distances.put(cityDto.getCode(), distance);
                return distance <= kilometersRadius * ONE_KILOMETER;
            }
        }).toList();

        return (List<CityDto>) this.orderByDistanceMap(nearbyCities, distances);

    }

    /**
     * Find closest city to the given latitude/longitude
     *
     * @param latitude the latitude
     * @param longitude the longitude
     * @param filteredSpots the filtered GeoSpot to evaluate
     * @return the closest GeoSpot
     */
    @Trace
    private GeoSpot findClosestGeoPoint(double latitude, double longitude, Collection<? extends GeoSpot> filteredSpots) {
        GeoSpot closestPoint = null;

        if (filteredSpots != null && !filteredSpots.isEmpty()) {
            List<?> closestSpots = this.orderClosestGeoPointsByHaversine(latitude, longitude, filteredSpots);
            if (!closestSpots.isEmpty()) {
                closestPoint = (GeoSpot) closestSpots.get(0);
            }
        }
        return closestPoint;
    }

    /**
     * Orders the given collection of cities by its distance to the given city.
     * @param geoPoint city to evaluate the distances from
     * @param geoPoints if contains a city that has an invalid location, it is not included in the returned list
     * @param nPoints size of the List to return
     * @return a new List with the ordered cities
     */
    List<?> orderClosestGeoPointsByHaversine(GeoSpot geoPoint, Collection<? extends GeoSpot> geoPoints, int nPoints) {
        List<?> orderedCities = this.orderClosestGeoPointsByHaversine(geoPoint.getLocation().getLatitude(), geoPoint
            .getLocation().getLongitude(), geoPoints);
        return FluentIterable.from(orderedCities).limit(nPoints).toList();
    };

    List<?> orderClosestGeoPointsByHaversine(Double latitude, Double longitude, Collection<? extends GeoSpot> points) {
        if (points == null || points.isEmpty()) {
            return Collections.emptyList();
        }

        final GeoPoint firstPoint = new GeoPoint(latitude, longitude);
        final Map<String, Integer> distances = new HashMap<String, Integer>();
        List<GeoSpot> usefulPoints = new ArrayList<GeoSpot>();

        for (GeoSpot point : points) {
            LocationDto location = point.getLocation();
            if (GeoUtils.isLongLatValuesOk(location.getLatitude(), location.getLongitude())) {
                GeoPoint secondPoint = new GeoPoint(point.getLocation().getLatitude(), point.getLocation().getLongitude());
                Integer distance = this.haverineDistanceBetweenGeoPoints(firstPoint, secondPoint);
                distances.put(point.getCode(), distance);
                usefulPoints.add(point);
            }
        }
        return this.orderByDistanceMap(usefulPoints, distances);
    };

    private Integer haverineDistanceBetweenGeoPoints(GeoPoint firstPoint, GeoPoint secondPoint) {
        double distance = GeoUtils.haversineDistanceBetweenTwoPoints(firstPoint, secondPoint);
        // There is no sense in evaluating less than a kilometer to compare geo point distances
        return new BigDecimal(distance).setScale(-3, RoundingMode.HALF_UP).intValue();
    }

    private List<?> orderByDistanceMap(Collection<? extends GeoSpot> geoPoints, final Map<String, Integer> distancesMap) {
        return FluentIterable.from(geoPoints).toSortedList(new Comparator<GeoSpot>() {

            @Override
            public int compare(GeoSpot geoPoint1, GeoSpot geoPoint) {
                Integer difference = (distancesMap.get(geoPoint1.getCode()) - distancesMap.get(geoPoint.getCode()));
                return difference.intValue();
            }
        });
    }

    @Override
    public Set<CityDto> closestCities(double latitude, double longitude, int radius) {
        return this.closestCities(latitude, longitude, radius, null);
    }

    @Trace
    private Set<CityDto> closestCities(double latitude, double longitude, int radius, List<String> cityWhitelist) {

        WGS84Point point = new WGS84Point(latitude, longitude);
        GeoHashCircleQuery query = new GeoHashCircleQuery(point, radius);

        Set<CityDto> result = Sets.newHashSet();
        for (GeoHash hash : query.getSearchHashes()) {
            List<CityDto> matchedCities = this.geohashedCities().get(hash.toBinaryString());
            if (matchedCities != null) {
                for (CityDto city : matchedCities) {
                    if (cityWhitelist == null || (cityWhitelist != null && cityWhitelist.contains(city.getCode()))) {
                        if (query.contains(GeoUtils.toWGS84Point(city))) {
                            result.add(city);
                        }
                    }
                }
            }
        }
        return result;
    }

    private String getCityCode(Long cityCode) {
        if (this.snapshot().getCitiesByCityCode().containsKey(cityCode)) {
            CityDto cityDTO = this.snapshot().getCitiesByCityCode().get(cityCode);
            return cityDTO.getCode();
        } else {
            return null;
        }
    }

    private Long getCityCountry(String iata) {
        if (this.snapshot().getCitiesByIata().containsKey(iata)) {
            CityDto cityDTO = this.snapshot().getCitiesByIata().get(iata);
            return cityDTO.getCountry_id();
        } else {
            return null;
        }
    }

    /*
     * Converts iata to a city iata
     */
    @Override
    public String normalizeIata(String iata) {
        String result = null;
        if (StringUtils.isNotBlank(iata)) {
            String upIata = iata.toUpperCase();
            result = upIata;
            if (this.snapshot().getAirportsByIata().containsKey(upIata)) {
                logger.trace("{} is an airport. Changing iata to corresponding city", upIata);
                Long cityOid = Long.parseLong(this.snapshot().getAirportsByIata().get(upIata).getCity_id());
                String cityCode = this.getCityCode(cityOid);
                result = cityCode == null ? upIata : cityCode;
            } else if (this.snapshot().getPortsByIata().containsKey(upIata)) {
                logger.trace("{} is a port. Changing iata to corresponding city", upIata);
                Long cityOid = Long.parseLong(this.snapshot().getPortsByIata().get(upIata).getCity_id());
                String cityCode = this.getCityCode(cityOid);
                result = cityCode == null ? upIata : cityCode;
            } else if (this.snapshot().getCitiesByIata().containsKey(upIata)) {
                logger.trace("{} is a city", upIata);
            } else {
                logger.trace("{} is not a city neither an airport. Normalization could not be done", upIata);
            }
            // fix cases where normalized city is in a different country than the original city
            // this might happen if a port/airport has the same IATA as a city
            if (!upIata.equalsIgnoreCase(result)) {
                Long originalCountry = this.getCityCountry(upIata);
                Long normalizedCountry = this.getCityCountry(result);
                if (originalCountry != null && normalizedCountry != null && !originalCountry.equals(normalizedCountry)) {
                    logger.trace("{} should not be normalized as normalized city is in a different country", upIata);
                    result = upIata;
                }
            }
        }
        return result;
    }

    /*
     * Converts iata to a city with airport (from whitelist)
     */
    @Override
    public String normalizeToCityAirport(String iata) {
        iata = this.normalizeIata(iata);

        if (!this.whitelistedCitiesIatas().contains(iata)) {
            CityDto cityDTO = this.snapshot().getCitiesByIata().get(iata);

            if (cityDTO == null) {
                return iata;
            }

            CityDto cityWithAprt = this.closestWhitelistedCity(cityDTO.getLocation().getLatitude(), cityDTO.getLocation()
                .getLongitude());

            if (cityWithAprt == null) { // no closest city with airport
                return iata;
            } else {
                return cityWithAprt.getCode();
            }
        }

        return iata;
    }

    @Override
    public CityDto getMainCityFromCountry(CountryCode countryCode) {

        List<CityDto> citiesDTO = this.mainCitiesByCountry().get(countryCode.toString());

        if (citiesDTO == null || citiesDTO.isEmpty()) {
            return null;
        } else {
            return citiesDTO.get(0);
        }
    }

    @Override
    public String getIataName(String iataCode, String langCode) {

        Preconditions.checkNotNull(iataCode);
        Preconditions.checkNotNull(langCode);

        iataCode = iataCode.toUpperCase();
        Map<String, String> descriptions = null;
        GeoPointDto simplifiedAirportDTO = null;

        CityDto city = this.snapshot().getCitiesByIata().get(iataCode);

        if (city != null) {
            descriptions = city.getDescriptions();
        } else {
            simplifiedAirportDTO = this.snapshot().getAirportsByIata().get(iataCode);
            if (simplifiedAirportDTO != null) {
                descriptions = simplifiedAirportDTO.getDescriptions();
            }
        }

        if (city == null && simplifiedAirportDTO == null) {
            logger.error("Unable to find iata {}.", iataCode);
            return iataCode;
        }

        if (descriptions == null) {
            logger.error("Unable to find iata {} description.", iataCode);
            return iataCode;
        }

        String name = descriptions.get(langCode.toLowerCase());

        if (name == null) {
            logger.error("Unable to find name for iata {} in language {}.", iataCode, langCode);
            name = descriptions.get(DEFAULT_LANGUAGE_CODE);
            return name != null ? name : iataCode;
        }

        return name;
    }

    private List<String> getIataCodes(Collection<CityDto> cities) {
        List<String> iatas = Lists.newArrayList();
        for (CityDto city : cities) {
            iatas.add(city.getCode());
        }
        return iatas;
    }

    private GeoCity buildGeoCityFromDTO(CityDto city) {
        GeoLocation location = new GeoLocation(city.getLocation().getLatitude(), city.getLocation().getLongitude(), city
            .getDescriptions().get(DEFAULT_LANGUAGE_CODE));
        return new GeoCity(Long.parseLong(city.getId()), city.getCode(), city.getCountry_id(),
            city.getAdministrative_division_id(), location, null);
    }

    private static GeoCity buildGeoCityFromDTO(CityDto city, CountryCode cc) {
        GeoLocation location = new GeoLocation(city.getLocation().getLatitude(), city.getLocation().getLongitude(), city
            .getDescriptions().get(DEFAULT_LANGUAGE_CODE));
        return new GeoCity(Long.parseLong(city.getId()), city.getCode(), city.getCountry_id(),
            city.getAdministrative_division_id(), location, cc);
    }

    public static String buildHttpsUrl(String originalUrl) {
        StringBuilder url = new StringBuilder(100);
        if (originalUrl.startsWith(GeoServiceImpl.GEO_PICTURE_PREFIX)) {
            url.append(GeoServiceImpl.GEO_PICTURE_HTTPS_PREFIX);
            url.append(originalUrl.substring(GeoServiceImpl.GEO_PICTURE_PREFIX_LENGTH));
            return url.toString();
        } else {
            return originalUrl;
        }
    }

    @VisibleForTesting
    public GeoSnapshot snapshot() {
        return this.geoSnapshotsService.snapshot();
    }

    private Map<Product, Map<Long, List<CityDto>>> nearbyCitiesByProduct() {
        return this.geoSnapshotsService.nearbyCitiesByProduct();
    }

    private ListMultimap<CountryRegion, String> cruiseIataRanking() {
        return this.geoSnapshotsService.cruiseIataRanking();
    }

    @VisibleForTesting
    SortedMap<String, List<GeoPointDto>> geohashedAirports() {
        return this.geoSnapshotsService.geohashedAirports();
    }

    private SortedMap<String, List<CityDto>> geohashedCities() {
        return this.geoSnapshotsService.geohashedCities();
    }

    private List<CityDto> whitelistedCities() {
        return this.geoSnapshotsService.whitelistedCities();
    }

    private List<String> whitelistedCitiesIatas() {
        return this.geoSnapshotsService.whitelistedCitiesIatas();
    }

    private Map<Long, List<CityDto>> whitelistedCitiesByCountry() {
        return this.geoSnapshotsService.whitelistedCitiesByCountry();
    }

    private Map<String, List<CityDto>> mainCitiesByCountry() {
        return this.geoSnapshotsService.mainCitiesByCountry();
    }

    @Override
    public List<String> getIatasByRegion(CountryCode country, String region) {
        return this.cruiseIataRanking().get(new CountryRegion(country.toString(), region));
    }

    public Map<String, CityDto> getAllCitesByIata() {
        return this.snapshot().getCitiesByIata();
    }

//    @Override
//    public LocationDto getGeoLocationFromIp(InetAddress ipAddress, String source) {
//        return this.geoLocalizationService.getGeolocation(ipAddress, source);
//    }

    @Override
    public String getClosestAirport(String iata) {
        iata = iata.toUpperCase();
        if (this.snapshot().getAirportsByIata().containsKey(iata)) {
            return iata;
        }
        String cityIata = this.normalizeIata(iata);
        Map<String, CityDto> citiesByIata = this.getAllCitesByIata();

        if (citiesByIata.containsKey(cityIata)) {
            CityDto dto = citiesByIata.get(cityIata);
            GeoPointDto airportDTO = this.closestAirport(dto.getLocation().getLatitude(), dto.getLocation().getLongitude());
            return airportDTO.getCode();
        }

        return "";
    }

    @Override
    public PictureDto getCityPicture(Long entityOid) {
        return this.snapshot().getPicturesByCityOid().get(entityOid);
    }

    @Override
    public List<CountryDto> getNewCountries() {
        List<CountryDto> result = new ArrayList<>();
        Map<String, CountryDto> allCountries = this.snapshot().getCountriesByIata();
        for (Map.Entry<String, CountryDto> entry : allCountries.entrySet()) {
            String country = entry.getKey();
            CountryCode cc = CountryCode.fromString(country);
            if (cc == null) {
                result.add(entry.getValue());
            }
        }
        Collections.sort(result, new Comparator<CountryDto>() {

            @Override
            public int compare(CountryDto a, CountryDto b) {
                return a.getCode().compareTo(b.getCode());
            }
        });
        return result;
    }

    @VisibleForTesting
    public void setGeoIntegratedSnapshot(final GeoSnapshotsService geoSnapshots) {
        this.geoSnapshotsService = geoSnapshots;
    }

    @VisibleForTesting
    public GeoSnapshotsService getGeoSnapshotsService() {
        return this.geoSnapshotsService;
    }

    public boolean isGeoSpot(Spot spot, String iata) {
        if (StringUtils.isBlank(iata)) {
            return false;
        }
        String upIata = iata.toUpperCase();
        Map<String, ? extends GeoSpot> spots = this.snapshot().getSpotByIata(spot);
        if (spots != null) {
            return spots.containsKey(upIata);
        }
        return false;
    }
}
