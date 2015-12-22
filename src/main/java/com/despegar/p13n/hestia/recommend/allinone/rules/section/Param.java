package com.despegar.p13n.hestia.recommend.allinone.rules.section;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.HomeUtils;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.City;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function.CarRankingType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function.CountryType;
import com.despegar.p13n.hestia.utils.DumpUtils;

/**
 * <p> Immutable parameter class to configurate {@link SectionFunction} implementations. </p>
 */
public class Param {

    private final StaticRankingTypes rankingType;
    private final CarRankingType carRankingType;
    private final Flow flow1;
    private final Product pr1;
    private final Flow flow2;
    private final Boolean seen;
    private final Boolean addSearch;
    private final Boolean addBuy;
    private final Boolean searchedDestination;
    private final Boolean checkIsDetail;
    private final CountryType countryType;
    private final City city;

    public Param() {
        this.rankingType = null;
        this.carRankingType = null;
        this.flow1 = null;
        this.pr1 = null;
        this.flow2 = null;
        this.seen = null;
        this.addSearch = null;
        this.addBuy = null;
        this.countryType = null;
        this.city = null;
        this.searchedDestination = null;
        this.checkIsDetail = null;
    }

    public Param(StaticRankingTypes rankingType, CarRankingType carRankingType, Flow flow1, Product pr1, Flow flow2,
        Boolean seen, Boolean addSearch, Boolean addBuy, CountryType countryType, City city, Boolean searchedDestination,
        Boolean checkIsDetail) {
        this.rankingType = rankingType;
        this.carRankingType = carRankingType;
        this.flow1 = flow1;
        this.pr1 = pr1;
        this.flow2 = flow2;
        this.seen = seen;
        this.addSearch = addSearch;
        this.addBuy = addBuy;
        this.countryType = countryType;
        this.city = city;
        this.searchedDestination = searchedDestination;
        this.checkIsDetail = checkIsDetail;
    }

    public Param(String rankingType, String carRankingType, String flow1, String pr1, String flow2, String seen,
        String addSearch, String addBuy, String countryType, String city, String searchedDestination, String checkIsDetail) {
        this.rankingType = rankingType == null ? null : StaticRankingTypes.valueOf(rankingType);
        this.carRankingType = carRankingType == null ? null : CarRankingType.valueOf(carRankingType);
        this.flow1 = flow1 == null ? null : Flow.valueOf(flow1);
        this.pr1 = pr1 == null ? null : Product.valueOf(pr1);
        this.flow2 = flow2 == null ? null : Flow.valueOf(flow2);
        this.seen = seen == null ? null : Boolean.valueOf(seen);
        this.addSearch = addSearch == null ? null : Boolean.valueOf(addSearch);
        this.addBuy = addBuy == null ? null : Boolean.valueOf(addBuy);
        this.countryType = addSearch == null ? null : CountryType.valueOf(countryType);
        this.city = city == null ? null : City.fromString(city);
        this.searchedDestination = searchedDestination == null ? null : Boolean.valueOf(searchedDestination);
        this.checkIsDetail = checkIsDetail == null ? null : Boolean.valueOf(checkIsDetail);
    }

    public static Param copy(Param param) {
        return new Param(param.rankingType, param.carRankingType, param.flow1, param.pr1, param.flow2, param.seen,
            param.addSearch, param.addBuy, param.countryType, param.city, param.searchedDestination, param.checkIsDetail);
    }

    public static ParamBuilder builder() {
        return new ParamBuilder();
    }

    public Flow getFlow1() {
        return this.flow1;
    }

    public Param flow1(Flow flow1) {
        return ParamBuilder.copy(this).flow1(flow1).build();
    }

    public StaticRankingTypes getRankingType() {
        return this.rankingType;
    }

    public Param rankingType(StaticRankingTypes rankingType) {
        return ParamBuilder.copy(this).rankingType(rankingType).build();
    }

    public CarRankingType getCarRankingType() {
        return this.carRankingType;
    }

    public Param carRankingType(CarRankingType carRankingType) {
        return ParamBuilder.copy(this).carRankingType(carRankingType).build();
    }

    public Product getPr1() {
        return this.pr1;
    }

    public Param pr1(Product pr1) {
        return ParamBuilder.copy(this).pr1(pr1).build();
    }

    public Flow getFlow2() {
        return this.flow2;
    }

    public Param crossFlow2(Flow flow2) {
        return ParamBuilder.copy(this).flow2(flow2).build();
    }

