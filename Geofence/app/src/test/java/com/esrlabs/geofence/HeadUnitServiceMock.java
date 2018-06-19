package com.esrlabs.geofence;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class HeadUnitServiceMock extends Service {
    public static final String TAG = "HeadUnitServiceMockTest";

    final IBinder headUnitStub;

    public HeadUnitServiceMock(IBinder headUnitStub) {
        this.headUnitStub = headUnitStub;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return headUnitStub;
    }
}
