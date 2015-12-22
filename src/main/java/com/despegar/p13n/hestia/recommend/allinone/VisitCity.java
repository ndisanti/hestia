package com.despegar.p13n.hestia.recommend.allinone;

import java.util.List;

import com.google.common.collect.Lists;

public enum VisitCity {

    MIAMI_ORLANDO("MIA", "ORL"), ANY();

    private List<String> iatas = Lists.newArrayList();

    VisitCity(String... iatas) {

        for (int i = 0; iatas != null && i < iatas.length; i++) {
            this.iatas.add(iatas[i]);
        }
    }


    public static VisitCity getVisitCity(String destination) {

        if (destination == null) {
            return VisitCity.ANY;
        }

        for (VisitCity visitCity : VisitCity.values()) {
            if (visitCity.iatas.contains(destination)) {
                return visitCity;
            }
        }

        return ANY;
    }

}
