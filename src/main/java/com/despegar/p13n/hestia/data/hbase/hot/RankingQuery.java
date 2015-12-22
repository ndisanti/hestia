package com.despegar.p13n.hestia.data.hbase.hot;

import org.joda.time.YearMonth;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.data.hbase.hot.types.RankingType;
import com.despegar.p13n.hestia.external.geo.GeoCity;
import com.despegar.p13n.hestia.external.geo.GeoCountry;

public class RankingQuery {

    public enum QueryDepth {
        NO_LOCATION, COUNTRY, DIVISION, CITY, FULL;
    }

    private RankingType rankingType;
    private Product product;

    private GeoCity city;
    private GeoCountry country;

    private QueryDepth depth;

    private CountryCode siteCC;
    private String origin;
    private YearMonth yearMonth;

    private String iataStr;
    private String divisionStr;
    private String countryOidStr;
    private String brand;

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setCity(GeoCity city) {
        this.city = city;
    }

    public void setCountry(GeoCountry country) {
        this.country = country;
    }

    public RankingType getRankingType() {
        return this.rankingType;
    }

    public Product getProduct() {
        return this.product;
    }

    public GeoCity getCity() {
        return this.city;
    }

    public GeoCountry getCountry() {
        return this.country;
    }

    public CountryCode getSiteCC() {
        return this.siteCC;
    }

    public String getOrigin() {
        return this.origin;
    }

    public YearMonth getYearMonth() {
        return this.yearMonth;
    }

    public String getIataStr() {
        return this.iataStr;
    }

    public String getDivisionStr() {
        return this.divisionStr;
    }

    public String getCountryOidStr() {
        return this.countryOidStr;
    }

    public QueryDepth getDepth() {
        return this.depth;
    }

    public void forCity() {
        this.depth = QueryDepth.CITY;
    }

    public void forDivision() {
        this.depth = QueryDepth.DIVISION;
    }

    public void forCountry() {
        this.depth = QueryDepth.COUNTRY;
    }

    public void forNoLocation() {
        this.depth = QueryDepth.NO_LOCATION;
    }

    public static class Builder {
        // Required Fields
        private final RankingType rankingType;
        private Product product;

        // Optional Fields
        private GeoCity city;
        private GeoCountry country;
        private CountryCode siteCC;
        private String origin;
        private YearMonth yearMonth;
        private QueryDepth depth;
        private String iataStr;
        private String divisionStr;
        private String countryOidStr;
        private String brand;

        public Builder(RankingType rankingType) {
            super();
            this.rankingType = rankingType;
        }

        public Builder(RankingType rankingType, Product product) {
            super();
            this.rankingType = rankingType;
            this.product = product;
        }

        public Builder withProduct(Product product) {
            this.product = product;
            return this;
        }

        public Builder withCity(GeoCity city) {
            this.city = city;
            return this;
        }

        public Builder withCountry(GeoCountry country) {
            this.country = country;
            return this;
        }

        public Builder withSiteCC(CountryCode siteCC) {
            this.siteCC = siteCC;
            return this;
        }

        public Builder withOrigin(String origin) {
            this.origin = origin;
            return this;
        }

        public Builder withYearMonth(YearMonth yearMonth) {
            this.yearMonth = yearMonth;
            return this;
        }

        public Builder withIataStr(String iata) {
            this.iataStr = iata;
            return this;
        }

        public Builder withDivisionStr(String division) {
            this.divisionStr = division;
            return this;
        }

        public Builder withCountryOidStr(String countryOidStr) {
            this.countryOidStr = countryOidStr;
            return this;
        }

        public Builder withBrand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder forCity() {
            this.depth = QueryDepth.CITY;
            return this;
        }

        public Builder forDivision() {
            this.depth = QueryDepth.DIVISION;
            return this;
        }

        public Builder forCountry() {
            this.depth = QueryDepth.COUNTRY;
            return this;
        }

        public Builder forNoLocation() {
            this.depth = QueryDepth.NO_LOCATION;
            return this;
        }

        public RankingQuery build() throws IllegalArgumentException {
            RankingQuery rk = new RankingQuery(this);
            return rk;
        }
    }

    private RankingQuery(Builder builder) {
        super();
        this.rankingType = builder.rankingType;
        this.product = builder.product;
        if (builder.iataStr != null || builder.divisionStr != null || builder.countryOidStr != null) {
            this.iataStr = builder.iataStr;
            this.divisionStr = builder.divisionStr;
            this.countryOidStr = builder.countryOidStr;
        } else {
            this.city = builder.city;
            this.country = builder.country;
        }
        this.siteCC = builder.siteCC;
        this.origin = builder.origin;
        this.yearMonth = builder.yearMonth;
        this.depth = (builder.depth != null ? builder.depth : QueryDepth.FULL);
        this.brand = builder.brand;
    }

}
