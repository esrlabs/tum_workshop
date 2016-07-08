package com.esrlabs.geofence;

import android.location.Location;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import esrlabs.com.geofence.BuildConfig;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.esrlabs.geofence.CircleGeofenceTest.distanceLargerThanRadiusInDeg;
import static com.esrlabs.geofence.Utils.location;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 17)
public class PolygonGeofenceTest extends TestCase {
    // Test polygon defined physically like:
    // 48.119033, 11.601664         48.119051, 11.603766
    //
    //              48.118051, 11.600766 -> This doesn't work - the test was defect
    //
    // 48.117726, 11.602404         48.117758, 11.602543
    public static final Location testPolygonTopLeftCorner = location(NETWORK_PROVIDER, 48.119033, 11.601664);
    public static final Location testPolygonTopRightCorner = location(NETWORK_PROVIDER, 48.119051, 11.603766);
    public static final Location testPolygonBottomRightCorner = location(NETWORK_PROVIDER, 48.117758, 11.602543);
    public static final Location testPolygonBottomLeftCorner = location(NETWORK_PROVIDER, 48.117726, 11.602404);
    public static final PolygonGeofence testGeofence = new PolygonGeofence(testPolygonTopRightCorner, testPolygonTopLeftCorner,
            testPolygonBottomRightCorner, testPolygonBottomLeftCorner);

    @Test
    public void testContainsLocation() throws Exception {
        assertTrue(testGeofence.containsLocation(someLocationInside()));
        assertTrue(testGeofence.containsLocation(anotherLocationInside()));
        assertFalse(testGeofence.containsLocation(someLocationOutside()));
    }

    private Location someLocationInside() {
        return location(NETWORK_PROVIDER, testPolygonTopRightCorner.getLatitude() - 0.0001, testPolygonTopRightCorner.getLongitude() - 0.0001);
    }

    private Location anotherLocationInside() {
        return location(NETWORK_PROVIDER, 48.118, 11.60245);
    }

    private Location someLocationOutside() {
        return location(NETWORK_PROVIDER, testPolygonTopRightCorner.getLatitude() + distanceLargerThanRadiusInDeg, testPolygonTopRightCorner.getLongitude());
    }
}