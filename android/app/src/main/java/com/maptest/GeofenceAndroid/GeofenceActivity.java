package com.maptest.GeofenceAndroid;

/**
 * Created by david on 11/26/16.
 */

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;

public class GeofenceActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {
    private GoogleApiClient mGoogleApiClient;
    private AbstractList<Geofence> mGeofenceList;
    private PendingIntent mGeofencePendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGeofenceList = new ArrayList<>();
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void createGeofence(String id, double latitude, double longitude, float radius) {
        mGeofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(id)
                .setCircularRegion(
                        latitude,
                        longitude,
                        radius
                )
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build());
        try {
            mGeofencePendingIntent = getGeofencePendingIntent();
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    mGeofencePendingIntent
            ).setResultCallback(this);
        } catch (SecurityException e) {
            return;
        }

        Log.d("GeofenceActivity", "Geofence created");
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    private void removeGeofence(String id){
        LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient, Arrays.asList(id));
    }

    protected void onStart() {
        Log.d("MainActivity", "onStart");
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        //mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Intent intent = this.getIntent();
        int action=intent.getIntExtra(Constants.ACTION,-1);
        String id = intent.getStringExtra(Constants.FENCEID);
        switch (action){
            case Constants.CREATE:
                double latitude = intent.getDoubleExtra(Constants.LATITUDE, 0);
                double longitude = intent.getDoubleExtra(Constants.LONGITUDE, 0);
                float radius = intent.getFloatExtra(Constants.RADIUS, 0);

                createGeofence(id, latitude, longitude, radius);
                break;
            case Constants.REMOVE:
                removeGeofence(id);
                break;
        }
        this.finish();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("MainActivity", connectionResult.toString());
        String s = connectionResult.getErrorMessage();
        Log.d("MainActivity", connectionResult.getErrorMessage());
        int i = 0;

    }

    @Override
    public void onResult(@NonNull Status status) {

    }
}
