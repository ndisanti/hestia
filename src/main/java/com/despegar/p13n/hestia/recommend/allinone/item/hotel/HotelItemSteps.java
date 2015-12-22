package com.despegar.p13n.hestia.recommend.allinone.item.hotel;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.WishList;
import com.despegar.p13n.euler.commons.client.model.WishListHotelEntry;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;

@Component
public class HotelItemSteps
    implements ItemStep {

	@Autowired
    private DetailDestinationHotelStep detailDestinationStep;
	@Autowired
	private DefaultHotelStep defStep;
	@Autowired
	private HotelItemBuilderCommonsFunctions commons;

    @Override
    public ItemHome execute(String destination, ActionRecommendation action) {
        ItemHome item = this.getLikedHotel(action, destination);
        if (item == null) {
            SearchActivity activity = action.getSearchActivity();
            UserActivity hotelActivity = activity == null ? null : activity.getActivity(Product.HOTELS);
            if (hotelActivity != null && hotelActivity.getAction().getFlow().equals(Flow.DETAIL)
                && hotelActivity.getDestination().equals(destination)) {

                return this.detailDestinationStep.execute(destination, action);
            } else {
                return this.defStep.execute(destination, action);
            }
        }
        return item;
    }

    private ItemHome getLikedHotel(ActionRecommendation action, String destination) {
        WishList wishList = action.getWishList().filterByProduct(Product.HOTELS);
        HotelItem item = null;
        Iterator<WishListHotelEntry> it = wishList.getEntriesAs(WishListHotelEntry.class).iterator();
        boolean found = false;
        while (it.hasNext() && !found) {
            WishListHotelEntry hotel = it.next();
            item = new HotelItem(hotel.getHotelId());
            if (hotel.getDestinationCode().equals(destination) && this.commons.isHotelAvailable(item)
                && this.commons.isUnique(action, item)) {
                found = true;
            }
        }
        return found ? item : null;
    }

}
