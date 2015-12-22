package com.despegar.p13n.hestia.recommend.allinone;

import java.util.List;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;
import com.google.common.collect.Lists;

public enum MultiObjecHomeVersion {

	 V1_GROUPED {

	        @Override
	        public List<ItemHomeCoordinate> getItems() {
	            return Lists.newArrayList(new ItemHomeCoordinate(Product.HOLIDAYS, SectionsEnum.ROW1, 5), //
	                new ItemHomeCoordinate(Product.NEWSLETTER_AS_PRODUCT, SectionsEnum.ROW1, 6), //
	                new ItemHomeCoordinate(Product.SECRET_OFFER_AS_PRODUCT, SectionsEnum.ROW2, 5), //
	                new ItemHomeCoordinate(Product.CITY_REVIEW, SectionsEnum.ROW2, 6), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW3, 5), //
	                new ItemHomeCoordinate(Product.TRUST_PILOT, SectionsEnum.ROW3, 6));
	        }
	    },

	    V1_INTERLIVED {
	        @Override
	        public List<ItemHomeCoordinate> getItems() {
	            return Lists.newArrayList(new ItemHomeCoordinate(Product.NEWSLETTER_AS_PRODUCT, SectionsEnum.ROW1, 1), //
	                new ItemHomeCoordinate(Product.HOLIDAYS, SectionsEnum.ROW1, 5), //
	                new ItemHomeCoordinate(Product.SECRET_OFFER_AS_PRODUCT, SectionsEnum.ROW2, 2), //
	                new ItemHomeCoordinate(Product.CITY_REVIEW, SectionsEnum.ROW2, 6), //
	                new ItemHomeCoordinate(Product.TRUST_PILOT, SectionsEnum.ROW3, 1), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW3, 4));
	        }
	    },
	    V2_GROUPED {
	        @Override
	        public List<ItemHomeCoordinate> getItems() {
	            return Lists.newArrayList(new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW1, 5), //
	                new ItemHomeCoordinate(Product.CITY_REVIEW, SectionsEnum.ROW1, 6), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW2, 5), //
	                new ItemHomeCoordinate(Product.FARE_ALERT, SectionsEnum.ROW2, 6), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW3, 5), //
	                new ItemHomeCoordinate(Product.TRUST_PILOT, SectionsEnum.ROW3, 6));
	        }
	    },
	    V2_INTERLIVED {
	        @Override
	        public List<ItemHomeCoordinate> getItems() {
	            return Lists.newArrayList(new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW1, 3), //
	                new ItemHomeCoordinate(Product.CITY_REVIEW, SectionsEnum.ROW1, 5), //
	                new ItemHomeCoordinate(Product.FARE_ALERT, SectionsEnum.ROW2, 2), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW2, 6), //
	                new ItemHomeCoordinate(Product.TRUST_PILOT, SectionsEnum.ROW3, 1), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW3, 4));
	        }
	    },
	    V3_GROUPED {
	        @Override
	        public List<ItemHomeCoordinate> getItems() {
	            return Lists.newArrayList(new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW1, 5), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW1, 6), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW2, 5), //
	                new ItemHomeCoordinate(Product.TRUST_PILOT, SectionsEnum.ROW3, 5), //
	                new ItemHomeCoordinate(Product.FARE_ALERT, SectionsEnum.ROW3, 6));
	        }
	    },
	    V3_INTERLIVED {
	        @Override
	        public List<ItemHomeCoordinate> getItems() {
	            return Lists.newArrayList(new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW1, 1), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW1, 5), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW2, 6), //
	                new ItemHomeCoordinate(Product.TRUST_PILOT, SectionsEnum.ROW3, 1), //
	                new ItemHomeCoordinate(Product.FARE_ALERT, SectionsEnum.ROW3, 6));
	        }
	    },
	    V4_ROW4 {
	        @Override
	        public List<ItemHomeCoordinate> getItems() {
	            return Lists.newArrayList(new ItemHomeCoordinate(Product.TRUST_PILOT, SectionsEnum.ROW4, 1), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW4, 2), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW4, 3), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW4, 4), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW4, 5), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW4, 6), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW4, 7), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW4, 8), //
	                new ItemHomeCoordinate(Product.HAPPY_CLIENT, SectionsEnum.ROW4, 9));
	        }
	    };

	    public abstract List<ItemHomeCoordinate> getItems();

	    public static MultiObjecHomeVersion fromString(String forceHomeVersionStr) {

	        for (MultiObjecHomeVersion version : MultiObjecHomeVersion.values()) {
	            if (version.toString().equals(forceHomeVersionStr)) {
	                return version;
	            }
	        }
	        return null;
	    }

	    public static String allNames() {
	        String names = "";
	        for (MultiObjecHomeVersion elem : MultiObjecHomeVersion.values()) {
	            names += elem.toString() + " ";
	        }
	        return names;
	    }
}
