package com.maptest.GeofenceAndroid;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.maptest.MainActivity;
import com.maptest.R;

import java.util.List;

import static com.facebook.react.common.ReactConstants.TAG;

/**
 * Created by david on 11/27/16.
 */

public class GeofenceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = Integer.toString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            // Triggering geofences should be > 0 unless the Android developers are stupid in their heads
            String requestId = triggeringGeofences.get(0).getRequestId();
            SharedPreferences sharedPref = context.getSharedPreferences(requestId, Context.MODE_PRIVATE);
            String stationSiteId = sharedPref.getString(Constants.STATION_SITEID, "");
            String lineNumber = sharedPref.getString(Constants.LINE_NUMBER, "");

            // Get the transition details as a String.
            final String geofenceTransitionDetails = geofencingEvent.getTriggeringLocation().toString();

            // Send notification and log the transition details.
            Log.i(TAG, geofenceTransitionDetails);
            Log.i(TAG, "geofenceReceiver");
            sendNotification(context, geofenceTransitionDetails);
            vibrate(context);
        } else {
            // Log the error.
            Log.e(TAG, Integer.toString(geofenceTransition));
        }

    }

    private void vibrate(Context context) {
        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(2000);
    }

    private void sendNotification(Context context, String notificationDetails) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(234, mBuilder.build());
    }
}
