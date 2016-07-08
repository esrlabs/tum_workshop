package com.esrlabs.geofence;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.esrlabs.headunitinterface.HeadUnit;

import java.util.List;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.esrlabs.geofence.Utils.location;

/**
 * Steps:
 * - register to the location provider
 * - connect to the HeadUnitService
 * - build geofence object
 * - implement the exercise logic (when the user is outside of the geofence show popup; hide it otherwise)
 *
 * See:
 * - that tests are green
 * - that the notification appears in the emulator
 */
public class GeofenceApp extends Service implements LocationListener {
    public static final String TAG = "GeofenceApp";

    private LocationManager locationManager;
    private Geofence geofence;
    private ServiceConnection mConnection;
    private HeadUnit mOurHeadUnitService;
    private Location mLatestLocation;

    public GeofenceApp(LocationManager aLocationManager, Geofence aGeofence) {
        locationManager = aLocationManager;
        geofence = aGeofence;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");

        if (locationManager == null) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }

        initLocationListener();
        initHeadUnitService();

        if (geofence == null) {
            final int someRadiusInMeters = 25;
            final Location someCenterForTheGeofence = location(NETWORK_PROVIDER, 48.118920, 11.601057);
            geofence = new CircleGeofence(someCenterForTheGeofence, someRadiusInMeters);
        }
    }



    private void initHeadUnitService() {
        Intent headUnitServiceIntent = new Intent(HeadUnit.class.getName());
        headUnitServiceIntent.setPackage("com.esrlabs.headunitservice");

        mConnection = new ServiceConnection() {
            // Called when the connection with the service is established
            public void onServiceConnected(ComponentName className, IBinder service) {
                // Following the example above for an AIDL interface,
                // this gets an instance of the IRemoteInterface, which we can use to call on the service
                mOurHeadUnitService = HeadUnit.Stub.asInterface(service);
            };

            // Called when the connection with the service disconnects unexpectedly
            public void onServiceDisconnected(ComponentName className) {
                Log.e(TAG, "Service has unexpectedly disconnected");
                mOurHeadUnitService = null;
            }
        };

        bindService(headUnitServiceIntent, mConnection, BIND_AUTO_CREATE);
    }

    private void initLocationListener() {
        List<String> locationProviders = locationManager.getAllProviders();

        for(String provider : locationProviders) {
            locationManager.requestLocationUpdates(provider, 0, 0, this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatestLocation = location;

        try {
            if(geofence.containsLocation(location)) {
                mOurHeadUnitService.hideAllNotifications();
            } else {
                mOurHeadUnitService.showNotification("Outside Geofence!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Location latestLocation() {
        return mLatestLocation;
    }
}
