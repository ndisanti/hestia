/**
 * 
 */
package ar.com.despegar.p13n.hestia;

import java.util.EnumSet;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.recommend.allinone.HomeContentRuleService;
import com.despegar.p13n.hestia.recommend.allinone.HomeParam;
import com.despegar.p13n.hestia.recommend.allinone.rules.builder.EngineBuilder;
import com.google.common.net.InetAddresses;


/**
 *
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({"classpath:test-mockhbase-domain-context.xml"})
@Ignore
public class NoHistoryHomeTest {

    @Autowired
    // uso la referencia para que se inicialize porque el contexto esta marcado como lazy y sino no lo hace
    private EngineBuilder builder;

    @Autowired
    private HomeContentRuleService homeContentService;

//    @Autowired
//    private MockGeoService mockGeo;
//
//    @Before
//    public void before() {
//        this.mockGeo.addMainCityForCountry(CountryCode.AR, new CityDto());
//        this.mockGeo.addGeoCountry(new GeoCountry(1l, CountryCode.AR.toString()));
//    }

    @Test
    public void testHomeFlightsAR() {

        HomeContent content = this.homeContentService.getContent(new HomeParam(CountryCode.AR, InetAddresses
            .forString("134.210.228.76"), EnumSet.of(Product.FLIGHTS), "NEW-UUID", Language.ES, false, false, null, null,
            null));

        System.out.println(content.getProducts().get(Product.FLIGHTS));
    }


}
