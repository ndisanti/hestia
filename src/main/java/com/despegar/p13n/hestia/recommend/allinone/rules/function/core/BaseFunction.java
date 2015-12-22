package com.despegar.p13n.hestia.recommend.allinone.rules.function.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.HomeUtils;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserAction;
import com.despegar.p13n.euler.commons.client.model.UserLocation;
import com.despegar.p13n.euler.commons.client.model.data.FlightData;
import com.despegar.p13n.euler.commons.client.model.data.ProductData;
import com.despegar.p13n.euler.commons.client.model.data.TripType;
import com.despegar.p13n.hestia.api.data.model.ActivityDestinationItem;
import com.despegar.p13n.hestia.api.data.model.CarDestinationItem;
import com.despegar.p13n.hestia.api.data.model.CarRankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesDestinationItem;
import com.despegar.p13n.hestia.api.data.model.CombinedProductsDestinationItem;
import com.despegar.p13n.hestia.api.data.model.CruiseItem;
import com.despegar.p13n.hestia.api.data.model.CruiseRegionItem;
import com.despegar.p13n.hestia.api.data.model.FlightDestinationItem;
import com.despegar.p13n.hestia.api.data.model.HotelDestinationItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTypeCodes;
import com.despegar.p13n.hestia.api.data.model.VacationRentalDestinationItem;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.client.RecommendationsClient;
import com.despegar.p13n.hestia.data.hbase.hot.RankingQuery;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.exception.InvalidRankingRequestException;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.utils.IPUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.net.InetAddresses;
import com.newrelic.api.agent.Trace;

@Service
public abstract class BaseFunction {

   	public static Integer RANKING_SIZE = 33;
    public static int IATA_RANKING_SIZE = 10;

    @Autowired
    private RankingsClient hotRankingService;
    @Autowired
    private RecommendationsClient recommendation;
    @Autowired
    private GeoService geoService;

    protected static final Logger log = LoggerFactory.getLogger(BaseFunction.class);

    @Trace
    protected Iterator<RankingItemDTO> getRankingIterator(Product prToShow, ActionRecommendation action,
        StaticRankingTypes type) {
        return this.getRankingList(prToShow, action, type).iterator();
    }

    @Trace
    protected List<RankingItemDTO> getRankingList(Product prToShow, ActionRecommendation action, StaticRankingTypes type) {
        List<RankingItemDTO> list = null;

        if (this.getIsCountryIpValid(action)) {
            list = this.getRankingIpList(prToShow, type, action);
        } else {
            list = this.getRankingCountryList(prToShow, type, action);
        }
        return list;
    }

    private boolean getIsCountryIpValid(ActionRecommendation action) {
        if (action.getIsCountryIpValid() == null) {
            boolean ret = this.areIpCountryAndCountryBrandEquals(action);
            action.setIsCountryIpValid(ret);
            return ret;
        } else {
            return action.getIsCountryIpValid();
        }
    }

    private List<RankingItemDTO> getAsList(RankingTreeDTO rankingDTO) {

        if (rankingDTO.getPodium().isEmpty()) {

            return Lists.newArrayList();

        } else {

            List<RankingItemDTO> rankingList = Lists.newArrayList();

            for (RankingPositionDTO dto : rankingDTO.getPodium()) {
                rankingList.add(new RankingItemDTO(dto.getDestination()));
            }
            return rankingList;
        }
    }

    private boolean areIpCountryAndCountryBrandEquals(ActionRecommendation action) {
        UserLocation userLocation = action.getHomeUserLocation();

        if (userLocation.getIp() == null || !IPUtils.isValidPublicIpAdress(InetAddresses.forString(userLocation.getIp()))) {
            return false;
        }

        // if location was manually set, return false to force ranking by country
        
        if (!(userLocation != null && userLocation.getCity() != null
                && userLocation.getCountryCode() != null)) {
            return false;
        }

        return action.getCountryCode().toString().equals(userLocation.getCountryCode());
    }

    private List<RankingItemDTO> getRankingIpList(Product prToShow, StaticRankingTypes type, ActionRecommendation action) {

        Preconditions.checkNotNull(action.getHomeUserLocation());
        Preconditions.checkNotNull(action.getHomeUserLocation().getIp());

        RankingTreeDTO ranking = this.getRankingIp(prToShow, type, action);

        List<RankingItemDTO> list = this.getAsList(ranking);

        return list;

    }

    private RankingTreeDTO getRankingIp(Product prToShow, StaticRankingTypes type, ActionRecommendation action) {

        Preconditions.checkNotNull(action.getHomeUserLocation());
        Preconditions.checkNotNull(action.getHomeUserLocation().getIp());
        RankingTreeDTO ranking = this.hotRankingService.getRankingFromIp(prToShow, InetAddresses.forString(action.getHomeUserLocation().getIp()).getHostAddress(), type, RANKING_SIZE);
        return this.filterZeroFrequency(ranking);
    }

    protected RankingTreeDTO getRankingIata(Product prToShow, String dest, StaticRankingTypes type) {

        // IATA ranking size is lower that RANKING_SIZE so less important cities have more data to show



        RankingTreeDTO ranking = this.hotRankingService
            .getRankingFromIataNoFallback(prToShow, dest, type, IATA_RANKING_SIZE);

        return this.filterZeroFrequency(ranking);
    }

    protected List<RankingItemDTO> getRankingCountryList(Product prToShow, StaticRankingTypes type,
        ActionRecommendation action) {

        RankingTreeDTO ranking = this.getRankingCountry(prToShow, type, action);

        return this.getAsList(this.filterZeroFrequency(ranking));
    }

