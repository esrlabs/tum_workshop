package com.esrlabs.geofence;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.esrlabs.headunitinterface.HeadUnit;

/**
 * Steps:
 * - register to the location provider
 * - connect to the HeadUnitService
 * - build geofence object
 * - implement the exercise logic (when the user is outside of the geofence show popup; hide it otherwise)
 * <p>
 * See:
 * - that tests are green
 * - that the notification appears in the emulator
 */
public class GeofenceApp extends Service implements LocationListener {
    public static final String TAG = "GeofenceApp";

    private LocationManager locationManager;
    private Geofence geofence;
    private HeadUnit headUnit;

    public GeofenceApp() {
    }

    public GeofenceApp(LocationManager locationManager, Geofence geofence) {
        this.locationManager = locationManager;
        this.geofence = geofence;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");

        // Fix for Android 8
        Utils.checkSdkVersionAndFixServiceInit("GeofenceApp", this);

        if (locationManager == null) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }

        initLocationListener();
        initHeadUnitService();
        if (geofence == null) {
            geofence = Geofences.getGeofence();
        }
    }

    /**
     * Bind to the HeadUnitService using a intent
     */
    private void initHeadUnitService() {
        // TODO: implement according to docstring
    }

    /**
     * Initialize the location listener to subscribe to location updates from all available location providers.
     */
    private void initLocationListener() {
        // TODO: implement according to docstring
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Overwrites the {@link LocationListener} method for location Updates
     * <br>
     * Check the new location against the geofence.
     * If the geofence was left, show a notification.
     * Otherwise, clear the notifications
     *
     * @param location The location optained from the update
     */
    @Override
    public void onLocationChanged(Location location) {
        // TODO: implement this function according to the docstring
    }

    /**
     * @return The latest location obtained by any location provider
     */
    public Location latestLocation() {
        // TODO: return the latest obtained location
        return null;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    /**
     * Only for testing
     */
    @Deprecated
    public void onCreate(IBinder service) {
        super.onCreate();

        if (locationManager == null) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }

        initLocationListener();
        this.headUnit = HeadUnit.Stub.asInterface(service);
        if (geofence == null) {
            geofence = Geofences.getGeofence();
        }
    }
}