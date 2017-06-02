package com.teller.GeofenceAndroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.teller.Utils.DepartureParser;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.react.common.ReactConstants.TAG;

/**
 * Created by david on 11/27/16.
 */

public class GeofenceReceiver extends BroadcastReceiver implements IAsync {

    private Context context;

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = Integer.toString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            // Triggering geofences should always be > 0, but you never know
            if (triggeringGeofences.size() < 1) {
                return;
            }
            String requestId = triggeringGeofences.get(0).getRequestId();

            DatabaseHandler db = new DatabaseHandler(context);
            List<Departure> list = db.getDepartures(requestId);

            // Find all stations used in this geofence to avoid making multiple requests for the same station.
            final List<Result> filteredList = new ArrayList<>();
            for (Departure dep : list) {
                boolean found = false;
                for (Result res : filteredList) {
                    if (res.getSiteId().equals(dep.getSiteId())) {
                        found = true;
                        res.addDeparture(dep);
                    }
                }
                if (!found) {
                    Result res = new Result(dep.getSiteId());
                    res.addDeparture(dep);
                    filteredList.add(res);
                }
            }

            // Create new handler to receive all request result, will then call onFinished in this class.
            IAsync callback = new RequestHandler(context, filteredList.size(), this);
            // Wait for all station information including real-time departures
            for (Result res : filteredList) {
                getDepartures(context, res, callback);
            }
        } else {
            // Log the error.
            Log.e(TAG, Integer.toString(geofenceTransition));
        }

    }

    private void getDepartures(final Context context, final Result result, final IAsync callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://sl.se/api/sv/RealTime/GetDepartures/" + result.getSiteId();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        result.setResponse(response);
                        callback.onFinished(result);
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result.setResponse(null);
                callback.onFinished(result);
            }
        }

        );
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void onFinished(Object obj) {
        List<Result> list = (List<Result>) obj;
        int minMinutes = 1000;
        for (Result res : list) {
            if (res.getResponse() != null) {
                int minutes = DepartureParser.findEarliestDeparture(res);
                if (minutes < minMinutes) {
                    minMinutes = minutes;
                }
            }
        }
        CharSequence text = "Minutes left: "+Integer.toString(minMinutes);
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        if (context != null) {
            //vibrate(context, parseMin("10:02"));
            vibrate(context, minMinutes);
            //sendNotification(context, destination + ", " + lineNumber + ", " + Integer.toString(parseMin(filteredDepartures.get(0).getDisplayTime())));
        }
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
}
