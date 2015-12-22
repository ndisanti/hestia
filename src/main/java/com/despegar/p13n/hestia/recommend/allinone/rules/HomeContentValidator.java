/**
 * 
 */
package com.despegar.p13n.hestia.recommend.allinone.rules;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.HomeUtils;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesDestinationItem;
import com.despegar.p13n.hestia.api.data.model.FlightDestinationItem;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.HotelDestinationItem;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.data.model.Title;
import com.despegar.p13n.hestia.api.data.model.Titles;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.google.common.base.Preconditions;
import com.newrelic.api.agent.Trace;

/**
 *
 */
@Component
public class HomeContentValidator {


    protected static final Logger LOG = LoggerFactory.getLogger(HomeContentValidator.class);

    @Value("${homecontent.lastResort.enabled:false}")
    private boolean lastResortEnabled;

    @Value("${homecontent.lastResort.validate:false}")
    private boolean validateLastResort;

    @PostConstruct
    private void init() {
        LOG.info("Home content: last resort enabled: {}", this.lastResortEnabled);
        LOG.info("Home content: validating last resort: {}", this.validateLastResort);
    }


    /**
     * @param product
     * @param content
     * @param action
     */
    @Trace
    public void validate(Product product, HomeContent content, ActionRecommendation action) {

        final Map<Product, HomeProduct> productsContent = content.getProducts();
        String userId = action.getUserId();
        CountryCode countryCode = action.getCountryCode();
        Language languaje = action.getLanguage();
        for (HomeProduct homeProduct : productsContent.values()) {

            Preconditions.checkNotNull(homeProduct, "HomeProduct not found. id: %s, pr: %s, cc: %s, lan: %s", userId,
                product, countryCode, languaje);

            this.checkRowOffer(homeProduct.getSpecialOffersModule(), userId, product, countryCode, languaje);

            List<RowHome> rowModules = homeProduct.getRowModules();
            Preconditions.checkNotNull(rowModules, "Rows not found. id: %s, pr: %s, cc: %s, lan: %s", userId, product,
                countryCode, languaje);
            Preconditions.checkState(rowModules.size() >= 3, "Rows are of diferent size:", rowModules.size());

            int rowNum = 1;


            for (RowHome row : rowModules) {
                this.checkRow("row#" + rowNum, row, userId, product, countryCode, languaje);
                rowNum++;
            }
        }

    }

    /**
     * @param product
     * @param cc
     * @param lan
     * @param homeProduct
     * @param id
     * @param isRowOffer
     */
    @Trace
    public void validate(Product product, CountryCode cc, Language lan, HomeProduct homeProduct, String id) {

        Preconditions.checkNotNull(homeProduct, "HomeProduct not found. id: %s, pr: %s, cc: %s, lan: %s", id, product, cc,
            lan);

        this.checkRowOffer(homeProduct.getSpecialOffersModule(), id, product, cc, lan);
        List<RowHome> rowModules = homeProduct.getRowModules();
        Preconditions.checkNotNull(rowModules, "Rows not found. id: %s, pr: %s, cc: %s, lan: %s", id, product, cc, lan);
        Preconditions.checkState(rowModules.size() >= 3, //
            "Rows are of diferent size: size: %s. id: %s, pr: %s, cc: %s, lan: %s", rowModules.size(), id, product, cc, lan);

        for (int i = 0; i < rowModules.size(); i++) {
            RowHome row = rowModules.get(i);
            this.checkRow("row#" + i + 1, row, id, product, cc, lan);
        }

    }

    private void checkRow(final String labelRow, RowHome row, String id, Product pr, CountryCode cc, Language lan) {
        Preconditions.checkNotNull(row, labelRow + " is null. id: %s, pr: %s, cc: %s, lan: %s", id, pr, cc, lan);


        Titles titles = row.getTitles();
        Preconditions.checkNotNull(titles, labelRow + ".titles is null. id: %s, pr: %s, cc: %s, lan: %s", id, pr, cc, lan);
        this.checkTitle(labelRow + ".main", titles.getMainTitle());

        List<? extends ItemHome> offers = row.getOffers();
        Preconditions.checkState(offers.size() >= HomeUtils.MIN_ROW_SIZE, "Offers of " + labelRow + " are too few: %s",
            offers.size());
        Preconditions.checkState(offers.size() <= HomeUtils.MAX_ROW_SIZE, "Offers of " + labelRow + " are too many: %s",
            offers.size());

        int i = 0;
        for (ItemHome itemHome : offers) {
            this.checkItem(labelRow + "[" + i + "]", itemHome);
            i++;
        }
    }

    private void checkOffer(Offer offer, String id, Product product, CountryCode cc, Language lan) {
        Preconditions.checkNotNull(offer, "Offer is null id: %s, pr: %s, cc: %s, lan: %s", id, product, cc, lan);

        if (offer.isTitleRequired()) {
            this.checkTitle("Offer", offer.getTitleOffer());
        }

        this.checkItem("Offer", offer.getOffer());
    }

    private void checkRowOffer(List<Offer> offers, String id, Product product, CountryCode cc, Language lan) {
        for (Offer offer : offers) {
            this.checkOffer(offer, id, product, cc, lan);
        }
    }


    private void checkTitle(String label, final Title titleOffer) {
        Preconditions.checkNotNull(titleOffer, "Title of %s is null", label);
        Preconditions.checkNotNull(titleOffer.getTitleId(), "TitleId of %s is null", label);
    }

    private void checkItem(String label, ItemHome offer) {
        Preconditions.checkNotNull(offer, "Item of %s is null", label);

        if (offer instanceof HotelItem) {

            HotelItem hotel = (HotelItem) offer;
            this.checkValue(hotel.getHid(), "HotelId", label);

        } else if (offer instanceof HotelDestinationItem) {

            HotelDestinationItem destinaton = (HotelDestinationItem) offer;
            this.checkValue(destinaton.getDestination(), "Hotel Destination", label);

        } else if (offer instanceof FlightDestinationItem) {

            FlightDestinationItem flight = (FlightDestinationItem) offer;
            this.checkValue(flight.getDestination(), "Flight Destination", label);
            this.checkValue(flight.getOrigin(), "Flight Origin", label);

            Preconditions.checkState(!flight.getDestination().equals(flight.getOrigin()),
                "Flight origin and destinantions are the same in %s", label);
        } else if (offer instanceof ClosedPackagesDestinationItem) {

            ClosedPackagesDestinationItem cp = (ClosedPackagesDestinationItem) offer;
            this.checkValue(cp.getDestination(), "CP Destination", label);
            this.checkValue(cp.getOrigin(), "CP Origin", label);

            Preconditions.checkState(!cp.getDestination().equals(cp.getOrigin()),
                "CP origin and destinantions are the same in %s", label);
        } else {
            // Preconditions.checkState(false, "Item of %s is of unsuported type: %s", label, offer.getClass());
        }

    }

    public boolean isLastResortEnabled() {
        return this.lastResortEnabled;
    }

    public boolean isValidateLastResort() {
        return this.validateLastResort;
    }

    private void checkValue(final String hid, final String name, final String label) {
        Preconditions.checkNotNull(hid, "%s of %s is null", name, label);
        Preconditions.checkState(StringUtils.isNotEmpty(hid), "%s of %s is empty", name, label);
        Preconditions.checkState(!hid.equals("null"), "%s of %s is string 'null'", name, label);
    }
}
