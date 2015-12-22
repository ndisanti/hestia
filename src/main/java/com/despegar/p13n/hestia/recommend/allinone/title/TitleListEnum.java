package com.despegar.p13n.hestia.recommend.allinone.title;

import java.util.List;

import com.google.common.collect.Lists;


public enum TitleListEnum {

    L1(TitleEnum.T1, TitleEnum.T2, TitleEnum.T30, TitleEnum.T31, TitleEnum.T40, TitleEnum.T41, TitleEnum.T70, TitleEnum.T71,
                    TitleEnum.T72), //
    L3(TitleEnum.T3, TitleEnum.T26, TitleEnum.T32, TitleEnum.T42, TitleEnum.T74), //
    L4(TitleEnum.T4, TitleEnum.T33, TitleEnum.T75, TitleEnum.T76, TitleEnum.T78), //
    L5(TitleEnum.T5, TitleEnum.T34, TitleEnum.T43), //
    L6(TitleEnum.T6, TitleEnum.T35, TitleEnum.T44, TitleEnum.T77, TitleEnum.T79), //
    L7(TitleEnum.T7, TitleEnum.T36), //
    L8(TitleEnum.T8), //
    L9(TitleEnum.T9, TitleEnum.T38, TitleEnum.T45), //
    L10(TitleEnum.T10, TitleEnum.T39, TitleEnum.T46, TitleEnum.T80), //
    L11(TitleEnum.T11), //
    L12(TitleEnum.T12), //
    L13(TitleEnum.T13), //
    L14(TitleEnum.T14, TitleEnum.T84, TitleEnum.T85, TitleEnum.T86), //
    L15(TitleEnum.T15), //
    L16(TitleEnum.T16), //
    L18(TitleEnum.T18), //
    L19(TitleEnum.T19), //
    L21(TitleEnum.T21), //
    L22(TitleEnum.T22), //
    L23(TitleEnum.T23), //
    L24(TitleEnum.T24, TitleEnum.T52, TitleEnum.T53, TitleEnum.T89), //
    L25(TitleEnum.T25, TitleEnum.T90, TitleEnum.T91), //
    L27(TitleEnum.T27), //
    L28(TitleEnum.T28), //
    L29(TitleEnum.T29), //
    L30(TitleEnum.T99, TitleEnum.T100), //
    L32(TitleEnum.T87, TitleEnum.T88), //
    L33(TitleEnum.T69, TitleEnum.T73), //
    L47(TitleEnum.T47), //
    L48(TitleEnum.T48, TitleEnum.T93), //
    L49(TitleEnum.T49, TitleEnum.T94), //
    L50(TitleEnum.T50), //
    L51(TitleEnum.T51, TitleEnum.T54, TitleEnum.T55), //
    L52(TitleEnum.T56, TitleEnum.T57, TitleEnum.T58, TitleEnum.T95, TitleEnum.T96, TitleEnum.T97, TitleEnum.T98,
                    TitleEnum.T101, TitleEnum.T102, TitleEnum.T103, TitleEnum.T104, TitleEnum.T105, TitleEnum.T106,
                    TitleEnum.T107, TitleEnum.T108, TitleEnum.T109, TitleEnum.T110), //
    L53(TitleEnum.T113, TitleEnum.T114, TitleEnum.T115, TitleEnum.T116, TitleEnum.T117, TitleEnum.T118, TitleEnum.T119,
                    TitleEnum.T120, TitleEnum.T121, TitleEnum.T122), //
    L54(TitleEnum.T59, TitleEnum.T60, TitleEnum.T61, TitleEnum.T62, TitleEnum.T123, TitleEnum.T124, TitleEnum.T125,
                    TitleEnum.T126, TitleEnum.T127, TitleEnum.T128, TitleEnum.T129), //
    L55(TitleEnum.T63, TitleEnum.T64, TitleEnum.T65, TitleEnum.T81, TitleEnum.T130, TitleEnum.T131, TitleEnum.T132,
                    TitleEnum.T133),
    L56(TitleEnum.T66), //
    L57(TitleEnum.T67),
    L58(TitleEnum.T68),
    L59(TitleEnum.T92);

    private final List<TitleEnum> titles;

    private TitleListEnum(TitleEnum... titles) {
        this.titles = Lists.newArrayList(titles);
    }

    public List<TitleEnum> getTitles() {
        return this.titles;
    }

}
