package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.google.common.base.Preconditions;

@Service
public class HotRankingIpCountryFunction
    extends BaseSectionFunction {

    private HotItemBuilder itemBuilder = new HotItemBuilder();

    @Override
    public List<Offer> buildOffers(Product home, Product prToShow, ActionRecommendation action, Param param) {

        Preconditions.checkNotNull(param.getRankingType());

        Iterator<RankingItemDTO> it = this.getRankingIterator(prToShow, action, param.getRankingType());
        action.getTitleData().addDestination(action.getOrigin());

        return ItemUtils.buildOffers(home, prToShow, action, param, it, this.itemBuilder);
    }


    @Override
    public RowHome buildRow(Product home, Product prToShow, ActionRecommendation action, Param param) {

        Preconditions.checkNotNull(param.getRankingType());

        Iterator<RankingItemDTO> it = this.getRankingIterator(prToShow, action, param.getRankingType());
        action.getTitleData().addDestination(action.getOrigin());

        return ItemUtils.buildRow(home, prToShow, action, param, it, this.itemBuilder);
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.HOT_RANKING_IP_COUNTRY;
    }

    private class HotItemBuilder
        implements ItemBuilder {

        @Override
        public ItemHome buildItem(Product home, Product prOffer, ActionRecommendation action, Param param, RankingItemDTO dto) {

            String destination = dto.getId();

            return HotRankingIpCountryFunction.this.buildDestination(prOffer, param.getRankingType(), destination,
                action.getOrigin(), action);

        }

    }

    @Override
    public String getDescription() {
        return "Ranking de destinos por ip (si país ip = brand) o por país (si país ip <> país brand)";
    }


}
