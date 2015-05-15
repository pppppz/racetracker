package com.app.raceanalyzer.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class createLapLocationChangeDB {

    private static final String DB_NAME_LapLocationChange = "LapLocationChange";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private String USER_ID = "user_id";
    private String AXIS_X = "axis_x";
    private String AXIS_Y = "axis_y";
    private String AXIS_Z = "axis_z";
    private String VELOCITY = "velocity";
    private String ROUND_ID = "round_id";
    private String Lat = "latitude";
    private String Lng = "longitude";
    private String LAP_ID = "lap_id";
    private String TIME = "time";

    public createLapLocationChangeDB(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long addNewLocationChange(String _user_id, double axis_x, double axis_y, double axis_z,
                                     double velocity, long round_id, double lat, double lng, long time, long lap_id) {
        ContentValues values = new ContentValues();
        values.put(USER_ID, _user_id);
        values.put(AXIS_X, axis_x);
        values.put(AXIS_Y, axis_y);
        values.put(AXIS_Z, axis_z);
        values.put(VELOCITY, velocity);
        values.put(ROUND_ID, round_id);
        values.put(Lat, lat);
        values.put(Lng, lng);
        values.put(TIME, time);
        values.put(LAP_ID, lap_id);
        return database.insert(DB_NAME_LapLocationChange, null, values);
    }
}
