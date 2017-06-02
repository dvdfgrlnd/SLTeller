package com.teller.GeofenceAndroid;

import android.content.Context;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by david on 12/19/16.
 */

public class RequestHandler implements IAsync {
    private final Context context;
    private int items;
    private List<Result> departures = new ArrayList<>();
    private Lock lock = new ReentrantLock();
    private IAsync mCallback;

    public RequestHandler(Context context, int numberOfRequests, IAsync callback) {
        this.context = context;
        this.items = numberOfRequests;
        this.mCallback = callback;
    }

    @Override
    public void onFinished(Object obj) {
        lock.lock();
        if (obj != null) {
            departures.add((Result) obj);
        }
        if (--items == 0) {
            mCallback.onFinished(departures);
        }
        lock.unlock();
    }

}
