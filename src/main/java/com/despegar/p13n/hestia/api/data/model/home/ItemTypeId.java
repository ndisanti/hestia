/**
 * 
 */
package com.despegar.p13n.hestia.api.data.model.home;

import java.util.Map;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemType;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;


/**
 * Id type for an item
 */
public enum ItemTypeId {
    DESTINATION, //
    CAR_CATEGORY, //
    HID, //
    CLUID, //
    REGION, //
    DID, //
    ACTID, //
    VRID;

    private static Map<ItemType, ItemTypeId> typeMapping = Maps.newHashMap();
    private static Map<ItemType, Product> productMapping = Maps.newHashMap();

    static {
        typeMapping.put(ItemType.CAR_CATEGORY, ItemTypeId.CAR_CATEGORY);
        typeMapping.put(ItemType.CAR_DESTINATION, ItemTypeId.DESTINATION);
        typeMapping.put(ItemType.CLOSED_PACKAGES, ItemTypeId.CLUID);
        typeMapping.put(ItemType.CLOSED_PACKAGES_DESTINATION, ItemTypeId.DESTINATION);
        typeMapping.put(ItemType.COMBINED_PRODUCTS, null); // not supported by now
        typeMapping.put(ItemType.CRUISE, DID);
        typeMapping.put(ItemType.CRUISE_REGION, REGION);
        typeMapping.put(ItemType.HOTEL, HID);
        typeMapping.put(ItemType.HOTEL_DESTINATION, DESTINATION);
        typeMapping.put(ItemType.FLIGHT_DESTINATION, DESTINATION);
        typeMapping.put(ItemType.ACTIVITY, ACTID);
        typeMapping.put(ItemType.ACTIVITY_DESTINATION, DESTINATION);
        typeMapping.put(ItemType.VACATION_RENTAL, VRID);
        typeMapping.put(ItemType.VACATION_RENTAL_DESTINATION, ItemTypeId.DESTINATION);
    }

    static {
        productMapping.put(ItemType.CAR_DESTINATION, Product.CARS);
        productMapping.put(ItemType.ACTIVITY, Product.ACTIVITIES);
        productMapping.put(ItemType.VACATION_RENTAL, Product.VACATIONRENTALS);
        productMapping.put(ItemType.VACATION_RENTAL_DESTINATION, Product.VACATIONRENTALS);
        productMapping.put(ItemType.ACTIVITY_DESTINATION, Product.ACTIVITIES);
        productMapping.put(ItemType.CLOSED_PACKAGES, Product.CLOSED_PACKAGES); // not supported by now
        productMapping.put(ItemType.CLOSED_PACKAGES_DESTINATION, Product.CLOSED_PACKAGES);
        productMapping.put(ItemType.CRUISE, Product.CRUISES);
        productMapping.put(ItemType.HOTEL, Product.HOTELS);
        productMapping.put(ItemType.HOTEL_DESTINATION, Product.HOTELS);
        productMapping.put(ItemType.FLIGHT_DESTINATION, Product.FLIGHTS);
    }

    public static ItemTypeId getItemType(ItemType type) {
        Preconditions.checkNotNull(type);
        ItemTypeId id = typeMapping.get(type);
        Preconditions.checkNotNull(id);
        return id;
    }

    public static Product getProduct(ItemType type) {
        Preconditions.checkNotNull(type);
        Product pr = productMapping.get(type);
        Preconditions.checkNotNull(pr);
        return pr;
    }

}
