package com.despegar.p13n.hestia.external.geo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.despegar.p13n.hestia.external.geo.GeoSpot.Spot;

/**
 * Snapshot of geo service data.
 *
 * @author jcastro
 * @since Nov 27, 2012
 */
public class GeoSnapshot
    implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3191892045108649410L;

    private Map<String, CityDto> citiesByIata;
    private Map<Long, Collection<CityDto>> citiesByCountryCode;
    private Map<Long, CityDto> citiesByCityCode;

    private Map<String, CountryDto> countriesByIata;
    private Map<Long, CountryDto> countriesByOid;

    private Map<String, GeoPointDto> airportsByIata;

    private Map<String, GeoPointDto> portsByIata;

    private Map<Long, PictureDto> picturesByCityOid;

    public GeoSnapshot() {
        this.citiesByCityCode = new HashMap<Long, CityDto>();
        this.citiesByCountryCode = new HashMap<Long, Collection<CityDto>>();
        this.citiesByIata = new HashMap<String, CityDto>();
        this.countriesByIata = new HashMap<String, CountryDto>();
        this.countriesByOid = new HashMap<Long, CountryDto>();
        this.airportsByIata = new HashMap<String, GeoPointDto>();
        this.portsByIata = new HashMap<String, GeoPointDto>();
        this.picturesByCityOid = new HashMap<Long, PictureDto>();
    }

    public Map<Long, Collection<CityDto>> getCitiesByCountryCode() {
        return this.citiesByCountryCode;
    }

    public Map<String, ? extends GeoSpot> getSpotByIata(Spot spot) {
        switch (spot) {
        case CITY:
            return this.citiesByIata;
        case AIRPORT:
            return this.airportsByIata;
        case PORT:
            return this.portsByIata;
        default:
            return null;
        }
    }

    public void setCitiesByIata(Map<String, CityDto> citiesByIata) {
        this.citiesByIata = Collections.unmodifiableMap(citiesByIata);
    }

    public void setCitiesByCountryCode(Map<Long, Collection<CityDto>> citiesByCountryCode) {
        this.citiesByCountryCode = Collections.unmodifiableMap(citiesByCountryCode);
    }

    public Map<Long, CityDto> getCitiesByCityCode() {
        return this.citiesByCityCode;
    }

    public void setCitiesByCityCode(Map<Long, CityDto> citiesByCityCode) {
        this.citiesByCityCode = Collections.unmodifiableMap(citiesByCityCode);
    }

    public Map<String, CountryDto> getCountriesByIata() {
        return this.countriesByIata;
    }

    public void setCountriesByIata(Map<String, CountryDto> countriesByIata) {
        this.countriesByIata = Collections.unmodifiableMap(countriesByIata);
    }

    public void setCountriesByOid(HashMap<Long, CountryDto> countryMapByOid) {
        this.countriesByOid = Collections.unmodifiableMap(countryMapByOid);
    }

    public Map<Long, CountryDto> getCountriesByOid() {
        return this.countriesByOid;
    }

    public Map<String, CityDto> getCitiesByIata() {
        return this.citiesByIata;
    }

    public Map<String, GeoPointDto> getAirportsByIata() {
        return this.airportsByIata;
    }

    public Map<String, GeoPointDto> getPortsByIata() {
        return this.portsByIata;
    }

    public void setAirportsByIata(Map<String, GeoPointDto> airportsByIata) {
        this.airportsByIata = Collections.unmodifiableMap(airportsByIata);
    }

    public void setPortByIata(Map<String, GeoPointDto> porstByIata) {
        this.portsByIata = Collections.unmodifiableMap(porstByIata);
    }

    public Map<Long, PictureDto> getPicturesByCityOid() {
        return this.picturesByCityOid;
    }

    public void setPicturesByCityOid(Map<Long, PictureDto> picturesByCityOid) {
        this.picturesByCityOid = Collections.unmodifiableMap(picturesByCityOid);
    }

}
