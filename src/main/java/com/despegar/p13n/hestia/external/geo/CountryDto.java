package com.despegar.p13n.hestia.external.geo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountryDto {

    private static final Logger logger = LoggerFactory.getLogger(CountryDto.class);

    private String id;

    private long longId;

    Map<String, String> descriptions;

    private String code;

    private String capital_id;

    private String alternative_code;

    private String continent_id;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
        try {
            this.longId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            logger.error("Country ID is not a number, ignoring country={}", id);
        }
    }

    public Map<String, String> getDescriptions() {
        return this.descriptions;
    }

    public void setDescriptions(Map<String, String> descriptions) {
        this.descriptions = descriptions;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCapital_id() {
        return this.capital_id;
    }

    public void setCapital_id(String capital_id) {
        this.capital_id = capital_id;
    }

    public String getAlternative_code() {
        return this.alternative_code;
    }

    public void setAlternative_code(String alternative_code) {
        this.alternative_code = alternative_code;
    }

    public String getContinent_id() {
        return this.continent_id;
    }

    public void setContinent_id(String continent_id) {
        this.continent_id = continent_id;
    }



    // public LocationDto getLocation() {
    // return this.location;
    // }
    //
    // public void setLocation(LocationDto location) {
    // this.location = location;
    // }

    public long getLongId() {
        return this.longId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        CountryDto other = (CountryDto) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }



}
