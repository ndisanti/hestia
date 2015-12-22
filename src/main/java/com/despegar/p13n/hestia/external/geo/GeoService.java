package com.despegar.p13n.hestia.external.geo;

import java.util.List;
import java.util.Set;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.external.geo.GeoSpot.Spot;

public interface GeoService {

	 /**
     * Gets the city info.
     */
    GeoCity getCityInfo(String iata);

    /**
     * Gets the city info.
     */
    GeoCity getCityInfo(Long cityOid);

    /**
     * Gets the country info.
     */
    GeoCountry getCountryInfo(String countryCode);

    /**
     * Gets the country info.
     */
    GeoCountry getCountryInfo(long countryOid);

    /**
     * Gets the country from a iata.
     */
    // TODO use GeoCity and GeoCountry
    String getCountryFromIata(String iata);

    /**
     * Gets the main city for a country based on a city.
     * Mendoza -> Buenos Aires
     */
    GeoCity getMainCountryCity(GeoCity geoCity);

    /**
     * Gets the main city for a given country.
     */
    CityDto getMainCityFromCountry(CountryCode countryCode);

    /**
     * Gets the city codes for a country.
     */
    List<String> getCitiesForCountry(String countryCode);

    /**
     * Find closest city based on a whitelist.
     */
    GeoCity findClosestWhitelistedCity(GeoCity geoCity);

    /**
     * Find closest city for the given latitude and longitude, from a whitelist
     */
    CityDto closestWhitelistedCity(double lat, double lon);

    /**
     * Find closest city located within the given radius for the given city
     */
    GeoCity findClosestRadiusWhitelistedCity(GeoCity geoCity, int kilometersRadius);

    /**
     * Find closests cities for the given latitude, longitude and radius
     */
    Set<CityDto> closestCities(double latitude, double longitude, int radius);

    /**
     * Find closest city for the given latitude and longitude
     */
    CityDto closestCity(double latitude, double longitude);

    /**
     * Gets the closest city based on a given location.
     */
    GeoCity getClosestCity(LocationDto geoLocation);

    /**
     * Find closest airport for the given latitude and longitude
     */
    GeoPointDto closestAirport(double lat, double lon);

    /**
     * Gets closest airport given a iata
     */
    String getClosestAirport(String iata);

    /**
     * Find n closest cities for a given product based on a whitelist.
     */
    List<String> getNearbyCities(Product product, String cityIata, int nTop);

    /**
     * Find n closest cities for a given product based on a whitelist and in haversine method to determine distance
     */
    List<String> getNearbyProductCitiesByHaversine(Product product, String cityIata, int nTop, int maxKmRadius);

    /**
     * Converts iata (from airport or port) to city iata
     */
    String normalizeIata(String iata);

    /**
     * Converts iata to city iata with airport (from whitelist)
     */
    String normalizeToCityAirport(String iata);

    /**
     * Get list of city iatas for a given country and cruise region
     */
    List<String> getIatasByRegion(CountryCode country, String region);

    /**
     * Gets the iata description for the given language
     */
    String getIataName(String iataCode, String langCode);

    /**
     * Get list of picture DTOs for a given city Oid
     */
    PictureDto getCityPicture(Long entityOid);

    /**
     * Defines if the given cities are within the acceptable radius to consider them close.
     */
    boolean areNearbyCities(String firstCityIata, String secondCityIata, int km_radius);

    boolean iataExists(String iata);

    public List<CountryDto> getNewCountries();

    boolean isGeoSpot(Spot spot, String iata);

    GeoCity getCityInfoGeoLocation(LocationDto geoLocation);
}
