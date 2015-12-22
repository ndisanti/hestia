package com.despegar.p13n.hestia.recommend.allinone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.EmailSource;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.hestia.api.data.model.AlertItem;
import com.despegar.p13n.hestia.api.data.model.BankOfferItem;
import com.despegar.p13n.hestia.api.data.model.CityReviewItem;
import com.despegar.p13n.hestia.api.data.model.HappyClientItem;
import com.despegar.p13n.hestia.api.data.model.HolidaysItem;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.MarketingBannersItem;
import com.despegar.p13n.hestia.api.data.model.NewsletterItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.data.model.SecretOfferItem;
import com.despegar.p13n.hestia.api.data.model.Title;
import com.despegar.p13n.hestia.api.data.model.TrustPilotItem;
import com.despegar.p13n.hestia.api.data.model.VacationRentalBanner;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.i18n.I18nService;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleBuilder;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleData;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleEnum;
import com.despegar.p13n.hestia.service.MultiObjectVersionService;
import com.despegar.p13n.hestia.utils.ProductCountrySupportUtils;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

@Service
public class MultiObjectFiller
    implements MultiObjectVersionService {

    private static final String ROUNDTRIP = "ROUNDTRIP";

    private List<String> destinations = Lists.newArrayList("DUB", "RIO", "SYD", "MIA", "LAS", "PAR", "NYC", "LON", "AKL",
        "ORL", "BKK", "AMS", "ROM", "TFN", "BCN", "SFO", "CHI", "YTO", "VCE", "LAX");
    private DateTime date = new DateTime(2016, 1, 1, 0, 0);

    @Autowired
    private I18nService i18nService;
    @Autowired
    private GeoService geoService;

    private int searchDestinationDemand = 9;

    @Override
    public HomeContent addObjectsInRows(HomeContent homeContent, ActionRecommendation action,
        MultiObjecHomeVersion multiObjHomeVersion) {

        switch (multiObjHomeVersion) {
        case V4_ROW4:
            for (Entry<Product, HomeProduct> element : homeContent.getProducts().entrySet()) {
                this.fillRows(element.getKey(), element.getValue().getRowModules(), multiObjHomeVersion.getItems(),
                    action.getCountryCode(), action.getLanguage());
            }
            break;

        default:
            boolean isTopSearchDestionation = this.getSearchDestinationsCount(action) >= this.searchDestinationDemand;

            UserRecord userProfile = action.getUserContext().getUserRecord();
            for (Entry<Product, HomeProduct> element : homeContent.getProducts().entrySet()) {
                this.fillRows(element, multiObjHomeVersion.getItems(), action, isTopSearchDestionation, userProfile);
            }
        }
        return homeContent;
    }

    @SuppressWarnings("unchecked")
    private void fillRows(Product product, List<RowHome> rows, List<ItemHomeCoordinate> items, CountryCode cc,
        Language language) {

        if ((CountryCode.isInternational(cc) || cc.equals(CountryCode.VE))) {
            product = product.equals(Product.CLOSED_PACKAGES) ? Product.HOTELS : product;
        }
        RowHome emptyRowHome = new RowHome(null, new ArrayList<ItemHome>());
        this.updateTitle(product, cc, language, emptyRowHome);
        rows.add(emptyRowHome);


        if ((CountryCode.isInternational(cc) || cc.equals(CountryCode.VE))) {
            product = product.equals(Product.CLOSED_PACKAGES) ? Product.HOTELS : product;
        }
        for (ItemHomeCoordinate item : items) {

            List<ItemHome> row = (List<ItemHome>) rows.get(item.getRow().getIndex() - 1).getOffers();
            ItemHome itemHome = null;

            switch (item.getProduct()) {

            case HAPPY_CLIENT:
                itemHome = this.getHappyClientItem(product);
                break;
            case TRUST_PILOT:
                itemHome = this.getTrustPilotItem();
                break;
            default:
                throw new NoSuchElementException();
            }

            if (itemHome != null) {
                if (row.size() < item.getCol()) {
                    row.add(itemHome);
                } else {
                    row.add(item.getCol() - 1, itemHome);
                }
            }
        }
    }

    private void updateTitle(Product product, CountryCode cc, Language language, RowHome emptyRowHome) {
        TitleEnum title = TitleEnum.T68;
        Title t = TitleBuilder.builder(title)//
            .iata(null)//
            .pr(product)//
            .origin(null)//
            .build();
        emptyRowHome.getTitles().setMainTitle(t);
        if (language != Language.UNKNOWN) {
            TitleData titleData = new TitleData(title);
            String titleDesc = this.i18nService.getI18nTitle(titleData, language, cc);
            t.setTitleDesc(titleDesc);
        }
    }

    @SuppressWarnings("unchecked")
    private void fillRows(Entry<Product, HomeProduct> element, List<ItemHomeCoordinate> list, ActionRecommendation action,
        boolean isTopSearchDestionation, UserRecord userProfile) {

        for (ItemHomeCoordinate item : list) {

            List<ItemHome> row = (List<ItemHome>) element.getValue().getRowModules().get(item.getRow().getIndex() - 1)
                .getOffers();
            ItemHome itemHome = null;

            switch (item.getProduct()) {

            case NEWSLETTER_AS_PRODUCT:
                itemHome = this.getRowNewsletterItem(userProfile, action.getLastDestination(), element.getKey());
                break;

            case SECRET_OFFER_AS_PRODUCT:
                itemHome = this.getSecretOffer();
                break;

            case FARE_ALERT:
                itemHome = this.getAlertItem(action.getLastDestination(), element.getKey());
                break;

            case CITY_REVIEW:
                itemHome = this.getCityReviewItem(action, element, item, isTopSearchDestionation);
                break;

            case HAPPY_CLIENT:
                itemHome = this.getHappyClientItem(element.getKey());
                break;

            case HOLIDAYS:
                itemHome = ProductCountrySupportUtils.isSupported(action.getCountryCode(), Product.FLIGHTS)
                    && this.date.isAfterNow() ? this.getHolidaysItem(action.getHomeUserLocation().getCity()) : this
                    .getHappyClientItem(element.getKey());
                break;

            case TRUST_PILOT:
                itemHome = this.getTrustPilotItem();
                break;

            default:
                throw new NoSuchElementException();
            }

            if (itemHome != null) {
                if (row.size() < item.getCol()) {
                    row.add(itemHome);
                } else {
                    row.add(item.getCol() - 1, itemHome);
                }
            }

        }
    }

    private ItemHome getHolidaysItem(String origin) {

        Collections.shuffle(this.destinations);
        String destination = this.destinations.get(0);
        destination = this.geoService.normalizeIata(destination);
        origin = this.geoService.normalizeToCityAirport(origin);
        return origin == null || destination == null ? null : new HolidaysItem(origin, destination, ROUNDTRIP);
    }

    private ItemHome getRowNewsletterItem(UserRecord userProfile, String destination, Product product) {

        NewsletterItem nl = this.getNewsLetterItem(userProfile);
        return nl == null ? this.getAlertItem(destination, product) : nl;
    }

    private NewsletterItem getNewsLetterItem(UserRecord userProfile) {
        if (userProfile.getEmailSources().values().isEmpty()) {
            return new NewsletterItem();

        } else {
            for (EmailSource mail : userProfile.getEmailSources().values()) {
                if (!EmailSource.SUBSCRIBE.equals(mail)) {
                    return new NewsletterItem();
                }
            }
        }
        return null;
    }

    private ItemHome getSecretOffer() {
        return new SecretOfferItem();
    }

    private String getRowDestination(RowHome row, ActionRecommendation action, SectionsEnum rowPosition) {

        return row.isMonoDestination() ? action.getSectionsDestinations().get(rowPosition).iterator().next() : action
            .getLastDestination();
    }

    private TrustPilotItem getTrustPilotItem() {
        return new TrustPilotItem();
    }

    private ItemHome getHappyClientItem(Product product) {
        product = this.override(product);
        return new HappyClientItem(product);
    }

    private ItemHome getCityReviewItem(ActionRecommendation action, Entry<Product, HomeProduct> element,
        ItemHomeCoordinate item, boolean isTopSearchDest) {

        if (this.isValidCountryForCityreview(action.getCountryCode())) {
            boolean isBuy = action.getBuyActivity() != null;
            boolean isCheckout = this.isCheckOut(action, element.getKey());

            if (isCheckout || isBuy || isTopSearchDest) {
                String rowDestination = this.getRowDestination(
                    element.getValue().getRowModules().get(item.getRow().getIndex() - 1), action, item.getRow());
                return new CityReviewItem(rowDestination);
            }
            return this.getHappyClientItem(element.getKey());
        }
        return null;
    }

    private boolean isValidCountryForCityreview(CountryCode countryCode) {
        return !CountryCode.isInternational(countryCode) && !countryCode.equals(CountryCode.US);
    }

    private boolean isCheckOut(ActionRecommendation action, Product product) {

        boolean isCheckout = false;
        if (action.getSearchActivity() != null) {
            UserActivity activity = action.getSearchActivity().getActivity(product);
            isCheckout = activity == null ? false : activity.getFlow().equals(Flow.CHECKOUT);
        }
        return isCheckout;
    }

    private int getSearchDestinationsCount(ActionRecommendation action) {

        Map<String, Integer> destinationCounter = action.getSearchDestination();

        int searchCount = 0;
        for (Entry<String, Integer> entry : destinationCounter.entrySet()) {
            searchCount = entry.getValue() >= searchCount ? searchCount + entry.getValue() : searchCount;
        }
        return searchCount;
    }

    private ItemHome getAlertItem(String destination, Product product) {
        return this.isValidAlertProduct(product) ? new AlertItem(destination, product) : null;
    }

    private boolean isValidAlertProduct(Product product) {
        return EnumSet.of(Product.FLIGHTS, Product.HOTELS, Product.CLOSED_PACKAGES).contains(product);
    }

    private Product override(Product product) {
        return product.equals(Product.HOME_AS_PRODUCT) ? Product.HOTELS : product;
    }

    private void generateRotator(Product product, List<Offer> specialOffersModule, CountryCode cc,
        UserRecord userProfile) {

        switch (product) {
        case VACATIONRENTALS:
            Offer vrOffer = new Offer(new VacationRentalBanner());
            specialOffersModule.add(vrOffer);
            break;

        default:

            if (cc.equals(CountryCode.MX) || cc.equals(CountryCode.CL)) {
                Offer offer = new Offer(new BankOfferItem());
                specialOffersModule.add(offer);
            }
            Offer offer = new Offer(new MarketingBannersItem());
            specialOffersModule.add(offer);
        }
    }

    @VisibleForTesting
    public void setSearchDestinationDemand(int limit) {
        this.searchDestinationDemand = limit;
    }

    @Override
    public void addObjectsInOffers(HomeContent homeContent, ActionRecommendation action) {

        UserRecord userProfile = action.getUserContext().getUserRecord();

        for (Entry<Product, HomeProduct> element : homeContent.getProducts().entrySet()) {
            this.generateRotator(element.getKey(), element.getValue().getSpecialOffersModule(), action.getCountryCode(),
                userProfile);
        }
    }

    @Override
    public void addObjectsInLastResort(Product homeProduct, List<Offer> specialOffersModule, List<RowHome> rows,
        Language language, CountryCode cc) {
        this.generateRotator(homeProduct, specialOffersModule, language, cc);
        this.addObjectsInRowsLastResort(homeProduct, rows, cc, language);
    }

    private void generateRotator(Product homeProduct, List<Offer> specialOffersModule, Language language, CountryCode cc) {

        switch (homeProduct) {
        case VACATIONRENTALS:
            Offer vrOffer = new Offer(new VacationRentalBanner());
            specialOffersModule.add(vrOffer);
            break;

        default:
            if (cc.equals(CountryCode.MX) || cc.equals(CountryCode.CL)) {
                Offer offer = new Offer(new BankOfferItem());
                specialOffersModule.add(offer);
            }
            Offer offer = new Offer(new MarketingBannersItem());
            specialOffersModule.add(offer);
        }
    }

    private void addObjectsInRowsLastResort(Product product, List<RowHome> rows, CountryCode cc, Language language) {
        this.fillRows(product, rows, MultiObjecHomeVersion.V4_ROW4.getItems(), cc, language);
    }

}
