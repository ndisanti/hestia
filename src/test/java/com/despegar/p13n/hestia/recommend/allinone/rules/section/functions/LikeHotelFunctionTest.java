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

import ar.com.despegar.p13n.hestia.test.MockitoAnnotationBaseTest;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.euler.commons.client.model.WishList;
import com.despegar.p13n.euler.commons.client.model.WishListEntry;
import com.despegar.p13n.euler.commons.client.model.WishListHotelEntry;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.hotel.HotelItemBuilderCommonsFunctions;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class LikeHotelFunctionTest
    extends MockitoAnnotationBaseTest {

    @InjectMocks
    private LikeHotelFunction function = new LikeHotelFunction();
    @Mock
    private HotelItemBuilderCommonsFunctions commons;
    @Mock
    private UserRecord userPrifileRecord;
    @Mock
    private UserContext userContextAccesor;

    @Before
    public void before() {
        Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(this.userPrifileRecord);
        Mockito.when(this.userPrifileRecord.getLastIntenseSearch()).thenReturn(null);
        Mockito.when(this.commons.isHotelAvailable(Mockito.any(HotelItem.class))).thenReturn(true);
    }

    @Test
    public void testStar() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setUserContext(this.userContextAccesor);
        context.setOrigin("BUE");

        Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(this.userPrifileRecord);

        List<Offer> offers = this.function.buildOffers(Product.HOTELS, Product.HOTELS, context, new Param());
        Assert.assertNull(offers);
    }

    @Test
    public void testStarDetail() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setOrigin("BUE");
        context.setUserContext(this.userContextAccesor);
               
        WishList wishList = new WishList();

        WishListHotelEntry hotelEntry1 = new WishListHotelEntry();
        hotelEntry1.setHotelId("1");

        WishListHotelEntry hotelEntry2 = new WishListHotelEntry();
        hotelEntry2.setHotelId("2");
        hotelEntry1.setProduct(Product.HOTELS);
        hotelEntry2.setProduct(Product.HOTELS);
        List<WishListEntry> wishListEntries = Lists.<WishListEntry> newArrayList(hotelEntry1, hotelEntry2);
        wishList.setWishListEntries(wishListEntries);

        Mockito.when(this.userContextAccesor.getWishlist()).thenReturn(wishList);
        Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(this.userPrifileRecord);
        context.setUserContext(this.userContextAccesor);
        Offer offer = this.function.buildOffers(Product.HOTELS, Product.HOTELS, context, new Param()).get(0);
        Assert.assertEquals("1", ((HotelItem) offer.getOffer()).getHid());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRowUnsupported() {
        this.function.buildRow(Product.HOME_AS_PRODUCT, Product.HOTELS, null, null);
    }
}
