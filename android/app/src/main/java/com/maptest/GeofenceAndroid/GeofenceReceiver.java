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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.react.bridge.ReactApplicationContext;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.gson.Gson;
import com.maptest.MainActivity;
import com.maptest.R;
import com.maptest.TransportInfo.BusGroup;
import com.maptest.TransportInfo.Data;
import com.maptest.TransportInfo.DepartureRoot;
import com.maptest.TransportInfo.IDeparture;
import com.maptest.TransportInfo.MetroGroup;
import com.maptest.TransportInfo.TrainGroup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.facebook.react.common.ReactConstants.TAG;
import static com.maptest.Utils.TimeParser.parseMin;

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
            String destination = sharedPref.getString(Constants.DESTINATION, "");
            String lineNumber = sharedPref.getString(Constants.LINE_NUMBER, "");

            // Get the transition details as a String.
            final String geofenceTransitionDetails = geofencingEvent.getTriggeringLocation().toString();

            // Send notification and log the transition details.
            Log.i(TAG, geofenceTransitionDetails);
            Log.i(TAG, "geofenceReceiver");
            getDepartures(context, destination, lineNumber,stationSiteId);
        } else {
            // Log the error.
            Log.e(TAG, Integer.toString(geofenceTransition));
        }

    }

    private void getDepartures(final Context context, final String destination, final String lineNumber, final String siteId) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://sl.se/api/sv/RealTime/GetDepartures/"+siteId;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        try {
                            DepartureRoot dr = gson.fromJson(response, DepartureRoot.class);
                            List<IDeparture> list = new ArrayList<>();
                            Data data = dr.getData();
                            for (BusGroup group : dr.getData().getBusGroups()) {
                                list.addAll(group.getDepartures());
                            }
                            for (MetroGroup group : dr.getData().getMetroGroups()) {
                                list.addAll(group.getDepartures());
                            }
                            for (TrainGroup group : dr.getData().getTrainGroups()) {
                                list.addAll(group.getDepartures());
                            }
                            List<IDeparture> filteredDepartures = new ArrayList<>();
                            for (IDeparture dep : list) {
                                if (dep.getDestination().equals(destination) && dep.getLineNumber().equals(lineNumber)) {
                                    filteredDepartures.add(dep);
                                }
                            }
                            Collections.sort(filteredDepartures, new Comparator<IDeparture>() {
                                @Override
                                public int compare(IDeparture iDeparture, IDeparture t1) {
                                    int diff = parseMin(iDeparture.getDisplayTime()) - parseMin(t1.getDisplayTime());
                                    if (diff < 0)
                                        return -1;
                                    else if (diff > 0)
                                        return 1;
                                    else
                                        return 0;
                                }
                            });
                            for (IDeparture dep : filteredDepartures) {
                                Log.d("MainActivity", String.format("%s, %s, %s", dep.getDestination(), dep.getLineNumber(), dep.getDisplayTime()));
                            }
                            //vibrate(context, parseMin("10:02"));
                            vibrate(context, parseMin(filteredDepartures.get(0).getDisplayTime()));
                            //sendNotification(context, destination + ", " + lineNumber + ", " + Integer.toString(parseMin(filteredDepartures.get(0).getDisplayTime())));
                        } catch (Exception e) {
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }

        );
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void vibrate(Context context, int minutes) {
        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (minutes == 0) {
            vib.vibrate(2000);
            return;
        } else if (minutes > 8) {
            vib.vibrate(4000);
            return;
        }
        long pause = 500;
        long active = 500;
        long[] pattern = new long[2 * minutes];
        for (int i = 0; i < minutes; i++) {
            pattern[(2 * i)] = active;
            pattern[(2 * i) + 1] = pause;
        }
        vib.vibrate(pattern, -1);
    }


    private void sendNotification(Context context, String notificationDetails) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("My notification")
                        .setContentText(notificationDetails);
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
