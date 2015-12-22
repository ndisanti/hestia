package com.despegar.hestia.recommend.allinone.item.hotel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.WishList;
import com.despegar.p13n.euler.commons.client.model.WishListEntry;
import com.despegar.p13n.euler.commons.client.model.WishListHotelEntry;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.HomeUserLocation;
import com.despegar.p13n.hestia.recommend.allinone.item.hotel.DefaultHotelStep;
import com.despegar.p13n.hestia.recommend.allinone.item.hotel.DetailDestinationHotelStep;
import com.despegar.p13n.hestia.recommend.allinone.item.hotel.HotelItemBuilderCommonsFunctions;
import com.despegar.p13n.hestia.recommend.allinone.item.hotel.HotelItemSteps;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;

@RunWith(MockitoJUnitRunner.class)
public class HotelItemStepsTest {

	@InjectMocks
    private HotelItemSteps steps = new HotelItemSteps();
    @Mock
    private DefaultHotelStep defStep;
    @Mock
    private DetailDestinationHotelStep detailDestinationStep;
    @Mock
    private HotelItemBuilderCommonsFunctions commons;


    @Test
    public void testGetItemDef() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.HOTELS, "MIA");
        action.setCurrentHome(Product.HOTELS);
        action.setCurrentSection(SectionsEnum.ROW1);
        WishList wishList = new WishList();
        wishList.processLike(this.createWishListHotelEntry("24000", "SYD"));
        wishList.processLike(this.createWishListHotelEntry("22000", "NYC"));
        UserContext userContext = Mockito.mock(UserContext.class);
        action.setUserContext(userContext);
        HomeUserLocation homeUserLocation = new HomeUserLocation();
        homeUserLocation.setIp("127.0.0.1");
        Mockito.when(userContext.getWishlist()).thenReturn(wishList);
        HotelItem itemHome2 = new HotelItem("24000");
        Mockito.when(this.defStep.execute("MIA", action)).thenReturn(itemHome2);
        ItemHome item = this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("24000"));
        HotelItem itemHome3 = new HotelItem("22000");
        Mockito.when(this.defStep.execute("MIA", action)).thenReturn(itemHome3);
        item = this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("22000"));
        HotelItem itemHome = new HotelItem("12345");
        Mockito.when(this.defStep.execute("MIA", action)).thenReturn(itemHome);
        item = this.steps.execute("MIA", action);
        Mockito.verifyNoMoreInteractions(this.detailDestinationStep);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals(itemHome.getHid()));
    }

    @Test
    public void testGetItemDetail() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Detail(Product.HOTELS, "MIA", "12345");
        action.setCurrentHome(Product.HOTELS);
        action.setCurrentSection(SectionsEnum.ROW1);
        WishList wishList = new WishList();
        wishList.processLike(this.createWishListHotelEntry("24000", "SYD"));
        wishList.processLike(this.createWishListHotelEntry("22000", "NYC"));
        UserContext userContext = Mockito.mock(UserContext.class);
        action.setUserContext(userContext);
        HomeUserLocation homeUserLocation = new HomeUserLocation();
        homeUserLocation.setIp("127.0.0.1");
        Mockito.when(userContext.getWishlist()).thenReturn(wishList);
        HotelItem itemHome2 = new HotelItem("24000");
        Mockito.when(this.detailDestinationStep.execute("MIA", action)).thenReturn(itemHome2);
        ItemHome item = this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("24000"));
        HotelItem itemHome3 = new HotelItem("22000");
        Mockito.when(this.detailDestinationStep.execute("MIA", action)).thenReturn(itemHome3);
        item = this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("22000"));
        HotelItem itemHome = new HotelItem("12345");
        Mockito.when(this.detailDestinationStep.execute("MIA", action)).thenReturn(itemHome);
        item = this.steps.execute("MIA", action);
        Mockito.verifyNoMoreInteractions(this.defStep);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals(itemHome.getHid()));
    }

    private WishListEntry createWishListHotelEntry(String hotelId, String destination) {
        WishListHotelEntry entry = new WishListHotelEntry();
        entry.setProduct(Product.HOTELS);
        entry.setHotelId(hotelId);
        entry.setDestinationCode(destination);
        return entry;
    }
}
