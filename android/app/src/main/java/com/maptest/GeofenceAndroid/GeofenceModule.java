package com.maptest.GeofenceAndroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

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
    public void registerGeofence(String requestId, double latitude, double longitude, double radius) {
        Intent intent = new Intent(getCurrentActivity(), GeofenceActivity.class);
        intent.putExtra(Constants.FENCEID, requestId);
        intent.putExtra(Constants.LATITUDE, latitude);
        intent.putExtra(Constants.LONGITUDE, longitude);
        intent.putExtra(Constants.RADIUS, (float)radius);
        getCurrentActivity().startActivity(intent);
    }

}
