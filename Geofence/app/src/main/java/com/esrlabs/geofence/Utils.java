package com.esrlabs.geofence;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class Utils {

    public static Location location(String provider, double latitude, double longitude) {
        Location location = new Location(provider);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setTime(System.currentTimeMillis());
        return location;
    }

    public static NotificationManager checkSdkVersionAndFixServiceInit(String channelId, Context ctx) {
        NotificationManager nm = null;

        if (Build.VERSION.SDK_INT > 25) {
            NotificationChannel channel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);

            nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(ctx, "HeadUnitService")
                    .setContentTitle("").setContentText("").build();

            ((Service) ctx).startForeground(1, notification);
        }

        return nm;
    }
}
