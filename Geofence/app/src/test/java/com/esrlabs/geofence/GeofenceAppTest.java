package com.esrlabs.geofence;

import android.content.ComponentName;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;

import com.esrlabs.headunitinterface.HeadUnit;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLocationManager;

import java.util.concurrent.atomic.AtomicBoolean;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.esrlabs.geofence.CircleGeofenceTest.someCircleGeofence;
import static com.esrlabs.geofence.Utils.location;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.robolectric.Shadows.shadowOf;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 17)
public class GeofenceAppTest extends TestCase {
    private final LocationManager locationManager = (LocationManager)
            RuntimeEnvironment.application.getSystemService(Context.LOCATION_SERVICE);
    private final ShadowLocationManager shadowLocationManager = shadowOf(locationManager);
    private final Location someLocation = Utils.location(NETWORK_PROVIDER, 12.0, 20.0);
    private final AtomicBoolean notificationVisibility = new AtomicBoolean(false);


    private IBinder headUnitStub;

    private GeofenceApp geofenceApp;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        initHeadUnitServiceMock();
        setupGeofenceApp();
    }

    private void initHeadUnitServiceMock() {
        headUnitStub = newTestHeadUnitServerBinder();
        shadowOf(RuntimeEnvironment.application).setComponentNameAndServiceForBindService(
                new ComponentName("com.esrlabs.headunitservice", "HeadUnit"), headUnitStub);
    }

    private IBinder newTestHeadUnitServerBinder() {
        return new HeadUnit.Stub() {
            @Override
            public void showNotification(String text) {
                notificationVisibility.set(true);
            }

            @Override
            public void hideAllNotifications() {
                notificationVisibility.set(false);
            }
        };
    }

    private void setupGeofenceApp() {
        GeofenceApp service = new GeofenceApp(locationManager, someCircleGeofence);
        service.onCreate(headUnitStub);
        geofenceApp = service;
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        geofenceApp.onDestroy();
    }

    @Test
    public void shouldReceiveTheLatestLocation() {
        simulateNewLocation(someLocation);
        assertThat(someLocation.getProvider(), is(geofenceApp.latestLocation().getProvider()));
        assertThat(someLocation.getLatitude(), is(geofenceApp.latestLocation().getLatitude()));
        assertThat(someLocation.getLongitude(), is(geofenceApp.latestLocation().getLongitude()));
        assertThat(someLocation.getTime(), is(geofenceApp.latestLocation().getTime()));
    }

    @Test
    public void shouldShowPopupWhenTheCurrentLocationIsOutsideTheGeofence() throws Exception {
        Location locationInside = location(NETWORK_PROVIDER, someCircleGeofence.center.getLatitude() + CircleGeofenceTest.distanceSmallerThanRadiusInDeg,
                someCircleGeofence.center.getLongitude());
        simulateNewLocation(locationInside);
        assertThat(locationInside.getProvider(), is(geofenceApp.latestLocation().getProvider()));
        assertThat(locationInside.getLatitude(), is(geofenceApp.latestLocation().getLatitude()));
        assertThat(locationInside.getLongitude(), is(geofenceApp.latestLocation().getLongitude()));
        assertThat(locationInside.getTime(), is(geofenceApp.latestLocation().getTime()));
        assertFalse(notificationVisibility.get());

        Thread.sleep(200);

        Location locationOutside = location(NETWORK_PROVIDER, someCircleGeofence.center.getLatitude() + CircleGeofenceTest.distanceLargerThanRadiusInDeg,
                someCircleGeofence.center.getLongitude());
        simulateNewLocation(locationOutside);
        assertThat(locationOutside.getProvider(), is(geofenceApp.latestLocation().getProvider()));
        assertThat(locationOutside.getLatitude(), is(geofenceApp.latestLocation().getLatitude()));
        assertThat(locationOutside.getLongitude(), is(geofenceApp.latestLocation().getLongitude()));
        assertThat(locationOutside.getTime(), is(geofenceApp.latestLocation().getTime()));
        assertTrue(notificationVisibility.get());

        Thread.sleep(200);

        Location nextLocationInside = location(NETWORK_PROVIDER, someCircleGeofence.center.getLatitude() + CircleGeofenceTest.distanceSmallerThanRadiusInDeg,
                someCircleGeofence.center.getLongitude());
        simulateNewLocation(nextLocationInside);
        assertThat(nextLocationInside.getProvider(), is(geofenceApp.latestLocation().getProvider()));
        assertThat(nextLocationInside.getLatitude(), is(geofenceApp.latestLocation().getLatitude()));
        assertThat(nextLocationInside.getLongitude(), is(geofenceApp.latestLocation().getLongitude()));
        assertThat(nextLocationInside.getTime(), is(geofenceApp.latestLocation().getTime()));
        assertFalse(notificationVisibility.get());
    }

    private void simulateNewLocation(Location someLocation) {
        shadowLocationManager.simulateLocation(someLocation);
    }
}