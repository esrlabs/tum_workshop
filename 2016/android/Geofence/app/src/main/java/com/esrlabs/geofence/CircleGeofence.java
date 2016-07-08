package com.esrlabs.geofence;

import android.location.Location;

public class CircleGeofence implements Geofence {
    final Location center;
    final float radiusInMeters;

    public CircleGeofence(Location center, float radiusInMeters) {
        this.center = center;
        this.radiusInMeters = radiusInMeters;
    }

    @Override
    public boolean containsLocation(Location location) {
        float distanceInMeters = center.distanceTo(location);

        return (distanceInMeters <= radiusInMeters);
    }
}
