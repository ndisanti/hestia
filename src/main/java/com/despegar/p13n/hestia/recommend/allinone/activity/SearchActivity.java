package com.despegar.p13n.hestia.recommend.allinone.activity;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.HomeUtils;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.data.ActivityData;
import com.despegar.p13n.euler.commons.client.model.data.ClosedPackageData;
import com.despegar.p13n.euler.commons.client.model.data.CombinedProductData;
import com.despegar.p13n.euler.commons.client.model.data.CruiseData;
import com.despegar.p13n.euler.commons.client.model.data.FlightData;
import com.despegar.p13n.euler.commons.client.model.data.HotelData;
import com.despegar.p13n.euler.commons.client.model.data.ProductData;
import com.despegar.p13n.euler.commons.client.model.data.VacationRentalsData;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.utils.HestiaStringUtils;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class SearchActivity {
    private Map<Product, UserActivity> activitiesByProduct;

    // itemtypeid -> productData
    private LinkedHashMultimap<ItemTypeId, ProductData> lastSearchedItems = LinkedHashMultimap.create();

    public SearchActivity() {
        this.activitiesByProduct = Maps.newLinkedHashMap();
    }

    public boolean isActivityFor(Product pr) {
        return this.activitiesByProduct.containsKey(pr);
    }

    public void addSearchActivity(Product pr, UserActivity lastActivity) {
        this.activitiesByProduct.put(pr, lastActivity);
    }

    /**
     * Get activity for the given product. If not activity is found for this product, the last search activity for any product is returned.
     */
    public UserActivity getActivityOrLast(Product pr) {
        final UserActivity prActivity = this.getActivity(pr);
        if (prActivity != null) {
            return prActivity;
        } else {
            return this.getLastActivity();
        }
    }

    /**
     * Get activity for the given product or null if no product activity
     */
    public UserActivity getActivity(Product pr) {
        return this.activitiesByProduct.get(pr);
    }

    public boolean isDetailOrCheckoutFor(Product pr) {
        return this.getDetailOrCheckoutFor(pr) != null;
    }

    public UserActivity getDetailOrCheckoutFor(Product pr) {

        if (!this.activitiesByProduct.containsKey(pr)) {
            return null;
        }

        if (HomeUtils.isDetailOrCheckout(this.activitiesByProduct.get(pr).getFlow())) {
            return this.activitiesByProduct.get(pr);
        } else {
            return null;
        }
    }


    public boolean isEmpty() {
        return this.activitiesByProduct.isEmpty();
    }

    /**
     * @return the latest activity for any product
     */
    public UserActivity getLastActivity() {
        UserActivity lastest = null;

        for (Entry<Product, UserActivity> entry : this.activitiesByProduct.entrySet()) {
            if (lastest == null || lastest.getAction().getTimestamp() < entry.getValue().getAction().getTimestamp()) {
                lastest = entry.getValue();
            }
        }

        return lastest;
    }

    /**
     * @return the latest origin or null
     */
    public String getLastOrigin() {
        long ts = 0;
        String lastOrigin = null;

        for (UserActivity activity : this.activitiesByProduct.values()) {

            if (HomeUtils.hasOrigin(activity.getProduct())) {

                String origin = this.getOrigin(activity);

                if (origin != null) {
                    if (lastOrigin == null || ts < activity.getAction().getTimestamp()) {
                        lastOrigin = origin;
                        ts = activity.getAction().getTimestamp();
                    }

                }
            }
        }
        return lastOrigin;
    }

    private String getOrigin(UserActivity activity) {

        if (HomeUtils.hasOrigin(activity.getProduct())) {

            switch (activity.getProduct()) {
            case FLIGHTS:
                FlightData flightData = (FlightData) ProductData.create(activity.getAction());
                return flightData.getOriginIata();
            case CLOSED_PACKAGES:
                ClosedPackageData cpData = (ClosedPackageData) ProductData.create(activity.getAction());
                return cpData.getOriginIata();
            case COMBINED_PRODUCTS:
                CombinedProductData combData = (CombinedProductData) ProductData.create(activity.getAction());
                return combData.getOriginIata();

            default:
                throw new UnsupportedOperationException("Product with no origin: " + activity.getProduct());
            }
        }
        return null;
    }


    /**
     * Returns true if there is no activity for the given product,
     * or if there is an activity that is a SEARCH for a destination and we are checking for a DETAIL for the same destination.
     * 
     * We want to get the most advanced flow for the last searched destination.
     */
    public boolean canSetAction(Product pr, String destToCheck, Flow flowTocheck) {

        if (!this.isActivityFor(pr)) {
            return true;
        }

        UserActivity lastActivity = this.activitiesByProduct.get(pr);

        if (!lastActivity.getDestination().equals(destToCheck)) {
            return false;
        }

        // we have a previous less advanced flow for the same destination
        if (HomeUtils.isLessThan(lastActivity.getFlow(), flowTocheck)) {
            return true;
        }

        return false;
    }


    public void addLastSearchedItem(ItemTypeId itemTypeId, ProductData productData) {

        if (HestiaStringUtils.isNotBlank(productData.getDestinationIata())) {
            this.lastSearchedItems.put(itemTypeId, productData);
        }

    }


    /**
     * Returns a collection of last destinations for user activity
     * 
     * Iatas will be sorted by the most representative flows. (CHECKOUT, DETAIL, SEARCH...)
     * 
     * @return collection of last destinations.
     */
    public List<String> getLastDestinations() {

        LinkedHashSet<String> destinations = Sets.newLinkedHashSet();

        for (ProductData productData : this.lastSearchedItems.get(ItemTypeId.DESTINATION)) {
            destinations.add(productData.getDestinationIata());
        }

        if (destinations.size() > 4) {
            return Lists.newArrayList(destinations).subList(0, 4);
        }
        return Lists.newArrayList(destinations);
    }

    public Multimap<String, String> getLastHotelsByDestination() {
        LinkedHashMultimap<String, String> hids = LinkedHashMultimap.create();

        for (ProductData productData : this.lastSearchedItems.get(ItemTypeId.HID)) {
            HotelData hotelData = ProductData.create(productData.getParent());
            hids.put(hotelData.getDestinationIata(), hotelData.hotelId());
        }
        return hids;
    }

    public Multimap<String, String> getLastClosedPackagesByDestination() {
        LinkedHashMultimap<String, String> cluids = LinkedHashMultimap.create();

        for (ProductData productData : this.lastSearchedItems.get(ItemTypeId.CLUID)) {
            ClosedPackageData cpData = ProductData.create(productData.getParent());
            cluids.put(cpData.getDestinationIata(), cpData.clusterId());
        }
        return cluids;
    }

    public Multimap<String, String> getLastActivitiesByDestination() {
        LinkedHashMultimap<String, String> actids = LinkedHashMultimap.create();

        for (ProductData productData : this.lastSearchedItems.get(ItemTypeId.ACTID)) {
            ActivityData aData = ProductData.create(productData.getParent());
            actids.put(aData.getDestinationIata(), aData.actid());
        }
        return actids;
    }

    public Multimap<String, String> getLastCruisesByDestination() {
        LinkedHashMultimap<String, String> dids = LinkedHashMultimap.create();

        for (ProductData productData : this.lastSearchedItems.get(ItemTypeId.DID)) {
            CruiseData aData = ProductData.create(productData.getParent());
            dids.put(aData.getDestinationIata(), aData.did());
        }
        return dids;
    }

    public Multimap<String, String> getLastVacationRentalsByDestination() {
        LinkedHashMultimap<String, String> vrids = LinkedHashMultimap.create();

        for (ProductData productData : this.lastSearchedItems.get(ItemTypeId.VRID)) {
            VacationRentalsData aData = ProductData.create(productData.getParent());
            vrids.put(aData.getDestinationIata(), aData.vrid());
        }
        return vrids;
    }


    public Collection<String> getLastHotels(String destination) {
        Collection<String> lastHotels = this.getLastHotelsByDestination().get(destination);
        if (lastHotels.size() > 4) {
            return Lists.newArrayList(lastHotels).subList(0, 4);
        }
        return lastHotels;
    }


    public List<String> toStringListForDebug() {

        List<String> list = Lists.newArrayList();

        for (Entry<Product, UserActivity> entry : this.activitiesByProduct.entrySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(entry.getKey()).append(": ").append(entry.getValue());
            list.add(sb.toString());
        }

        return list;
    }

    @Override
    public String toString() {
        return "SearchActivity [activities=" + this.activitiesByProduct + "]";
    }

}