    private RankingTreeDTO getRankingCountry(Product prToShow, StaticRankingTypes type, ActionRecommendation action) {
        return this.hotRankingService.getRanking(prToShow, type, action.getCountryCode(), RANKING_SIZE);
    }

    private RankingTreeDTO filterZeroFrequency(RankingTreeDTO ranking) {
        RankingTreeDTO filteredRanking = new RankingTreeDTO();
        filteredRanking.setChild(ranking.getChild());
        filteredRanking.setCity(ranking.getCity());

        for (RankingPositionDTO item : ranking.getPodium()) {
            if (item.getFrequency() > 0) {
                filteredRanking.addPosition(item);
            }
        }

        return filteredRanking;
    }

    public ItemHome buildDestination(final Product pr, final StaticRankingTypes rankingType, final String destination,
        final String origin, ActionRecommendation action) {

        action.getTitleData().addDestination(destination);
        switch (pr) {
        case FLIGHTS: {

            String normalizedOrigin = this.normalizeWithAirport(origin);
            String normalizedDestination = this.normalizeWithAirport(destination);

            if (!HomeUtils.isOriginOk(pr, normalizedOrigin, normalizedDestination)) {
                return null;
            }

            String tt = this.getTripType(normalizedOrigin, normalizedDestination, action);
            return new FlightDestinationItem(normalizedDestination, normalizedOrigin, tt);
        }
        case CRUISES:
            switch (rankingType) {
            case CRUISE_DID:
                return new CruiseItem(destination);
            case CRUISE_REGIONS:
                return this.isValidRegion(destination) ? new CruiseRegionItem(destination) : null;
            default:
                throw new IllegalArgumentException("Ranking type: " + rankingType);
            }
        case CLOSED_PACKAGES: {

            String normalizedOrigin = this.normalizeWithAirport(origin);
            String normalizedDestination = this.normalize(destination);

            if (!HomeUtils.isOriginOk(pr, normalizedOrigin, normalizedDestination)) {
                return null;
            }

            return new ClosedPackagesDestinationItem(normalizedDestination, normalizedOrigin);
        }
        case VACATIONRENTALS:
            return new VacationRentalDestinationItem(this.normalize(destination));
        case COMBINED_PRODUCTS:
            return new CombinedProductsDestinationItem(this.normalizeWithAirport(destination));
        case HOTELS: {
            return new HotelDestinationItem(this.normalize(destination));
        }
        case CARS:
            return new CarDestinationItem(this.normalize(destination), "city");

        case ACTIVITIES:
            return new ActivityDestinationItem(this.normalize(destination));
        default:
            throw new IllegalArgumentException("Product not suported for destination: " + pr);
        }
    }

    private boolean isValidRegion(String destination) {
        return StringUtils.isAlpha(destination);
    }

    private String getTripType(String origin, String destination, ActionRecommendation action) {

        if (action.getSearchActivity() != null && action.getSearchActivity().isActivityFor(Product.FLIGHTS)) {

            UserAction userAction = action.getSearchActivity().getActivity(Product.FLIGHTS).getAction();
            FlightData flightSearch = ProductData.create(userAction);

            TripType tt = flightSearch.tipType();

            if (tt != null && TripType.HOME_TYPES.contains(tt)) {
                return tt.toString();
            }
        }

        RankingQuery query = new RankingQuery.Builder(StaticRankingTypes.FLIGHT_TRIP_TYPE, Product.FLIGHTS)
            .withSiteCC(action.getCountryCode()).withOrigin(origin).build();

        try {
        	
            RankingTreeDTO ranking = this.hotRankingService.getRankingNoLocation(query, null, destination, null, 5);

            if (ranking != null && !ranking.getPodium().isEmpty()) {
                String tripType = ranking.getPodium().get(0).getDestination();

                if (tripType.equals(TripType.ROUNDTRIP.toString()) || tripType.equals(TripType.ONE_WAY.toString())) {
                    return tripType;
                } else {
                    log.error("Invalid TripType {} for CC {} origin : {} destination : {} ", tripType,
                        action.getCountryCode(), origin, destination);
                }
            }
        } catch (InvalidRankingRequestException e) {
            log.error("Error while trying to fetch ranking:" + RankingTypeCodes.FLIGHT_TRIP_TYPE + " for city:"
                + destination + " userId: " + action.getUserId());
        }
        return TripType.ROUNDTRIP.toString();
    }

    public void fillDefaultDestinations(ActionRecommendation action, Product product, List<String> destinations, int limit) {
        Iterator<RankingItemDTO> ranking = this.getRankingIterator(product, action,
            StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY);
        while (destinations.size() < limit && ranking.hasNext()) {
            RankingItemDTO item = ranking.next();
            destinations.add(item.getId());
        }
    }

    public GeoService getGeoService() {
        return this.geoService;
    }

    private String normalize(String iata) {
        if (this.geoService == null) {
            return iata;
        }
        return this.getGeoService().normalizeIata(iata);
    }

    public ArrayList<CarRankingPositionDTO> getNormalizedCarRankingPositionDTOList(Iterator<CarRankingPositionDTO> it) {

        ArrayList<CarRankingPositionDTO> dtos = Lists.newArrayList(it);
        for (CarRankingPositionDTO dto : dtos) {
            dto.setPul(this.normalize(dto.getPul()));
        }
        return dtos;
    }

    private String normalizeWithAirport(String iata) {
        if (this.geoService == null) {
            return iata;
        }
        return this.getGeoService().normalizeToCityAirport(iata);
    }

    public abstract String getDescription();

	public RankingsClient getHotRankingService() {
		return hotRankingService;
	}

	public RecommendationsClient getRecommendation() {
		return recommendation;
	}
}
