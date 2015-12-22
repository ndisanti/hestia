package com.despegar.p13n.hestia.recommend.allinone;

import java.util.List;
import java.util.Map;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ItemIdFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ItemIdFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFunction;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AtomicLongMap;

/**
 * Memoization for functions calls
 */
public class FunctionPrecalc {

    private Map<ItemIdFuncCode, List<String>> idFunctionList = Maps.newHashMap();
    private AtomicLongMap<ItemIdFuncCode> idIndex = AtomicLongMap.create();

    private Map<ProductKey, List<Product>> prFunctionList = Maps.newHashMap();
    private AtomicLongMap<ProductKey> prIndex = AtomicLongMap.create();

    private List<String> callItemIdFunction(ItemIdFunction itemIdFunction, ActionRecommendation action) {

        ItemIdFuncCode code = itemIdFunction.getFunctionCode();

        if (!this.idFunctionList.containsKey(code)) {

            List<String> ids = itemIdFunction.getItemIds(action);
            action.addDebug("Ids: " + "(" + code + "," + itemIdFunction.getItemTypeId() + ")" + " -> " + ids);
            this.idFunctionList.put(code, ids);
        }

        return this.idFunctionList.get(code);
    }

    public String getId(ItemIdFunction itemIdFunction, ActionRecommendation action, int index) {
        List<String> ids = this.callItemIdFunction(itemIdFunction, action);
        return ids == null ? null : ids.get(index);
    }

    public String getNextId(ItemIdFunction itemIdFunction, ActionRecommendation action) {
        int next = (int) this.idIndex.getAndIncrement(itemIdFunction.getFunctionCode());
        return this.callItemIdFunction(itemIdFunction, action).get(next);
    }

    private List<Product> callProductFunction(SectionsEnum section, ProductFunction prFunction, ItemTypeId itemTypeId,
        String id, ActionRecommendation action) {

        ProductKey prKey = new ProductKey(section, prFunction.getFunctionCode(), itemTypeId, id);

        if (!this.prFunctionList.containsKey(prKey)) {

            List<Product> products = prFunction.getProducts(itemTypeId, id, action);
            action.addDebug("Products: " + prKey.toDebug() + " -> " + products);
            this.prFunctionList.put(prKey, products);
        }

        return this.prFunctionList.get(prKey);
    }


    public Product getNextProduct(SectionsEnum section, ProductFunction prFunction, ItemTypeId itemTypeId, String id,
        ActionRecommendation action) {
        int next = (int) this.prIndex.getAndIncrement(new ProductKey(section, prFunction.getFunctionCode(), itemTypeId, id));
        return this.callProductFunction(section, prFunction, itemTypeId, id, action).get(next);
    }

    private static class ProductKey {
        private ProductFuncCode prFunctionCode;
        private ItemTypeId itemTypeId;
        private String id;
        private SectionsEnum section;

        ProductKey(SectionsEnum section, ProductFuncCode prFunctionCode, ItemTypeId itemTypeId, String id) {
            this.prFunctionCode = prFunctionCode;
            this.itemTypeId = itemTypeId;
            this.id = id;
            this.section = section;
        }

        public String toDebug() {
            return "(" + this.prFunctionCode + "," + this.itemTypeId + "," + this.id + ")";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
            result = prime * result + ((this.itemTypeId == null) ? 0 : this.itemTypeId.hashCode());
            result = prime * result + ((this.prFunctionCode == null) ? 0 : this.prFunctionCode.hashCode());
            result = prime * result + ((this.section == null) ? 0 : this.section.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            ProductKey other = (ProductKey) obj;
            if (this.id == null) {
                if (other.id != null) {
                    return false;
                }
            } else if (!this.id.equals(other.id)) {
                return false;
            }
            if (this.itemTypeId != other.itemTypeId) {
                return false;
            }
            if (this.prFunctionCode != other.prFunctionCode) {
                return false;
            }
            if (this.section != other.section) {
                return false;
            }
            return true;
        }



    }

}
