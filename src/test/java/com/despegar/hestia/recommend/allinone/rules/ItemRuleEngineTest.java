package com.despegar.hestia.recommend.allinone.rules;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemHomeService;
import com.despegar.p13n.hestia.recommend.allinone.rules.ItemRuleEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.LastResortEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.NoHistoryEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.destination.functions.BuyDestinationItemFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ItemIdFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ItemIdFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.products.functions.HotelProductFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.version.MonoDestinationRules;
import com.despegar.p13n.hestia.recommend.allinone.rules.version.MultiDestinationRules;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleEngine;

@RunWith(MockitoJUnitRunner.class)
public class ItemRuleEngineTest {

    private ItemRuleEngine itemRuleEngine;
    @Mock
    private Map<ProductFuncCode, ProductFunction> prFunctions;
    @Mock
    private Map<ItemIdFuncCode, ItemIdFunction> itemFunctions;
    @Mock
    private ItemHomeService itemHomeService;
    @Mock
    private TitleEngine titleEngine;
    @Mock
    private NoHistoryEngine noHistoryEngine;
    @Mock
    private LastResortEngine lastResortEngine;

    @Before
    public void setUp() {
        this.itemRuleEngine = new ItemRuleEngine(this.itemFunctions, this.prFunctions, this.itemHomeService,
            this.noHistoryEngine, this.titleEngine, this.lastResortEngine);
        new MonoDestinationRules(this.itemRuleEngine);
        new MultiDestinationRules(this.itemRuleEngine);
        this.itemRuleEngine.start();
    }

    @Test
    @Ignore
    public void testBuildHomeContent() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CLOSED_PACKAGES, "MIA");
        action.setVersion(RulesVersion.MONO_DESTINATION);
        Mockito.when(this.itemFunctions.get(Mockito.any(ItemIdFuncCode.class))).thenReturn(new BuyDestinationItemFunction());
        Mockito.when(this.prFunctions.get(Mockito.any(ProductFuncCode.class))).thenReturn(new HotelProductFunction());
        Mockito.when(
            this.itemHomeService.buildItemHome(Mockito.eq(ItemTypeId.DESTINATION), Mockito.anyString(),
                Mockito.any(Product.class), Mockito.eq(action))).thenReturn(new HotelItem("11111"));
        HomeContent homeContent = this.itemRuleEngine.buildHomeContent(action);
        Assert.assertNotNull(homeContent);
    }

    @Test
    public void testBuildHomeContentForNoHistory() {
        ActionRecommendation action = Mockito.mock(ActionRecommendation.class);
        Mockito.when(action.getActivityType()).thenReturn(ActivityType.NO_HISTORY);
        EnumSet<Product> products = EnumSet.of(Product.HOTELS, Product.HOTELS, Product.HOTELS);
        Mockito.when(action.getHomes()).thenReturn(products);
        List<RowHome> rowModules = new ArrayList<RowHome>();
        List<ItemHome> offers = new ArrayList<ItemHome>();
        offers.add(new HotelItem("12345"));
        rowModules.add(new RowHome(null, offers));
        HomeProduct homeProduct = new HomeProduct(null, rowModules);
        Mockito.when(this.noHistoryEngine.buildHomeForProduct(Mockito.eq(action), Mockito.any(Product.class))).thenReturn(
            homeProduct);
        HomeContent homeContent = this.itemRuleEngine.buildHomeContent(action);
        Mockito.verify(this.noHistoryEngine).buildHomeForProduct(Mockito.eq(action), Mockito.any(Product.class));
        Assert.assertNotNull(homeContent);
    }
}
