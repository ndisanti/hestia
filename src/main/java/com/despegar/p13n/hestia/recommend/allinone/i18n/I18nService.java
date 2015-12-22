package com.despegar.p13n.hestia.recommend.allinone.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleData;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleEnum;
import com.google.common.base.Preconditions;
import com.newrelic.api.agent.Trace;

/**
 * @author msarno
 *
 */
@Service
public class I18nService {

    private static final String PRODUCT_SINGULAR_TAG = "<PRODUCT_SINGULAR>";
    private static final String PRODUCT_PLURAL_TAG = "<PRODUCT_PLURAL>";
    private static final String ORIGIN_TAG = "<IATA_ORIGIN>";
    private static final String DESTINATION_TAG = "<IATA_DESTINATION>";
    private static final String MESSAGES = "i18n.home.dinamic-titles";
    private static final String PRODUCTS_SINGULAR = "i18n.model.product-singular-names";
    private static final String PRODUCTS_PLURAL = "i18n.model.product-plural-names";

    @Autowired
    private GeoService geoService;

    @Trace
    public String getI18nTitle(TitleData td, Language language, CountryCode country) {

        Locale locale = new Locale(language.name(), country.name());
        String title = this.getTitle(td.getTitle(), locale);

        if (td.getTitle().isProductRequired()) {
            String prodReplace = this.getProductSingular(td.getOffer(), locale);
            Preconditions.checkNotNull(prodReplace);
            title = title.replaceAll(PRODUCT_SINGULAR_TAG, prodReplace);

            prodReplace = this.getProductPlural(td.getOffer(), locale);
            Preconditions.checkNotNull(prodReplace);
            title = title.replaceAll(PRODUCT_PLURAL_TAG, prodReplace);
        }

        if (td.getTitle().isOriginRequired()) {
            String originReplace = this.geoService.getIataName(td.getOrigin(), locale.getLanguage());
            Preconditions.checkNotNull(originReplace);
            title = title.replaceAll(ORIGIN_TAG, originReplace);
        }

        if (td.getTitle().isDestinationRequired()) {
            String destinationReplace = this.geoService.getIataName(td.getDestinations().iterator().next(),
                locale.getLanguage());
            Preconditions.checkNotNull(destinationReplace);
            title = title.replaceAll(DESTINATION_TAG, destinationReplace);
        }

        return title;
    }

    public String getTitle(TitleEnum title, Locale locale) {
        Preconditions.checkNotNull(title);
        Preconditions.checkNotNull(locale);
        ResourceBundle messages = ResourceBundle.getBundle(MESSAGES, locale);
        Preconditions.checkNotNull(messages);
        return messages.getString(title.name());
    }

    private String getProductSingular(Product offer, Locale locale) {
        Preconditions.checkNotNull(offer);
        Preconditions.checkNotNull(locale);
        ResourceBundle messages = ResourceBundle.getBundle(PRODUCTS_SINGULAR, locale);
        Preconditions.checkNotNull(messages);
        return messages.getString(offer.name());
    }

    private String getProductPlural(Product offer, Locale locale) {
        Preconditions.checkNotNull(offer);
        Preconditions.checkNotNull(locale);
        ResourceBundle messages = ResourceBundle.getBundle(PRODUCTS_PLURAL, locale);
        Preconditions.checkNotNull(messages);
        return messages.getString(offer.name());
    }
}
