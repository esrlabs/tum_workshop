package com.esrlabs.geofence;

import android.location.Location;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.esrlabs.geofence.CircleGeofenceTest.distanceLargerThanRadiusInDeg;
import static com.esrlabs.geofence.Utils.location;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 17)
public class PolygonGeofenceTest extends TestCase {

    // Test polygon defined physically like:
    // 48.119033, 11.601664 +-------+ 48.119051, 11.601766
    //                      |       |
    // 48.117726, 11.602404 +-------+ 48.117758, 11.602543
    private static final Location testPolygonTopLeftCorner = location(NETWORK_PROVIDER, 48.119033, 11.601664);
    private static final Location testPolygonTopRightCorner = location(NETWORK_PROVIDER, 48.119051, 11.601966);
    private static final Location testPolygonBottomLeftCorner = location(NETWORK_PROVIDER, 48.116726, 11.602404);
    private static final Location testPolygonBottomRightCorner = location(NETWORK_PROVIDER, 48.116758, 11.602743);

    private static final PolygonGeofence testGeofence = new PolygonGeofence(testPolygonTopRightCorner, testPolygonTopLeftCorner,
            testPolygonBottomLeftCorner, testPolygonBottomRightCorner, testPolygonTopRightCorner);

    @Test
    public void testContainsLocation() {
        assertTrue(testGeofence.containsLocation(someLocationInside()));
        assertFalse(testGeofence.containsLocation(someLocationOutside()));
    }

    private Location someLocationInside() {
        return location(NETWORK_PROVIDER, 48.118862d, 11.601826d);
    }

    private Location someLocationOutside() {
        return location(NETWORK_PROVIDER, testPolygonTopRightCorner.getLatitude() + distanceLargerThanRadiusInDeg, testPolygonTopRightCorner.getLongitude());
    }
}