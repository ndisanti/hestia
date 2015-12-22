package ar.com.despegar.p13n.hestia;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.recommend.allinone.RulesVersionService;



public class RulesVersionDataServiceTest {


    private RulesVersionService rulesVersionDataService;

    @Before
    public void setUp() {
        this.rulesVersionDataService = new RulesVersionService();
    }

    @Test
    public void testGetStaticRulesVersion() {
        RulesVersion version = this.rulesVersionDataService.getRulesVersion(CountryCode.AR);
        Assert.assertNotNull(version);
        Assert.assertSame(version, RulesVersion.DYNAMIC_SERVICE);
        version = this.rulesVersionDataService.getRulesVersion(CountryCode.BR);
        Assert.assertNotNull(version);
        Assert.assertSame(version, RulesVersion.DYNAMIC_SERVICE);
    }


    @Test
    public void testGetMultiDestinationRulesVersion() {
        RulesVersion version = this.rulesVersionDataService.getRulesVersion(CountryCode.PE);
        Assert.assertNotNull(version);
        Assert.assertSame(version, RulesVersion.MULTI_DESTINATION);
    }
}
