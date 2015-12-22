package com.despegar.p13n.hestia.recommend.allinone.title;

import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;

public enum SectionType {
    OFFER, ROW;

    public static SectionType getSectionType(SectionsEnum section) {
        return (section == SectionsEnum.OFFER) ? OFFER : ROW;
    }
}
