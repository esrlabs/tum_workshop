package com.esrlabs.headunitservice;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.esrlabs.headunitinterface.HeadUnit;

/**
 * A test implementation of the HeadUnit interface
 * <p>
 * Provides a way to run against the interface without the presence of a real headunit
 * <p>
 * Will use the Android UI instead
 */
public class HeadUnitService extends Service {

    public static final String TAG = "HeadUnitServiceMock";

    private static final String CHANNEL_ID = "HeadUnitService";
    private volatile boolean notificationsAreHidden = false;
    private final HeadUnit.Stub binder = new HeadUnit.Stub() {
        @Override
        public void showNotification(String text) {
            Log.i(TAG, "showNotification '" + text + "'");
            Notification notification;
            if (Build.VERSION.SDK_INT < 26) {
                notification = new Notification
                        .Builder(getBaseContext())
                            .setContentText(text)
                            .setContentTitle("HeadUnit")
                            .setSmallIcon(R.drawable.tum_logo)
                        .build();
            } else {
                notification = new NotificationCompat
                        .Builder(HeadUnitService.this, CHANNEL_ID)
                            .setContentTitle("HeadUnit")
                            .setContentText(text)
                            .setSmallIcon(R.drawable.tum_logo)
                        .build();

            }
            notificationService().notify(0, notification);

            notificationsAreHidden = false;
        }

        @Override
        public void hideAllNotifications() {
            Log.i(TAG, "hideAllNotifications");

            if (!notificationsAreHidden) {
                if (Build.VERSION.SDK_INT < 26) {
                    notificationService().cancelAll();
                } else {
                    notificationService().deleteNotificationChannel(CHANNEL_ID);
                    establishNotificationChannel();
                }
                notificationsAreHidden = true;
            }
        }
    };

    @SuppressLint("NewApi")
    private void establishNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);

        notificationService().createNotificationChannel(channel);

        Notification notification = new NotificationCompat
                .Builder(HeadUnitService.this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("")
                .build();

        startForeground(1, notification);
    }

    public HeadUnitService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        if (Build.VERSION.SDK_INT > 25) {
            establishNotificationChannel();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return binder;
    }

    private NotificationManager notificationService() {
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
