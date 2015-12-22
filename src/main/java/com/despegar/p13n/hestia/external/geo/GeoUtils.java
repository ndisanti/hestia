package com.despegar.p13n.hestia.external.geo;

import org.jfree.util.Log;

import ch.hsr.geohash.WGS84Point;

public class GeoUtils {

    private static final int EARTH_RADIUS_METERS = 6371000;

    /**
     * 
     * Calculates the Haversine distance between two points(latitude-longitude). This method considers the earth curvature
     * @return the distance between the two points in meters
     */
    public static double haversineDistanceBetweenTwoPoints(GeoPoint firstPoint, GeoPoint secondPoint) {
        // Haversine Formula (H)
        // ** H(d/R) = H(phi2 - phi1) + cos(phi1)*cos(phi2)*H(lambda2 - lambda1) **
        // where H(phi) = sin^2(phi/2)
        // and d represents the distance
        // and phi values references to latitude
        // and lambda values references to longitude
        double latitude1 = firstPoint.getLatitude();
        double longitude1 = firstPoint.getLongitude();
        double latitude2 = secondPoint.getLatitude();
        double longitude2 = secondPoint.getLongitude();

        double radianLat1 = Math.toRadians(latitude1);
        double radianLat2 = Math.toRadians(latitude2);
        double deltaRadianLatiutud = Math.toRadians(latitude2 - latitude1);
        double deltaRadianLongitud = Math.toRadians(longitude2 - longitude1);

        double haversineDeltaLatitude = Math.pow(Math.sin(deltaRadianLatiutud / 2), 2);
        double haversineDelataLongitud = Math.pow(Math.sin(deltaRadianLongitud / 2), 2);

        double haversineDOverR = haversineDeltaLatitude + haversineDelataLongitud * Math.cos(radianLat1)
            * Math.cos(radianLat2);
        double dOverR = 2 * Math.atan2(Math.sqrt(haversineDOverR), Math.sqrt(1 - haversineDOverR));
        return dOverR * EARTH_RADIUS_METERS;
    }

    public static boolean isLongLatValuesOk(Double latitude, Double longitude) {

        if (latitude == null || longitude == null) {
            return false;
        } else {
            if (longitude > 180 || longitude < -180 || latitude < -90 || latitude > 90) {
                Log.error("Latitude: " + latitude + " or Longitude: " + longitude + " values are not valid");
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 
     * Defines if to points are nearby considering the given radius measured in meters
     */
    public static boolean areNearbyPoints(GeoPoint firstPoint, GeoPoint secondPoint, int metersRadius) {
        return GeoUtils.haversineDistanceBetweenTwoPoints(firstPoint, secondPoint) <= metersRadius;
    }

    public static WGS84Point toWGS84Point(CityDto dto) {
        return new WGS84Point(dto.getLocation().getLatitude(), dto.getLocation().getLongitude());
    }

    public static WGS84Point toWGS84Point(GeoPointDto dto) {
        return new WGS84Point(dto.getLocation().getLatitude(), dto.getLocation().getLongitude());
    }
}
