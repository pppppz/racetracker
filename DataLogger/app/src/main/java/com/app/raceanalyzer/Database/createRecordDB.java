package com.app.raceanalyzer.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class createRecordDB {

    private static final String TABLE_RECORD = "Record";
    private static final String USER_ID = "user_id";
    private static final String ROUND_ID = "round_id";
    private static final String BEST_LAP = "bestLap";
    private final int round = 0;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;


    public createRecordDB(Context context) {

        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long addNewRecord(String _user_id) {

        ContentValues values = new ContentValues();
        values.put(USER_ID, _user_id);
        Log.e(createRecordDB.class.getName(), _user_id);
        return database.insert(TABLE_RECORD, null, values);
    }


    public Cursor getBiggestInTheColumn() {
        return database.query(TABLE_RECORD, null,
                null, null, null, null, null);
    }

    public Cursor readAllLocation() {


        Cursor mCursor = database.query(true, TABLE_RECORD, new String[]{USER_ID}, null, null, null, null, null, null);
        //Cursor mCursor = database.query;
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public int updateRecord(String user_id, long bestLap, long round_id) {

        ContentValues values = new ContentValues();
        values.put(USER_ID, user_id);
        values.put(ROUND_ID, round_id);
        values.put(BEST_LAP, bestLap);

        return database.update(TABLE_RECORD, values, ROUND_ID + "=?", new String[]{String.valueOf(round_id)});
    }


}
