package com.teller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.teller.GeofenceAndroid.ConnectDatabase;
import com.teller.GeofenceAndroid.Constants;
import com.teller.GeofenceAndroid.DatabaseHandler;
import com.teller.GeofenceAndroid.Departure;
import com.teller.GeofenceAndroid.GeofenceActivity;
import com.teller.GeofenceAndroid.IAsync;

import java.util.List;

/**
 * Created by david on 1/21/17.
 */
public class BootReceiver extends BroadcastReceiver implements IAsync {
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        new ConnectDatabase(this).execute(context);
    }

    @Override
    public void onFinished(Object obj) {
        DatabaseHandler db = (DatabaseHandler) obj;
        Intent intent = createRegistrationIntent(db, mContext);
        mContext.startActivity(intent);
    }

    public static Intent createRegistrationIntent(DatabaseHandler db, Context mContext) {
        List<Departure> list = db.getAllGeofences();
        String[] ids = new String[list.size()];
        double[] latitudes = new double[list.size()];
        double[] longitudes = new double[list.size()];
        float[] radius = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Departure dep = list.get(i);
            ids[i] = dep.getRequestId();
            latitudes[i] = dep.getLatitude();
            longitudes[i] = dep.getLongitude();
            radius[i] = (float) dep.getRadius();
        }

        Intent intent = new Intent(mContext, GeofenceActivity.class);
        intent.putExtra(Constants.ACTION, Constants.RE_REGISTER);
        intent.putExtra(Constants.FENCEID, ids);
        intent.putExtra(Constants.LATITUDE, latitudes);
        intent.putExtra(Constants.LONGITUDE, longitudes);
        intent.putExtra(Constants.RADIUS, radius);
        return intent;
    }
}
