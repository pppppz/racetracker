package com.app.raceanalyzer.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class HeadLapDatabase {

    private static final String DB_NAME = "HeadLap";
    public String USER_ID = "user_id";
    public String TIME = "time";
    public String ROUND_ID = "record_id";
    public String HEADLAP_KEYID = "headlap_KEYID";
    public String LAPCOUNT = "lap_count";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;



    public HeadLapDatabase(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }
/*
    public int updateRecord(long headlap_keyid , long time ) {
        ContentValues values = new ContentValues();
        values.put(HEADLAP_KEYID , headlap_keyid);
        String[] args = new String[]{String.valueOf(time)};
        return database.update(DB_NAME, values, TIME + "=?" , args);
    }*/

    public int updateRecord(long headlap_keyid, long time) {
        ContentValues values = new ContentValues();
        values.put(HEADLAP_KEYID, headlap_keyid);
        values.put(TIME, time);
        String[] args = new String[]{String.valueOf(headlap_keyid)};
        return database.update(DB_NAME, values, HEADLAP_KEYID + "=?", args);
    }


    public long addNewLap(String uid, long rid, int count) {
        ContentValues values = new ContentValues();
        values.put(USER_ID, uid);
        values.put(ROUND_ID, rid);
        values.put(LAPCOUNT, count);
        return database.insert(DB_NAME, null, values);
    }

    public List<Long> readAllHeadLap(String userName, long record_id) {
        List<Long> list = new ArrayList<>();
        Cursor mCursor = database.rawQuery("SELECT HeadLap.lap_count FROM HeadLap where user_id='" + userName + "' AND record_id='" + record_id + "';", null);
        // looping through all rows and adding to list
        if (mCursor.moveToFirst()) {
            do {
                list.add(mCursor.getLong(0));//adding 1 st column data
            } while (mCursor.moveToNext());
        }
        // closing connection
        mCursor.close();
        database.close();
        // returning lables
        return list;
    }
}