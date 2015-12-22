package com.despegar.p13n.hestia.service;

import java.util.List;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.MultiObjecHomeVersion;

public interface MultiObjectVersionService {

    HomeContent addObjectsInRows(HomeContent homeContent, ActionRecommendation action, MultiObjecHomeVersion homeVersion);

    void addObjectsInOffers(HomeContent homeContent, ActionRecommendation action);

    void addObjectsInLastResort(Product homeProduct, List<Offer> specialOffersModule, List<RowHome> rows, Language language,
        CountryCode cc);

}
