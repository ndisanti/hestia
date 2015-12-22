package com.despegar.p13n.hestia.recommend.allinone.item;

import java.util.List;

import javax.activation.UnsupportedDataTypeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.item.activity.ActivityItemSteps;
import com.despegar.p13n.hestia.recommend.allinone.item.car.CarItemSteps;
import com.despegar.p13n.hestia.recommend.allinone.item.closedpackage.TemporaryClosedPackagesSteps;
import com.despegar.p13n.hestia.recommend.allinone.item.cruise.CruiseItemSteps;
import com.despegar.p13n.hestia.recommend.allinone.item.flight.FlightItemSteps;
import com.despegar.p13n.hestia.recommend.allinone.item.hotel.HotelItemSteps;
import com.despegar.p13n.hestia.recommend.allinone.item.vacactionrentals.VacationRentalsItemSteps;
import com.despegar.p13n.hestia.utils.HestiaUtils;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * Builds a concrete {@link ItemHome}
 */

// SearchActivity los findByDestination
@Service
public class ItemHomeService {

    @Autowired
    private CarItemSteps carSteps;
    @Autowired
    private FlightItemSteps flightSteps;
    @Autowired
    private HotelItemSteps hotelSteps;
    @Autowired
    private TemporaryClosedPackagesSteps closedPackageSteps;
    @Autowired
    private CruiseItemSteps cruiseSteps;
    @Autowired
    private ActivityItemSteps activitySteps;
    @Autowired
    private VacationRentalsItemSteps vacactionRentalsSteps;


    private List<Product> validProducts = Lists.newArrayList(Product.ACTIVITIES, Product.FLIGHTS, Product.HOTELS,
        Product.CLOSED_PACKAGES, Product.CARS, Product.CRUISES, Product.INSURANCE, Product.VACATIONRENTALS);

    /**
     * Based on the {@link ItemTypeId}, item id, {@link Product} and {@link ActionRecommendation}, it returns the concrete {@link ItemHome}
     * @throws UnsupportedDataTypeException
     */
    public ItemHome buildItemHome(ItemTypeId idType, String itemId, Product pr, ActionRecommendation action) {
        this.checkPreconditions(idType, itemId, pr, action);


        // TODO: soportar todos los item type id y productos
        ItemHome item = null;
        switch (pr) {
        case FLIGHTS:
            item = this.getFlightItem(itemId, action);
            break;
        case HOTELS:
            item = this.getHotelItem(itemId, action);
            break;
        case CARS:
            item = this.getCarItem(itemId, action);
            break;
        case CLOSED_PACKAGES:
            item = this.getClosedPackageItem(itemId, action);
            break;
        case CRUISES:
            item = this.getCruiseItem(itemId, action);
            break;
        case ACTIVITIES:
            item = this.getActivityItem(itemId, action);
            break;
        case VACATIONRENTALS:
            item = this.getVacationRentalsItem(itemId, action);
            break;
        case INSURANCE:
            item = this.getHotelItem(itemId, action);
            break;
        default:
            throw new IllegalArgumentException(pr.toString());
        }
        return item;
    }

    private void checkPreconditions(ItemTypeId idType, String itemId, Product pr, ActionRecommendation action) {
    	HestiaUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH, ActivityType.BUY);
        Preconditions.checkNotNull(idType, "idType cannot be null.");
        Preconditions.checkNotNull(itemId, "itemId cannot be null.");
        Preconditions.checkNotNull(pr, "pr cannot be null.");
        Preconditions.checkNotNull(action, "action cannot be null.");
        Preconditions.checkArgument(this.validProducts.contains(pr));
    }

    private ItemHome getCruiseItem(String destination, ActionRecommendation action) {
        return this.cruiseSteps.execute(destination, action);
    }

    private ItemHome getVacationRentalsItem(String destination, ActionRecommendation action) {
        return this.vacactionRentalsSteps.execute(destination, action);
    }


    private ItemHome getCarItem(String destination, ActionRecommendation action) {
        return this.carSteps.execute(destination, action);
    }

    private ItemHome getActivityItem(String destination, ActionRecommendation action) {
        return this.activitySteps.execute(destination, action);
    }

    private ItemHome getClosedPackageItem(String destination, ActionRecommendation action) {
        return this.closedPackageSteps.execute(destination, action);
    }

    private ItemHome getHotelItem(String destination, ActionRecommendation action) {
        return this.hotelSteps.execute(destination, action);
    }

    private ItemHome getFlightItem(String destination, ActionRecommendation action) {
        return this.flightSteps.execute(destination, action);
    }

    @VisibleForTesting
    public void setCarSteps(CarItemSteps carSteps) {
        this.carSteps = carSteps;
    }

    @VisibleForTesting
    public void setFlightSteps(FlightItemSteps flightSteps) {
        this.flightSteps = flightSteps;
    }

    @VisibleForTesting
    public void setHotelSteps(HotelItemSteps hotelSteps) {
        this.hotelSteps = hotelSteps;
    }

    @VisibleForTesting
    public void setClosedPackageSteps(TemporaryClosedPackagesSteps closedPackageSteps) {
        this.closedPackageSteps = closedPackageSteps;
    }

    @VisibleForTesting
    public void setCruiseSteps(CruiseItemSteps cruiseSteps) {
        this.cruiseSteps = cruiseSteps;
    }

    @VisibleForTesting
    public void setActivitySteps(ActivityItemSteps activitySteps) {
        this.activitySteps = activitySteps;
    }

}
