package com.app.raceanalyzer.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class createLapHeaderDB {

    private static final String DB_NAME = "LapHeader";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private String USER_ID = "user_id";
    private String LAP_TIME = "lap_time";
    private String ROUND_ID = "round_id";


    public createLapHeaderDB(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long addNewLapHeader(String user_id,
                                double lapTime, long round_id) {
        ContentValues values = new ContentValues();
        values.put(LAP_TIME, lapTime);
        values.put(ROUND_ID, round_id);
        values.put(USER_ID, user_id);
        return database.insert(DB_NAME, null, values);
    }

    public int updateRecord(String user_id, double lapTime, long round_id) {

        ContentValues values = new ContentValues();
        values.put(USER_ID, user_id);
        values.put(ROUND_ID, round_id);
        values.put(LAP_TIME, lapTime);

        return database.update(DB_NAME, values, LAP_TIME + "=?", new String[]{String.valueOf(lapTime)});
    }
}
