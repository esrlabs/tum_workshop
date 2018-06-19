package com.esrlabs.geofence;

import android.location.Location;

public class CircleGeofence implements Geofence {

    private final float radiusInMeters;
    final Location center;

    CircleGeofence(Location center, float radiusInMeters) {
        this.center = center;
        this.radiusInMeters = radiusInMeters;
    }

    /**
     * Predicate checking the {@param location} against the defined {@link CircleGeofence}
     * @param location: The location to check
     * @return true if geofence contains the location
     */
    @Override
    public boolean containsLocation(Location location) {
        // TODO: implement this function according to the docstring
        return false;
    }
}
