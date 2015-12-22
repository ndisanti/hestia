package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import static org.mockito.Mockito.when;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.test.MockitoAnnotationBaseTest;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.external.geo.CityDto;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleData;


@RunWith(MockitoJUnitRunner.class)
public class LastResortFunctionTest
    extends MockitoAnnotationBaseTest {


    @Mock
    private Properties rankings;
    @Mock
    private GeoService geoService;
    @Mock
    private RankingsClient hotRankingService;
    @InjectMocks
    LastResortFunction function = new LastResortFunction();

    @Test
    public void test() throws UnknownHostException {

        final ActionRecommendation context = new ActionRecommendation("userid", Product.HOTELS, CountryCode.AR, Language.ES,
            InetAddress.getByName("127.0.0.1"), RulesVersion.DYNAMIC_SERVICE, null, null);
        context.setOrigin("BUE");
        context.setTitleData(new TitleData());
        context.setCurrentHome(Product.CARS);

        RankingTreeDTO ranking = new RankingTreeDTO();

        for (long i = 1; i <= 28; i++) {
            ranking.addPosition(new RankingPositionDTO("id-" + i, i));
        }

        Mockito.when(this.hotRankingService.getRanking(Mockito.any(Product.class), Mockito.any(StaticRankingTypes.class),Mockito.any(CountryCode.class), Mockito.anyInt())).thenReturn(ranking);
        CityDto cityDTO = new CityDto();
        cityDTO.setCode("BUE");
        when(this.geoService.getMainCityFromCountry(CountryCode.AR)).thenReturn(cityDTO);
        when(this.rankings.get(Mockito.anyString())).thenReturn(
            "MIA,ORL,NYC,RIO,BUE,CUN,SCL,ROM,LON,BCL,SAO,LAX,IGU,LAS,MEX,ATL");
       Mockito.when(this.geoService.normalizeIata(Mockito.anyString())).thenReturn("MIA","ORL","NYC","RIO","BUE","CUN","SCL","ROM","LON","BCL","SAO","LAX","IGU","LAS","MEX","ATL","ABC","DEF","GHI","MMM","NNN","OOO");
        context.setCurrentSection(SectionsEnum.OFFER);
        Offer offer = this.function.buildOffers(Product.HOTELS, Product.HOTELS, context,
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build()).get(0);

        Assert.assertEquals("MIA", offer.getOffer().getId());

        context.setCurrentSection(SectionsEnum.ROW1);
        RowHome rowHome1 = this.function.buildRow(Product.HOTELS, Product.HOTELS, context,
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build());

        Assert.assertEquals("ORL", rowHome1.getOffers().get(0).getId());
        Assert.assertEquals("NYC", rowHome1.getOffers().get(1).getId());
        Assert.assertEquals("RIO", rowHome1.getOffers().get(2).getId());
        Assert.assertEquals("BUE", rowHome1.getOffers().get(3).getId());
        Assert.assertEquals("CUN", rowHome1.getOffers().get(4).getId());
        Assert.assertEquals("SCL", rowHome1.getOffers().get(5).getId());
        Assert.assertEquals("ROM", rowHome1.getOffers().get(6).getId());

        context.setCurrentSection(SectionsEnum.ROW2);
        RowHome rowHome2 = this.function.buildRow(Product.HOTELS, Product.HOTELS, context,
            Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build());

        Assert.assertEquals("LAX", rowHome2.getOffers().get(0).getId());
        Assert.assertEquals("IGU", rowHome2.getOffers().get(1).getId());
        Assert.assertEquals("LAS", rowHome2.getOffers().get(2).getId());
        Assert.assertEquals("MEX", rowHome2.getOffers().get(3).getId());
        Assert.assertEquals("ATL", rowHome2.getOffers().get(4).getId());
        Assert.assertEquals("ABC", rowHome2.getOffers().get(5).getId());
        Assert.assertEquals("DEF", rowHome2.getOffers().get(6).getId());

    }
}
