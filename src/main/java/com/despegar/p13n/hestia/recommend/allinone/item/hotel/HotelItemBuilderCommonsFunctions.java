package com.despegar.p13n.hestia.recommend.allinone.item.hotel;

import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Component
public class HotelItemBuilderCommonsFunctions {

    // TODO aca reemplazar con lo de disponibilidad
    public boolean isHotelAvailable(HotelItem item) {
        return true;
    }

    public boolean isUnique(ActionRecommendation action, HotelItem item) {
        return ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.HOTELS) != null;
    }
}
