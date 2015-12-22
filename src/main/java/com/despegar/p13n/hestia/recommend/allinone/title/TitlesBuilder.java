package com.despegar.p13n.hestia.recommend.allinone.title;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.Title;
import com.despegar.p13n.hestia.api.data.model.Titles;
import com.google.common.base.Preconditions;

public class TitlesBuilder {

    private Title mainTitle;
    private Title subtitleHighlighted;
    private Title subtitleOffers;

    public static TitlesBuilder builder() {
        return new TitlesBuilder();
    }

    public TitlesBuilder main(Title title) {
        Preconditions.checkNotNull(title);
        Preconditions.checkState(this.mainTitle == null, "Main title is already set");
        this.mainTitle = title;
        return this;
    }

    public TitlesBuilder high(Title title) {
        Preconditions.checkNotNull(title);
        Preconditions.checkState(this.subtitleHighlighted == null, "SubtitleHighlighted is already set");
        this.subtitleHighlighted = title;
        return this;
    }

    public TitlesBuilder offer(Title title) {
        Preconditions.checkNotNull(title);
        Preconditions.checkState(this.subtitleOffers == null, "SubtitleOffers is already set");
        this.subtitleOffers = title;
        return this;
    }

    public Titles build() {
        return new Titles(this.mainTitle, this.subtitleHighlighted, this.subtitleOffers);
    }


    public static void main(String[] args) {

        Titles title = TitlesBuilder.builder().//
            main(TitleBuilder.builder(TitleEnum.T1).iata("MIA").pr(Product.HOTELS).build()).//
            build();

        System.out.println(title);
    }

}
