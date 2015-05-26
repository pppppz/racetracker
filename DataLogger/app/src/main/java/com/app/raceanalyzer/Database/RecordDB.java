package com.app.raceanalyzer.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RecordDB {

    public static final String USER_ID = "user_id";
    public static final String RECORD_ID = "record_id";
    private static final String TABLE_RECORD = "Record";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;


    public RecordDB(Context context) {

        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long addNewRecord(String _user_id) {

        ContentValues values = new ContentValues();
        values.put(USER_ID, _user_id);
        Log.e(RecordDB.class.getName(), _user_id);
        return database.insert(TABLE_RECORD, null, values);
    }


    public Cursor readAllRecord() {


        //    Cursor mCursor = database.query(true, TABLE_RECORD, new String[]{USER_ID , ROUND_ID}, null, null, null, null, null, null);
        //  String query = "select * from " + TABLE_RECORD ;
        //     Cursor mCursor = database.rawQuery(query , null);
        Cursor mCursor = database.rawQuery("select Record.record_id as _id, Record.* from Record", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public int updateRecord(String user_id, long round_id) {

        ContentValues values = new ContentValues();
        values.put(USER_ID, user_id);
        values.put(RECORD_ID, round_id);

        return database.update(TABLE_RECORD, values, RECORD_ID + "=?", new String[]{String.valueOf(round_id)});
    }


}
