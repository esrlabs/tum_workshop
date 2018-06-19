package com.esrlabs.geofence;

import android.location.Location;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.esrlabs.geofence.Utils.location;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 17)
public class CircleGeofenceTest extends TestCase {
    public static final double distanceSmallerThanRadiusInDeg = 0.0001;
    public static final double distanceLargerThanRadiusInDeg = 0.001;

    private static final float someRadiusInMeters = 100;
    private static final Location someCenterForTheGeofence = location(NETWORK_PROVIDER, 48.118920, 11.601057);
    static final CircleGeofence someCircleGeofence = new CircleGeofence(someCenterForTheGeofence, someRadiusInMeters);

    @Test
    public void testContainsLocation() {
        assertTrue(someCircleGeofence.containsLocation(someLocationInside()));
        assertFalse(someCircleGeofence.containsLocation(someLocationOutside()));
    }

    private Location someLocationInside() {
        return location(NETWORK_PROVIDER, someCenterForTheGeofence.getLatitude() + distanceSmallerThanRadiusInDeg, someCenterForTheGeofence.getLongitude());
    }

    private Location someLocationOutside() {
        return location(NETWORK_PROVIDER, someCenterForTheGeofence.getLatitude() + distanceLargerThanRadiusInDeg, someCenterForTheGeofence.getLongitude());
    }
}