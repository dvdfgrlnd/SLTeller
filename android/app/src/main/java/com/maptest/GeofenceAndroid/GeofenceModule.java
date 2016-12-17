package com.maptest.GeofenceAndroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.JavaOnlyArray;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.devsupport.DoubleTapReloadRecognizer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.maptest.TransportInfo.DepartureRoot;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by david on 11/22/16.
 */
public class GeofenceModule extends ReactContextBaseJavaModule {

    private Callback mResultCallback;
    private SQLiteDatabase db;

    public GeofenceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    @Override
    public String getName() {
        return "GeofenceAndroid";
    }

    @ReactMethod
    public void getGeofences(Callback c) {
        SharedPreferences sharedPrefFiles = getCurrentActivity().getSharedPreferences(Constants.FILES, Context.MODE_PRIVATE);
        Map<String, ?> map = sharedPrefFiles.getAll();
        WritableArray array = new WritableNativeArray();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            array.pushString(entry.getKey());
            Log.d("GeofenceModule", entry.getKey());
        }
        c.invoke(array);
    }

    @ReactMethod
    public void removeGeofence(String requestId, Callback c) {
        mResultCallback=c;
        SharedPreferences sharedPrefFiles = getCurrentActivity().getSharedPreferences(Constants.FILES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefFiles.edit();
        editor.remove(requestId);
        editor.commit();
        // Remove the sharedpreferences for this geofence.
        // NOTE: This will not remove the file! Should use a separate SQLite instance instead of SharedPreferences
        getCurrentActivity().getSharedPreferences(requestId, Context.MODE_PRIVATE).edit().clear().commit();

        Intent intent = new Intent(getCurrentActivity(), GeofenceActivity.class);
        intent.putExtra(Constants.ACTION, Constants.REMOVE);
        intent.putExtra(Constants.FENCEID, requestId);
        getCurrentActivity().startActivityForResult(intent,0);
    }

    @ReactMethod
    public void registerGeofence(double latitude, double longitude, double radius, String stationSiteId, String destination, String lineNumber, Callback callback) {
        mResultCallback=callback;

        String requestId = String.format(Locale.ENGLISH, "%s|%s|%s|%f|%f", stationSiteId, destination, lineNumber, latitude, longitude);
        // Save all requestId's because it's stupidly hard to retrieve all files
        SharedPreferences sharedPrefFiles = getCurrentActivity().getSharedPreferences(Constants.FILES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefFiles.edit();
        editor.putString(requestId, requestId);
        editor.commit();

        // Create preference file with requestId as file name, it will be unique and we will get it with the geofence event.
        SharedPreferences sharedPref = getCurrentActivity().getSharedPreferences(requestId, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putString(Constants.STATION_SITEID, stationSiteId);
        editor.putString(Constants.DESTINATION, destination);
        editor.putString(Constants.LINE_NUMBER, lineNumber);
        editor.putFloat(Constants.LATITUDE, (float) latitude);
        editor.putFloat(Constants.LONGITUDE, (float) longitude);
        editor.putFloat(Constants.RADIUS, (float) radius);
        editor.commit();
        Intent intent = new Intent(getCurrentActivity(), GeofenceActivity.class);
        intent.putExtra(Constants.ACTION, Constants.CREATE);
        intent.putExtra(Constants.FENCEID, requestId);
        intent.putExtra(Constants.LATITUDE, latitude);
        intent.putExtra(Constants.LONGITUDE, longitude);
        intent.putExtra(Constants.RADIUS, (float) radius);
        getCurrentActivity().startActivityForResult(intent,0);
    }

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if(mResultCallback!=null){
                mResultCallback.invoke(data.getIntExtra(Constants.STATUS, 0));
            }
        }
    };


}
