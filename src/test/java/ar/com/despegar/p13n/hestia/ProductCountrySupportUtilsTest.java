package ar.com.despegar.p13n.hestia;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.utils.ProductCountrySupportUtils;

public class ProductCountrySupportUtilsTest {

    @Test
    public void test() throws Exception {
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.AR, Product.HOTELS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.BO, Product.FLIGHTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.VE, Product.CRUISES));
    }

    @Test
    public void homeSupport() throws Exception {
        Assert.assertEquals(EnumSet.of(Product.HOTELS, Product.CARS, Product.ACTIVITIES, Product.HOME_AS_PRODUCT,
            Product.VACATIONRENTALS, Product.INSURANCE), ProductCountrySupportUtils.getHomesSupported(CountryCode.BO));
    }

    @Test
    public void testCountryCode() throws Exception {
        Assert.assertFalse(CountryCode.ALL.contains(CountryCode.UNKNOWN));
        Assert.assertTrue(CountryCode.ALL.contains(CountryCode.AR));
        Assert.assertTrue(CountryCode.RELEVANT_INTERNATIONAL.contains(CountryCode.IE));
        Assert.assertFalse(CountryCode.INTERNATIONAL.contains(CountryCode.AR));
    }

    @Test
    public void homeSupportForAllCountries() {

        // BOLIVIA
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.BO, Product.FLIGHTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.BO, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.BO, Product.COMBINED_PRODUCTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.BO, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.BO, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.BO, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.BO, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.BO, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.BO, Product.VACATIONRENTALS));

        // COSTA RICA
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.CR, Product.CRUISES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.CR, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CR, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CR, Product.CLOSED_PACKAGES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CR, Product.COMBINED_PRODUCTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CR, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CR, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CR, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CR, Product.VACATIONRENTALS));

        // REPUBLICA DOMINICANA
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.DO, Product.FLIGHTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.DO, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.DO, Product.COMBINED_PRODUCTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.DO, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.DO, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.DO, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.DO, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.DO, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.DO, Product.VACATIONRENTALS));

        // ECUADOR
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.EC, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.EC, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.EC, Product.CLOSED_PACKAGES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.EC, Product.COMBINED_PRODUCTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.EC, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.EC, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.EC, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.EC, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.EC, Product.VACATIONRENTALS));

        // ESPAÑA
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.ES, Product.CRUISES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.ES, Product.FLIGHTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.ES, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.ES, Product.COMBINED_PRODUCTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.ES, Product.ACTIVITIES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.ES, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.ES, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.ES, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.ES, Product.VACATIONRENTALS));

        // GUATEMALA
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.GT, Product.CRUISES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.GT, Product.FLIGHTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.GT, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.GT, Product.COMBINED_PRODUCTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.GT, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.GT, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.ES, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.ES, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.ES, Product.VACATIONRENTALS));

        // HONDURAS
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.HN, Product.CRUISES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.HN, Product.FLIGHTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.HN, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.HN, Product.COMBINED_PRODUCTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.HN, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.HN, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.HN, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.HN, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.HN, Product.VACATIONRENTALS));

        // NICARAGUA
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.NI, Product.CRUISES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.NI, Product.FLIGHTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.NI, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.NI, Product.COMBINED_PRODUCTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.NI, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.NI, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.NI, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.NI, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.NI, Product.VACATIONRENTALS));

        // PANAMA
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.PA, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PA, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PA, Product.CLOSED_PACKAGES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PA, Product.COMBINED_PRODUCTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PA, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PA, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PA, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PA, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PA, Product.VACATIONRENTALS));

        // PERU
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.PE, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PE, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PE, Product.CLOSED_PACKAGES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PE, Product.COMBINED_PRODUCTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PE, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PE, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PE, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PE, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PE, Product.VACATIONRENTALS));

        // PUERTO RICO
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.PR, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PR, Product.FLIGHTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.PR, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.PR, Product.COMBINED_PRODUCTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PR, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PR, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PR, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PR, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PR, Product.VACATIONRENTALS));


        // EL SALVADOR
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.SV, Product.CRUISES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.SV, Product.FLIGHTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.SV, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.SV, Product.COMBINED_PRODUCTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.SV, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.SV, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.SV, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.SV, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.SV, Product.VACATIONRENTALS));

        // URUGUAY
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.UY, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.UY, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.UY, Product.CLOSED_PACKAGES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.UY, Product.COMBINED_PRODUCTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.UY, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.UY, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.UY, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.UY, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.UY, Product.VACATIONRENTALS));

        // VENEZUELA
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.VE, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.VE, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.VE, Product.CLOSED_PACKAGES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.VE, Product.COMBINED_PRODUCTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.VE, Product.ACTIVITIES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.VE, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.VE, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.VE, Product.CARS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.VE, Product.VACATIONRENTALS));

        // ESTADOS UNIDOS
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.US, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.US, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.US, Product.CLOSED_PACKAGES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.US, Product.COMBINED_PRODUCTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.US, Product.ACTIVITIES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.US, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.US, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.US, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.US, Product.VACATIONRENTALS));

        // CANADA
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.CA, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CA, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CA, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.CA, Product.COMBINED_PRODUCTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.CA, Product.ACTIVITIES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.CA, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CA, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CA, Product.CARS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.CA, Product.VACATIONRENTALS));

        // AUSTRALIA
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.AU, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.AU, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.AU, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.AU, Product.COMBINED_PRODUCTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.AU, Product.ACTIVITIES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.AU, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.AU, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.AU, Product.CARS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.AU, Product.VACATIONRENTALS));

        // ARGENTINA
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.AR, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.AR, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.AR, Product.CLOSED_PACKAGES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.AR, Product.COMBINED_PRODUCTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.AR, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.AR, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.AR, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.AR, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.AR, Product.VACATIONRENTALS));

        // BRASIL
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.BR, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.BR, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.BR, Product.CLOSED_PACKAGES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.BR, Product.COMBINED_PRODUCTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.BR, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.BR, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.BR, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.BR, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.BR, Product.VACATIONRENTALS));

        // CHILE
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CL, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CL, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CL, Product.CLOSED_PACKAGES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CL, Product.COMBINED_PRODUCTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CL, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CL, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CL, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CL, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CL, Product.VACATIONRENTALS));

        // COLOMBIA
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CO, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CO, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CO, Product.CLOSED_PACKAGES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CO, Product.COMBINED_PRODUCTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CO, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CO, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CO, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CO, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.CO, Product.VACATIONRENTALS));

        // MEXICO
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.MX, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.MX, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.MX, Product.CLOSED_PACKAGES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.MX, Product.COMBINED_PRODUCTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.MX, Product.ACTIVITIES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.MX, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.MX, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.MX, Product.CARS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.MX, Product.VACATIONRENTALS));

        // REINO UNIDO
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.GB, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.GB, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.GB, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.GB, Product.COMBINED_PRODUCTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.GB, Product.ACTIVITIES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.GB, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.GB, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.GB, Product.CARS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.GB, Product.VACATIONRENTALS));

        // NUEVA ZELANDA
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.NZ, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.NZ, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.NZ, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.NZ, Product.COMBINED_PRODUCTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.NZ, Product.ACTIVITIES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.NZ, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.NZ, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.NZ, Product.CARS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.NZ, Product.VACATIONRENTALS));

        // SINGAPUR
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.SG, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.SG, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.SG, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.SG, Product.COMBINED_PRODUCTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.SG, Product.ACTIVITIES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.SG, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.SG, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.SG, Product.CARS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.SG, Product.VACATIONRENTALS));

        // IRLANDA
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.IE, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.IE, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.IE, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.IE, Product.COMBINED_PRODUCTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.IE, Product.ACTIVITIES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.IE, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.IE, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.IE, Product.CARS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.IE, Product.VACATIONRENTALS));

        // INDIA
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.IN, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.IN, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.IN, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.IN, Product.COMBINED_PRODUCTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.IN, Product.ACTIVITIES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.IN, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.IN, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.IN, Product.CARS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.IN, Product.VACATIONRENTALS));

        // FILIPINAS
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.PH, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PH, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PH, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.PH, Product.COMBINED_PRODUCTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.PH, Product.ACTIVITIES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.PH, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PH, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PH, Product.CARS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.PH, Product.VACATIONRENTALS));

        // JAMAICA
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.JM, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.JM, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.JM, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.JM, Product.COMBINED_PRODUCTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.JM, Product.ACTIVITIES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.JM, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.JM, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.JM, Product.CARS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.JM, Product.VACATIONRENTALS));

        // PORTUGAL
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.PT, Product.CRUISES));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PT, Product.FLIGHTS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PT, Product.CLOSED_PACKAGES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.PT, Product.COMBINED_PRODUCTS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.PT, Product.ACTIVITIES));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.PT, Product.INSURANCE));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PT, Product.HOTELS));
        Assert.assertFalse(ProductCountrySupportUtils.isMissing(CountryCode.PT, Product.CARS));
        Assert.assertTrue(ProductCountrySupportUtils.isMissing(CountryCode.PT, Product.VACATIONRENTALS));

    }

}
