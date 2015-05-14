package com.app.raceanalyzer.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class createLapHeaderDB {

    private static final String DB_NAME_LapLocationChange = "LapHeader";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private String USER_ID = "user_id";
    private String MAX_VELOCITY = "max_velocity";
    private String ROUND_ID = "round_id";


    public createLapHeaderDB(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long addNewLap(String _user_id,
                          double max_velocity, long round_id) {
        ContentValues values = new ContentValues();
        values.put(USER_ID, _user_id);
        values.put(MAX_VELOCITY, max_velocity);
        values.put(ROUND_ID, round_id);
        return database.insert(DB_NAME_LapLocationChange, null, values);
    }
}
