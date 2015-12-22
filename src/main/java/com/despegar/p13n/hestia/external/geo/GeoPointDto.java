package com.despegar.p13n.hestia.external.geo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeoPointDto
    implements GeoSpot {

    protected static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(GeoPointDto.class);

    protected String id;

    protected long longId;

    Map<String, String> descriptions;

    protected String code;

    protected LocationDto location;

    private String city_id;

    private boolean commercial;

    private String type;

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
        GeoPointDto other = (GeoPointDto) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
        try {
            this.longId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            logger.error("GeoPoint ID is not a number, ignoring GeoPoint={}", id);
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

    public String getCity_id() {
        return this.city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public boolean isCommercial() {
        return this.commercial;
    }

    public void setCommercial(boolean commercial) {
        this.commercial = commercial;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocationDto getLocation() {
        return this.location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public long getLongId() {
        return this.longId;
    }


}
