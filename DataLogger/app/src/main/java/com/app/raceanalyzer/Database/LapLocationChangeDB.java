package com.app.raceanalyzer.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class LapLocationChangeDB {

    private static final String DB_NAME_LapLocationChange = "LapLocationChange";
    private static SQLiteDatabase database;
    private static String USER_ID = "user_id";
    private static String AXIS_X = "axis_x";
    private static String AXIS_Y = "axis_y";
    private static String AXIS_Z = "axis_z";
    private static String VELOCITY = "velocity";
    private static String ROUND_ID = "round_id";
    private static String Lat = "latitude";
    private static String Lng = "longitude";
    private static String LAP_ID = "lap_id";
    private static String TIME = "time";
    private DatabaseHelper dbHelper;


    public LapLocationChangeDB(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public static long addNewLocationChange(String _user_id, double axis_x, double axis_y, double axis_z,
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

    public Cursor readAllLap() {
        Cursor cursor = database.query(true, DB_NAME_LapLocationChange, new String[]{AXIS_X, AXIS_Y, AXIS_Y, VELOCITY, ROUND_ID, Lat, Lng, LAP_ID, TIME}, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;

    }

    //get all lap change
    public List<lapChangeAdapter> getAllLapChange() {
        List<lapChangeAdapter> lapChange = new ArrayList<>();

        //  Cursor cursor = database.query(DB_NAME_LapLocationChange,
        //allColumns, null, null, null, null, null);
        String query = "select * from " + DB_NAME_LapLocationChange;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            lapChangeAdapter lapChangeAdapter = cursorToLap(cursor);
            lapChange.add(lapChangeAdapter);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return lapChange;
    }

    private lapChangeAdapter cursorToLap(Cursor cursor) {
        lapChangeAdapter lap = new lapChangeAdapter();
        lap.setId(cursor.getString(0));
        lap.setLapID(cursor.getInt(1));
        lap.setVelocity(cursor.getInt(2));
        lap.setAXIS_X(cursor.getInt(3));
        lap.setAXIS_Y(cursor.getInt(4));
        lap.setAXIS_Z(cursor.getInt(5));
        lap.setRoundId(cursor.getInt(5));
        lap.setLat(cursor.getString(6));
        lap.setLng(cursor.getString(7));
        return lap;
    }
}
