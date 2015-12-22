package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.WishListHotelEntry;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.item.hotel.HotelItemBuilderCommonsFunctions;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Service
public class LikeHotelFunction
    extends BaseSectionFunction {

    @Autowired
    private HotelItemBuilderCommonsFunctions commons;

    @Override
    public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {

        ItemUtils.checkProduct(pr, Product.HOTELS);
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH, ActivityType.BUY);

        if (action.getWishList() != null) {

            WishListHotelEntry latestHotel = null;

            List<WishListHotelEntry> list = action.getWishList().filterByProduct(Product.HOTELS)
                .getEntriesAs(WishListHotelEntry.class);

            for (WishListHotelEntry hotelEntry : list) {
                if (latestHotel == null || latestHotel.getTimestamp() < hotelEntry.getTimestamp()) {
                    latestHotel = hotelEntry;
                }
            }

            if (latestHotel != null) {
                action.getTitleData().addDestination(latestHotel.getDestinationCode());
                HotelItem item = new HotelItem(latestHotel.getHotelId());
                return this.commons.isHotelAvailable(item) ? ItemUtils.buildOffers(home, item, action, pr) : null;
            }
        }

        return null;
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.LIKE_HOTEL_FUNCTION;
    }

    @Override
    public String getDescription() {
        return "Ultimo hotel id que tiene un like";
    }

}
