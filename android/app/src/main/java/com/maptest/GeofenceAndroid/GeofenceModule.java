package com.maptest.GeofenceAndroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.devsupport.DoubleTapReloadRecognizer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.gson.Gson;
import com.maptest.TransportInfo.DepartureRoot;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by david on 11/22/16.
 */
public class GeofenceModule extends ReactContextBaseJavaModule {

    protected GoogleApiClient mGoogleApiClient;

    public GeofenceModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "GeofenceAndroid";
    }

    @ReactMethod
    public void testJson() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getCurrentActivity());
        String url = "http://sl.se/api/sv/RealTime/GetDepartures/9001";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        try {
                            DepartureRoot dr = gson.fromJson(response, DepartureRoot.class);
                            int t = 1;
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

    @ReactMethod
    public void registerGeofence(double latitude, double longitude, double radius, String stationSiteId, String lineNumber) {
        String requestId = String.format(Locale.ENGLISH, "%s|%s|%f|%f", stationSiteId, lineNumber, latitude, longitude);
        // Create preference file with requestId as file name, it will be unique and we will get it with the geofence event.
        SharedPreferences sharedPref = getCurrentActivity().getSharedPreferences(requestId, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.STATION_SITEID, stationSiteId);
        editor.putString(Constants.LINE_NUMBER, lineNumber);
        editor.putFloat(Constants.LATITUDE, (float) latitude);
        editor.putFloat(Constants.LONGITUDE, (float) longitude);
        editor.putFloat(Constants.RADIUS, (float) radius);
        editor.commit();
        Intent intent = new Intent(getCurrentActivity(), GeofenceActivity.class);
        intent.putExtra(Constants.FENCEID, requestId);
        intent.putExtra(Constants.LATITUDE, latitude);
        intent.putExtra(Constants.LONGITUDE, longitude);
        intent.putExtra(Constants.RADIUS, (float) radius);
        getCurrentActivity().startActivity(intent);
    }

}
