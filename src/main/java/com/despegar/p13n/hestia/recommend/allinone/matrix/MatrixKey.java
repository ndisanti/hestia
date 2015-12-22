package com.despegar.p13n.hestia.recommend.allinone.matrix;

import org.apache.commons.lang.StringUtils;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.data.RouteType;

public class MatrixKey {

    private SearchCount searchCount;
    private Boolean buy;
    private Product product;
    private LastActionMatrix lastAction;
    private AnticipationMatrix anticipation;
    private CountryCode country;
    private RouteType routeType;
    private String destination;

    public MatrixKey() {
    }

    public MatrixKey(SearchCount searchCount, Boolean buy, Product product, LastActionMatrix lastAction,
        AnticipationMatrix anticipation, CountryCode country, RouteType routeType, String destination) {
        this.searchCount = searchCount;
        this.buy = buy;
        this.product = product;
        this.lastAction = lastAction;
        this.anticipation = anticipation;
        this.country = country;
        this.routeType = routeType;
        this.destination = destination;
    }

    public static MatrixKey forCountry(CountryCode cc) {
        return new MatrixKey(null, null, null, null, null, cc, null, null);
    }

    public SearchCount getSearchCount() {
        return this.searchCount;
    }

    public void setSearchCount(SearchCount searchCount) {
        this.searchCount = searchCount;
    }

    public Boolean isBuy() {
        return this.buy;
    }

    public void setBuy(Boolean buy) {
        this.buy = buy;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LastActionMatrix getLastAction() {
        return this.lastAction;
    }

    public void setLastAction(LastActionMatrix lastAction) {
        this.lastAction = lastAction;
    }

    public AnticipationMatrix getAnticipation() {
        return this.anticipation;
    }

    public void setAnticipation(AnticipationMatrix anticipation) {
        this.anticipation = anticipation;
    }

    public CountryCode getCountry() {
        return this.country;
    }

    public void setCountry(CountryCode country) {
        this.country = country;
    }

    public RouteType getRouteType() {
        return this.routeType;
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.anticipation == null) ? 0 : this.anticipation.hashCode());
        result = prime * result + ((this.buy == null) ? 0 : this.buy.hashCode());
        result = prime * result + ((this.country == null) ? 0 : this.country.hashCode());
        result = prime * result + ((this.destination == null) ? 0 : this.destination.hashCode());
        result = prime * result + ((this.lastAction == null) ? 0 : this.lastAction.hashCode());
        result = prime * result + ((this.product == null) ? 0 : this.product.hashCode());
        result = prime * result + ((this.routeType == null) ? 0 : this.routeType.hashCode());
        result = prime * result + ((this.searchCount == null) ? 0 : this.searchCount.hashCode());
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
        MatrixKey other = (MatrixKey) obj;
        if (this.anticipation != other.anticipation) {
            return false;
        }
        if (this.buy == null) {
            if (other.buy != null) {
                return false;
            }
        } else if (!this.buy.equals(other.buy)) {
            return false;
        }
        if (this.country != other.country) {
            return false;
        }
        if (this.destination == null) {
            if (other.destination != null) {
                return false;
            }
        } else if (!this.destination.equals(other.destination)) {
            return false;
        }
        if (this.lastAction != other.lastAction) {
            return false;
        }
        if (this.product != other.product) {
            return false;
        }
        if (this.routeType != other.routeType) {
            return false;
        }
        if (this.searchCount != other.searchCount) {
            return false;
        }
        return true;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String asStringForKey() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.searchCount).append("|");
        sb.append(this.buy).append("|");
        sb.append(this.product).append("|");
        sb.append(this.lastAction).append("|");
        sb.append(this.anticipation).append("|");
        sb.append(this.country).append("|");
        sb.append(this.routeType).append("|");
        sb.append(this.destination).append("|");
        return sb.toString();
    }

    public static MatrixKey fromHbaseKey(String key) {

        String[] v = StringUtils.split(key, '|');

        SearchCount sc = SearchCount.valueOf(v[0]);
        boolean buy = Boolean.valueOf(v[1]);
        Product pr = Product.fromString(v[2]);
        LastActionMatrix lam = LastActionMatrix.valueOf(v[3]);
        AnticipationMatrix am = AnticipationMatrix.valueOf(v[4]);
        CountryCode cc = CountryCode.fromString(v[5]);
        RouteType rt = RouteType.fromDescription(v[6]);
        // TODO arreglar
        String dest = v.length == 8 ? v[7] : null;
        return new MatrixKey(sc, buy, pr, lam, am, cc, rt, dest);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MatrixKey [");

        if (this.searchCount != null) {
            builder.append("searchCount=").append(this.searchCount);
        }

        if (this.buy != null) {
            builder.append(" buy=").append(this.buy);
        }

        if (this.product != null) {
            builder.append(" product=").append(this.product);
        }

        if (this.lastAction != null) {
            builder.append(" lastAction=").append(this.lastAction);
        }

        if (this.anticipation != null) {
            builder.append(" anticipation=").append(this.anticipation);
        }

        if (this.country != null) {
            builder.append(" country=").append(this.country);
        }

        if (this.routeType != null) {
            builder.append(" routeType=").append(this.routeType);
        }

        if (this.destination != null) {
            builder.append(" destination=").append(this.destination);
        }

        builder.append("]");
        return builder.toString();
    }
}
