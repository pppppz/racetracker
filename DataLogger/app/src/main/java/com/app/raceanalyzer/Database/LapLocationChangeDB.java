package com.app.raceanalyzer.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class LapLocationChangeDB {

    private static final String DB_NAME_LapLocationChange = "LapLocationChange";
    private static String AXIS_X = "axis_x";
    private static String AXIS_Y = "axis_y";
    private static String AXIS_Z = "axis_z";
    private static SQLiteDatabase database;
    private static String HEADLAP_KEYID = "headlap_KEYID";
    private static String USER_ID = "user_id";
    private static String LAPCOUNT = "lap_count";
    private static String Lat = "latitude";
    private static String Lng = "longitude";
    private static String RECORD_ID = "record_id";
    private static String TIME = "time";
    private static String VELOCITY = "velocity";
    private DatabaseHelper dbHelper;

    public LapLocationChangeDB(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public static long addNewLocationChange(String _user_id, double axis_x, double axis_y, double axis_z,
                                            double velocity, double lat, double lng, long time, long record_id, int count) {
        ContentValues values = new ContentValues();
        values.put(USER_ID, _user_id);
        values.put(AXIS_X, axis_x);
        values.put(AXIS_Y, axis_y);
        values.put(AXIS_Z, axis_z);
        values.put(VELOCITY, velocity);
        values.put(RECORD_ID, record_id);
        values.put(Lat, lat);
        values.put(Lng, lng);
        values.put(TIME, time);
        values.put(LAPCOUNT, count);
        return database.insert(DB_NAME_LapLocationChange, null, values);
    }

    public List readDataLapChange(String userName, long lap_count, long record_id) {
        List<LapChange> list = new ArrayList<>();
        Cursor mCursor = database.rawQuery("select LapLocationChange.lapLocationChangeID as _id ,LapLocationChange.* FROM LapLocationChange where user_id='" + userName + "' AND lap_count='" + lap_count + "' AND record_id='" + record_id + "';", null);
        // looping through all rows and adding to list
        LapChange lapChange;
        if (mCursor.moveToFirst()) {
            do {
                lapChange = new LapChange();
                lapChange.setLAPCOUNT(mCursor.getInt(mCursor.getColumnIndex(LAPCOUNT)));
                lapChange.setVelocity(mCursor.getDouble(mCursor.getColumnIndex(VELOCITY)));
                lapChange.setAXIS_X(mCursor.getDouble(mCursor.getColumnIndex(AXIS_X)));
                lapChange.setAXIS_Y(mCursor.getDouble(mCursor.getColumnIndex(AXIS_Y)));
                lapChange.setAXIS_Z(mCursor.getDouble(mCursor.getColumnIndex(AXIS_Z)));
                lapChange.setRoundId(mCursor.getInt(mCursor.getColumnIndex(RECORD_ID)));
                lapChange.setLat(mCursor.getDouble(mCursor.getColumnIndex(Lat)));
                lapChange.setLng(mCursor.getDouble(mCursor.getColumnIndex(Lng)));
                lapChange.setLAPCOUNT(mCursor.getInt(mCursor.getColumnIndex(LAPCOUNT)));
                list.add(lapChange);
            } while (mCursor.moveToNext());
        }
        // closing connection
        mCursor.close();
        database.close();
        // returning lables
        return list;
    }

    private LapChange cursorToLap(Cursor cursor) {
        LapChange lap = new LapChange();
        lap.setId(cursor.getString(0));
        lap.setLAPCOUNT(cursor.getInt(1));
        lap.setVelocity(cursor.getInt(2));
        lap.setAXIS_X(cursor.getInt(3));
        lap.setAXIS_Y(cursor.getInt(4));
        lap.setAXIS_Z(cursor.getInt(6));
        lap.setRoundId(cursor.getInt(7));
        lap.setLat(cursor.getLong(8));
        lap.setLng(cursor.getLong(9));
        return lap;
    }
}
