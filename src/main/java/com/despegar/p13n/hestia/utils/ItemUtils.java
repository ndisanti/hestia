package com.despegar.p13n.hestia.utils;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.CarRankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.ItemType;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.CarsItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.ItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleData;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleEnum;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.newrelic.api.agent.NewRelic;

@Component
public class ItemUtils {

    public static EnumSet<Flow> SEARCH_FLOWS = EnumSet.of(Flow.SEARCH, Flow.LANDING);

    public static EnumSet<Flow> DETAIL_FLOWS = EnumSet.of(Flow.DETAIL, Flow.LANDING_DETAIL);

    public static EnumSet<Flow> CHECKOUT_FLOWS = EnumSet.of(Flow.CHECKOUT);

    public static EnumSet<Flow> BUY_FLOWS = EnumSet.of(Flow.THANKS);
    
	public static int MIN_ROW_SIZE = 5;
	public static int MAX_ROW_SIZE = 10;

	public static void checkItemSupport(SectionFunctionCode functionCode,
			List<Offer> offers) {
		if (offers != null) {
			for (Offer offer : offers) {
				checkItemSupport(functionCode, offer.getOffer());
			}
		}
	}

	public static void addSubtitles(ActionRecommendation action) {
		action.setSubtitleHighlightData(new TitleData(TitleEnum.ST1));
		action.setSubtitleOfferData(new TitleData(TitleEnum.ST2));
	}

	public static void checkItemSupport(SectionFunctionCode functionCode,
			ItemHome itemHome) {

		if (itemHome == null) {
			return;
		}
		ItemType itemType = ItemType.getItemType(itemHome.getClass());
		if (!functionCode.getItemTypes().contains(itemType)) {
			String key = functionCode + "|" + itemType;
			NewRelic.noticeError("Not supported " + key);
		}
	}

	public static ItemHome checkUnique(Product home, ItemHome itemHome,
			ActionRecommendation action, Product prOffer) {

		if (itemHome == null) {
			return null;
		}

		// returning null if id was already used
		// also marking as used, if it was not used
		ItemTypeId itemTypeId = ItemTypeId.getItemType(itemHome.getOfferType());

		boolean added = addUnique(itemTypeId, home, prOffer, action,
				itemHome.getId());

		if (added) {
			return itemHome;
		} else {
			return null;
		}
	}

	private static boolean addUnique(ItemTypeId itemType, Product home,
			Product prOffer, ActionRecommendation action, String id) {

		// ranking cluster bug: returning 'null' for hid
		if (id.equalsIgnoreCase("null")) {
			return false;
		}

		switch (itemType) {
		case DESTINATION:
			return action.addDestination(home, prOffer, id);
		case CLUID:
			return action.addCluid(home, prOffer, id);
		case DID:
			return action.addDid(home, prOffer, id);
		case HID:
			return action.addHid(home, prOffer, id);
		case VRID:
			return action.addVrid(home, prOffer, id);
		case REGION:
			return action.addRegion(home, prOffer, id);
		case ACTID:
			return action.addActid(home, prOffer, id);
		default:
			throw new UnsupportedOperationException();
		}
	}

	public static void checkProduct(Product prToCheck, Product... supported) {
		boolean supports = false;
		for (Product pr : supported) {
			if (pr == prToCheck) {
				supports = true;
				break;
			}
		}

		Preconditions.checkArgument(supports,
				"Only (%s) are supported. Received: (%s)", //
				Arrays.toString(supported), prToCheck);
	}

	public static void checkCountry(CountryCode countryToCheck,
			CountryCode... supported) {
		boolean supports = false;
		for (CountryCode cc : supported) {
			if (cc == countryToCheck) {
				supports = true;
				break;
			}
		}

		Preconditions.checkArgument(supports,
				"Only (%s) are supported. Received: (%s)", //
				Arrays.toString(supported), countryToCheck);
	}

	public static void checkActivityType(ActivityType toCheck,
			ActivityType... supported) {
		boolean supports = false;
		for (ActivityType type : supported) {
			if (toCheck == type) {
				supports = true;
				break;
			}
		}

		Preconditions.checkArgument(supports,
				"Only (%s) are supported. Received: (%s)", //
				Arrays.toString(supported), toCheck);
	}

	public static List<Offer> buildOffers(Product home, ItemHome itemHome,
			ActionRecommendation action, Product prOffer) {

		ItemHome newItemHome = checkUnique(home, itemHome, action, prOffer);

		return itemHome == null ? null : Lists.newArrayList(new Offer(
				newItemHome, null));
	}

	public static List<Offer> buildOffers(Product home, Product prOffer,
			ActionRecommendation action, Param param,
			Iterator<RankingItemDTO> it, ItemBuilder builder) {

		if (it == null) {
			return null;
		}

		while (it.hasNext()) {
			RankingItemDTO dto = it.next();

			Preconditions.checkNotNull(dto);

			ItemHome itemHome = builder.buildItem(home, prOffer, action, param,
					dto);

			if (itemHome != null) {
				ItemTypeId itemTypeId = ItemTypeId.getItemType(itemHome
						.getOfferType());

				boolean added = addUnique(itemTypeId, home, prOffer, action,
						itemHome.getId());

				if (added) {
					return Lists.newArrayList(new Offer(itemHome, null));
				}

			}

		}

		return null;
	}