    public boolean isSeen() {
        return this.seen == null ? false : this.seen;
    }

    public Param seen(boolean seen) {
        return ParamBuilder.copy(this).seen(seen).build();
    }

    public boolean isAddSearch() {
        return this.addSearch == null ? false : this.addSearch;
    }

    public Param addSearch(boolean addSearch) {
        return ParamBuilder.copy(this).addSearch(addSearch).build();
    }

    public boolean isCheckDetail() {
        return this.checkIsDetail == null ? false : this.checkIsDetail;
    }

    public Param checkIsDetail(boolean checkIsDetail) {
        return ParamBuilder.copy(this).checkIsDetail(checkIsDetail).build();
    }

    public boolean isSearchedDestination() {
        return this.searchedDestination == null ? false : this.searchedDestination;
    }

    public Param searchedDestination(boolean searchedDestination) {
        return ParamBuilder.copy(this).searchedDestination(searchedDestination).build();
    }

    public boolean isAddBuy() {
        return this.addBuy == null ? false : this.addBuy;
    }

    public Param addBuy(boolean addBuy) {
        return ParamBuilder.copy(this).addBuy(addBuy).build();
    }

    public CountryType getCountryType() {
        return this.countryType;
    }

    public Param countryType(CountryType countryType) {
        return ParamBuilder.copy(this).countryType(countryType).build();
    }

    public boolean isSearch() {
        return HomeUtils.isSearch(this.getFlow1());
    }

    public boolean isDetail() {
        return HomeUtils.isDetailOrCheckout(this.getFlow1());
    }

    public Param city(City city) {
        return ParamBuilder.copy(this).city(city).build();
    }

    public City getCity() {
        return this.city;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.rankingType != null) {
            builder.append(this.rankingTypeToString(this.rankingType)).append(" ");
        }
        if (this.carRankingType != null) {
            builder.append("cars=").append(this.carRankingType).append(" ");
        }
        if (this.flow1 != null) {
            builder.append("fl1=").append(this.flow1).append(" ");
        }
        if (this.pr1 != null) {
            builder.append("pr1=").append(DumpUtils.dumpProduct(this.pr1.toString())).append(" ");
        }
        if (this.flow2 != null) {
            builder.append("fl2=").append(this.flow2).append(" ");
        }
        if (this.seen != null && this.seen == true) {
            builder.append("seen").append(" ");
        }
        if (this.addSearch != null && this.addSearch == true) {
            builder.append("search").append(" ");
        }

        if (this.searchedDestination != null && this.searchedDestination == true) {
            builder.append("searchedDestination").append(" ");
        }

