package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.despegar.hestia.api.data.model.DetailAction;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.hotel.HotelItemBuilderCommonsFunctions;
import com.despegar.p13n.hestia.recommend.allinone.matrix.SearchCount;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;

@RunWith(MockitoJUnitRunner.class)
public class LastDetailFunctionTest {

    @InjectMocks
    private LastDetailFunction function = new LastDetailFunction();
    @Mock
    private HotelItemBuilderCommonsFunctions commons;

    @Mock
    private UserContext userContextAccesor;
    @Mock
    private UserRecord userPrifileRecord;


    @Before
    public void setUp() {
        Mockito.when(this.commons.isHotelAvailable(Mockito.any(HotelItem.class))).thenReturn(true);
        Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(this.userPrifileRecord);
        Mockito.when(this.userPrifileRecord.getLastIntenseSearch()).thenReturn(null);

    }

    @Test
    public void testStar() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setOrigin("BUE");
        context.setUserContext(this.userContextAccesor);
        List<Offer> offers = this.function.buildOffers(Product.HOTELS, Product.HOTELS, context, new Param());
        Assert.assertNull(offers);
    }

    @Test
    public void testStarDetail() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setOrigin("BUE");
        context.setUserContext(this.userContextAccesor);
        DetailAction detailAction = new DetailAction();
        detailAction.setUserId("12345");
        detailAction.setProduct(Product.HOTELS);
        detailAction.setFlow(Flow.DETAIL);

        UserActivity lastActivity = new UserActivity(2, "MIA", "123", 2, detailAction, SearchCount.ONE_TO_THREE);

        SearchActivity searchActivity = new SearchActivity();
        searchActivity.addSearchActivity(Product.HOTELS, lastActivity);

        context.setSearchActivity(searchActivity);

        Offer offer = this.function.buildOffers(Product.HOTELS, Product.HOTELS, context, new Param()).get(0);
        Assert.assertEquals("123", ((HotelItem) offer.getOffer()).getHid());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRowUnsupported() {
        this.function.buildRow(Product.HOME_AS_PRODUCT, Product.HOTELS, null, null);
    }
}
