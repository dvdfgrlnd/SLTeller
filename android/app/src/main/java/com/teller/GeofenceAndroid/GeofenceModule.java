package com.teller.GeofenceAndroid;

import android.content.Intent;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.teller.BootReceiver;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 11/22/16.
 */
public class GeofenceModule extends ReactContextBaseJavaModule implements IAsync, LifecycleEventListener {

    private Callback mResultCallback;
    private DatabaseHandler db;

    public GeofenceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(mActivityEventListener);
        reactContext.addLifecycleEventListener(this);
        new ConnectDatabase(this).execute(getReactApplicationContext());
    }

    @Override
    public String getName() {
        return "GeofenceModule";
    }

    @ReactMethod
    public void getGeofences(Callback c) {
        List<Departure> list = db.getAllGeofences();
        // Filter the list to account for multiple-departure geofences having more than one departure with the same requestId.
        List<Departure> filteredList = new ArrayList<>();
        for (Departure dep : list) {
            boolean found = false;
            for (Departure filtered : filteredList) {
                if (filtered.getRequestId().equals(dep.getRequestId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                filteredList.add(dep);
            }
        }
        WritableArray array = new WritableNativeArray();
        for (Departure dep : filteredList) {
            WritableMap map = new WritableNativeMap();
            map.putString("requestId", dep.getRequestId());
            map.putString("siteId", dep.getSiteId());
            map.putString("destination", dep.getDestination());
            map.putString("lineNumber", dep.getLineNumber());
            map.putDouble("latitude", dep.getLatitude());
            map.putDouble("longitude", dep.getLongitude());
            map.putDouble("radius", dep.getRadius());
            array.pushMap(map);
        }
        c.invoke(array);
    }

    @ReactMethod
    public void removeGeofence(String requestId, Callback c) {
        mResultCallback = c;

        db.deleteGeofence(requestId);

        Intent intent = new Intent(getCurrentActivity(), GeofenceActivity.class);
        intent.putExtra(Constants.ACTION, Constants.REMOVE);
        intent.putExtra(Constants.FENCEID, requestId);
        getCurrentActivity().startActivityForResult(intent, 0);
    }

    @ReactMethod
    public void registerGeofence(ReadableMap obj, Callback callback) {
        mResultCallback = callback;

        List<Departure> list = new ArrayList<>();
        String requestId = obj.getString("requestId");
        ReadableArray array = obj.getArray("departures");
        for (int i = 0; i < array.size(); i++) {
            ReadableMap map = array.getMap(i);
            String siteId = map.getString("siteId");
            String lineNumber = map.getString("lineNumber");
            String destination = map.getString("destination");
            double latitude = map.getDouble("latitude");
            double longitude = map.getDouble("longitude");
            double radius = map.getDouble("radius");
            Departure dep = new Departure(latitude, longitude, radius, siteId, destination, lineNumber, requestId);
            list.add(dep);
        }
        db.insertApp(list);
        Departure dep = list.get(0);
        // Create intent to the activity and send all relevant info for creating the geofence
        Intent intent = new Intent(getCurrentActivity(), GeofenceActivity.class);
        intent.putExtra(Constants.ACTION, Constants.CREATE);
        intent.putExtra(Constants.FENCEID, dep.getRequestId());
        intent.putExtra(Constants.LATITUDE, dep.getLatitude());
        intent.putExtra(Constants.LONGITUDE, dep.getLongitude());
        intent.putExtra(Constants.RADIUS, (float) dep.getRadius());
        getCurrentActivity().startActivityForResult(intent, 0);
    }

    @ReactMethod
    public void reRegisterGeofence(Callback callback) {
        mResultCallback = callback;

        Intent intent = BootReceiver.createRegistrationIntent(db, getReactApplicationContext());
        getCurrentActivity().startActivityForResult(intent, 0);
    }

    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {
        if (db != null) {
            db.close();
        }
    }

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if (mResultCallback != null) {
                mResultCallback.invoke(data.getIntExtra(Constants.STATUS, 0));
            }
        }
    };

    @Override
    public void onFinished(Object obj) {
        db = (DatabaseHandler) obj;
    }
}
