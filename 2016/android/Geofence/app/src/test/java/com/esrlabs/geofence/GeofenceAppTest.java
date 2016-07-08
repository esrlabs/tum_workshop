package com.esrlabs.geofence;

import android.content.ComponentName;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.RemoteException;

import com.esrlabs.headunitinterface.HeadUnit;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLocationManager;

import java.util.concurrent.atomic.AtomicBoolean;

import esrlabs.com.geofence.BuildConfig;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.esrlabs.geofence.CircleGeofenceTest.someCircleGeofence;
import static com.esrlabs.geofence.Utils.location;
import static org.robolectric.Shadows.shadowOf;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 17)
public class GeofenceAppTest extends TestCase {
    private final LocationManager locationManager = (LocationManager)
            RuntimeEnvironment.application.getSystemService(Context.LOCATION_SERVICE);
    private final ShadowLocationManager shadowLocationManager = shadowOf(locationManager);
    private final Location someLocation = Utils.location(NETWORK_PROVIDER, 12.0, 20.0);
    private final AtomicBoolean notificationVisibility = new AtomicBoolean(false);


    private GeofenceApp geofenceApp;

    @Before
    public void setUp() throws Exception {
        initHeadUnitServiceMock();
        setupGeofenceApp();
    }

    private void initHeadUnitServiceMock() {
        IBinder headUnitStub = newTestHeadUnitServerBinder();
        shadowOf(RuntimeEnvironment.application).setComponentNameAndServiceForBindService(
                new ComponentName("com.esrlabs.headunitinterface", "HeadUnit"), headUnitStub);
    }

    private IBinder newTestHeadUnitServerBinder() {
        return new HeadUnit.Stub() {
                @Override
                public void showNotification(String text) throws RemoteException {
                    notificationVisibility.set(true);
                }

                @Override
                public void hideAllNotifications() throws RemoteException {
                    notificationVisibility.set(false);
                }
            };
    }

    private void setupGeofenceApp() {
        GeofenceApp service = new GeofenceApp(locationManager, someCircleGeofence);
        service.onCreate();
        geofenceApp = service;
    }

    @After
    public void tearDown() throws Exception {
        geofenceApp.onDestroy();
    }

    @Test
    public void shouldReceiveTheLatestLocation() throws Exception {
        simulateNewLocation(someLocation);
        Location testLocation = geofenceApp.latestLocation();
        assertTrue(someLocation.equals(testLocation));
    }

    @Test
    public void shouldShowPopupWhenTheCurrentLocationIsOutsideTheGeofence() throws Exception {
        Location locationInside = location(NETWORK_PROVIDER, someCircleGeofence.center.getLatitude() + CircleGeofenceTest.distanceSmallerThanRadiusInDeg,
                someCircleGeofence.center.getLongitude());
        simulateNewLocation(locationInside);
        assertTrue(locationInside.equals(geofenceApp.latestLocation()));
        assertFalse(notificationVisibility.get());

        Location locationOutside = location(NETWORK_PROVIDER, someCircleGeofence.center.getLatitude() + CircleGeofenceTest.distanceLargerThanRadiusInDeg,
                someCircleGeofence.center.getLongitude());
        simulateNewLocation(locationOutside);
        assertTrue(locationOutside.equals(geofenceApp.latestLocation()));
        assertTrue(notificationVisibility.get());

        Location nextLocationInside = location(NETWORK_PROVIDER, someCircleGeofence.center.getLatitude() + CircleGeofenceTest.distanceSmallerThanRadiusInDeg,
                someCircleGeofence.center.getLongitude());
        simulateNewLocation(nextLocationInside);
        assertTrue(nextLocationInside.equals(geofenceApp.latestLocation()));
        assertFalse(notificationVisibility.get());
    }

    private void simulateNewLocation(Location someLocation) {
        shadowLocationManager.simulateLocation(someLocation);
    }


}