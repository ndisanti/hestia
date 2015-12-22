/**
 * 
 */
package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import org.junit.Assert;
import org.junit.Test;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.data.ClosedPackageData;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesDestinationItem;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.api.data.model.SearchAction;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.matrix.SearchCount;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleData;
import com.google.common.net.InetAddresses;


public class PackageSearchFunctionTest {

    private PackageSearchFunction function = new PackageSearchFunction();

    @Test
    public void testClosedPackagesDestination() {

        final ActionRecommendation context = this.buildPackageSearch("MIA");
        context.setOrigin("BUE");

        Offer offer = this.function.buildOffers(Product.HOME_AS_PRODUCT, Product.CLOSED_PACKAGES, context, null).get(0);

        Assert.assertNotNull(offer);
        Assert.assertNotNull(offer.getOffer());
        Assert.assertTrue(offer.getOffer() instanceof ClosedPackagesDestinationItem);
        Assert.assertEquals("MIA", ((ClosedPackagesDestinationItem) offer.getOffer()).getDestination());
    }


    @Test
    public void testClosedPackagesItem() {

        final ActionRecommendation context = this.buildPackageDetail("12345");

        Offer offer = this.function.buildOffers(Product.HOME_AS_PRODUCT, Product.CLOSED_PACKAGES, context, null).get(0);

        Assert.assertNotNull(offer.getOffer());
        Assert.assertTrue(offer.getOffer() instanceof ClosedPackagesItem);
        Assert.assertEquals("12345", ((ClosedPackagesItem) offer.getOffer()).getCluid());
    }

    private ActionRecommendation buildPackageSearch(String destination) {
        final ActionRecommendation context = new ActionRecommendation("12345", Product.HOTELS, CountryCode.AR, Language.ES,
            InetAddresses.forString("123.123.123.123"), RulesVersion.DYNAMIC_SERVICE, null, null);

        SearchAction searchAction = new SearchAction();
        searchAction.setUserId("12345");
        searchAction.setProduct(Product.CLOSED_PACKAGES);
        searchAction.setFlow(Flow.SEARCH);

        UserActivity lastActivity = new UserActivity(2, destination, "", 2, searchAction, SearchCount.ONE_TO_THREE);

        SearchActivity searchActivity = new SearchActivity();
        searchActivity.addSearchActivity(Product.CLOSED_PACKAGES, lastActivity);

        context.setSearchActivity(searchActivity);
        context.setTitleData(new TitleData());
        context.setCurrentSection(SectionsEnum.OFFER);
        return context;
    }

    private ActionRecommendation buildPackageDetail(String cluid) {
        final ActionRecommendation context = new ActionRecommendation("12345", Product.HOTELS, CountryCode.AR, Language.ES,
            InetAddresses.forString("123.123.123.123"), RulesVersion.DYNAMIC_SERVICE, null, null);

        SearchAction searchAction = new SearchAction();
        searchAction.setUserId("12345");
        searchAction.setProduct(Product.CLOSED_PACKAGES);
        searchAction.setFlow(Flow.DETAIL);

        searchAction.getActionData().put(ClosedPackageData.CLUID, cluid);

        UserActivity lastActivity = new UserActivity(2, "MIA", "", 2, searchAction, SearchCount.ONE_TO_THREE);

        SearchActivity searchActivity = new SearchActivity();
        searchActivity.addSearchActivity(Product.CLOSED_PACKAGES, lastActivity);

        context.setSearchActivity(searchActivity);
        context.setTitleData(new TitleData());
        context.setCurrentSection(SectionsEnum.OFFER);
        return context;
    }

}
