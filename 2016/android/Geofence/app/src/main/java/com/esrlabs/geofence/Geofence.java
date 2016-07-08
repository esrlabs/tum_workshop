package com.esrlabs.geofence;

import android.location.Location;

public interface Geofence {
    public boolean containsLocation(Location location);
}