	public static List<Offer> buildOffersCars(Product home, Product prOffer,
			ActionRecommendation action, Param param,
			List<CarRankingPositionDTO> list, CarsItemBuilder builder) {

		Iterator<CarRankingPositionDTO> it = list.iterator();
		if (it == null) {
			return null;
		}

		while (it.hasNext()) {
			CarRankingPositionDTO dto = it.next();

			Preconditions.checkNotNull(dto);

			ItemHome itemHome = builder.buildItem(home, prOffer, action, param,
					dto);

			if (itemHome != null) {
				ItemTypeId itemTypeId = ItemTypeId.getItemType(itemHome
						.getOfferType());

				boolean added = addUnique(itemTypeId, home, prOffer, action,
						dto);

				if (added) {
					return Lists.newArrayList(new Offer(itemHome, null));
				}

			}

		}

		return null;
	}

	public static RowHome buildRow(Product home, Product prOffer,
			ActionRecommendation action, Param param,
			Iterator<RankingItemDTO> it, ItemBuilder builder) {

		List<ItemHome> itemsRow = Lists
				.newArrayListWithExpectedSize(MAX_ROW_SIZE);

		while (itemsRow.size() < MAX_ROW_SIZE && it.hasNext()) {

			RankingItemDTO dto = it.next();

			Preconditions.checkNotNull(dto);

			ItemHome item = builder
					.buildItem(home, prOffer, action, param, dto);

			if (item == null) {
				continue;
			}

			ItemTypeId itemTypeId = ItemTypeId.getItemType(item.getOfferType());

			boolean added = addUnique(itemTypeId, home, prOffer, action,
					item.getId());

			if (added) {
				itemsRow.add(item);
			}

		}

		if (itemsRow.size() < MIN_ROW_SIZE) {
			return null;
		}

		return new RowHome(null, itemsRow);
	}

	public static RowHome buildRowCars(Product home, Product prOffer,
			ActionRecommendation action, Param param,
			List<CarRankingPositionDTO> list, CarsItemBuilder builder) {

		List<ItemHome> itemsRow = Lists
				.newArrayListWithExpectedSize(MAX_ROW_SIZE);

		Iterator<CarRankingPositionDTO> it = list.iterator();

		while (itemsRow.size() < MAX_ROW_SIZE && it.hasNext()) {

			CarRankingPositionDTO dto = it.next();

			Preconditions.checkNotNull(dto);

			ItemHome item = builder
					.buildItem(home, prOffer, action, param, dto);

			if (item == null) {
				continue;
			}

			ItemTypeId itemTypeId = ItemTypeId.getItemType(item.getOfferType());

			boolean added = addUnique(itemTypeId, home, prOffer, action, dto);

			if (added) {
				itemsRow.add(item);
			}
		}

		if (itemsRow.size() < MIN_ROW_SIZE) {
			return null;
		}

		return new RowHome(null, itemsRow);
	}

	private static boolean addUnique(ItemTypeId itemType, Product home,
			Product prOffer, ActionRecommendation action,
			CarRankingPositionDTO rankingDTO) {

		// only destination and car category are supported
		switch (itemType) {
		case DESTINATION:
			return action.addDestination(home, prOffer, rankingDTO.getPul());
		case CAR_CATEGORY:
			return action.addCarCategory(home, prOffer, rankingDTO.getPul(),
					rankingDTO.getCarcat());
		default:
			throw new UnsupportedOperationException();
		}
	}

	public static boolean isOriginOk(Product pr, String origin,
			String destination) {

		if (hasOrigin(pr)) {
			Preconditions
					.checkNotNull(origin, "Origin can't be null for " + pr);
			return !destination.equals(origin);
		} else {
			return true;
		}
	}

	public static boolean hasOrigin(Product pr) {
		return WITH_ORIGIN.contains(pr);
	}

	public static EnumSet<Product> WITH_ORIGIN = EnumSet.of(Product.FLIGHTS,
			Product.CLOSED_PACKAGES, Product.COMBINED_PRODUCTS);

    private static boolean isDetail(Flow flow) {
        return DETAIL_FLOWS.contains(flow);
    }

    private static boolean isCheckout(Flow flow) {
        return CHECKOUT_FLOWS.contains(flow);
    }
    

    public static boolean isSearch(Flow flow) {
        return SEARCH_FLOWS.contains(flow);
    }
    
    public static boolean isDetailOrCheckout(Flow flow) {
        return isDetail(flow) || isCheckout(flow);
    }
    
    public static void checkItemTypeId(ItemTypeId toCheck, ItemTypeId... supported) {
        boolean supports = false;
        for (ItemTypeId itemTypeId : supported) {
            if (toCheck == itemTypeId) {
                supports = true;
                break;
            }
        }
        Preconditions.checkArgument(supports, "Only (%s) are supported. Received: (%s)", //
            Arrays.toString(supported), toCheck);
    }
}
