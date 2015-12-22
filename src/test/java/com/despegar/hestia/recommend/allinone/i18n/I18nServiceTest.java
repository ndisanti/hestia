package com.despegar.hestia.recommend.allinone.i18n;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import ar.com.despegar.p13n.hestia.MockitoAnnotationBaseTest;

import com.despegar.framework.lang.Pair;
import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.i18n.I18nService;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleData;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleEnum;
import com.google.common.collect.Lists;


/**
 * @author msarno
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class I18nServiceTest
    extends MockitoAnnotationBaseTest {

	@InjectMocks
    private I18nService i18nService = new I18nService();

    @Mock
    private GeoService geoService;

    private static final String NYC = "NYC";
    private static final String BUE = "BUE";



    @Before
    public void before() {

        Mockito.when(this.geoService.getIataName(NYC, Language.ES.lower())).thenReturn("Nueva York");
        Mockito.when(this.geoService.getIataName(NYC, Language.EN.lower())).thenReturn("New York");
        Mockito.when(this.geoService.getIataName(NYC, Language.PT.lower())).thenReturn("Nova York");
        Mockito.when(this.geoService.getIataName(NYC, Language.UNKNOWN.lower())).thenReturn("Nouveau York");
        Mockito.when(this.geoService.getIataName(BUE, Language.ES.lower())).thenReturn("Buenos Aires");
        Mockito.when(this.geoService.getIataName(BUE, Language.EN.lower())).thenReturn("Buenos Aires");
        Mockito.when(this.geoService.getIataName(BUE, Language.PT.lower())).thenReturn("Buenos Aires");
        Mockito.when(this.geoService.getIataName(BUE, Language.UNKNOWN.lower())).thenReturn("Buenos Aires");

        Mockito.when(this.geoService.normalizeIata(Matchers.anyString())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (String) args[0];
            }
        });

    }

    @Test
    public void testI18nTitles() {
        TitleData td1 = new TitleData(TitleEnum.T1, Product.CARS, BUE, NYC);

        String title1 = this.i18nService.getI18nTitle(td1, Language.ES, CountryCode.AR);
        Assert.assertTrue("Title es_AR", title1.equals("Oferta de Autos en Nueva York"));

        title1 = this.i18nService.getI18nTitle(td1, Language.ES, CountryCode.CO);
        Assert.assertTrue("Title es_CO", title1.equals("Oferta de Carros en Nueva York"));

        title1 = this.i18nService.getI18nTitle(td1, Language.PT, CountryCode.BR);
        Assert.assertTrue("Title pt_BR", title1.equals("Oferta de Carros em Nova York"));

        title1 = this.i18nService.getI18nTitle(td1, Language.EN, CountryCode.US);
        Assert.assertTrue("Title en_US", title1.equals("Car deals in New York"));


        TitleData td2 = new TitleData(TitleEnum.T43, Product.FLIGHTS, BUE, NYC);

        String title2 = this.i18nService.getI18nTitle(td2, Language.ES, CountryCode.AR);
        Assert.assertTrue("Title es_AR",
            title2.equals("Consigue estas ofertas de Vuelos a Nueva York saliendo desde Buenos Aires"));

        title2 = this.i18nService.getI18nTitle(td2, Language.ES, CountryCode.CO);
        Assert.assertTrue("Title es_CO",
            title2.equals("Consigue estas ofertas de Vuelos a Nueva York saliendo desde Buenos Aires"));

        title2 = this.i18nService.getI18nTitle(td2, Language.PT, CountryCode.BR);
        Assert.assertTrue("Title pt_BR", title2.equals("Aproveite as ofertas de Passagens saindo de Buenos Aires"));

        title2 = this.i18nService.getI18nTitle(td2, Language.EN, CountryCode.US);
        Assert.assertTrue("Title en_US", title2.equals("Get deals of Flights from Buenos Aires"));
    }

    @Test
    public void testTitleLoad() {
        // add here all title locales to be tested
        List<Pair<Language, CountryCode>> langCountry = Lists.newLinkedList();
        langCountry.add(new Pair<Language, CountryCode>(Language.EN, CountryCode.US));
        langCountry.add(new Pair<Language, CountryCode>(Language.ES, CountryCode.AR));
        langCountry.add(new Pair<Language, CountryCode>(Language.ES, CountryCode.CO));
        langCountry.add(new Pair<Language, CountryCode>(Language.ES, CountryCode.MX));
        langCountry.add(new Pair<Language, CountryCode>(Language.ES, CountryCode.VE));
        langCountry.add(new Pair<Language, CountryCode>(Language.PT, CountryCode.BR));
        langCountry.add(new Pair<Language, CountryCode>(Language.UNKNOWN, CountryCode.UNKNOWN));

        TitleData td = new TitleData(TitleEnum.T1, Product.CARS, BUE, NYC);

        TitleEnum[] titleEnums = TitleEnum.values();

        for (TitleEnum te : titleEnums) {
            td.setTitle(te);

            for (Pair<Language, CountryCode> pair : langCountry) {

                String title = this.i18nService.getI18nTitle(td, pair.getLeft(), pair.getRight());
                Assert.assertNotNull(title);
                Assert
                    .assertFalse("Title contains invalid character '<' or '>'", title.contains("<") || title.contains(">"));
                Assert
                    .assertFalse("Title contains invalid character '[' or ']'", title.contains("[") || title.contains("["));
            }
        }
    }
}
