package com.despegar.p13n.hestia.recommend.allinone.rules;

import java.util.EnumSet;

public enum SectionsEnum {

	  OFFER(0), //
	    ROW1(1),
	    ROW2(2),
	    ROW3(3),
	    ROW4(4);

	    public static final EnumSet<SectionsEnum> ORIGINAL_SECTIONS = EnumSet.of(OFFER, ROW1, ROW2, ROW3);

	    private int index;

	    private SectionsEnum(int index) {
	        this.index = index;
	    }

	    public int getIndex() {
	        return this.index;
	    };

}
