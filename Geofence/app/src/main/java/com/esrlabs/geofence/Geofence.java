package com.esrlabs.geofence;

import android.location.Location;

public interface Geofence {

    boolean containsLocation(Location location);
}
