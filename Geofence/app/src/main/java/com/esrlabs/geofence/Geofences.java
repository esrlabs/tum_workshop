package com.esrlabs.geofence;

import android.location.Location;
import android.support.annotation.NonNull;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class Geofences {

    @NonNull
    public static Geofence getGeofence(){
        return getCircleEsrlabs();
    }

    @NonNull
    public static Geofence getCircleGarching(){
         final int someRadiusInMeters = 25;
         final Location someCenterForTheGeofence = Utils.location(NETWORK_PROVIDER, 48.262541, 11.670033);
         return new CircleGeofence(someCenterForTheGeofence, someRadiusInMeters);
    }

    @NonNull
    public static Geofence getCircleEsrlabs(){
        // Location inside: 48,119, 11.601
        final int someRadiusInMeters = 25;
        final Location someCenterForTheGeofence = Utils.location(NETWORK_PROVIDER, 48.118920, 11.601057);
        return new CircleGeofence(someCenterForTheGeofence, someRadiusInMeters);
    }

    @NonNull
    public static Geofence getPolygonGarching(){
        // Location inside: 48.261841, 11.670078
        final Location testPolygonTopLeftCorner = Utils.location(NETWORK_PROVIDER, 48.263943, 11.669885);
        final Location testPolygonTopRightCorner = Utils.location(NETWORK_PROVIDER, 48.263574, 11.671915);
        final Location testPolygonBottomLeftCorner = Utils.location(NETWORK_PROVIDER, 48.260834, 11.666707);
        final Location testPolygonBottomRightCorner = Utils.location(NETWORK_PROVIDER, 48.260538, 11.670596);
        return new PolygonGeofence(testPolygonTopRightCorner, testPolygonTopLeftCorner,
                testPolygonBottomLeftCorner, testPolygonBottomRightCorner, testPolygonTopRightCorner);
    }

    @NonNull
    public static Geofence getPolygonEsrlabs(){
        // Location inside: 48.117345, 11.603132
        final Location testPolygonTopLeftCorner = Utils.location(NETWORK_PROVIDER, 48.117563, 11.602369); // 48.119549, 11.602188
        final Location testPolygonTopRightCorner = Utils.location(NETWORK_PROVIDER, 48.117845, 11.603586); // 48.119718, 11.603057
        final Location testPolygonBottomLeftCorner = Utils.location(NETWORK_PROVIDER, 48.116908, 11.602651); // 48.118159, 11.603778
        final Location testPolygonBottomRightCorner = Utils.location(NETWORK_PROVIDER, 48.117195, 11.603968); // 48.118092, 11.603218
        return new PolygonGeofence(testPolygonTopRightCorner, testPolygonTopLeftCorner,
                testPolygonBottomLeftCorner, testPolygonBottomRightCorner, testPolygonTopRightCorner);
    }
}