        if (this.checkIsDetail != null && this.checkIsDetail == true) {
            builder.append("checkIsDetail").append(" ");
        }
        if (this.addBuy != null && this.addBuy == true) {
            builder.append("buy").append(" ");
        }
        if (this.countryType != null) {
            builder.append(this.countryType).append(" ");
        }
        if (this.city != null) {
            builder.append(this.city);
        }

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.addBuy == null) ? 0 : this.addBuy.hashCode());
        result = prime * result + ((this.addSearch == null) ? 0 : this.addSearch.hashCode());
        result = prime * result + ((this.carRankingType == null) ? 0 : this.carRankingType.hashCode());
        result = prime * result + ((this.checkIsDetail == null) ? 0 : this.checkIsDetail.hashCode());
        result = prime * result + ((this.city == null) ? 0 : this.city.hashCode());
        result = prime * result + ((this.countryType == null) ? 0 : this.countryType.hashCode());
        result = prime * result + ((this.flow1 == null) ? 0 : this.flow1.hashCode());
        result = prime * result + ((this.flow2 == null) ? 0 : this.flow2.hashCode());
        result = prime * result + ((this.pr1 == null) ? 0 : this.pr1.hashCode());
        result = prime * result + ((this.rankingType == null) ? 0 : this.rankingType.hashCode());
        result = prime * result + ((this.searchedDestination == null) ? 0 : this.searchedDestination.hashCode());
        result = prime * result + ((this.seen == null) ? 0 : this.seen.hashCode());
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
        Param other = (Param) obj;
        if (this.addBuy == null) {
            if (other.addBuy != null) {
                return false;
            }
        } else if (!this.addBuy.equals(other.addBuy)) {
            return false;
        }
        if (this.addSearch == null) {
            if (other.addSearch != null) {
                return false;
            }
        } else if (!this.addSearch.equals(other.addSearch)) {
            return false;
        }
        if (this.carRankingType != other.carRankingType) {
            return false;
        }
        if (this.checkIsDetail == null) {
            if (other.checkIsDetail != null) {
                return false;
            }
        } else if (!this.checkIsDetail.equals(other.checkIsDetail)) {
            return false;
        }
        if (this.city != other.city) {
            return false;
        }
        if (this.countryType != other.countryType) {
            return false;
        }
        if (this.flow1 != other.flow1) {
            return false;
        }
        if (this.flow2 != other.flow2) {
            return false;
        }
        if (this.pr1 != other.pr1) {
            return false;
        }
        if (this.rankingType != other.rankingType) {
            return false;
        }
        if (this.searchedDestination == null) {
            if (other.searchedDestination != null) {
                return false;
            }
        } else if (!this.searchedDestination.equals(other.searchedDestination)) {
            return false;
        }
        if (this.seen == null) {
            if (other.seen != null) {
                return false;
            }
        } else if (!this.seen.equals(other.seen)) {
            return false;
        }
        return true;
    }

    private String rankingTypeToString(StaticRankingTypes rankingType) {
        switch (rankingType) {
        case HOT_SEARCHES_DESTINATIONS_ANY:
            return "ANY";
        case HOT_SEARCHES_DESTINATIONS_DOMESTIC:
            return "DOMESTIC";
        case HOT_SEARCHES_DESTINATIONS_INTERNATIONAL:
            return "INTERNATIONAL";
        default:
            return rankingType.toString();
        }
    }

    public boolean isEmpty() {
        return this.rankingType == null && //
            this.carRankingType == null && //
            this.flow1 == null && //
            this.pr1 == null && //
            this.flow2 == null && //
            this.seen == null && //
            this.addSearch == null && //
            this.addBuy == null && //
            this.searchedDestination == null;
    }

    public static class ParamBuilder {
        private StaticRankingTypes rankingType;
        private CarRankingType carRankingType;
        private Flow flow1;
        private Product pr1;
        private Flow flow2;
        private Boolean seen;
        private Boolean addSearch;
        private Boolean addBuy;
        private CountryType countryType;
        private City city;
        private Boolean searchedDestination;
        private Boolean checkIsDetail;

        public static ParamBuilder copy(Param param) {

            ParamBuilder build = new ParamBuilder();

            build.rankingType = param.rankingType;
            build.carRankingType = param.carRankingType;
            build.countryType = param.countryType;
            build.flow1 = param.flow1;
            build.pr1 = param.pr1;
            build.flow2 = param.flow2;
            build.seen = param.seen;
            build.addSearch = param.addSearch;
            build.addBuy = param.addBuy;
            build.city = param.city;
            build.searchedDestination = param.searchedDestination;
            build.checkIsDetail = param.checkIsDetail;
            return build;
        }

        public ParamBuilder rankingType(StaticRankingTypes rankingType) {
            this.rankingType = rankingType;
            return this;
        }

        public ParamBuilder carRankingType(CarRankingType carRankingType) {
            this.carRankingType = carRankingType;
            return this;
        }

        public ParamBuilder flow1(Flow flow1) {
            this.flow1 = flow1;
            return this;
        }

        public ParamBuilder pr1(Product pr1) {
            this.pr1 = pr1;
            return this;
        }

        public ParamBuilder flow2(Flow flow2) {
            this.flow2 = flow2;
            return this;
        }

        public ParamBuilder seen(Boolean seen) {
            this.seen = seen;
            return this;
        }


        public ParamBuilder addSearch(Boolean addSearch) {
            this.addSearch = addSearch;
            return this;
        }

        public ParamBuilder checkIsDetail(Boolean checkIsDetail) {
            this.checkIsDetail = checkIsDetail;
            return this;
        }

        public ParamBuilder searchedDestination(Boolean searchedDestination) {
            this.searchedDestination = searchedDestination;
            return this;
        }


        public ParamBuilder addBuy(Boolean addBuy) {
            this.addBuy = addBuy;
            return this;
        }

        public ParamBuilder countryType(CountryType countryType) {
            this.countryType = countryType;
            return this;
        }

        public ParamBuilder city(City city) {
            this.city = city;
            return this;
        }

        public Param build() {
            return new Param(this.rankingType, this.carRankingType, this.flow1, this.pr1, this.flow2, this.seen,
                this.addSearch, this.addBuy, this.countryType, this.city, this.searchedDestination, this.checkIsDetail);
        }

    }

    public static void main(String[] args) {
        System.out.println(Param.builder().flow1(Flow.SEARCH).build());
    }

}
