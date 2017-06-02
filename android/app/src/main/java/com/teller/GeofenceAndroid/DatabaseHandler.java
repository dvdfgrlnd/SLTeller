package com.teller.GeofenceAndroid;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 12/17/16.
 */

public final class DatabaseHandler {
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
        // Gets the data repository in write mode
        db = mDbHelper.getWritableDatabase();
    }

    public ArrayList<Departure> getAllGeofences() {
        db.beginTransaction();
        Cursor cursor = db.rawQuery(FeedEntry.SELECT_ALL, null);
        ArrayList<Departure> list = parseRows(cursor);
        db.setTransactionSuccessful();
        db.endTransaction();
        return list;
    }

    public ArrayList<Departure> getDepartures(String requestId) {
        db.beginTransaction();
        Cursor cursor = db.rawQuery(FeedEntry.SELECT_REQUEST, new String[]{requestId});
        ArrayList<Departure> list = parseRows(cursor);
        db.setTransactionSuccessful();
        db.endTransaction();
        return list;
    }

    private ArrayList<Departure> parseRows(Cursor cursor) {
        Departure dep;
        ArrayList<Departure> list = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            dep = new Departure();
            // Set application name
            dep.setDestination(cursor.getString(cursor.getColumnIndex(FeedEntry.DESTINATION)));
            dep.setLatitude(cursor.getDouble(cursor.getColumnIndex(FeedEntry.LATITUDE)));
            dep.setLongitude(cursor.getDouble(cursor.getColumnIndex(FeedEntry.LONGITUDE)));
            dep.setLineNumber(cursor.getString(cursor.getColumnIndex(FeedEntry.LINE_NUMBER)));
            dep.setRadius(cursor.getDouble(cursor.getColumnIndex(FeedEntry.RADIUS)));
            dep.setRequestId(cursor.getString(cursor.getColumnIndex(FeedEntry.REQUEST_ID)));
            dep.setSiteId(cursor.getString(cursor.getColumnIndex(FeedEntry.SITE_ID)));
            // Add app to list
            list.add(dep);
            // Move to next row in result table
            cursor.moveToNext();
        }
        // IMPORTANT! Close cursor to avoid memory leak
        cursor.close();
        return list;
    }

    public void insertApp(List<Departure> list) {
        db.beginTransaction();
        for (Departure dep : list) {
            Object[] args = {
                    dep.getRequestId(),
                    dep.getDestination(),
                    dep.getLineNumber(),
                    dep.getSiteId(),
                    dep.getLatitude(),
                    dep.getLongitude(),
                    dep.getRadius()
            };
            db.execSQL(FeedEntry.INSERT_STATEMENT, args);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void deleteGeofence(String requestId) {
        String[] args = {requestId};
        db.beginTransaction();
        db.execSQL(FeedEntry.DELETE_GEOFENCE_STATEMENT, args);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void deleteTable() {
        db.beginTransaction();
        db.execSQL(FeedReaderDbHelper.SQL_DELETE_ENTRIES);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void close(){
        if(db!=null){
            db.close();
        }
    }

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        static final String REQUEST_ID = "requestID";
        static final String DESTINATION = "destination";
        static final String LINE_NUMBER = "linenumber";
        static final String SITE_ID = "siteid";
        static final String LATITUDE = "latitude";
        static final String LONGITUDE = "longitude";
        static final String RADIUS = "radius";

        public static final String INSERT_STATEMENT = "INSERT INTO " + FeedReaderDbHelper.TABLE_NAME + " (" + FeedEntry.REQUEST_ID + "," + FeedEntry.DESTINATION + "," + FeedEntry.LINE_NUMBER + "," + FeedEntry.SITE_ID + "," + FeedEntry.LATITUDE + "," + FeedEntry.LONGITUDE + "," + FeedEntry.RADIUS + ") VALUES (?, ?, ?, ?, ?, ?, ?)";

        public static final String SELECT_REQUEST = "SELECT * FROM " + FeedReaderDbHelper.TABLE_NAME + " WHERE " + FeedEntry.REQUEST_ID + "=?";

        public static final String SELECT_ALL = "SELECT * FROM " + FeedReaderDbHelper.TABLE_NAME;

        public static final String DELETE_GEOFENCE_STATEMENT = "DELETE FROM " + FeedReaderDbHelper.TABLE_NAME + " WHERE " + FeedEntry.REQUEST_ID + "=?";
    }

    public class FeedReaderDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 2;
        public static final String DATABASE_NAME = "FeedReader.db";
        public static final String TABLE_NAME = "geofences";
        private static final String TEXT_TYPE = " TEXT";
        private static final String REAL_TYPE = " REAL";
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        FeedEntry.REQUEST_ID + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.DESTINATION + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.LINE_NUMBER + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.SITE_ID + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.LATITUDE + REAL_TYPE + COMMA_SEP +
                        FeedEntry.LONGITUDE + REAL_TYPE + COMMA_SEP +
                        FeedEntry.RADIUS + REAL_TYPE + COMMA_SEP + " PRIMARY KEY (" + FeedEntry.REQUEST_ID + COMMA_SEP + FeedEntry.SITE_ID + COMMA_SEP + FeedEntry.DESTINATION + COMMA_SEP + FeedEntry.LINE_NUMBER + " ))";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public FeedReaderDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.beginTransaction();
            db.execSQL(SQL_CREATE_ENTRIES);
            db.setTransactionSuccessful();
            db.endTransaction();
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.beginTransaction();
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
            db.setTransactionSuccessful();
            db.endTransaction();
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

}
