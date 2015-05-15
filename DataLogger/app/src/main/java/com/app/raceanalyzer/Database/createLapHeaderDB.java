package com.app.raceanalyzer.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class createLapHeaderDB {

    private static final String DB_NAME_LapLocationChange = "LapHeader";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private String USER_ID = "user_id";
    private String FASTEST_LAP = "fastest_lap";
    private String ROUND_ID = "round_id";


    public createLapHeaderDB(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long addNewLapHeader(String _user_id,
                                double fastest_lap, long round_id) {
        ContentValues values = new ContentValues();
        values.put(FASTEST_LAP, fastest_lap);
        values.put(ROUND_ID, round_id);
        values.put(USER_ID, _user_id);
        return database.insert(DB_NAME_LapLocationChange, null, values);
    }
}
